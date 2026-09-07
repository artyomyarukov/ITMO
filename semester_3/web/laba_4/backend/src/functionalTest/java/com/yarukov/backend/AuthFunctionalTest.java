package com.yarukov.backend;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthFunctionalTest {
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;
    private static final String APP_URL = "http://localhost:3000";

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500)
        );

    }

    @AfterAll
    static void tearDownBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setLocale("ru-RU"));
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        if (context != null) context.close();
    }

    @Test
    @Order(1)
    @DisplayName("tc-01: Успешная авторизация пользователя")
    void testLoginSuccess() {
        page.navigate(APP_URL);
        page.locator("input[placeholder='Имя пользователя']").fill("testuser");
        page.locator("input[placeholder='Пароль']").fill("password123");
        page.locator("button.auth-button").click();
        page.waitForTimeout(2000);
        assertThat(page.locator(".header-bar")).containsText("testuser");
    }

    @Test
    @Order(2)
    @DisplayName("tc-02: Ошибка авторизации (неверный пароль)")
    void testLoginInvalidPassword() {
        page.navigate(APP_URL);
        page.locator("input[placeholder='Имя пользователя']").fill("testuser");
        page.locator("input[placeholder='Пароль']").fill("wrong_password");
        page.onceDialog(
                dialog -> {
                    assertEquals("Неверное имя пользователя или пароль", dialog.message());
                    dialog.accept();
                }
        );
        page.locator("button[type='submit']").click();
    }

    @Test
    @Order(3)
    @DisplayName("tc-03: Ошибка регистрации нового аккаунта (пользователь уже существует)")
    void testUserExist() {
        page.navigate(APP_URL);
        page.locator(".toggle-text").click();
        page.locator("input[placeholder='Имя пользователя']").fill("artem");
        page.locator("input[placeholder='Пароль']").fill("password123");
        page.onceDialog(
                dialog -> {
                    assertEquals("Имя пользователя уже занято", dialog.message());
                    dialog.accept();
                }
        );
        page.locator("button[type='submit']").click();
    }

    @Test
    @Order(4)
    @DisplayName("tc-04: Регистрация нового уникального пользователя в системе")
    void testUserSuccesReguster() {
        page.navigate(APP_URL);
        page.locator(".toggle-text").click();
        String uniqueUsername = "user_" + System.currentTimeMillis();
        page.locator("input[placeholder='Имя пользователя']").fill(uniqueUsername);
        page.locator("input[placeholder='Пароль']").fill("password123");
        page.onceDialog(
                dialog -> {
                    assertEquals("Регистрация успешна! Теперь войдите.", dialog.message());
                    dialog.accept();
                }
        );
        page.locator("button[type='submit']").click();
    }

    @Test
    @Order(5)
    @DisplayName("tc-05: Выход из системы")
    void exitFromMainPage(){
        page.navigate(APP_URL);
        page.locator("input[placeholder='Имя пользователя']").fill("testuser");
        page.locator("input[placeholder='Пароль']").fill("password123");
        page.locator("button.auth-button").click();
        page.locator(".header-bar button").click();
        assertThat(page.locator(".auth-container")).isVisible();
    }


}
