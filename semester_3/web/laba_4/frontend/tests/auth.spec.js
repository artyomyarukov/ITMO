const {test, expect} = require('@playwright/test')

test.describe('Тестирование страниц авторизации', () => {
        test.beforeEach(async ({page}) => {
            page.setDefaultNavigationTimeout(60000);
            await page.goto('http://localhost:3000');
        })


        test('tc-01: успешный вход в систему', async ({page}) => {
            await page.locator('input[type="text"]').fill('testuser');
            await page.locator('input[type="password"]').fill('password123');
            await page.locator('button.auth-button').click();
            const canvas = page.locator('canvas.graph-canvas');
            await expect(canvas).toBeVisible({ timeout: 10000 });
        });

        test('tc-02: ошибка авторизации', async({page})     => {
            page.on('dialog', async dialog => {
                expect(dialog.message()).toContain('Неверное имя пользователя или пароль');
                await dialog.accept();
            });

            await page.locator('input[type="text"]').fill('testuser');
            await page.locator('input[type="password"]').fill('wrong_password');
            await page.locator('button.auth-button').click();
        });

    test('tc-03: пользователь уже существует', async({page})     => {
        page.on('dialog', async dialog => {
            expect(dialog.message()).toContain('Имя пользователя уже занято');
            await dialog.accept();
        });
        await page.locator('p.toggle-text').click()
        await page.locator('input[type="text"]').fill('testuser');
        await page.locator('input[type="password"]').fill('wrong_password');
        await page.locator('button.auth-button').click();
        await expect(page.locator('h2')).toHaveText('Регистрация');
    });


    test('tc-04: успешная регистрация нового пользователя', async ({ page }) => {
        page.on('dialog', async dialog => {
            expect(dialog.message()).toContain('Регистрация успешна');
            await dialog.accept();
        });
        await page.locator('p.toggle-text').click();
        const uniqueUsername = `user_${Date.now()}`;
        await page.locator('input[type="text"]').fill(uniqueUsername);
        await page.locator('input[type="password"]').fill('password123');
        await page.locator('button.auth-button').click();
        await expect(page.locator('h2')).toHaveText('Авторизация',{ timeout: 10000 });
    });

    test('tc-05: Вход и выход из системы', async ({ page }) => {
        await page.locator('input[type="text"]').fill('testuser');
        await page.locator('input[type="password"]').fill('password123');
        await page.locator('button.auth-button').click();
        const canvas = page.locator('canvas.graph-canvas');
        await expect(canvas).toBeVisible({ timeout: 10000 });
        await page.locator('button:has-text("Выход")').click();
        await expect(page.locator('h2')).toHaveText('Авторизация');
    });








    }
);





