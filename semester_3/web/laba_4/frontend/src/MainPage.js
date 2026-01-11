import React, {useState, useEffect, useRef} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import './MainPage.css';

const MainPage = () => {
    const [x, setX] = useState(0);
    const [y, setY] = useState("");
    const [r, setR] = useState(1);
    const [results, setResults] = useState([]);

    const canvasRef = useRef(null);
    const currentUser = useSelector(state => state.user) || localStorage.getItem('username');
    const dispatch = useDispatch();

    const xValues = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];
    const rValues = [0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5];

    const isDataValid = (xVal, yVal) => {
        const yFloat = parseFloat(String(yVal).replace(',', '.'));
        if (xVal < -2 || xVal > 2) return false;
        if (isNaN(yFloat) || yFloat <= -3 || yFloat >= 5) return false;
        return true;
    };

    const fetchPoints = async () => {
        const token = localStorage.getItem('token');
        if (!token) return;

        try {
            const response = await fetch('http://localhost:8080/api/points', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (response.ok) {
                const data = await response.json();
                setResults(data); // Сохраняем загруженные точки в стейт
            }
        } catch (error) {
            console.error("Ошибка загрузки истории:", error);
        }
    };



    const drawGraph = (rValue) => {
        const canvas = canvasRef.current;
        if (!canvas) return;
        const ctx = canvas.getContext('2d');
        const width = canvas.width;
        const height = canvas.height;
        const centerX = width / 2;
        const centerY = height / 2;
        const scale = 30;

        ctx.clearRect(0, 0, width, height);


        ctx.fillStyle = 'rgba(74, 144, 226, 0.6)';

        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, rValue * scale, Math.PI, -Math.PI / 2, false);
        ctx.fill();
        ctx.beginPath();
        ctx.rect(centerX, centerY - rValue * scale, (rValue / 2) * scale, rValue * scale);
        ctx.fill();

        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + (rValue / 2) * scale, centerY);
        ctx.lineTo(centerX, centerY + rValue * scale);
        ctx.closePath();
        ctx.fill();


        // 2. Оси

        ctx.strokeStyle = '#333';
        ctx.lineWidth = 1;
        ctx.beginPath();
// Горизонтальная ось X
        ctx.moveTo(0, centerY);
        ctx.lineTo(width, centerY);
