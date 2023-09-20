package fptu.swp391.shoppingcart.user.authentication.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class MailOtpService {
    @Value("${mailgun.domain.name}")
    private String mailgunDomainName;

    @Value("${mailgun.api.key}")
    private String apiKey;

    public JsonNode sendOtpTo(String email, String otp) throws UnirestException, IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("email.html"); // in src/main/resources

        // Read the InputStream and store it in a String variable.
        String htmlContent = readInputStreamToString(inputStream);

        // Replace VERIFICATION_CODE with the real code
        htmlContent = htmlContent.replace("VERIFICATION_CODE", otp);

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/"
                        + mailgunDomainName + "/messages")
                .basicAuth("api", apiKey)
                .queryString("from", "Thống Đặng Hoàng<noreply@thongdanghoang.id.vn>")
                .queryString("to", email)
                .queryString("subject", "Your verification code")
                .queryString("html", htmlContent)
                .asJson();
        return request.getBody();
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
