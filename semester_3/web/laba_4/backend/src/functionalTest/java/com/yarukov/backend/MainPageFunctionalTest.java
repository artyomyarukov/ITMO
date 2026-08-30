package com.yarukov.backend;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainPageFunctionalTest {


    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;


    private static final String APP_URL = "http://localhost:3000";

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }
    @AfterAll
    static void tearDownBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
    @BeforeEach
    void setUp() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setLocale("ru-RU")
        );
        page = context.newPage();
        page.navigate(APP_URL);
        page.locator("input[placeholder='Имя пользователя']").fill("testuser");
        page.locator("input[placeholder='Пароль']").fill("password123");
        page.locator("button.auth-button").click();
        assertThat(page.locator(".header-bar")).isVisible();
    }
    @AfterEach
    void tearDown() {
        if (context != null) context.close();
    }

    private void submitPoint(String x,String y,String r){
        page.locator(".control-group").nth(0).locator("button:text-is('" + x + "')").click();
        page.locator(".control-group").nth(1).locator("input").fill(y);
        page.locator(".control-group").nth(2).locator("button:text-is('" + r + "')").click();
        page.locator("button.send-btn").click();
    }
    @Test
    @Order(6)
    @DisplayName("tc-06: Попытка отправки координаты Y, выходящей за нижнюю границу диапазона")
    void testYBelowLowerBound() {
        page.onceDialog(
                dialog -> {
                    assertTrue(dialog.message().contains("Некорректные данные"));
                    dialog.accept();
                }
        );
        submitPoint("0","-4","0.5");

    }
    @Test
    @Order(7)
    @DisplayName("tc-07: Попытка отправки координаты Y, выходящей за верхнюю границу диапазона ")
    void testYAboveBound() {
        page.onceDialog(
                dialog -> {
                    assertTrue(dialog.message().contains("Некорректные данные"));
                    dialog.accept();
                }
        );
        submitPoint("0","6","0.5");
    }
    @Test
    @Order(8)
    @DisplayName("tc-08: Граничное допустимое Y (-2.99)")
    void testYLowerValid(){
        submitPoint("0","-2.99","0.5");
        assertThat(page.locator("table tbody tr").first()).containsText("-2.99");

    }

    @Test
    @Order(9)
    @DisplayName("tc-09: Граничное допустимое Y (4.99)")
    void testYUpperValid() {
        submitPoint("0", "4.99", "2");
        assertThat(page.locator("table tbody tr").first()).containsText("4.99");
    }

    @Test
    @Order(10)
    @DisplayName("tc-10: Недопустимая Y (-3)")
    void testInvalidLowerY(){
        page.onceDialog(
                dialog -> {
                    assertTrue(dialog.message().contains("Некорректные данные"));
                    dialog.accept();
                }
        );
        submitPoint("0","-3","0.5");
    }

    @Test
    @Order(11)
    @DisplayName("tc-11: Недопустимая Y (5)")
    void testInvalidUpperY(){
        page.onceDialog(
                dialog -> {
                    assertTrue(dialog.message().contains("Некорректные данные"));
                    dialog.accept();
                }
        );
        submitPoint("0","5","0.5");
    }
    @Test
    @Order(12)
    @DisplayName("tc-12: Отправка валидных X,Y и R")
    void testSendCustomCoordinates() {
        submitPoint("-1.5", "2", "3");
        assertThat(page.locator("table tbody tr").first()).containsText("-1.5");
        assertThat(page.locator("table tbody tr").first()).containsText("3");
    }
    @Test
    @Order(13)
    @DisplayName("tc-13:  Попадание")
    void testHit(){
        submitPoint("0.5", "1", "2");
        assertThat(page.locator("table tbody tr").first()).containsText("Попал");
    }
    @Test
    @Order(14)
    @DisplayName("tc-14:  Промах")
    void testMiss(){
        submitPoint("1", "1", "1");
        assertThat(page.locator("table tbody tr").first()).containsText("Мимо");
    }
    @Test
    @Order(15)
    @DisplayName("tc-15: Попадание на границе")
    void testHitBounf(){
        submitPoint("1", "2", "2");
        assertThat(page.locator("table tbody tr").first()).containsText("Попал");
    }

}
