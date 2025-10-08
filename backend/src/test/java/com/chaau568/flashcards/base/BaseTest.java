package com.chaau568.flashcards.base;

import java.time.Duration;
import java.net.http.HttpClient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.chaau568.flashcards.util.ApiHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    protected static WebDriver driver;
    protected static HttpClient httpClient;
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static final String FRONTEND_BASE = "http://localhost:5173";
    public static final String BACKEND_BASE = "http://localhost:8080/flashcard";

    protected static ApiHelper apiHelper;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

        apiHelper = new ApiHelper(BACKEND_BASE);
    }

    @AfterAll
    public static void teardownAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected String getJSessionIdFromBrowser() {
        var cookie = driver.manage().getCookieNamed("JSESSIONID");
        return cookie == null ? null : cookie.getValue();
    }

    protected void injectJSessionToBrowser(String jsession) {
        driver.get(FRONTEND_BASE + "/");
        org.openqa.selenium.Cookie c = new org.openqa.selenium.Cookie.Builder("JSESSIONID", jsession)
                .domain("localhost")
                .path("/")
                .isHttpOnly(true)
                .build();
        driver.manage().addCookie(c);
        driver.navigate().refresh();
    }
}
