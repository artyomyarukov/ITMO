import React from 'react';
import ReactDOM from 'react-dom/client'; // В новых версиях React используется client
import './index.css';
import App from './App';
import { Provider } from 'react-redux';
import store from './redux/store';


// возможно придется импортировать базовую тему здесь.


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
        <App />
    </Provider>
);