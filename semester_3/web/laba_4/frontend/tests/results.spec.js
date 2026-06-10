const { test, expect } = require('@playwright/test');

test.describe('Тестирование результатов попадания точек ', () => {


    test.beforeEach(async ({ page }) => {
        await page.goto('http://localhost:3000');
        await page.locator('input[type="text"]').fill('testuser');
        await page.locator('input[type="password"]').fill('password123');
        await page.locator('button.auth-button').click();

        await expect(page.locator('canvas.graph-canvas')).toBeVisible({ timeout: 10000 });
        await page.waitForLoadState('networkidle');
    });

    test('tc-12: отправка точки с выбором координат X и R', async ({ page }) => {
        const xGroup = page.locator('.control-group', { hasText: 'Координата X:' });
        await xGroup.getByRole('button', { name: '-1.5', exact: true }).click();
        const rGroup = page.locator('.control-group', { hasText: 'Радиус R:' });
        await rGroup.getByRole('button', { name: '3', exact: true }).click();
        await page.locator('input[placeholder="От -3 до 5"]').fill('2');
        await page.locator('button.send-btn').click();
       const firstRow = page.locator('table tbody tr').first();
        await expect(firstRow).toContainText('-1.50000', { timeout: 10000 });
        await expect(firstRow).toContainText('2.00000', { timeout: 10000 });
        await expect(firstRow).toContainText('3', { timeout: 10000 });
    });

    test('tc-13: попадание в область прямоугольника', async ({ page }) => {
        const xGroup = page.locator('.control-group', { hasText: 'Координата X:' });
        await xGroup.getByRole('button', { name: '0.5', exact: true }).click();
        const rGroup = page.locator('.control-group', { hasText: 'Радиус R:' });
        await rGroup.getByRole('button', { name: '2', exact: true }).click();
        await page.locator('input[placeholder="От -3 до 5"]').fill('1');
        await page.locator('button.send-btn').click();
        const firstRowResult = page.locator('table tbody tr').first().locator('td').last();
        await expect(firstRowResult).toHaveText('Попал', { timeout: 10000 });
        await expect(firstRowResult).toHaveClass('hit');
    });

    test('tc-14: промах мимо области', async ({ page }) => {
        const xGroup = page.locator('.control-group', { hasText: 'Координата X:' });
        await xGroup.getByRole('button', { name: '1', exact: true }).click();
        const rGroup = page.locator('.control-group', { hasText: 'Радиус R:' });
        await rGroup.getByRole('button', { name: '1', exact: true }).click();
        await page.locator('input[placeholder="От -3 до 5"]').fill('1');
        await page.locator('button.send-btn').click();
        const firstRowResult = page.locator('table tbody tr').first().locator('td').last();
        await expect(firstRowResult).toHaveText('Мимо', { timeout: 10000 });
        await expect(firstRowResult).toHaveClass('miss');
    });

    test('tc-15: попадание на границе прямоугольника', async ({ page }) => {
        const xGroup = page.locator('.control-group', { hasText: 'Координата X:' });
        await xGroup.getByRole('button', { name: '1', exact: true }).click();
        const rGroup = page.locator('.control-group', { hasText: 'Радиус R:' });
        await rGroup.getByRole('button', { name: '2', exact: true }).click();
        await page.locator('input[placeholder="От -3 до 5"]').fill('2');
        await page.locator('button.send-btn').click();
        const firstRowResult = page.locator('table tbody tr').first().locator('td').last();
        await expect(firstRowResult).toHaveText('Попал', { timeout: 10000 });
        await expect(firstRowResult).toHaveClass('hit');
    });
});