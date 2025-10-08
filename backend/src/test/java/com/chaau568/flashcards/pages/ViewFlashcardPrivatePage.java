package com.chaau568.flashcards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ViewFlashcardPrivatePage {
    private final WebDriver driver;

    private final String deckId;
    private static final String PRIVATEFLASHCARE_PATH = "/deck_owner";
    private final String FRONTEND_BASE_URL;

    private final By STEP1_CONTAINER = By.id("deck-step-1");
    private final By STEP2_CONTAINER = By.id("deck-step-2");
    private final By STEP3_CONTAINER = By.id("deck-step-3");
    private final By DECK_NAME_HEADER = By.tagName("h2");
    private final By CARD_NUMBER = By.id("card-number");
    private final By BTN_EDIT_DECK = By.id("btn-edit-deck");
    private final By BTN_PLAY_DECK = By.id("btn-play-deck");
    private final By BTN_REMOVE_DECK = By.id("btn-remove-deck");
    private final By CARD_CONTENT = By.id("card-content");
    private final By BTN_SHOW_ANSWER = By.id("btn-show-answer");
    private final By BTN_SELECT_PROGRESS = By.id("btn-select-progress");
    private final By BTN_EASY_PROGRESS = By.cssSelector("button#btn-select-progress:first-of-type");

    public ViewFlashcardPrivatePage(WebDriver driver, String frontendBaseUrl, String deckId) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
        this.deckId = deckId;
    }

    public void waitForContentLoad() {
        driver.get(FRONTEND_BASE_URL + PRIVATEFLASHCARE_PATH + "/" + deckId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
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

    public boolean isStep3Visible() {
        try {
            return driver.findElement(STEP3_CONTAINER).isDisplayed();
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

    public boolean isEditButtonVisible() {
        try {
            return driver.findElement(BTN_EDIT_DECK).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isPlayButtonVisible() {
        try {
            return driver.findElement(BTN_PLAY_DECK).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isRemoveButtonVisible() {
        try {
            return driver.findElement(BTN_REMOVE_DECK).isDisplayed();
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

    public boolean isSelectProgressButtonVisible() {
        try {
            return driver.findElement(BTN_SELECT_PROGRESS).isDisplayed();
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

    public void clickEditDeck() {
        driver.findElement(BTN_EDIT_DECK).click();
    }

    public void clickPlayDeck() {
        driver.findElement(BTN_PLAY_DECK).click();
    }

    public void clickRemoveDeck() {
        driver.findElement(BTN_REMOVE_DECK).click();
    }

    public void clickShowAnswer() {
        driver.findElement(BTN_SHOW_ANSWER).click();
    }

    public void clickSelectProgress() {
        driver.findElement(BTN_SELECT_PROGRESS).click();
    }

    public void clickEasyProgress() {
        driver.findElement(BTN_EASY_PROGRESS).click();
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
