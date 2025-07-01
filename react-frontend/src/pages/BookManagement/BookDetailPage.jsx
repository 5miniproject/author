import React, { useState, useEffect } from 'react';
import { useParams, Link, useLocation } from 'react-router-dom';;
import apiService from '../../api/apiService';

const BookDetailPage = () => {
    const { bookId } = useParams(); // URL 파라미터에서 bookId를 추출
    const location = useLocation(); 
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBookDetail = async () => {
            // subscriberId는 실제 로그인 정보로 대체해야 함 일단은 기본값
            const subscriberId = 1; 

            if (!subscriberId) {
                setError("책 정보를 불러오려면 로그인이 필요합니다.");
                setLoading(false);
                return;
            }

            try {
                // 추출한 bookId로 상세 정보를 요청
                const response = await apiService.post(`/books/${bookId}/read`, { subscriberId }); 
                setBook(response.data);
            } catch (err) {
                if (err.response?.status === 403) {
                    setError('이 책을 열람할 구독 권한이 없습니다.');
                } else {
                    setError('책 정보를 불러오는 데 실패했습니다.');
                }
                console.error('상세 정보 조회 실패:', err);
            } finally {
                setLoading(false);
            }
        };

        if (location.state?.bookSummary) {
            // 상세 정보가 부족하므로, title 등 일부만 먼저 보여주고 상세 정보를 불러옵니다.
            setBook(location.state.bookSummary); 
        }

        fetchBookDetail();
    }, [bookId, location.state]); // bookId가 변경될 때마다 다시 데이터를 가져옴

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;
    if (!book) return <div>책 정보를 찾을 수 없습니다.</div>;

    return (
        <div>
            <Link to="/books">← 목록으로 돌아가기</Link>
            <hr />
            <h2>{book.title}</h2>
            <p><strong>저자:</strong> {book.authorName}</p>
            <p><strong>줄거리:</strong> {book.plot}</p>
            <hr />
            <h3>내용</h3>
            {/* 만약 내용이 HTML이라면 dangerouslySetInnerHTML을 사용 */}
            {/* <div dangerouslySetInnerHTML={{ __html: book.contents }} /> */}
            <p>{book.contents}</p>
        </div>
    );
};

export default BookDetailPage;
