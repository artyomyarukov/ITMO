/**
 * Валидация формы ввода координат
 */
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('pointForm');
    const yInput = document.querySelector('input[name="y"]');

    if (form && yInput) {

        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                return false;
            }
        });

        yInput.addEventListener('input', function() {
            validateYField();
        });

        yInput.addEventListener('blur', function() {
            validateYField();
        });
    }
});

/**
 * Валидация всего формы
 */
function validateForm() {
    const xInput = document.querySelector('input[name="x"]');
    const yInput = document.querySelector('input[name="y"]');
    const rRadios = document.querySelectorAll('input[name="r"]');

    let isValid = true;
    let errorMessage = '';


    const xValue = parseFloat(xInput.value);
    if (isNaN(xValue)) {
        errorMessage += '• Координата X должна быть числом\n';
        isValid = false;
    } else if (xValue < -5 || xValue > 5) {
        errorMessage += '• Координата X должна быть в диапазоне от -5 до 5\n';
        isValid = false;
    }


    const yValue = parseFloat(yInput.value);
    if (isNaN(yValue)) {
        errorMessage += '• Координата Y должна быть числом\n';
        isValid = false;
    } else if (yValue < -3 || yValue > 5) {
        errorMessage += '• Координата Y должна быть в диапазоне от -3 до 5\n';
        isValid = false;
    }

    let rSelected = false;
    rRadios.forEach(radio => {
        if (radio.checked) rSelected = true;
    });

    if (!rSelected) {
        errorMessage += '• Пожалуйста, выберите радиус R\n';
        isValid = false;
    }


    if (!isValid) {
        alert('Обнаружены ошибки:\n' + errorMessage);
    }

    return isValid;
}


function validateYField() {
    const yInput = document.querySelector('input[name="y"]');
    const yValue = yInput.value.trim();


    yInput.classList.remove('error-field');

    if (yValue === '') {
        return true;
    }

    const numValue = parseFloat(yValue);
    if (isNaN(numValue)) {
        showFieldError(yInput, 'Y должно быть числом');
        return false;
    }

    if (numValue < -3 || numValue > 5) {
        showFieldError(yInput, 'Y должен быть от -3 до 5');
        return false;
    }


    if (yValue.includes('.') && yValue.split('.')[1].length > 10) {
        yInput.value = numValue.toFixed(10);
    }

    return true;
}


function showFieldError(field, message) {
    field.classList.add('error-field');


    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }


    const errorElement = document.createElement('div');
    errorElement.className = 'field-error';
    errorElement.style.color = 'red';
    errorElement.style.fontSize = '12px';
    errorElement.style.marginTop = '5px';
    errorElement.textContent = message;

    field.parentNode.appendChild(errorElement);
}


function clearFieldError(field) {
    field.classList.remove('error-field');
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
}

function clearResults() {
    if (confirm('Вы уверены, что хотите очистить всю историю проверок?')) {

        window.location.href = 'controller?action=clear';
    }
}



function prepareFormSubmission() {
    console.log('Preparing form submission...');

    const xInput = document.querySelector('input[name="x"]');
    const yInput = document.querySelector('input[name="y"]');
    const rRadios = document.querySelectorAll('input[name="r"]');
    const canvasXInput = document.getElementById('canvasX');
    const canvasYInput = document.getElementById('canvasY');


    const x = parseFloat(xInput.value);
    const y = parseFloat(yInput.value);


    let r = null;
    rRadios.forEach(radio => {
        if (radio.checked) r = parseFloat(radio.value);
    });

    if (r && !isNaN(x) && !isNaN(y)) {

        const canvasCoords = mathToCanvas(x, y);
        console.log('Math coordinates:', x, y, 'Canvas coordinates:', canvasCoords);


        canvasXInput.value = canvasCoords.x;
        canvasYInput.value = canvasCoords.y;
    } else {

        canvasXInput.value = '';
        canvasYInput.value = '';
    }
}

/**
 * Конвертация математических координат в координаты canvas
 */
function mathToCanvas(mathX, mathY) {
    const scale = 30;
    const centerX = 200;
    const centerY = 200;

    return {
        x: centerX + mathX * scale,
        y: centerY - mathY * scale
    };
}