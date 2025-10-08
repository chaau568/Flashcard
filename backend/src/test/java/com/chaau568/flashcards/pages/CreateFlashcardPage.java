package com.chaau568.flashcards.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreateFlashcardPage {
    private final WebDriver driver;

    private static final String DECK_CREATE_PATH = "/deck_create";
    private final String FRONTEND_BASE_URL;

    private final By STEP1_CONTAINER = By.id("deck_step_1");
    private final By STEP2_CONTAINER = By.id("deck_step_2");
    private final By STEP3_CONTAINER = By.id("deck_step_3");
    private final By HEADER = By.tagName("h2");
    private final By BTN_PUBLIC_DECK = By.id("public");
    private final By BTN_PRIVATE_DECK = By.id("private");
    private final By DECK_NAME = By.id("deck_name");
    private final By TAG_NAME = By.id("tag_name");
    private final By BTN_ADD_TAG = By.id("btn_add_tag");
    private final By BTN_CREATE_DECK = By.id("btn_create_deck");
    private final By FRONT_CARD = By.id("front_card");
    private final By BACK_CARD = By.id("back_card");
    private final By BTN_FINISH = By.id("btn_finish");
    private final By BTN_SUBMIT_DECK = By.id("btn_submit_deck");

    public CreateFlashcardPage(WebDriver driver, String frontendBaseUrl) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
    }

    public void waitForContentLoad() {
        driver.get(FRONTEND_BASE_URL + DECK_CREATE_PATH);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
        }

        wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(STEP1_CONTAINER),
                ExpectedConditions.invisibilityOfElementLocated(HEADER)));
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

    public boolean isHeaderVisible() {
        try {
            return driver.findElement(HEADER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickPublicDeck() {
        driver.findElement(BTN_PUBLIC_DECK).click();
    }

    public void clickPrivateDeck() {
        driver.findElement(BTN_PRIVATE_DECK).click();
    }

    public boolean isDeckNameInputVisible() {
        try {
            return driver.findElement(DECK_NAME).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isTagNameInputVisible() {
        try {
            return driver.findElement(TAG_NAME).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickAddTag() {
        driver.findElement(BTN_ADD_TAG);
    }

    public void clickCreateDeck() {
        driver.findElement(BTN_CREATE_DECK).click();
    }

    public boolean isFrontCardVisible() {
        try {
            return driver.findElement(FRONT_CARD).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isBackCardVisible() {
        try {
            return driver.findElement(BACK_CARD).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickFinish() {
        driver.findElement(BTN_FINISH).click();
    }

    public void clickSubmit() {
        driver.findElement(BTN_SUBMIT_DECK).click();
    }

    public void setDeckAndTagName(String deckName, String tagName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement deckNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(DECK_NAME));
        WebElement tagNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(TAG_NAME));

        deckNameInput.clear();
        tagNameInput.clear();

        deckNameInput.sendKeys(deckName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        tagNameInput.sendKeys(tagName);
    }

    public void setFrontAndBackCard(String frontcard, String backcard) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.visibilityOfElementLocated(STEP2_CONTAINER));
        WebElement frontcardInput = wait.until(ExpectedConditions.visibilityOfElementLocated(FRONT_CARD));
        WebElement backcardInput = wait.until(ExpectedConditions.visibilityOfElementLocated(BACK_CARD));

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