package vn.id.thongdanghoang.user_service.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class OidcE2ETest {

    private static Playwright playwright;
    @LocalServerPort
    private int port;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
    }

    @AfterAll
    static void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void openidConfigurationShouldBeValid() {
        var request = playwright.request().newContext();
        String discoveryUrl = "http://localhost:" + port + "/.well-known/openid-configuration";

        APIResponse response = request.get(discoveryUrl);

        // Verify status code
        assertThat(response.status()).isEqualTo(200);

        // Parse JSON and verify essential OIDC fields
        var config = response.text();
        assertThat(config).contains("\"issuer\":\"http://localhost:" + port + "\"");
        assertThat(config).contains("\"authorization_endpoint\"");
        assertThat(config).contains("\"token_endpoint\"");
        assertThat(config).contains("\"jwks_uri\"");
        assertThat(config).contains("\"userinfo_endpoint\"");
    }

    @Test
    void authorizationEndpointShouldRedirectToLogin() {
        try (var browser = playwright.chromium().launch()) {
            var page = browser.newPage();

            // TDD: Construct a dummy authorization request
            String authUrl = String.format(
                    "http://localhost:%d/oauth2/authorize?response_type=code&client_id=playwright&scope=openid&redirect_uri=http://localhost:3000",
                    port);

            page.navigate(authUrl);

            // Assert that the user is redirected to your custom login page defined in UserServiceRunner
            assertThat(page.url()).contains("/ui/login.html");
            assertThat(page.isVisible("text=Login to User Service")).isTrue();
        }
    }
}