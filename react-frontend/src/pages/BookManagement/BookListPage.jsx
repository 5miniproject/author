import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiService from '../../api/apiService';

const BookListPage = () => {
    const [books, setBooks] = useState([]);
    // 초기값을 1로 설정하되, 유효하지 않은 입력은 null로 처리하여 버튼 비활성화에 활용합니다.
    const [currentSubscriberId, setCurrentSubscriberId] = useState(1); 

    // 컴포넌트 렌더링 시 currentSubscriberId 값을 콘솔에 출력하여 확인합니다.
    // 이 값이 변경될 때마다 콘솔에 새로운 ID가 출력되어야 합니다.
    // eslint-disable-next-line no-console
    console.log('BookListPage 렌더링: currentSubscriberId 현재 값:', currentSubscriberId);

    useEffect(() => {
        fetchBooks();
    }, []);

    const fetchBooks = async () => {
        try {
            const response = await apiService.get('/books');
            const bookList = response.data.content || [];
            setBooks(bookList);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('책 목록 조회 실패:', error);
        }
    };
    
    const handleDeleteBook = async (bookId) => {
        if (!window.confirm("정말로 이 책을 삭제하시겠습니까?")) {
            return;
        }

        try {
            await apiService.delete(`/books/${bookId}`);
            alert('책이 삭제되었습니다.');
            setBooks(prevBooks => prevBooks.filter(b => b.id != bookId));
        } catch (error) {
            if (error.response && error.response.status === 403) {
                alert("이 책을 삭제할 권한이 없습니다.");
            } else {
                // eslint-disable-next-line no-console
                console.error('책 삭제 실패:', error);
            }
        }
    };

    // ==============================================================
    // [수정된 부분]
    // 구독자 ID 입력 필드의 값 변경 핸들러: 유효하지 않은 입력은 null로 설정
    const handleSubscriberIdChange = (e) => {
        const value = e.target.value;
        if (value === '') {
            setCurrentSubscriberId(''); // 입력 필드가 비어있을 때
        } else {
            const numValue = parseInt(value, 10);
            if (!isNaN(numValue) && numValue > 0) {
                setCurrentSubscriberId(numValue); // 유효한 양의 정수일 때만 상태 업데이트
            } else {
                // 유효하지 않은 숫자나 음수/0일 경우, null로 설정하여 열람 버튼을 비활성화
                setCurrentSubscriberId(null); 
            }
        }
    };

    // '구독자 ID 설정' 버튼 클릭 핸들러
    const handleSetSubscriberId = () => {
        // currentSubscriberId가 null이면 (즉, 입력이 유효하지 않거나 비어있으면) 경고 후 1로 강제 설정
        if (currentSubscriberId === null || currentSubscriberId === '' || isNaN(currentSubscriberId) || currentSubscriberId <= 0) {
            alert('유효한 구독자 ID를 입력해주세요. (양의 정수)');
            setCurrentSubscriberId(1); // 유효성 검사 실패 시 기본값 1로 재설정
        } else {
            alert(`현재 구독자 ID가 ${currentSubscriberId}로 설정되었습니다.`);
            // 상태는 이미 onChange에서 업데이트되었으므로 여기서는 추가 작업이 필요 없습니다.
        }
    };
    // ==============================================================

    return (
        <div>
            <h2>서재 플랫폼</h2>
            <hr />

            <div style={{ marginBottom: '20px', padding: '10px', border: '1px solid #eee', borderRadius: '5px', backgroundColor: '#f9f9f9' }}>
                <h3>현재 구독자 ID 설정</h3>
                {/* currentSubscriberId가 null이면 '설정되지 않음'으로 표시 */}
                <p>현재 열람에 사용될 구독자 ID: <strong>{currentSubscriberId === null || currentSubscriberId === '' ? '설정되지 않음' : currentSubscriberId}</strong></p>
                <input
                    type="number"
                    value={currentSubscriberId === null ? '' : currentSubscriberId} // null일 때는 입력 필드를 비워 보이게 함
                    onChange={handleSubscriberIdChange}
                    placeholder="구독자 ID 입력"
                    min="1"
                    style={{ marginRight: '10px', padding: '8px', border: '1px solid #ccc', borderRadius: '4px' }}
                />
                <button
                    onClick={handleSetSubscriberId}
                    // '구독자 ID 설정' 버튼은 currentSubscriberId가 유효한 숫자일 때만 활성화
                    disabled={currentSubscriberId === null || currentSubscriberId === '' || isNaN(currentSubscriberId) || currentSubscriberId <= 0}
                    style={{ padding: '8px 15px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                >
                    구독자 ID 설정
                </button>
            </div>

            <h3>도서 목록</h3>
            <ul>
                {books.map((book) => (
                    <li key={book.id} style={{ border: '1px solid #ccc', margin: '10px', padding: '10px' }}>
                        <h4>
                            {book.isBestSeller && <span style={{ color: 'red', fontWeight: 'bold', marginRight: '5px' }}>[BEST]</span>}
                            {book.title}
                        </h4>
                        <p>저자: {book.authorName} | 조회수: {book.views}</p>

                        <Link
                            // Link to 속성도 currentSubscriberId가 유효할 때만 올바른 URL을 생성
                            to={currentSubscriberId === null || currentSubscriberId === '' ? '#' : `/books/${book.id}?subscriberId=${currentSubscriberId}`}
                            state={{ bookSummary: book }}
                        >
                            {/* '열람하기' 버튼은 currentSubscriberId가 유효한 숫자일 때만 활성화 */}
                            <button
                                disabled={currentSubscriberId === null || currentSubscriberId === '' || isNaN(currentSubscriberId) || currentSubscriberId <= 0}
                            >
                                열람하기
                            </button>
                        </Link>
                        <button onClick={() => handleDeleteBook(book.id)} style={{ marginLeft: '10px', backgroundColor: '#dc3545', color: 'white', border: 'none', padding: '8px 15px', borderRadius: '4px', cursor: 'pointer' }}>
                            삭제 (관리자용)
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default BookListPage;