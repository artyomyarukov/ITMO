import React from 'react';
import './Header.css';

const Header = () => {
    return (
        <header className="main-header">
            <div className="header-content">
                <span className="info-item name">Яруков Артём Дмитриевич</span>
                <span className="info-item group">Группа: P3212</span>
                <span className="info-item variant">Вариант: 474228</span>
            </div>
        </header>
    );
};

export default Header;