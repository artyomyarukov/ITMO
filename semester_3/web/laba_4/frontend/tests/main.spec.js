const { test, expect } = require('@playwright/test');

test.describe('Тестирование валидации Y', () => {

    test.beforeEach(async ({ page }) => {
        await page.goto('http://localhost:3000');
        await page.locator('input[type="text"]').fill('testuser');
        await page.locator('input[type="password"]').fill('password123');
        await page.locator('button.auth-button').click();
        await expect(page.locator('canvas.graph-canvas')).toBeVisible({ timeout: 10000 });
        await page.waitForLoadState('networkidle');
        const xGroup = page.locator('.control-group', { hasText: 'Координата X:' });
        await xGroup.getByRole('button', { name: '1', exact: true }).click();
        const rGroup = page.locator('.control-group', { hasText: 'Радиус R:' });
        await rGroup.getByRole('button', { name: '2', exact: true }).click()
    });

    test('tc-06: валидация Y выход за нижнюю границу -4', async ({ page }) => {
        page.on('dialog', async dialog => {
            expect(dialog.message()).toContain('Некорректные данные');
            await dialog.accept();
        });
        await page.locator('input[placeholder="От -3 до 5"]').fill('-4');
        await page.locator('button.send-btn').click();
    });

   test('tc-07: валидация Y выход за верхнюю границу 6)', async ({ page }) => {
        page.on('dialog', async dialog => {
            expect(dialog.message()).toContain('Некорректные данные');
            await dialog.accept();
        });
        await page.locator('input[placeholder="От -3 до 5"]').fill('6');
        await page.locator('button.send-btn').click();
    });

    test('tc-08: граничное значение Y -2.99', async ({ page }) => {
        await page.locator('input[placeholder="От -3 до 5"]').fill('-2.99');
        await page.locator('button.send-btn').click();
        const firstRow = page.locator('table tbody tr').first();
        await expect(firstRow).toContainText('-2.99000',{ timeout: 10000 });
    });

    test('tc-09: граничное значение  4.99', async ({ page }) => {
        await page.locator('input[placeholder="От -3 до 5"]').fill('4.99');
        await page.locator('button.send-btn').click();
        const firstRow = page.locator('table tbody tr').first();
        await expect(firstRow).toContainText('4.99000');
    });

    test('tc-10: недопустимая граница Y  -3', async ({ page }) => {
        page.on('dialog', async dialog => {
            expect(dialog.message()).toContain('Некорректные данные');
            await dialog.accept();
        });
        await page.locator('input[placeholder="От -3 до 5"]').fill('-3');
        await page.locator('button.send-btn').click();
    });

    test('tc-11: недопустимая граница Y  5', async ({ page }) => {
        page.on('dialog', async dialog => {
            expect(dialog.message()).toContain('Некорректные данные');
            await dialog.accept();
        });
        await page.locator('input[placeholder="От -3 до 5"]').fill('5');
        await page.locator('button.send-btn').click();
    });
});