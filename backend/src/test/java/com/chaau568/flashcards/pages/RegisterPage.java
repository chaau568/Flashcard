package com.chaau568.flashcards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;

    private static final String LOGIN_PATH = "/login";
    private static final String REGISTER_PATH = "/register";
    private final String FRONTEND_BASE_URL;

    private final By USERNAME_FIELD = By.id("username");
    private final By PASSWORD_FIELD = By.id("password");
    private final By CONFIRM_PASSWORD_FIELD = By.id("confirm_password");
    private final By REGISTER_BUTTON = By.id("btn_register");

    public RegisterPage(WebDriver driver, String frontendBaseUrl) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
    }

    public void navigateToRegisterPage() {
        driver.get(FRONTEND_BASE_URL + REGISTER_PATH);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
    }

    public void enterUsername(String username) {
        driver.findElement(USERNAME_FIELD).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        driver.findElement(CONFIRM_PASSWORD_FIELD).sendKeys(confirmPassword);
    }

    public void clickRegisterButton() {
        driver.findElement(REGISTER_BUTTON).click();
    }

    public void register(String username, String password, String confirmPassword) {
        navigateToRegisterPage();
        enterUsername(username);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        enterPassword(password);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        enterConfirmPassword(confirmPassword);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        clickRegisterButton();
    }

    public void waitForRedirectionToLogin() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE_URL + LOGIN_PATH));
    }

    public String getRegisterErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            wait.until(ExpectedConditions.alertIsPresent());

            String errorMessage = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();

            return errorMessage;
        } catch (Exception e) {
            return null;
        }
    }
}
