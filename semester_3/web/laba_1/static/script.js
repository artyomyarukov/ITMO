document.addEventListener('DOMContentLoaded', function () {
    // Элементы формы
    const form = document.getElementById('pointForm');
    const rSelect = document.getElementById('r-value');
    const yInput = document.getElementById('y-value');
    const resultsTable = document.getElementById('resultsTable').querySelector('tbody');
    const canvas = document.getElementById('areaGraph');
    const ctx = canvas.getContext('2d');


    loadHistoryOnStart();
    // Инициализация графика с текущим значением R
    const initialR = parseFloat(rSelect.value) || 3;
    drawGraph(initialR);

    // Обработчик изменения
    rSelect.addEventListener('change', function () {
        const rValue = parseFloat(this.value) || 3;
        console.log('R changed to:', rValue);
        drawGraph(rValue);
    });

    // Валидация формы
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        if (validateForm()) {
            const formData = new FormData(form);
            sendRequest(formData);
        }
    });

    // Валидация поля Y
    yInput.addEventListener('input', function () {
        validateYField();
    });




    // Функция валидации формы
    function validateForm() {
        const rValue = rSelect.value;
        const yValue = yInput.value.trim();

        // Проверка R
        if (!rValue) {
            showError('Пожалуйста, выберите значение R');
            return false;
        }

        // Проверка X
        if (!validateXField()) {
            return false;
        }

        // Проверка Y
        if (!validateYField()) {
            return false;
        }

        return true;
    }

    function validateXField() {
        const checkboxes = document.querySelectorAll('input[name="x"]:checked');
        const errorElement = document.querySelector('.x-error') || createXErrorElement();

        errorElement.textContent = '';

        if (checkboxes.length === 0) {
            errorElement.textContent = 'Выберите хотя бы одно значение X';
            return false;
        }

        if (checkboxes.length > 1) {
            errorElement.textContent = 'Выберите только одно значение X';
            return false;
        }

        return true;
    }

    function createXErrorElement() {
        const errorElement = document.createElement('div');
        errorElement.className = 'error-message x-error';
        errorElement.style.color = '#e74c3c';
        errorElement.style.fontSize = '0.9em';
        errorElement.style.marginTop = '5px';
        document.querySelector('.checkbox-group').parentNode.appendChild(errorElement);
        return errorElement;
    }

    // Валидация поля Y
    function validateYField() {
        const yValue = yInput.value.trim();
        const errorElement = yInput.parentNode.querySelector('.error-message') || createErrorElement();

        // Очистка предыдущих ошибок
        errorElement.textContent = '';

        if (!yValue) {
            errorElement.textContent = 'Пожалуйста, введите значение Y';
            return false;
        }

        const yNum = parseFloat(yValue);
        if (isNaN(yNum)) {
            errorElement.textContent = 'Y должно быть числом';
            return false;
        }

        if (yNum < -5 || yNum > 3) {
            errorElement.textContent = 'Y должно быть в диапазоне от -5 до 3';
            return false;
        }

        return true;
    }

    function createErrorElement() {
        const errorElement = document.createElement('div');
        errorElement.className = 'error-message';
        errorElement.style.color = '#e74c3c';
        errorElement.style.fontSize = '0.9em';
        errorElement.style.marginTop = '5px';
        yInput.parentNode.appendChild(errorElement);
        return errorElement;
    }


    // Отправка AJAX запроса
    function sendRequest(formData) {
        // Получаем выбранный X
        const selectedX = document.querySelector('input[name="x"]:checked').value;
        const rValue = document.getElementById('r-value').value;
        const yValue = document.getElementById('y-value').value;


        const params = new URLSearchParams();
        params.append('x', selectedX);
        params.append('y', yValue);
        params.append('r', rValue);

        const submitBtn = form.querySelector('.submit-btn');
        const originalText = submitBtn.textContent;

        // Показываем загрузку
        submitBtn.textContent = 'Отправка...';
        submitBtn.disabled = true;

        fetch('/calculate', {
            method: 'POST',
            body: params,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка сервера');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    addResultToTable(data);
                    updateGraph(data.x, data.y, data.result);
                } else {
                    showError(data.message || 'Произошла ошибка');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('Ошибка соединения с сервером');
            })
            .finally(() => {
                // Восстанавливаем кнопку
                submitBtn.textContent = originalText;
                submitBtn.disabled = false;
            });
    }

    // Добавление результата в таблицу
    function addResultToTable(data) {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${new Date(data.timestamp).toLocaleString()}</td>
            <td>${data.r}</td>
            <td>${data.x}</td>
            <td>${data.y}</td>
            <td class="${data.result ? 'hit' : 'miss'}">
                ${data.result ? ' Попадание' : ' Промах'}
            </td>
            <td>${data.executionTime} мс</td>
        `;

        resultsTable.insertBefore(row, resultsTable.firstChild);

        // Анимация добавления
        row.style.animation = 'fadeIn 0.5s ease-out';
        saveToHistory(data);
    }

    // Отрисовка графика
    function drawGraph(r = 3) {
        const width = canvas.width;
        const height = canvas.height;
        const padding = 40;
        const scale = 30;

        // Очистка canvas
        ctx.clearRect(0, 0, width, height);

        // Сетка и оси
        //drawGrid(width, height, scale, padding);
        drawAxes(width, height, padding, scale);

        // Область попадания
        drawArea(width, height, scale, padding, r);
    }

    function drawGrid(width, height, scale, padding) {
        const centerX = width / 2;
        const centerY = height / 2;

        ctx.strokeStyle = '#e0e0e0';
        ctx.lineWidth = 1;

        // Вертикальные линии (выровненные по сетке)
        for (let i = -5; i <= 5; i++) {
            const x = centerX + i * scale;
            ctx.beginPath();
            ctx.moveTo(x, padding);
            ctx.lineTo(x, height - padding);
            ctx.stroke();
        }

        // Горизонтальные линии (выровненные по сетке)
        for (let i = -5; i <= 5; i++) {
            const y = centerY - i * scale;
            ctx.beginPath();
            ctx.moveTo(padding, y);
            ctx.lineTo(width - padding, y);
            ctx.stroke();
        }

        // Жирные линии на целых числах
        ctx.strokeStyle = '#ccc';
        ctx.lineWidth = 1.5;

        // Вертикальные жирные линии
        for (let i = -5; i <= 5; i++) {
            if (i % 1 === 0) { // только целые числа
                const x = centerX + i * scale;
                ctx.beginPath();
                ctx.moveTo(x, padding);
                ctx.lineTo(x, height - padding);
                ctx.stroke();
            }
        }

        // Горизонтальные жирные линии
        for (let i = -5; i <= 5; i++) {
            if (i % 1 === 0) { // только целые числа
                const y = centerY - i * scale;
                ctx.beginPath();
                ctx.moveTo(padding, y);
                ctx.lineTo(width - padding, y);
                ctx.stroke();
            }
        }
    }

    function drawAxes(width, height, padding, scale) {
        const centerX = Math.round(width / 2); // Центр canvas по X
        const centerY = Math.round(height / 2); // Центр canvas по Y

        ctx.strokeStyle = '#000';
        ctx.lineWidth = 2;

        // Ось X
        ctx.beginPath();
        ctx.moveTo(padding, centerY); // Линия начинается от левого края
        ctx.lineTo(width - padding, centerY); // Линия до правого края
        ctx.stroke();

        // Ось Y
        ctx.beginPath();
        ctx.moveTo(centerX, padding); // Линия начинается от верхнего края
        ctx.lineTo(centerX, height - padding); // Линия до нижнего края
        ctx.stroke();

        // Стрелки на осях
        drawArrow(width - padding, centerY, 10, 0); // Стрелка на оси X
        drawArrow(centerX, padding, 10, 3 * Math.PI / 2); // Стрелка на оси Y

        // Подписи осей
        ctx.fillStyle = '#000';
        ctx.font = '14px Arial';
        ctx.textAlign = 'center';
        ctx.fillText('X', width - padding + 20, centerY - 10);
        ctx.fillText('Y', centerX + 10, padding - 20);
    }

// Функция для рисования стрелок
    function drawArrow(x, y, size, angle) {
        ctx.save();
        ctx.translate(x, y);
        ctx.rotate(angle);

        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(-size, -size / 2);
        ctx.lineTo(-size, size / 2);
        ctx.closePath();
        ctx.fillStyle = '#000';
        ctx.fill();

        ctx.restore();
    }

    function drawArea(width, height, scale, padding, r = 3) {
        const centerX = width / 2;
        const centerY = height / 2;

        ctx.save();

        // 1. Прямоугольник
        ctx.fillStyle = 'rgba(52, 152, 219, 0.3)';
        ctx.beginPath();
        ctx.rect(centerX - r * scale, centerY - (r / 2) * scale, r * scale, r / 2 * scale);
        ctx.fill();
        ctx.strokeStyle = '#2980b9';
        ctx.stroke();

        // 2. Треугольник (правый нижний квадрант)
        ctx.fillStyle = 'rgba(46, 204, 113, 0.3)';
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + r * scale, centerY);
        ctx.lineTo(centerX, centerY - r / 2 * scale);
        ctx.closePath();
        ctx.fill();
        ctx.strokeStyle = '#27ae60';
        ctx.stroke();

        // 3. Сектор круга
        ctx.fillStyle = 'rgba(231, 76, 60, 0.3)';
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, r * scale, Math.PI, Math.PI / 2, true);
        ctx.lineTo(centerX, centerY);
        ctx.closePath();
        ctx.fill();
        ctx.strokeStyle = '#c0392b';
        ctx.stroke();

        // Подписи осей
        ctx.fillStyle = '#000';
        ctx.font = '12px Arial';
        ctx.textAlign = 'center';

        // Ось X
        for (let i = -5; i <= 5; i++) {
            if (i !== 0) {
                const x = centerX + i * scale;
                ctx.fillText(i, x, centerY + 15);
                // Черточки напротив целых чисел
                if (i != 5) {
                    ctx.strokeStyle = '#808080';
                    ctx.beginPath();
                    ctx.moveTo(x, centerY - 3);
                    ctx.lineTo(x, centerY + 3);
                    ctx.stroke();
                }
            }
        }

        // Ось Y
        for (let i = -5; i <= 5; i++) {
            if (i !== 0) {
                const y = centerY - i * scale;
                ctx.fillText(i, centerX - 10, y);
                // Черточки напротив целых чисел
                if (i != 5) {
                    ctx.strokeStyle = '#808080';
                    ctx.beginPath();
                    ctx.moveTo(centerX - 3, y);
                    ctx.lineTo(centerX + 3, y);
                    ctx.stroke();
                }
            }
        }

        // Подписи R
        ctx.fillStyle = '#7f8c8d';
        ctx.fillText(`R = ${r}`, centerX + 50, centerY - 50);

        ctx.restore();
    }

    // Обработка клика по графику

    // Обновление графика с новой точкой
    function updateGraph(x, y, isHit) {
        const r = parseFloat(rSelect.value) || 3;
        const width = canvas.width;
        const height = canvas.height;
        const scale = 30;
        const padding = 40;

        // Перерисовываем график с текущим R
        drawGraph(r);

        // Преобразование математических координат в canvas
        const canvasX = width / 2 + x * scale;
        const canvasY = height / 2 - y * scale;

        // Рисуем точку
        ctx.beginPath();
        ctx.arc(canvasX, canvasY, 5, 0, 2 * Math.PI);
        ctx.fillStyle = isHit ? '#27ae60' : '#e74c3c';
        ctx.fill();
        ctx.strokeStyle = '#000';
        ctx.lineWidth = 1;
        ctx.stroke();
    }

    // Показать ошибку
    function showError(message) {
        alert('Ошибка: ' + message);
    }

    // Функция для установки значения X через чекбоксы
    function setXValue(value) {
        // Снимаем все выделения
        document.querySelectorAll('input[name="x"]').forEach(checkbox => {
            checkbox.checked = false;
        });

        // Ищем чекбокс с нужным значением
        const targetCheckbox = document.querySelector(`input[name="x"][value="${value}"]`);
        if (targetCheckbox) {
            targetCheckbox.checked = true;
        }
    }

    // Загрузка истории при старте
    loadHistory();
});

// Загрузка истории результатов
async function loadHistory() {
    try {
        const response = await fetch('/history');
        if (response.ok) {
            const history = await response.json();
            history.forEach(data => {
                // Нужно будет реализовать addResultToTable вне DOMContentLoaded
                console.log('History item:', data);
            });
        }
    } catch (error) {
        console.error('Ошибка загрузки истории:', error);
    }
}



// Сохранение результата в историю
function saveToHistory(data) {
    // Получаем текущую историю или создаем пустую
    let history = JSON.parse(localStorage.getItem('pointHistory') || '[]');

    // Добавляем новый результат в начало
    history.unshift(data);

    // Ограничиваем историю 50 последними результатами
    if (history.length > 50) {
        history = history.slice(0, 50);
    }

    // Сохраняем обратно в localStorage
    localStorage.setItem('pointHistory', JSON.stringify(history));
}

// Загрузка истории при старте
function loadHistoryOnStart() {
    const history = JSON.parse(localStorage.getItem('pointHistory') || '[]');

    history.forEach(data => {
        // добавляем строки в таблицу
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(data.timestamp).toLocaleString()}</td>
            <td>${data.r}</td>
            <td>${data.x}</td>
            <td>${data.y}</td>
            <td class="${data.result ? 'hit' : 'miss'}">
                ${data.result ? 'Попадание' : 'Промах'}
            </td>
            <td>${data.executionTime} мс</td>
        `;
        resultsTable.appendChild(row);
    });
}

// Очистка истории
function clearHistory() {
    if (confirm('Очистить всю историю результатов?')) {
        localStorage.removeItem('pointHistory');
        // Очищаем таблицу, оставляя заголовок
        resultsTable.innerHTML = `
            <thead>
                <tr>
                    <th>Время</th>
                    <th>R</th>
                    <th>X</th>
                    <th>Y</th>
                    <th>Результат</th>
                    <th>Время работы</th>
                </tr>
            </thead>
            <tbody></tbody>
        `;
        resultsTable = document.getElementById('resultsTable').querySelector('tbody');
    }
}















