package com.chaau568.flashcards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;

    private static final String LOGIN_PATH = "/login";
    private final String FRONTEND_BASE_URL;

    private final By USERNAME_FIELD = By.id("username");
    private final By PASSWORD_FIELD = By.id("password");
    private final By LOGIN_BUTTON = By.id("btn_login");
    private final By REGISTER_LINK = By.id("btn_register");

    public LoginPage(WebDriver driver, String frontendBaseUrl) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
    }

    public void navigateToLoginPage() {
        driver.get(FRONTEND_BASE_URL + LOGIN_PATH);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
    }

    public void enterUsername(String username) {
        driver.findElement(USERNAME_FIELD).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(LOGIN_BUTTON).click();
    }

    public void login(String username, String password) {
        navigateToLoginPage();

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
        
        clickLoginButton();
    }

    public void clickRegisterLink() {
        driver.findElement(REGISTER_LINK).click();
    }

    public String getLoginErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
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
