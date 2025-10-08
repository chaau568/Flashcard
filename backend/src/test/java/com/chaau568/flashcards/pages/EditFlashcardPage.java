package com.chaau568.flashcards.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditFlashcardPage {
    private final WebDriver driver;

    private final String deckId;
    private static final String EDIT_DECK_PATH = "/deck_update";
    private final String FRONTEND_BASE_URL;

    private final By DECK_NAME_HEADER = By.tagName("h2");
    private final By DECK_NAME_INPUT = By.id("deck-name-input");
    private final By DECK_PUBLIC_RADIO = By.id("public-radio");
    private final By DECK_PRIVATE_RADIO = By.id("private-radio");
    private final By BTN_ADD_TAG = By.id("add-tag-btn");
    private final By FRONT_CARD_INPUT = By.id("front-card-input");
    private final By BACK_CARD_INPUT = By.id("back-card-input");
    private final By BTN_PREVIOUS = By.id("previous-btn");
    private final By BTN_NEXT = By.id("next-btn");
    private final By BTN_DELETE_CARD = By.id("delete-card-btn");
    private final By BTN_ADD_CARD = By.id("add-card-btn");
    private final By BTN_SAVE = By.id("save-btn");

    public EditFlashcardPage(WebDriver driver, String frontendBaseUrl, String deckId) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
        this.deckId = deckId;
    }

    public void waitForContentLoad() {
        driver.get(FRONTEND_BASE_URL + EDIT_DECK_PATH + "/" + deckId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DECK_NAME_HEADER));
    }

    public boolean isDeckNameVisible() {
        try {
            return driver.findElement(DECK_NAME_HEADER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isDeckNameInputVisible() {
        try {
            return driver.findElement(DECK_NAME_INPUT).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isPublicRadioVisible() {
        try {
            return driver.findElement(DECK_PUBLIC_RADIO).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isPrivateRadioVisible() {
        try {
            return driver.findElement(DECK_PRIVATE_RADIO).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isFrontCardInputVisible() {
        try {
            return driver.findElement(FRONT_CARD_INPUT).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isBackCardInputVisible() {
        try {
            return driver.findElement(BACK_CARD_INPUT).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickAddTag() {
        driver.findElement(BTN_ADD_TAG).click();
    }

    public void clickPrevious() {
        driver.findElement(BTN_PREVIOUS).click();
    }

    public void clickNext() {
        driver.findElement(BTN_NEXT).click();
    }

    public void clickDeleteCard() {
        driver.findElement(BTN_DELETE_CARD).click();
    }

    public void clickAddCard() {
        driver.findElement(BTN_ADD_CARD).click();
    }

    public void clickSave() {
        driver.findElement(BTN_SAVE).click();
    }

    public void addCard(String frontcard, String backcard) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement frontcardInput = wait.until(ExpectedConditions.visibilityOfElementLocated(FRONT_CARD_INPUT));
        WebElement backcardInput = wait.until(ExpectedConditions.visibilityOfElementLocated(BACK_CARD_INPUT));

        frontcardInput.clear();
        backcardInput.clear();

        frontcardInput.sendKeys(frontcard);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        backcardInput.sendKeys(backcard);
    }
}
