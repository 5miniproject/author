import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => (
    <header style={{ padding: '10px 20px', backgroundColor: '#f0f0f0', borderBottom: '1px solid #ddd' }}>
        <h1 style={{ margin: '0' }}>MSA Front-end Dashboard</h1>
        <nav style={{ marginTop: '10px' }}>
            <Link to='/' style={{ marginRight: '15px' }}>홈</Link>
            <Link to='/authors' style={{ marginRight: '15px' }}>작가 관리</Link>
            <Link to='/manuscripts' style={{ marginRight: '15px' }}>원고 관리</Link>
            <Link to='/ai-service' style={{ marginRight: '15px' }}>AI 서비스</Link>
            <Link to='/books' style={{ marginRight: '15px' }}>서재 플랫폼</Link>
            <Link to='/subscriptions' style={{ marginRight: '15px' }}>구독 관리</Link>
            <Link to='/points'>포인트 관리</Link>
        </nav>
        <hr />
    </header>
);

export default Header;