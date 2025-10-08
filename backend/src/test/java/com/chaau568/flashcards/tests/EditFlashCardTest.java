package com.chaau568.flashcards.tests;

import com.chaau568.flashcards.base.BaseTest;
import com.chaau568.flashcards.pages.EditFlashcardPage;
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

public class EditFlashCardTest extends BaseTest {

    private LoginPage loginPage;
    private LogoutPage logoutPage;
    private InventoryPage inventoryPage;
    private ViewFlashcardPrivatePage viewFlashcardPrivatePage;
    private EditFlashcardPage editFlashcardPage;

    private static final String TEST_USER = "testedituser";
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

    @Test // testcase 14 : test edit flashcard => สำเร็จ
          // (สามารถดึงข้อมูลมาแสดงได้อย่างถูกต้อง โดยใช้ deck id
          // และอัทเดตข้อมูลได้ถูกต้อง)
    void testEditOwnerFlashcardPage_LoadsCorrectly() {
        inventoryPage.waitForContentLoad();

        testDeckId = inventoryPage.clickVisitFlashcard();

        viewFlashcardPrivatePage = new ViewFlashcardPrivatePage(driver, FRONTEND_BASE, testDeckId);

        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait1.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/deck_owner/" + testDeckId));

        String expectedUrl = FRONTEND_BASE + "/deck_owner/" + testDeckId;
        assertEquals(expectedUrl, driver.getCurrentUrl(), "Deck Owner Page must have url like this.");

        viewFlashcardPrivatePage.clickEditDeck();

        editFlashcardPage = new EditFlashcardPage(driver, FRONTEND_BASE, testDeckId);

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait2.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/deck_update/" + testDeckId));

        expectedUrl = FRONTEND_BASE + "/deck_update/" + testDeckId;
        assertEquals(expectedUrl, driver.getCurrentUrl(), "Deck Update Page must have url like this.");

        assertTrue(editFlashcardPage.isDeckNameVisible() && editFlashcardPage.isDeckNameInputVisible(),
                "In deck update must have deck.");

        editFlashcardPage.clickAddCard();
        assertTrue(editFlashcardPage.isFrontCardInputVisible() && editFlashcardPage.isBackCardInputVisible(),
                "Deck Update Page must have front and back card input.");
        editFlashcardPage.addCard("Test Add Front Card", "Test Add Back Card");

        editFlashcardPage.clickSave();

        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait3.until(ExpectedConditions.urlToBe(FRONTEND_BASE + "/inventory"));

        expectedUrl = FRONTEND_BASE + "/inventory";
        assertEquals(expectedUrl, driver.getCurrentUrl(), "After Save Flashcard, it must navigate to inventory page.");
    }
}
