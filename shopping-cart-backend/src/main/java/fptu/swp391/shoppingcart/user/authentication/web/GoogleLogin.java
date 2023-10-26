package fptu.swp391.shoppingcart.user.authentication.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.repository.UserRepository;
import fptu.swp391.shoppingcart.user.authentication.service.GoogleAuthtication;
import fptu.swp391.shoppingcart.user.authentication.service.UserAuthService;
import fptu.swp391.shoppingcart.user.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
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
    private UserAuthService userAuthService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    private String scope = "openid%20profile%20email";
    private String authorizationGrantType = "authorization_code";
    private String redirectUri = "http://localhost:8080/login/oauth2/code/google/callback";

    @GetMapping("/authorize")
    public RedirectView getGoogleAuthUrl() {
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectUri + "&response_type=code&scope=" + scope;
        return new RedirectView(url);
    }

    //callback
    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String code, HttpServletRequest request) {
        // exchange code for access token and id token
        // use rest template to call google api
        String url = "https://oauth2.googleapis.com/token?code=" + code + "&client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + redirectUri + "&grant_type=" + authorizationGrantType;
        String response = restTemplate.postForObject(url, null, String.class);
        // log response
        Logger.getLogger("GoogleLogin").info(response);
//        {
//            "access_token": "VQhGGNkorNpX4itzZdzwmW22N22XmzDeYMTs0Z82c2KDgsEgTni04N0acPTdTGAiAuCf2DDnOnlEi_-9rdyPmxVm-aCgYKAbQSARISFQGOcNnC7or7s5PLIVf4OOlRzITwrQ0171",
//                "expires_in": 3599,
//                "scope": "https://www.googleapis.com/auth/userinfo.profile openid https://www.googleapis.com/auth/userinfo.email",
//                "token_type": "Bearer",
//                "id_token": "NDdmODM2OTMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIzMDkyMjMyMzk0OTctbTBiYzJkNWh2dmZ0cHZvMnRha2hvMnVkdnBrbjloOTAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIzMDkyMjMyMzk0OTctbTBiYzJkNWh2dmZ0cHZvMnRha2hvMnVkdnBrbjloOTAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDgwNzUxNTExNTk2MDQ0NDI4MTEiLCJoZCI6ImZwdC5lZHUudm4iLCJlbWFpbCI6Im5nYW5udHFlMTcwMjM2QGZwdC5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IkVtN2tVd2RkVjR3b2JQbDFNRHJYMHciLCJuYW1lIjoiTmd1eWVuIFRodWMgTmdhbiAoSzE3IFFOKSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NMRVZmWXZxd1c1dDQzUmZrNXF4eHBCM2FkdVJCVl9mN2hRejBBYXBnekZNbGc9czk2LWMiLCJnaXZlbl9uYW1lIjoiTmd1eWVuIFRodWMgTmdhbiIsImZhbWlseV9uYW1lIjoiKEsxNyBRTikiLCJsb2NhbGUiOiJlbi1HQiIsImlhdCI6MTY5NzY4MzA4MSwiZXhwIjoxNjk3Njg2NjgxfQ.RPALrLmLVix2oHev_EAxL4fOTjbZ54cO13Re5XP6bT_MwjK7zGNJHrmDV91yIahMOX3lp1z7LURmbaAn89TzMqObcN69FRvIhEKLrrGYVhHKW6FJxHiYwRW2AQ42Gy_iSTY4X6oztmMjoIoBLwlE9Xy-CuWvdC3USn49DAkP_TGbF6MWPsQhrKjmeWGcuPPBWBInbrBfByrpCDLU9iFYy5Pdzk_I-lIns7mC8xDX3AKB6P6LgmhOsmZ8sq3FvFzp9T172U-8qh5dEC0ByKsEmZf0bbrQOJLSs_eoh9jcyQkqGtoWxxtIptsqYocSnflDYH7IZifieSZ_TEg8vsRo8A"
//        }
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
        String idToken = (String) responseMap.get("id_token");

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

        String email = (String) responseMap.get("email");

        // login user with email
        Optional<UserAuthEntity> userAuth = userRepository.findByProfileEmail(email);

        if (userAuth.isPresent()) {
            Authentication authentication = new GoogleAuthtication(userAuth.get().getUsername(), email, true, userAuth.get());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return new RedirectView("http://localhost:3000");
        } else {
            // TODO : create new user with email
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your email is not registered");
    }
}
