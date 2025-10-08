package com.chaau568.flashcards.tests;

import com.chaau568.flashcards.base.BaseTest;
import com.chaau568.flashcards.pages.FinishPlayFlashcardPage;
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

public class PlayPrivateFlashcardTest extends BaseTest {

    private LoginPage loginPage;
    private LogoutPage logoutPage;
    private InventoryPage inventoryPage;
    private ViewFlashcardPrivatePage viewFlashcardPrivatePage;
    private FinishPlayFlashcardPage finishPlayFlashcardPage;

    private static final String TEST_USER = "admin";
    private static final String TEST_PASS = "admin1234";

    private String testDeckId;

    @BeforeEach
    void setupTest() throws Exception {
        driver.manage().deleteAllCookies();
        loginPage = new LoginPage(driver, FRONTEND_BASE);
        logoutPage = new LogoutPage(driver, FRONTEND_BASE);
        inventoryPage = new InventoryPage(driver, FRONTEND_BASE);
        finishPlayFlashcardPage = new FinishPlayFlashcardPage(driver, FRONTEND_BASE);

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

    @Test // testcase 12 : test play flashcard => สำเร็จ
    // (สามารถดึงข้อมูลมาแสดงได้อย่างถูกต้อง โดยใช้ deck id private
    // และอัทเดตข้อมูลได้ถูกต้อง)
    void testPlayOwnerFlashcardPage_LoadsCorrectly() {
        inventoryPage.waitForContentLoad();

        testDeckId = inventoryPage.clickVisitFlashcard();

        viewFlashcardPrivatePage = new ViewFlashcardPrivatePage(driver, FRONTEND_BASE, testDeckId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/deck_owner/" + testDeckId));

        String expectedUrl = FRONTEND_BASE + "/deck_owner/" + testDeckId;
        assertEquals(expectedUrl, driver.getCurrentUrl());
        assertTrue(viewFlashcardPrivatePage.isStep1Visible() && viewFlashcardPrivatePage.isH2Visible()
                && viewFlashcardPrivatePage.isEditButtonVisible() && viewFlashcardPrivatePage.isPlayButtonVisible()
                && viewFlashcardPrivatePage.isRemoveButtonVisible(),
                "Private Flashcard page must display container step1, deck name, edit button, play button and remove button.");

        int card_number = viewFlashcardPrivatePage.getCardNumber();

        viewFlashcardPrivatePage.clickPlayDeck();
        assertTrue(viewFlashcardPrivatePage.isH2Visible() && viewFlashcardPrivatePage.isShowAnswerButtonVisible(),
                "Click play private flashcard must display deck name and show answer button.");

        for (int i = 0; i < card_number; i++) {
            assertTrue(viewFlashcardPrivatePage.isCardContentVisible(),
                    "Click Next Button must display Front Card.");

            viewFlashcardPrivatePage.clickShowAnswer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            assertTrue(viewFlashcardPrivatePage.isCardContentVisible(),
                    "Click Show Answer Button must display Back Card.");

            viewFlashcardPrivatePage.clickEasyProgress();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        expectedUrl = FRONTEND_BASE + "/deck_finish";
        assertEquals(expectedUrl, driver.getCurrentUrl(), "When play finished, will navigate to deck finish.");
        assertTrue(finishPlayFlashcardPage.isH2Visible() && finishPlayFlashcardPage.isExitButtonVisible(),
                "Finish Deck Page must display deck name and exit button");

        finishPlayFlashcardPage.clickEditDeck();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        expectedUrl = FRONTEND_BASE + "/inventory";
        assertEquals(expectedUrl, driver.getCurrentUrl(), "Click exit button must navigate to inventory page.");
    }
}
