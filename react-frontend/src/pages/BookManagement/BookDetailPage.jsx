import React, { useState, useEffect } from 'react';
import { useParams, Link, useLocation } from 'react-router-dom'; // useLocation 임포트
import apiService from '../../api/apiService';

const BookDetailPage = () => {
    const { bookId } = useParams(); // URL 파라미터에서 bookId를 추출 (예: /books/123 -> bookId = 123)
    const location = useLocation(); // 현재 URL의 location 객체를 가져옴
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBookDetail = async () => {
            // ==============================================================
            // [수정된 부분]
            // URL 쿼리 파라미터에서 subscriberId를 추출합니다.
            // ==============================================================
            const queryParams = new URLSearchParams(location.search);
            const subscriberIdParam = queryParams.get('subscriberId'); // 'subscriberId' 쿼리 파라미터 값 추출
            
            // 추출된 subscriberIdParam이 없거나 유효하지 않으면 기본값 1을 사용합니다.
            // 필요에 따라 로그인된 사용자 ID를 가져오는 로직으로 대체할 수 있습니다.
            const subscriberId = subscriberIdParam ? parseInt(subscriberIdParam, 10) : 1;
            // 숫자로 변환할 수 없거나 0 이하의 값인 경우에도 기본값 1 사용
            if (isNaN(subscriberId) || subscriberId <= 0) {
                setError("유효하지 않은 구독자 ID입니다. 기본값으로 설정됩니다.");
                setLoading(false);
                return;
            }
            // ==============================================================
            // [수정된 부분 끝]
            // ==============================================================

            if (!subscriberId) { // subscriberId가 여전히 없으면 에러 처리
                setError("책 정보를 불러오려면 구독자 ID가 필요합니다.");
                setLoading(false);
                return;
            }

            try {
                // 추출한 bookId와 subscriberId를 사용하여 상세 정보를 요청
                // eslint-disable-next-line no-console
                console.log(`책 ID: ${bookId}, 구독자 ID: ${subscriberId} 로 상세 정보 요청`);
                const response = await apiService.post(`/books/${bookId}/read`, { subscriberId }); 
                setBook(response.data);
            } catch (err) {
                if (err.response?.status === 403) {
                    setError('이 책을 열람할 구독 권한이 없습니다.');
                } else {
                    setError('책 정보를 불러오는 데 실패했습니다.');
                }
                // eslint-disable-next-line no-console
                console.error('상세 정보 조회 실패:', err);
            } finally {
                setLoading(false);
            }
        };

        // location.state에서 bookSummary를 먼저 설정하여 로딩 중에도 일부 정보를 보여줍니다.
        if (location.state?.bookSummary) {
            setBook(location.state.bookSummary); 
            // 상세 정보는 나중에 비동기적으로 불러오므로 loading은 여전히 true로 유지
        }

        fetchBookDetail();
    }, [bookId, location.search, location.state]); // bookId와 location.search(쿼리 파라미터) 변경 시 다시 데이터를 가져옴

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