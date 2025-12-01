/**
 * Модуль для работы с интерактивным графиком
 */
const GraphManager = {
    canvas: null,
    ctx: null,
    currentR: null,
    scale: 30,
    centerX: 200,
    centerY: 200,
    init: function () {
        this.canvas = document.getElementById('graphCanvas');
        if (!this.canvas) {
            console.log('Canvas не найден');
            return;
        }

        this.ctx = this.canvas.getContext('2d');
        this.drawCoordinateSystem();

        if (this.currentR) {
            this.drawAllPoints();
        }


        // Обработчик клика по графику
        this.canvas.addEventListener('click', (e) => {
            this.handleCanvasClick(e);
        });


        const rRadios = document.querySelectorAll('input[name="r"]');
        let initialRSet = false;

        rRadios.forEach(radio => {
            radio.addEventListener('change', (e) => {
                if (e.target.checked) {
                    this.currentR = parseFloat(e.target.value);
                    console.log('R selected:', this.currentR);
                    this.drawCoordinateSystem();
                    this.drawArea(this.currentR);
                    this.drawAllPoints();
                }
            });

            // Устанавливаем начальный радиус если какой-то уже выбран
            if (radio.checked && !initialRSet) {
                this.currentR = parseFloat(radio.value);
                initialRSet = true;
                console.log('Initial R set:', this.currentR);
            }
        });

        // Если ни один радиус не выбран, рисуем без области
        if (!initialRSet) {
            console.log('No R selected initially');
            this.currentR = null;
        }

        console.log('Graph manager initialized');
    },
    /**
     * Отрисовка координатной системы
     */
    drawCoordinateSystem: function () {
        const width = this.canvas.width;
        const height = this.canvas.height;
        const ctx = this.ctx;

        // Очистка canvas
        ctx.clearRect(0, 0, width, height);


        ctx.fillStyle = '#f8f8f8';
        ctx.fillRect(0, 0, width, height);


        ctx.strokeStyle = '#000';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(0, this.centerY);
        ctx.lineTo(width, this.centerY);
        ctx.stroke();

        ctx.beginPath();
        ctx.moveTo(this.centerX, 0);
        ctx.lineTo(this.centerX, height);
        ctx.stroke();


        this.drawArrowY(this.centerX, 0, this.centerX, 20, '#000'); // Y стрелка вверх
        this.drawArrowX(width - 20, this.centerY, width, this.centerY, '#000'); // X стрелка вправо


        ctx.fillStyle = '#000';
        ctx.font = '14px Arial';
        ctx.fillText('X', width - 15, this.centerY - 10);
        ctx.fillText('Y', this.centerX + 10, 15);


        this.drawAxisLabels();

        if (this.currentR) {
            this.drawArea(this.currentR);
        }

        if (this.currentR) {
            this.drawAllPoints();
        }
    },

    /**
     * Отрисовка стрелок
     */
    drawArrowX: function (fromX, fromY, toX, toY, color) {
        this.ctx.strokeStyle = color;
        this.ctx.fillStyle = color;

        const headlen = 10;
        const angle = Math.atan2(toY - fromY, toX - fromX);

        // Линия оси
        this.ctx.beginPath();
        this.ctx.moveTo(fromX, fromY);
        this.ctx.lineTo(toX, toY);
        this.ctx.stroke();

        this.ctx.beginPath();
        this.ctx.moveTo(toX, toY);  // острие в конце
        this.ctx.lineTo(toX - headlen * Math.cos(angle - Math.PI / 6),
            toY - headlen * Math.sin(angle - Math.PI / 6));
        this.ctx.lineTo(toX - headlen * Math.cos(angle + Math.PI / 6),
            toY - headlen * Math.sin(angle + Math.PI / 6));
        this.ctx.closePath();
        this.ctx.fill();
    },

    drawArrowY: function (fromX, fromY, toX, toY, color) {
        this.ctx.strokeStyle = color;
        this.ctx.fillStyle = color;

        const headlen = 10;
        const angle = Math.atan2(toY - fromY, toX - fromX);

        // Линия оси
        this.ctx.beginPath();
        this.ctx.moveTo(fromX, fromY);
        this.ctx.lineTo(toX, toY);
        this.ctx.stroke();

        // Наконечник в НАЧАЛЕ линии (вниз ↓)
        this.ctx.beginPath();
        this.ctx.moveTo(fromX, fromY);  // острие в начале
        this.ctx.lineTo(fromX + headlen * Math.cos(angle - Math.PI / 6),
            fromY + headlen * Math.sin(angle - Math.PI / 6));
        this.ctx.lineTo(fromX + headlen * Math.cos(angle + Math.PI / 6),
            fromY + headlen * Math.sin(angle + Math.PI / 6));
        this.ctx.closePath();
        this.ctx.fill();
    },

    /**
     * Разметка осей
     */
    drawAxisLabels: function () {
        const ctx = this.ctx;
        ctx.fillStyle = '#000';
        ctx.font = '12px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';

        // Метки по оси X
        for (let i = -5; i <= 5; i++) {
            if (i === 0) continue;
            const x = this.centerX + i * this.scale;
            ctx.fillText(i.toString(), x, this.centerY + 15);

            // Засечки
            ctx.beginPath();
            ctx.moveTo(x, this.centerY - 5);
            ctx.lineTo(x, this.centerY + 5);
            ctx.stroke();
        }

        // Метки по оси Y
        for (let i = -5; i <= 5; i++) {
            if (i === 0) continue;
            const y = this.centerY - i * this.scale;
            ctx.fillText(i.toString(), this.centerX - 15, y);

            // Засечки
            ctx.beginPath();
            ctx.moveTo(this.centerX - 5, y);
            ctx.lineTo(this.centerX + 5, y);
            ctx.stroke();
        }

        // Ноль
        ctx.fillText('0', this.centerX - 10, this.centerY + 15);
    },

    /**
     * Отрисовка области попадания
     */
    drawArea: function (r) {
        const ctx = this.ctx;
        const scale = this.scale;

        ctx.fillStyle = 'rgba(0, 100, 255, 0.3)';
        ctx.strokeStyle = 'rgba(0, 100, 255, 0.7)';
        ctx.lineWidth = 1;

        // 1. Прямоугольник (2 четверть)
        ctx.beginPath();
        ctx.rect(this.centerX - r * scale, this.centerY - r * scale, r * scale, r * scale);
        ctx.fill();
        ctx.stroke();

        // 2. Треугольник (4 четверть)
        ctx.beginPath();
        ctx.moveTo(this.centerX, this.centerY);
        ctx.lineTo(this.centerX + (r / 2 * scale), this.centerY);
        ctx.lineTo(this.centerX, this.centerY + (r * scale));
        ctx.closePath();
        ctx.fill();
        ctx.stroke();

        // 3. 1/4 круга (1 четверть)
        ctx.beginPath();
        ctx.moveTo(this.centerX, this.centerY);
        ctx.arc(this.centerX, this.centerY, r * scale, 3 * Math.PI / 2, 0, false);
        ctx.closePath();
        ctx.fill();
        ctx.stroke();

        console.log('Area drawn for R =', r);
    },

    /**
     * Обработчик клика по canvas
     */
    handleCanvasClick: function (e) {
        if (!this.currentR) {
            alert('Сначала выберите радиус R!');
            return;
        }

        const rect = this.canvas.getBoundingClientRect();
        const canvasX = e.clientX - rect.left;
        const canvasY = e.clientY - rect.top;

        const mathCoords = this.canvasToMath(canvasX, canvasY);

        // Рисуем точку
        this.drawPoint(canvasX, canvasY, mathCoords.x, mathCoords.y);

        // Отправляем с координатами canvas
        this.sendPoint(mathCoords.x, mathCoords.y, this.currentR, canvasX, canvasY);
    },

    /**
     * Конвертация координат canvas в математические
     */
    canvasToMath: function (canvasX, canvasY) {
        return {
            x: (canvasX - this.centerX) / this.scale,
            y: (this.centerY - canvasY) / this.scale
        };
    },

    /**
     * Отрисовка точки на графике
     */
    drawPoint: function (canvasX, canvasY, mathX, mathY) {
        const ctx = this.ctx;

        // Определяем цвет точки
        const isHit = this.checkHit(mathX, mathY, this.currentR);
        ctx.fillStyle = isHit ? 'green' : 'red';

        // Рисуем точку
        ctx.beginPath();
        ctx.arc(canvasX, canvasY, 4, 0, 2 * Math.PI);
        ctx.fill();

        // Обводка
        ctx.strokeStyle = '#000';
        ctx.lineWidth = 1;
        ctx.stroke();
    },

    /**
     * Отрисовка всех сохраненных точек
     */
    drawAllPoints: function () {
        console.log('Drawing all points...');
        const results = this.getResultsFromPage();
        if (!results) {
            console.log('No results found');
            return;
        }
        const filteredResults = results.filter(result => {
            return result.r === this.currentR;
        });

        console.log('Found results:', results);
        filteredResults.forEach(result => {
            if (result.canvasX && result.canvasY) {
                console.log('Drawing point:', result.canvasX, result.canvasY, 'R=', result.r, 'Hit=', result.hit);
                this.drawSavedPoint(result.canvasX, result.canvasY, result.hit);
            }
        });
    },

    /**
     * Получение результатов со страницы
     */
    getResultsFromPage: function () {
        try {
            // Ищем данные в hidden field
            const resultsData = document.getElementById('resultsData');
            if (resultsData && resultsData.value) {
                console.log('Found resultsData:', resultsData.value);
                return JSON.parse(resultsData.value);
            }
            console.log('No resultsData found');
            return null;
        } catch (error) {
            console.error('Error parsing results:', error);
            return null;
        }
    },

    /**
     * Отрисовка сохраненной точки
     */
    drawSavedPoint: function (canvasX, canvasY, isHit) {
        const ctx = this.ctx;

        ctx.fillStyle = isHit ? 'green' : 'red';
        ctx.beginPath();
        ctx.arc(canvasX, canvasY, 6, 0, 2 * Math.PI); // Немного больше для заметности
        ctx.fill();

        ctx.strokeStyle = '#000';
        ctx.lineWidth = 2;
        ctx.stroke();

        console.log('SavedPoint drawn at:', canvasX, canvasY, isHit ? 'HIT' : 'MISS');
    },

    /**
     * Проверка попадания точки в область
     */
    checkHit: function (x, y, r) {
        let popalIliNet = false;
        if (x <= 0 && y >= 0 && x >= -r && y <= r) {
            popalIliNet = true;
        }
        if (x >= 0 && y >= 0 && (x * x + y * y) <= r * r) {
            popalIliNet = true;
        }
        if (x >= 0 && y <= 0 && x <= r / 2 && y >= 2 * x - r) {
            popalIliNet = true;
        }

        return popalIliNet;
    },

    /**
     * Отправка точки на сервер
     */
    sendPoint: function (x, y, r, canvasX, canvasY) {
        console.log('Sending point to server:', x, y, r, canvasX, canvasY);


        window.location.href = 'controller?x=' + x.toFixed(2) +
            '&y=' + y.toFixed(2) +
            '&r=' + r +
            '&canvasX=' + canvasX +
            '&canvasY=' + canvasY;
    }
};


document.addEventListener('DOMContentLoaded', function () {
    GraphManager.init();
});