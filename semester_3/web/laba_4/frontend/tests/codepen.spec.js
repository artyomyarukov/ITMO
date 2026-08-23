const {test,expect} = require('@playwright/test')


test( 'Ввод данных', async ({page}) => {
    await page.goto('https://codepen.io/');
    const login = page.locator('[data-test-id="signup-button"]');
    await login.click();
    await expect(page).toHaveURL(/.*signup.*/);



    });






