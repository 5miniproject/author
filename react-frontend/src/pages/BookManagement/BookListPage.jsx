import React, { useState, useEffect } from 'react';
import apiService from '../../api/apiService';

const BookListPage = () => {
    const [books, setBooks] = useState([]);
    const [bestSellers, setBestSellers] = useState([]);

    useEffect(() => {
        fetchBooks();
        fetchBestSellers();
    }, []);

    const fetchBooks = async () => {
        try {
            // ReadModel: 출판물 목록 (가정)
            const response = await apiService.get('/books');
            setBooks(response.data);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('책 목록 조회 실패:', error);
        }
    };

    const fetchBestSellers = async () => {
        try {
            // ReadModel: 베스트셀러 목록 (가정)
            const response = await apiService.get('/books/bestsellers');
            setBestSellers(response.data);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('베스트셀러 목록 조회 실패:', error);
        }
    };

    const handleReadBook = async (bookId) => {
        try {
            // Command: 도서열람
            await apiService.post(`/books/${bookId}/read`, { views: 1 }); // views를 1 증가
            alert(`"${bookId}" 책을 열람합니다.`);
            // 이후 ReadModel을 다시 조회하여 views를 갱신하거나, UI 상태를 업데이트
            fetchBooks();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('도서 열람 실패:', error);
        }
    };
    
    // 관리자 기능 - 책 삭제
    const handleDeleteBook = async (bookId) => {
        try {
            // Command: 책 삭제
            await apiService.delete(`/books/${bookId}`);
            alert('책이 삭제되었습니다.');
            fetchBooks();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('책 삭제 실패:', error);
        }
    };

    return (
        <div>
            <h2>서재 플랫폼</h2>
            <hr />
            <h3>베스트 셀러</h3>
            <ul>
                {bestSellers.map((book) => (
                    <li key={book.id}>
                        <strong>{book.title}</strong> - Views: {book.views}
                    </li>
                ))}
            </ul>
            <hr />
            <h3>전체 서적 목록</h3>
            <ul>
                {books.map((book) => (
                    <li key={book.id}>
                        <h4>{book.title} by {book.authorName}</h4>
                        <button onClick={() => handleReadBook(book.id)}>열람하기</button>
                        <button onClick={() => handleDeleteBook(book.id)}>삭제 (관리자용)</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default BookListPage;