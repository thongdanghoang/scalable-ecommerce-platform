package fptu.swp391.shoppingcart.user.authentication.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fptu.swp391.shoppingcart.user.authentication.utils.RandomPasswordGenerator;
import fptu.swp391.shoppingcart.product.services.ImageService;
import fptu.swp391.shoppingcart.user.authentication.entity.Authority;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.exceptions.UsernameAlreadyExists;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.authentication.service.GoogleAuthtication;
import fptu.swp391.shoppingcart.user.profile.entity.ProfileEntity;
import fptu.swp391.shoppingcart.user.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/login/oauth2/code/google")
public class GoogleLogin {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ImageService imageService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    private String scope = "openid%20profile%20email";
    private String authorizationGrantType = "authorization_code";
    private String redirectUri = "https://thongdanghoang.id.vn/isc-301/api/login/oauth2/code/google/callback";

    @GetMapping("/authorize")
    public RedirectView getGoogleAuthUrl(HttpServletRequest request) {
        // Generate a unique state value
        String state = UUID.randomUUID().toString();

        // Store the state in the user's session
        HttpSession session = request.getSession();
        session.setAttribute("google_oauth_state", state);
        // store request url in session
        session.setAttribute("request_url", request.getHeader("referer"));
        Logger.getLogger("GoogleLogin").info("request url: " + request.getHeader("referer"));
        Logger.getLogger("GoogleLogin").info("state: " + state);


        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectUri + "&response_type=code&scope=" + scope + "&state=" + state;
        return new RedirectView(url);
    }

    //callback
    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String code,
                                 @RequestParam String state,
                                 HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionState = (String) session.getAttribute("google_oauth_state");
        String requestUrl = (String) session.getAttribute("request_url");
        if(state == null || !state.equals(sessionState)) {
            // return error page
            return new RedirectView(requestUrl.concat("?error=state_mismatch"));
        }
        // exchange code for access token and id token
        // use rest template to call google api
        String url = "https://oauth2.googleapis.com/token?code=" + code + "&client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + redirectUri + "&grant_type=" + authorizationGrantType;
        String response = restTemplate.postForObject(url, null, String.class);
        // log response
        // Parse the JSON response to extract the access token
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = null;
        try {
            responseMap = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String accessToken = (String) responseMap.get("access_token");
//        String idToken = (String) responseMap.get("id_token");

        // Use the access token to get the user info
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, requestEntity, String.class);

        String userInfo = userInfoResponse.getBody();
        // Parse "userInfo" JSON response to extract user details

        try {
            responseMap = objectMapper.readValue(userInfo, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Logger.getLogger("GoogleLogin").info("userInfo: " + userInfo);
        String email = (String) responseMap.get("email");
        String avatar = (String) responseMap.get("picture");
        String name = (String) responseMap.get("name");

        // login user with email
        var userAuth = userRepository.findByProfileEmail(email);

        if (userAuth.isPresent()) {
            var userEntity = userAuth.get();
            if (!userEntity.getProfile().isEmailVerified()) {
                userEntity.getProfile().setEmailVerified(true);
                profileRepository.save(userEntity.getProfile());
            }
            Authentication authentication = new GoogleAuthtication(userEntity.getUsername(), email, true, userEntity);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return new RedirectView(requestUrl);
        } else {
            UserAuthEntity register = new UserAuthEntity();
            register.setUsername(email);
            String randomPassword = RandomPasswordGenerator.generateRandomPassword(32);
            register.setPassword(bCryptPasswordEncoder.encode(randomPassword));
            register.setAuthorities(Set.of(new Authority("ROLE_USER")));
            UserAuthEntity saved;
            try {
                saved = userRepository.save(register);
            } catch (DataIntegrityViolationException e) {
                throw new UsernameAlreadyExists("Username already exists");
            } // created and saved user

            ProfileEntity profileEntity = new ProfileEntity();
            try {
                profileEntity.setAvatar(imageService.uploadImageFromUrl(avatar));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profileEntity.setEmail(email);
            profileEntity.setEmailVerified(true);
            profileEntity.setFullName(name);
            profileEntity.setUser(saved);
            profileRepository.save(profileEntity);

            Authentication authentication = new GoogleAuthtication(saved.getUsername(), email, true, saved);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return new RedirectView(requestUrl);
        }
    }
}
