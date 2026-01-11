import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import './LoginPage.css';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const dispatch = useDispatch();

    const handleLogin = async (e) => {
        e.preventDefault(); // останавливаем перезагрузку страницы
        console.log("Попытка входа...", { username, password });

        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }) // Поля должны совпадать с AuthRequest в Java
            });

            if (response.ok) {
                const data = await response.json();
                console.log("Успех! Токен получен:", data.token);


                localStorage.setItem('token', data.token);

                // Отправляем в Redux
                dispatch({ type: 'LOGIN', payload: username });
            } else {
                const errorData = await response.json();
                alert("Ошибка: " + (errorData.error || "Неверные данные"));
            }
        } catch (error) {
            console.error("Ошибка связи с сервером:", error);
            alert("Сервер недоступен. Проверь, запущен ли Spring Boot на порту 8080");
        }
    };

    return (
        <div className="login-container">
            <div className="login-form-wrapper">
                <h2>Авторизация</h2>
                <form onSubmit={handleLogin}>
                    <div style={{ marginBottom: '10px' }}>
                        <input
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            style={{ width: '100%', padding: '8px' }}
                            required
                        />
                    </div>
                    <div style={{ marginBottom: '10px' }}>
                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            style={{ width: '100%', padding: '8px' }}
                            required
                        />
                    </div>
                    <button type="submit" style={{ width: '100%', padding: '10px', cursor: 'pointer' }}>
                        Войти
                    </button>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;