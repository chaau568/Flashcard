package com.chaau568.flashcards.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ViewFlashcardPublicPage {
    private final WebDriver driver;

    private final String deckId;
    private static final String PUBLICFLASHCARD_PATH = "/deck_public";
    private final String FRONTEND_BASE_URL;

    private final By STEP1_CONTAINER = By.id("deck-step-1");
    private final By STEP2_CONTAINER = By.id("deck-step-2");
    private final By DECK_NAME_HEADER = By.tagName("h2");
    private final By BTN_VIEW_DECK = By.id("btn-view-deck");
    private final By BTN_SHOW_ANSWER = By.id("btn-show-answer");
    private final By BTN_NEXT_CARD = By.id("btn-next-card");
    private final By BTN_EXIT_FINISHED = By.id("btn-exit-finished");
    private final By CARD_NUMBER = By.id("card-number");
    private final By CARD_CONTENT = By.id("card-content");

    public ViewFlashcardPublicPage(WebDriver driver, String frontendBaseUrl, String deckId) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
        this.deckId = deckId;
    }

    public void waitForContentLoad() {
        driver.get(FRONTEND_BASE_URL + PUBLICFLASHCARD_PATH + "/" + deckId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(STEP1_CONTAINER));
    }

    public boolean isStep1Visible() {
        try {
            return driver.findElement(STEP1_CONTAINER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isStep2Visible() {
        try {
            return driver.findElement(STEP2_CONTAINER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isViewButtonVisible() {
        try {
            return driver.findElement(BTN_VIEW_DECK).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isShowAnswerButtonVisible() {
        try {
            return driver.findElement(BTN_SHOW_ANSWER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isNextButtonVisible() {
        try {
            return driver.findElement(BTN_NEXT_CARD).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isH2Visible() {
        try {
            return driver.findElement(DECK_NAME_HEADER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isExitFinishedButtonVisible() {
        try {
            return driver.findElement(BTN_EXIT_FINISHED).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isCardContentVisible() {
        try {
            return driver.findElement(CARD_CONTENT).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickViewDeck() {
        driver.findElement(BTN_VIEW_DECK).click();
    }

    public void clickShowAnswer() {
        driver.findElement(BTN_SHOW_ANSWER).click();
    }

    public void clickNextCard() {
        driver.findElement(BTN_NEXT_CARD).click();
    }

    public void clickExitFinished() {
        driver.findElement(BTN_EXIT_FINISHED).click();
    }

    public int getCardNumber() {
        try {
            String text = driver.findElement(CARD_NUMBER).getText();

            String numberString = text.split(": ")[1].trim();

            return Integer.parseInt(numberString);

        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.err.println("Element 'card-number' not found.");
            return 0;
        } catch (Exception e) {
            System.err.println("Failed to parse card number: " + e.getMessage());
            return 0;
        }
    }

}
