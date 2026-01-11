import React from 'react';
import { useSelector } from 'react-redux';
import Header from './Header';
import LoginPage from './AuthPage';
import MainPage from './MainPage';
import './App.css';

function App() {
    // Достаем состояние из Redux
    const isLoggedIn = useSelector(state => state.isLoggedIn);
    console.log("Текущий статус входа в App.js:", isLoggedIn)
    return (
        <div className="App">
            <Header />
            <main>
                {/* Если залогинен — показываем MainPage, если нет — LoginPage */}
                {isLoggedIn ? <MainPage /> : <LoginPage />}
            </main>
        </div>
    );
}

export default App;