// Вертикальная ось Y
        ctx.moveTo(centerX, 0);
        ctx.lineTo(centerX, height);
        ctx.stroke();
        ctx.beginPath();

        ctx.moveTo(centerX - 5, 10);
        ctx.lineTo(centerX, 0);
        ctx.lineTo(centerX + 5, 10);

        ctx.moveTo(width - 10, centerY - 5);
        ctx.lineTo(width, centerY);
        ctx.lineTo(width - 10, centerY + 5);
        ctx.stroke();


        // 3. Стрелки и разметка
        ctx.font = 'bold 12px Arial';
        ctx.fillStyle = 'black';
        ctx.fillText('Y', centerX - 20, 15);
        ctx.fillText('X', width - 15, centerY - 20);

        for (let i = -5; i <= 5; i++) {
            if (i === 0) continue;
            const xPos = centerX + i * scale;
            const yPos = centerY - i * scale;
            ctx.beginPath();
            ctx.moveTo(xPos, centerY - 5);
            ctx.lineTo(xPos, centerY + 5);
            ctx.moveTo(centerX - 5, yPos);
            ctx.lineTo(centerX + 5, yPos);
            ctx.stroke();
            ctx.fillText(i, xPos - 5, centerY + 18);
            ctx.fillText(i, centerX + 10, yPos + 4);
        }

        // 4. Точки
        results
            .filter(p => Number(p.r) === Number(rValue))
            .forEach(p => {
                const pX = centerX + p.x * scale;
                const pY = centerY - p.y * scale;

                ctx.beginPath();
                ctx.arc(pX, pY, 4, 0, 2 * Math.PI);
                ctx.fillStyle = p.hit ? '#32CD32' : '#FF4500';
                ctx.fill();
                ctx.strokeStyle = 'white';
                ctx.lineWidth = 1;
                ctx.stroke();
            });
    };

    useEffect(() => {
        drawGraph(r);
    }, [r, results]);


    //эт при входе
    useEffect(() => {
        fetchPoints();
    }, []);

    const sendPoint = async (xVal, yVal, rVal) => {
        const token = localStorage.getItem('token');

        const savedUsername = localStorage.getItem('username');

        if (!savedUsername) {
            alert("Ошибка: Имя пользователя потеряно. Пожалуйста, перезайдите в систему.");
            return;
        }

        const pointData = {
            x: Number(xVal),
            y: Number(yVal),
            r: Number(rVal),
            username: savedUsername // Отправляем, что достали
        };

        console.log("Пытаемся отправить данные:", pointData);

        try {
            const response = await fetch('http://localhost:8080/api/points', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(pointData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Успех! Ответ сервера:", result);
                setResults(prev => [result, ...prev]);
            } else {
                const errorData = await response.json().catch(() => ({}));
                console.error("Сервер отклонил запрос:", errorData);
                alert("Ошибка валидации. Проверьте консоль бэкенда.");
            }
        } catch (error) {
            console.error("Ошибка сети:", error);
            alert("Нет связи с сервером");
        }
    };
    const handleSend = () => {
        const yNum = parseFloat(y.replace(',', '.'));
        if (isDataValid(x, yNum)) {
            sendPoint(x, yNum, r);
        } else {
            alert("Некорректные данные! X: [-2..2], Y: (-3..5)");
        }
    };

    const handleCanvasClick = (e) => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const rect = canvas.getBoundingClientRect();
        const scale = 30;
        const clientX = e.clientX;
        const clientY = e.clientY;
        const xPx = (clientX - rect.left) * (canvas.width / rect.width);
        const yPx = (clientY - rect.top) * (canvas.height / rect.height);

        const centerX = canvas.width / 2;
        const centerY = canvas.height / 2;
        const rawX = (xPx - centerX) / scale;
        const rawY = (centerY - yPx) / scale;

        sendPoint(rawX.toFixed(5), rawY.toFixed(5), r);
    };

    const logout = () => {
        localStorage.clear();
        dispatch({type: 'LOGOUT'});
    };

    const formatTime = (dateString) => {
        if (!dateString) return "-";
        const date = new Date(dateString);
        return date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit', second: '2-digit'});
    };

    return (
        <div className="main-page">
            <div className="header-bar">
                <span>Пользователь: <b>{currentUser}</b></span>
                <button onClick={logout}>Выход</button>
            </div>

            <div className="main-content">
                <canvas
                    ref={canvasRef}
                    width="400"
                    height="400"
                    onClick={handleCanvasClick}
                    className="graph-canvas"
                />

                <div className="controls">
                    <div className="control-group">
                        <label>Координата X:</label>
                        <div className="btn-row">
                            {xValues.map(v => (
                                <button key={v} className={x === v ? 'active' : ''} onClick={() => setX(v)}>{v}</button>
                            ))}
                        </div>
                    </div>

                    <div className="control-group">
                        <label>Координата Y (-3..5):</label>
                        <input
                            type="text"
                            value={y}
                            placeholder="От -3 до 5"
                            onChange={(e) => setY(e.target.value)}
                        />
                    </div>

                    <div className="control-group">
                        <label>Радиус R:</label>
                        <div className="btn-row">
                            {rValues.map(v => (
                                <button key={v} className={r === v ? 'active' : ''} onClick={() => setR(v)}>{v}</button>
                            ))}
                        </div>
                    </div>

                    <button className="send-btn" onClick={handleSend}>Проверить</button>
                </div>
            </div>

            <div className="history">
                <table>
                    <thead>
                    <tr>
                        <th>X</th>
                        <th>Y</th>
                        <th>R</th>
                        <th>Время</th>
                        {/* Новый заголовок */}
                        <th>Результат</th>
                    </tr>
                    </thead>
                    <tbody>
                    {results.map((res, i) => (
                        <tr key={i}>
                            <td>{Number(res.x).toFixed(5)}</td>
                            <td>{Number(res.y).toFixed(5)}</td>
                            <td>{res.r}</td>
                            <td>{formatTime(res.executionTime)}</td>
                            {/* Новая ячейка */}
                            <td className={res.hit ? 'hit' : 'miss'}>
                                {res.hit ? 'Попал' : 'Мимо'}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>


        </div>
    );
};

export default MainPage;