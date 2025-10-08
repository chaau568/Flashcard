package com.chaau568.flashcards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FinishPlayFlashcardPage {
    private final WebDriver driver;

    private static final String FINISHPLAYFLASHCARD_PATH = "/deck_finish";
    private final String FRONTEND_BASE_URL;

    public final By DECK_NAME_HEADER = By.tagName("h2");
    public final By BTN_EXIT = By.id("btn-exit");

    public FinishPlayFlashcardPage(WebDriver driver, String frontendBaseUrl) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
    }

    public void waitForContentLoad() {
        driver.get(FRONTEND_BASE_URL + FINISHPLAYFLASHCARD_PATH);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(BTN_EXIT));
    }

    public boolean isH2Visible() {
        try {
            return driver.findElement(DECK_NAME_HEADER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isExitButtonVisible() {
        try {
            return driver.findElement(BTN_EXIT).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickEditDeck() {
        driver.findElement(BTN_EXIT).click();
    }
}
