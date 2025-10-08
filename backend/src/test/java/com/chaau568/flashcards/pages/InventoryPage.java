package com.chaau568.flashcards.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage {
    private final WebDriver driver;

    private static final String INVENTORY_PATH = "/inventory";
    private final String FRONTEND_BASE_URL;

    private final By NO_DECKS_FOUND = By.id("no-decks-found");
    private final By LOADING_INDICATOR = By.id("loading-decks");
    private final By DECK_LIST_CONTAINER = By.id("deck-list-container");
    private final By ADD_NEW_CARD_BUTTON = By.id("btn-add-new-card");
    private final By FIRST_DECK_ITEM = By.cssSelector("#deck-list-container > div:first-child");

    public InventoryPage(WebDriver driver, String frontendBaseUrl) {
        this.driver = driver;
        this.FRONTEND_BASE_URL = frontendBaseUrl;
    }

    public void waitForContentLoad() {
        driver.get(FRONTEND_BASE_URL + INVENTORY_PATH);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_INDICATOR));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(NO_DECKS_FOUND),
                ExpectedConditions.visibilityOfElementLocated(DECK_LIST_CONTAINER),
                ExpectedConditions.visibilityOfElementLocated(ADD_NEW_CARD_BUTTON)));
    }

    public boolean isNoDecksMessageDisplayed() {
        try {
            return driver.findElement(NO_DECKS_FOUND).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isDeckListVisible() {
        try {
            return driver.findElement(DECK_LIST_CONTAINER).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isAddNewCardButtonVisible() {
        try {
            return driver.findElement(ADD_NEW_CARD_BUTTON).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickAddNewCard() {
        driver.findElement(ADD_NEW_CARD_BUTTON).click();
    }

    public String clickVisitFlashcard() {
        driver.findElement(FIRST_DECK_ITEM).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        wait.until(ExpectedConditions.urlMatches(FRONTEND_BASE_URL + "/deck_owner/[a-zA-Z0-9-]+"));

        String currentUrl = driver.getCurrentUrl();

        String[] urlParts = currentUrl.split("/");

        if (urlParts.length < 2) {
            throw new RuntimeException("URL is too short to extract deckId: " + currentUrl);
        }

        String deckId = urlParts[urlParts.length - 1];

        if (deckId.isEmpty()) {
            throw new RuntimeException("Extracted deckId is empty. Current URL: " + currentUrl);
        }

        return deckId;
    }

}
