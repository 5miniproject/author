import React, { useState, useEffect } from 'react';
import apiService from '../../api/apiService';

const BookListPage = () => {
    const [books, setBooks] = useState([]);
    // const { userInfo } = useAuth(); 사용자 인증 정보

    useEffect(() => {
        fetchBooks();
    }, []);

    const fetchBooks = async () => {
        try {
            // BookSummaryDto 호출
            const response = await apiService.get('/books');
            
            // setBooks(response.data);
            setBooks(response.data.content);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('책 목록 조회 실패:', error);
        }
    };
    
    // 관리자 기능 - 책 삭제
    const handleDeleteBook = async (bookId) => {
        if (!window.confirm("정말로 이 책을 삭제하시겠습니까?")) {
            return;
        }

        try {
            // Command: 책 삭제
            await apiService.delete(`/books/${bookId}`);
            alert('책이 삭제되었습니다.');

            // fetchBooks() 대신 로컬 상태 직접 업데이트
            setBooks(prevBooks => prevBooks.filter(b => b.id != bookId));
        } catch (error) {
            if (error.response && error.response.status === 403) {
                alert("이 책을 삭제할 권한이 없습니다.");
            } else {
                console.error('책 삭제 실패:', error);
            }
        }
    };

    return (
        <div>
            <h2>서재 플랫폼</h2>
            <hr />
            <h3>도서 목록</h3>
            <ul>
                {books.map((book) => (
                    <li key={book.id} style={{ border: '1px solid #ccc', margin: '10px', padding: '10px' }}>
                        <h4>
                            {/* isBestSeller 필드를 사용하여 배지 표시 */}
                            {book.isBestSeller && <span style={{ color: 'red', fontWeight: 'bold' }}>[BEST] </span>}
                            {book.title}
                        </h4>
                        <p>저자: {book.authorName} | 조회수: {book.views}</p>
                        {/* 열람하기: 상세 페이지로 이동하는 역할 */}                
                        <Link 
                            to={`/books/${book.id}`}
                            // 상세 페이지 이동 시 DTO 데이터 함께 전달
                            state={{ bookSummary: book }}
                        >
                            <button>열람하기</button>
                        </Link>
                        {/* 현재 유저가 작가이고, 책의 저자와 일치할 때만 삭제 버튼 표시 */}
                        {/* {userInfo && userInfo.isApprove === true && userInfo.id === book.authorId && ( */}
                        <button onClick={() => handleDeleteBook(book.id)}>삭제 (관리자용)</button>
                        {/* )} */}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default BookListPage;