package com.chaau568.flashcards.tests;

import com.chaau568.flashcards.base.BaseTest;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.pages.LoginPage;
import com.chaau568.flashcards.pages.LogoutPage;
import com.chaau568.flashcards.pages.RegisterPage;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTests extends BaseTest {

    private LoginPage loginPage;
    private LogoutPage logoutPage;
    private RegisterPage registerPage;

    private static final String TEST_USER = "nobodyahhh";
    private static final String TEST_PASS = "nobodyahhh12";

    private String testUserId;

    @BeforeEach
    void setupTest() throws Exception {
        driver.manage().deleteAllCookies();
        driver.get(FRONTEND_BASE + "/login");
        loginPage = new LoginPage(driver, FRONTEND_BASE);
        logoutPage = new LogoutPage(driver, FRONTEND_BASE);
        registerPage = new RegisterPage(driver, FRONTEND_BASE);

        try {
            User user = new User();
            user.setUsername(TEST_USER);
            user.setPassword(TEST_PASS);
            testUserId = apiHelper.register(user);
        } catch (RuntimeException e) {
            if (!e.getMessage().contains("already exists")) {
                throw e;
            }
        }
    }

    @AfterEach
    void teardownTest() {
        try {
            // ensure login before delete
            if (testUserId != null) {
                apiHelper.login(TEST_USER, TEST_PASS);
                apiHelper.deleteAccount(testUserId);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete test user: " + TEST_USER + ". Error: " + e.getMessage());
        }
    }

    @Test // testcase 1 : test login 1 => สำเร็จ (ทั้ง username และ password ถูกต้อง)
    void testLoginSuccess_UINavigatesToHome() {
        loginPage.login(TEST_USER, TEST_PASS);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/"));

        String expecteUrl = FRONTEND_BASE + "/";
        String actualUrl = driver.getCurrentUrl();

        // message นี้คือ Assertion Message ใช่ในการเเสดงผ่าน console.log
        assertEquals(expecteUrl, actualUrl, "Login successful but not navigated to the correct Home URL.");
        assertNotNull(driver.manage().getCookieNamed("JSESSIONID"),
                "JSESSIONID cookie was not set after successful login.");
    }

    @Test // testcase 2 : test login 2 => ไม่สำเร็จ (username ไม่มีใน database)
    void testLoginFailed_UserNotFound_ShowsAlert() {
        String nonExistentUser = "non_existent_user_xyz";

        loginPage.login(nonExistentUser, TEST_PASS);

        String errorMessage = loginPage.getLoginErrorMessage();
        assertNotNull(errorMessage, "User not found, but no alert message was displayed.");

        // contains จะต้องตรงกับ backend + frontend
        assertTrue(errorMessage.contains("Login failed: User with name '" +
                nonExistentUser + "' was not found."),
                "Error message content for user not found is incorrect.");

        String expectedUrl = FRONTEND_BASE + "/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl,
                "After failed login, the page should remain on the Login URL.");
    }

    @Test // testcase 3 : test login 3 => ไม่สำเร็จ (password ผิด)
    void testLoginFailed_IncorrectPassword_ShowsAlert() {
        String incorrectPass = "wrong_password";

        loginPage.login(TEST_USER, incorrectPass);

        String errorMessage = loginPage.getLoginErrorMessage();
        assertNotNull(errorMessage, "Login failed, but no alert message was displayed.");

        // contains จะต้องตรงกับ backend + frontend
        assertTrue(errorMessage.contains("Login failed: Password '" + incorrectPass +
                "' was incorrect."),
                "Error message content is incorrect.");

        String expectedUrl = FRONTEND_BASE + "/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl,
                "After failed login, the page should remain on the Login URL.");
    }

    @Test // testcase 4 : test logout 1 => สำเร็จ (กด CONFIRM แล้วทำการลบ session จาก
          // backend)
    void testLogoutSuccess_UINavigatesToLogin() {
        loginPage.login(TEST_USER, TEST_PASS);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/"));

        logoutPage.logout("confirm");

        logoutPage.waitForRedirectionToLogin();

        String expectedUrl = FRONTEND_BASE + "/login";
        String actualUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, actualUrl, "Logout successful but not navigated to the Login URL.");

        assertNull(driver.manage().getCookieNamed("JSESSIONID"),
                "JSESSIONID cookie should be removed after successful logout.");
    }

    @Test // testcase 5 : test logout 2 => สำเร็จ (กด CANCEL แล้วกลับหน้า Home โดย Session
          // ยังอยู่)
    void testLogoutCancel_UIRemainsAtHome() {
        loginPage.login(TEST_USER, TEST_PASS);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/"));

        logoutPage.logout("cancle");

        logoutPage.waitForRedirectionToHome();

        String expectedUrl = FRONTEND_BASE + "/";
        String actualUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, actualUrl, "Logout canceled, but not returned to the Home URL.");

        assertNotNull(driver.manage().getCookieNamed("JSESSIONID"),
                "JSESSIONID cookie should still be present after canceling logout.");
    }

    @Test // testcase 6 : test register 1 => สำเร็จ (username ไม่ซ้ำ)
    void testRegisterSuccess_UIRemainsAtLogin() {
        String uniqueUser = "unique_user_" + System.currentTimeMillis();
        String uniquePass = "reg_pass123";
        String newUserId;

        registerPage.register(uniqueUser, uniquePass, uniquePass);

        String successMessage = registerPage.getRegisterErrorMessage();

        assertNotNull(successMessage, "Registration successful, but no success alert was displayed.");

        assertTrue(successMessage.contains("Registration successfully: User created successfully."),
                "Success message content is incorrect. Actual: " + successMessage);

        registerPage.waitForRedirectionToLogin();

        String expectedUrl = FRONTEND_BASE + "/login";
        String actualUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, actualUrl,
                "After successful registration, the page should navigate to the Login URL.");

        try {
            apiHelper.login(uniqueUser, uniquePass);
            newUserId = apiHelper.getUserIdFromSession();
            apiHelper.deleteAccount(newUserId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean up newly registered user: " + uniqueUser, e);
        }
    }

    @Test // testcase 7 : test register 2 => ไม่สำเร็จ (username ซ้ำ)
    void testRegisterFailed_UsernameAlreadyExists() {
        String existingUser = TEST_USER;
        String newPass = "test_pass_again";

        registerPage.register(existingUser, newPass, newPass);

        String errorMessage = registerPage.getRegisterErrorMessage();

        assertNotNull(errorMessage, "Registration failed (duplicate user), but no error alert was displayed.");

        assertTrue(errorMessage.contains("Registration failed: Username '" + existingUser
                + "' already exists."),
                "Error message content for duplicate username is incorrect.");

        String expectedUrl = FRONTEND_BASE + "/register";
        String actualUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, actualUrl, "After failed registration, the page should remain on the Register URL.");
    }

    @Test // testcase 8 : test register 3 => ไม่สำเร็จ (password และ confirm password
          // ไม่ตรงกัน)
    void testRegisterFailed_PasswordAndConfirmPasswordNotMatch() {
        String uniqueUser = "unique_user_" + System.currentTimeMillis();
        String uniquePass = "reg_pass123";
        String uniqueConP = "reg_pass120";

        registerPage.register(uniqueUser, uniquePass, uniqueConP);

        String errorMessage = registerPage.getRegisterErrorMessage();

        assertNotNull(errorMessage,
                "Registration failed (password and confirm password not match), but no error alert was displayed.");

        assertTrue(errorMessage.contains("Registration failed: Your password and confirm password do not match."),
                "Error message content for duplicate username is incorrect.");

        String expectedUrl = FRONTEND_BASE + "/register";
        String actualUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, actualUrl, "After failed registration, the page should remain on the Register URL.");
    }

}
