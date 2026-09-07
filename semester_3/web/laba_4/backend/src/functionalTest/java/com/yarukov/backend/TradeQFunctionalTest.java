package com.yarukov.backend;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TradeQFunctionalTest{

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void init(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

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
        page.navigate("https://ru.tradingview.com/markets");
    }

    @AfterEach
    void tearDown() {
        if (context != null) context.close();
    }



    @Test
    @DisplayName("IndexCheck")
    void testIndex(){
        String text  = (page.locator("span.title-eWiky_c0").nth(0).textContent());
        String text1  = (page.locator("span.title-eWiky_c0").nth(1).textContent());
        boolean check = false;


        if(text.contains("Индекс МосБиржи") && text1.contains("Индекс МосБиржи (дополнительная сессия)")){
            check = true;
        }
        assertTrue(check);
        /*assertThat(page.locator(".title-eHRarde6 .apply-overflow-tooltip").nth(1)).containsText("Индекс МосБиржы (дополнительная сессия)");
        assertThat(page.locator(".title-eHRarde6 .apply-overflow-tooltip").nth(2)).containsText("Индекс РТС");*/
    }
}














