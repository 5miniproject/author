import React, { useState, useEffect } from 'react';
import { useParams, Link, useLocation } from 'react-router-dom'; 
import apiService from '../../api/apiService';

const BookDetailPage = () => {
    const { bookId } = useParams(); 
    const location = useLocation(); 
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBookDetail = async () => {
            const queryParams = new URLSearchParams(location.search);
            const subscriberIdParam = queryParams.get('subscriberId');
            
            const subscriberId = subscriberIdParam ? parseInt(subscriberIdParam, 10) : 1;
            if (isNaN(subscriberId) || subscriberId <= 0) {
                setError("유효하지 않은 구독자 ID입니다. 기본값으로 설정됩니다.");
                setLoading(false);
                return;
            }

            if (!subscriberId) {
                setError("책 정보를 불러오려면 구독자 ID가 필요합니다.");
                setLoading(false);
                return;
            }

            try {
                // 1. 책 상세 정보 (열람 요청) - 이 API에서 이미 isBestSeller를 포함한 모든 Book 정보가 반환됩니다.
                // eslint-disable-next-line no-console
                console.log(`책 ID: ${bookId}, 구독자 ID: ${subscriberId} 로 상세 정보 요청`);
                const response = await apiService.post(`/books/${bookId}/read`, { subscriberId }); 
                let fetchedBook = response.data; // Book 객체 전체를 받음
                
                setBook(fetchedBook); // 최종 통합된 책 정보로 상태 업데이트

            } catch (err) {
                if (err.response?.status === 403) {
                    setError('이 책을 열람할 구독 권한이 없습니다.');
                } else if (err.response?.status === 404) {
                    setError('책을 찾을 수 없거나 이미 삭제된 책입니다.');
                }
                else {
                    setError('책 정보를 불러오는 데 실패했습니다.');
                }
                // eslint-disable-next-line no-console
                console.error('상세 정보 조회 실패:', err);
            } finally {
                setLoading(false);
            }
        };

        // location.state에서 bookSummary가 있다면 먼저 설정
        if (location.state?.bookSummary) {
            // bookSummary는 isBestSeller 정보를 이미 가지고 있을 수 있지만,
            // fetchBookDetail에서 다시 가져와서 최신 상태로 업데이트할 것이므로 여기서는 임시 설정
            setBook(location.state.bookSummary); 
        }

        fetchBookDetail();
    }, [bookId, location.search, location.state]); 

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;
    if (!book) return <div>책 정보를 찾을 수 없습니다.</div>;

    return (
        <div>
            <Link to="/books">← 목록으로 돌아가기</Link>
            <hr />
            <h2>
                {book.isBestSeller && <span style={{ color: 'red', fontWeight: 'bold', marginRight: '10px' }}>[BEST]</span>} {/* 베스트셀러 배지 */}
                {book.title} (ID: {book.id}) {/* <-- [추가된 부분] 책 ID 표시 */}
            </h2>
            <p><strong>저자:</strong> {book.authorName}</p>
            <p><strong>줄거리:</strong> {book.plot}</p>
            <hr />
            <h3>내용</h3>
            <p>{book.contents}</p>
        </div>
    );
};

export default BookDetailPage;