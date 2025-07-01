import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import PointPage from './pages/PointManagement/PointPage';
import SubscriptionPage from './pages/SubscriptionManagement/SubscriptionPage';
import AuthorGridPage from './pages/AuthorManagement/AuthorGridPage';
import ManuscriptPage from './pages/ManuscriptManagement/ManuscriptPage';
import BookListPage from './pages/BookManagement/BookListPage';
import AIPage from './pages/AIService/AIPage';

const App = () => {
    return (
        <Router>
            <Header />
            <main style={{ padding: '20px 20px 60px 20px' }}>
                <Routes>
                    <Route
                        path='/'
                        element={
                            <div>
                                <h2>환영합니다!</h2>
                                <p>상단 메뉴를 통해 각 서비스 페이지로 이동하세요.</p>
                            </div>
                        }
                    />
                    <Route path='/authors' element={<AuthorGridPage />} />
                    <Route path='/manuscripts' element={<ManuscriptPage />} />
                    <Route path='/books' element={<BookListPage />} />
                    <Route path="/books/:bookId" element={<BookDetailPage />} />
                    <Route path='/ai-service' element={<AIPage />} />
                    <Route path='/subscriptions' element={<SubscriptionPage />} />
                    <Route path='/points' element={<PointPage />} />
                </Routes>
            </main>
            <Footer />
        </Router>
    );
};

export default App;