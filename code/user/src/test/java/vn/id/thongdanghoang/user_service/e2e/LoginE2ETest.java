package vn.id.thongdanghoang.user_service.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class LoginE2ETest {

    private static Browser browser;
    private static Playwright playwright;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        // Use setHeadless(false) if you want to see the browser UI during tests
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void tearDownClass() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private Page page;

    @LocalServerPort
    private int port;

    @Test
    void loginPageShouldRenderCorrectly() {
        // Navigate to the login page
        page.navigate("http://localhost:" + port + "/ui/login.html");

        // TDD Verification: Ensure elements exist before checking logic
        assertThat(page.title()).isEqualTo("Login - User Service");

        // Check for login buttons
        assertThat(page.isVisible("text=Login with GitHub")).isTrue();
        assertThat(page.isVisible("text=Login with Google")).isTrue();
    }

    @BeforeEach
    void setup() {
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
        if (page != null) {
            page.close();
        }
    }
}