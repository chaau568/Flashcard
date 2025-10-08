package com.chaau568.flashcards.tests;

import com.chaau568.flashcards.base.BaseTest;
import com.chaau568.flashcards.pages.InventoryPage;
import com.chaau568.flashcards.pages.LoginPage;
import com.chaau568.flashcards.pages.LogoutPage;
import com.chaau568.flashcards.pages.ViewFlashcardPrivatePage;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteFlashCardTest extends BaseTest {
    private LoginPage loginPage;
    private LogoutPage logoutPage;
    private InventoryPage inventoryPage;
    private ViewFlashcardPrivatePage viewFlashcardPrivatePage;

    private static final String TEST_USER = "testdeleteuser";
    private static final String TEST_PASS = "1234";

    private String testDeckId;

    @BeforeEach
    void setupTest() throws Exception {
        driver.manage().deleteAllCookies();
        loginPage = new LoginPage(driver, FRONTEND_BASE);
        logoutPage = new LogoutPage(driver, FRONTEND_BASE);
        inventoryPage = new InventoryPage(driver, FRONTEND_BASE);

        loginPage.login(TEST_USER, TEST_PASS);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/"));

        driver.get(FRONTEND_BASE + "/");
    }

    @AfterEach
    void teardownTest() {
        driver.get(FRONTEND_BASE + "/logout");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/logout"));

        logoutPage.logout("confirm");
    }

    @Test // testcase 15 : test remove flashcard => สำเร็จ (สามารถลบ flashcard โดยใช้ deck
          // id)
    void testDeleteOwnerFlashcard_LoadsCorrectly() {
        inventoryPage.waitForContentLoad();

        testDeckId = inventoryPage.clickVisitFlashcard();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/deck_owner/" + testDeckId));

        String expectedUrl = FRONTEND_BASE + "/deck_owner/" + testDeckId;
        assertEquals(expectedUrl, driver.getCurrentUrl(), "Click Visit Flashcard must navigate to deck owner.");

        viewFlashcardPrivatePage = new ViewFlashcardPrivatePage(driver, FRONTEND_BASE, testDeckId);

        assertTrue(viewFlashcardPrivatePage.isRemoveButtonVisible(), "In owner deck must have remove button.");
        viewFlashcardPrivatePage.clickRemoveDeck();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/inventory"));
        assertEquals(FRONTEND_BASE + "/inventory", driver.getCurrentUrl(),
                "After deleting deck must navigate to inventory page.");
    }
}
