package com.chaau568.flashcards.tests;

import com.chaau568.flashcards.base.BaseTest;
import com.chaau568.flashcards.entity.User;
import com.chaau568.flashcards.pages.CreateFlashcardPage;
import com.chaau568.flashcards.pages.HomePage;
import com.chaau568.flashcards.pages.InventoryPage;
import com.chaau568.flashcards.pages.LoginPage;
import com.chaau568.flashcards.pages.ViewFlashcardPublicPage;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardManagementTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private InventoryPage inventoryPage;
    private ViewFlashcardPublicPage viewFlashcardPublicPage;
    private CreateFlashcardPage createFlashcardPage;

    private static final String TEST_USER = "nobodyahhh";
    private static final String TEST_PASS = "nobodyahhh12";

    private String testUserId;
    private String testDeckId;

    @BeforeEach
    void setupTest() throws Exception {
        driver.manage().deleteAllCookies();
        loginPage = new LoginPage(driver, FRONTEND_BASE);
        homePage = new HomePage(driver, FRONTEND_BASE);
        inventoryPage = new InventoryPage(driver, FRONTEND_BASE);

        try {
            User user = new User();
            user.setUsername(TEST_USER);
            user.setPassword(TEST_PASS);
            testUserId = apiHelper.register(user);

        } catch (RuntimeException e) {
            if (!e.getMessage().contains("already exists")) {
                throw e;
            }
        }

        loginPage.login(TEST_USER, TEST_PASS);

        if (testUserId == null) {
            testUserId = apiHelper.getUserIdFromSession();
        }

        driver.get(FRONTEND_BASE + "/");
    }

    @AfterEach
    void teardownTest() throws Exception {
        try {
            apiHelper.login(TEST_USER, TEST_PASS);
            testDeckId = apiHelper.getLastCreatedDeckId();
            if (testDeckId != null) {
                apiHelper.deleteDeck(testDeckId);
                System.out.println("Deleted deck: " + testDeckId);
            }

            if (testUserId != null) {
                apiHelper.deleteAccount(testUserId);
                System.out.println("Deleted test user: " + TEST_USER);
            }

        } catch (Exception e) {
            System.err.println("Failed to cleanup test: " + e.getMessage());
        }
    }

    @Test // testcase 9 : test home => สำเร็จ (สามารถดึงข้อมูลได้ถูกต้อง จาก deck ที่เป็น
          // public)
    void testHomePage_LoadsCorrectly() {
        homePage.waitForContentLoad();

        String expectedUrl = FRONTEND_BASE + "/";
        assertEquals(expectedUrl, driver.getCurrentUrl());

        assertTrue(homePage.isNoDecksMessageDisplayed() || homePage.isDeckListVisible(),
                "Home page must display either decks or the 'No decks found' message after loading.");
    }

    @Test // testcase 10 : test inventory => สำเร็จ (สามารถดึงข้อมูล deck ทั้งหมดจาก id
          // ของเจ้าของ deck)
    void testInventoryPage_LoadsCorrectly() {
        inventoryPage.waitForContentLoad();

        String expectedUrl = FRONTEND_BASE + "/inventory";
        assertEquals(expectedUrl, driver.getCurrentUrl());

        assertTrue(inventoryPage.isNoDecksMessageDisplayed() || inventoryPage.isDeckListVisible(),
                "Inventory page must display either decks or the 'No decks found' message after loading.");

        assertTrue(inventoryPage.isAddNewCardButtonVisible(),
                "Inventory page must display add new card button.");
    }

    @Test // testcase 11 : test view flashcard => สำเร็จ
          // (สามารถดึงข้อมูลมาแสดงได้อย่างถูกต้อง โดยใช้ deck id public)
    void testViewPublicFlashcardPage_LoadsCorrectly() {
        homePage.waitForContentLoad();

        testDeckId = homePage.clickVisitFlashcard();

        viewFlashcardPublicPage = new ViewFlashcardPublicPage(driver, FRONTEND_BASE, testDeckId);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/deck_public/" + testDeckId));

        String expectedUrl = FRONTEND_BASE + "/deck_public/" + testDeckId;
        assertEquals(expectedUrl, driver.getCurrentUrl());
        assertTrue(viewFlashcardPublicPage.isStep1Visible() && viewFlashcardPublicPage.isH2Visible(),
                "Public Flashcard page must display container step1 and deck name.");

        int card_number = viewFlashcardPublicPage.getCardNumber();

        viewFlashcardPublicPage.clickViewDeck();
        assertTrue(viewFlashcardPublicPage.isH2Visible() && viewFlashcardPublicPage.isShowAnswerButtonVisible(),
                "Click view public flashcard must display deck name and show answer button.");

        for (int i = 0; i < card_number; i++) {
            assertTrue(viewFlashcardPublicPage.isCardContentVisible(),
                    "Click Next Button must display Front Card.");

            viewFlashcardPublicPage.clickShowAnswer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            assertTrue(viewFlashcardPublicPage.isCardContentVisible(),
                    "Click Show Answer Button must display Back Card.");

            viewFlashcardPublicPage.clickNextCard();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        assertTrue(viewFlashcardPublicPage.isExitFinishedButtonVisible(),
                "View flashcard finished must display Exit Button.");

        viewFlashcardPublicPage.clickExitFinished();

        expectedUrl = FRONTEND_BASE + "/";
        assertEquals(expectedUrl, driver.getCurrentUrl(), "Click Exit Button must navigate to Home Page.");
    }

    @Test // testcase 13 : test create flashcard => สำเร็จ (ข้อมูลครบท่วน)
    void testCreateFlashcard_Success() {
        inventoryPage.waitForContentLoad();

        inventoryPage.clickAddNewCard();
        String expectedUrl = FRONTEND_BASE + "/deck_create";
        assertEquals(expectedUrl, driver.getCurrentUrl(), "Click Add New Flashcard must navigate to Create Deck Page");

        createFlashcardPage = new CreateFlashcardPage(driver, FRONTEND_BASE);
        assertTrue(
                createFlashcardPage.isStep1Visible() && createFlashcardPage.isHeaderVisible()
                        && createFlashcardPage.isDeckNameInputVisible() && createFlashcardPage.isTagNameInputVisible(),
                "Create Deck Page must visible step1, header, deckname and tagname.");

        createFlashcardPage.clickPublicDeck();
        createFlashcardPage.setDeckAndTagName("Create From Selenium", "Testcase 13");
        createFlashcardPage.clickAddTag();
        createFlashcardPage.clickCreateDeck();

        assertTrue(createFlashcardPage.isStep2Visible() && createFlashcardPage.isFrontCardVisible()
                && createFlashcardPage.isBackCardVisible(),
                "After creating the deck, it must have step2, frontcard adn backcard");

        createFlashcardPage.setFrontAndBackCard("What subjects will we get an A?", "Software Testing");
        createFlashcardPage.clickFinish();

        assertTrue(createFlashcardPage.isStep3Visible(), "After add front and back card, it must have step3,");
        createFlashcardPage.clickSubmit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/inventory"));

        expectedUrl = FRONTEND_BASE + "/inventory";
        assertEquals(expectedUrl, driver.getCurrentUrl(),
                "After click submit button, it must navigate to inventory page.");
    }
}
