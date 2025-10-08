package com.chaau568.flashcards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogoutPage {
    private final WebDriver driver;

    private static final String HOME_PATH = "/";
    private static final String LOGIN_PATH = "/login";
    private static final String LOGOUT_PATH = "/logout";
    private final String FRONTEND_BASE_URL;

    private final By CANCEL_BUTTON = By.id("btn_cancel");
    private final By CONFIRM_BUTTON = By.id("btn_confirm");

    public LogoutPage(WebDriver driver, String frontendBaseUrl) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
    }

    public void navigateToLogoutPage() {
        driver.get(FRONTEND_BASE_URL + LOGOUT_PATH);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.visibilityOfElementLocated(CONFIRM_BUTTON));
    }

    public void clickCancelButton() {
        driver.findElement(CANCEL_BUTTON).click();
    }

    public void clickConfirmButton() {
        driver.findElement(CONFIRM_BUTTON).click();
    }

    public void logout(String mode) {
        navigateToLogoutPage();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.elementToBeClickable(CONFIRM_BUTTON));
        wait.until(ExpectedConditions.elementToBeClickable(CANCEL_BUTTON));

        if ("confirm".equals(mode)) {
            clickConfirmButton();
            waitForRedirectionToLogin();
        } else {
            clickCancelButton();
            waitForRedirectionToHome();
        }
    }

    public void waitForRedirectionToLogin() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE_URL + LOGIN_PATH));
    }

    public void waitForRedirectionToHome() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE_URL + HOME_PATH));
    }

    public String getLogoutErrorMessage() {
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
