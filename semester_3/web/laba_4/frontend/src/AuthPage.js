import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import './AuthPage.css';

const AuthPage = () => {
    const [isLoginView, setIsLoginView] = useState(true);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const dispatch = useDispatch();

    // Очищаем поля при переключении между входом и регистрацией
    useEffect(() => {
        setUsername('');
        setPassword('');
    }, [isLoginView]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const endpoint = isLoginView ? '/api/auth/login' : '/api/auth/register';

        try {
            const response = await fetch(`http://localhost:8080${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            if (response.ok) {
                if (isLoginView) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('username', username);
                    dispatch({ type: 'LOGIN', payload: username });
                } else {
                    alert("Регистрация успешна! Теперь войдите.");
                    setIsLoginView(true);
                }
            } else {

                alert(data.message || "Ошибка аутентификации");
            }
        } catch (error) {
            alert("Нет связи с бэкендом");
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-box">
                <h2>{isLoginView ? "Авторизация" : "Регистрация"}</h2>
                <form className="auth-form" onSubmit={handleSubmit}>
                    <input
                        className="auth-input"
                        type="text"
                        placeholder="Имя пользователя"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <input
                        className="auth-input"
                        type="password"
                        placeholder="Пароль"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button className="auth-button" type="submit">
                        {isLoginView ? "Войти" : "Зарегистрироваться"}
                    </button>
                </form>
                <p className="toggle-text" onClick={() => setIsLoginView(!isLoginView)}>
                    {isLoginView ? "Нет аккаунта? Зарегистрироваться" : "Уже есть аккаунт? Войти"}
                </p>
            </div>
        </div>
    );
};

export default AuthPage;