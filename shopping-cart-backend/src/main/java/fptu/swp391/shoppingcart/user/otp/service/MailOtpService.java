package fptu.swp391.shoppingcart.user.otp.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fptu.swp391.shoppingcart.user.otp.entities.EmailOtpEntity;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpExpiredException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpIncorrectException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpMaxAttemptsExceededException;
import fptu.swp391.shoppingcart.user.otp.exceptions.OtpStillActiveException;
import fptu.swp391.shoppingcart.user.otp.repo.EmailOtpRepository;
import fptu.swp391.shoppingcart.user.otp.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MailOtpService {
    @Value("${mailgun.domain.name}")
    private String mailgunDomainName;

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Autowired
    private EmailOtpRepository emailOtpRepository;

    public boolean isOtpSent(String target) {
        return emailOtpRepository.findByEmail(target).isPresent();
    }

    // Cases:
    // 1. OTP was expired -> delete it and create a new one
    // 2. OTP exceed max attempts (more than 3 times) -> throw OtpMaxAttemptsExceededException
    // 3. OTP still active -> throw OtpStillActiveException
    public void sendVerification(String target) throws OtpStillActiveException, OtpMaxAttemptsExceededException {
        Optional<EmailOtpEntity> mailOtp = emailOtpRepository.findByEmail(target);
        if (mailOtp.isPresent()) { // OTP still exists
            EmailOtpEntity emailOtp = mailOtp.get();
            if (emailOtp.getExpiredIn().isAfter(LocalDateTime.now())) {
                if (emailOtp.getWrongSubmit() > 3) { // OTP max attempts exceeded
                    throw new OtpMaxAttemptsExceededException("OTP max attempts exceeded, please try again later.");
                } else { // OTP still available
                    throw new OtpStillActiveException("OTP still active, please enter OTP or wait for it to expire.");
                }
            } else { // OTP expired
                emailOtpRepository.delete(emailOtp);
            }
        }

        EmailOtpEntity emailOtp = new EmailOtpEntity();
        emailOtp.setEmail(target);
        emailOtp.setOtp(Generator.generateOtp());
        emailOtp.setExpiredIn(LocalDateTime.now().plusMinutes(10));
        emailOtpRepository.save(emailOtp);

        // read email template from src/main/resources
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("email.html");
        String htmlContent = null;
        try {
            htmlContent = readInputStreamToString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        htmlContent = htmlContent.replace("VERIFICATION_CODE", emailOtp.getOtp());

        try { // send email by mailgun
            Unirest.post("https://api.mailgun.net/v3/"
                            + mailgunDomainName + "/messages")
                    .basicAuth("api", apiKey)
                    .queryString("from", "Thống Đặng Hoàng<noreply@thongdanghoang.id.vn>")
                    .queryString("to", target)
                    .queryString("subject", "Your verification code")
                    .queryString("html", htmlContent)
                    .asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkVerification(String target, String otp)
            throws OtpMaxAttemptsExceededException, OtpExpiredException, OtpIncorrectException {
        EmailOtpEntity mailOtp = emailOtpRepository.findByEmail(target).
                orElseThrow(() -> new OtpIncorrectException("OTP not found, please request a new one."));
        if (mailOtp.getExpiredIn().isBefore(LocalDateTime.now())) { // OTP expired so delete it
            emailOtpRepository.delete(mailOtp);
            throw new OtpExpiredException("OTP expired, please request a new one.");
        }
        if (mailOtp.getWrongSubmit() == 3) {
            throw new OtpMaxAttemptsExceededException("OTP max attempts exceeded, please try again later.");
        }
        if (!mailOtp.getOtp().equals(otp)) {
            mailOtp.setWrongSubmit(mailOtp.getWrongSubmit() + 1);
            emailOtpRepository.save(mailOtp);
            throw new OtpIncorrectException("OTP incorrect, please try again.");
        }
        emailOtpRepository.delete(mailOtp);
    }

    private String readInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
