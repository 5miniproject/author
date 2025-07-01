import React, { useState, useEffect } from 'react';
import apiService from '../../api/apiService';

const SubscriptionPage = () => {
    const [subscriberId, setSubscriberId] = useState('subscriber-456'); // 예시 구독자 ID
    const [bookId, setBookId] = useState('');
    const [subscriptionStatus, setSubscriptionStatus] = useState('조회 중...');

    useEffect(() => {
        // eslint-disable-next-line no-console
        console.log('Fetching subscription status:', subscriberId);
        fetchSubscriptionStatus();
    }, [subscriberId]);

    const fetchSubscriptionStatus = async () => {
        try {
            // ReadModel: 구독 현황 확인
            const statusData = await apiService.get(`/subscribers/${subscriberId}`);
            setSubscriptionStatus(statusData.status);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('구독 현황 조회 실패:', error);
            setSubscriptionStatus('조회 실패');
        }
    };

    const handleSubscribe = async () => {
        try {
            // Command: 서적 구독 신청
            await apiService.post('/subscriptions/subscribe', {
                subscriberId,
                bookId,
                authorId: 'author-789',
            });
            alert('서적 구독 신청이 완료되었습니다.');
            fetchSubscriptionStatus();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('구독 신청 실패:', error);
            alert('구독 신청에 실패했습니다. 이미 구독 중이거나 문제가 발생했습니다.');
        }
    };

    const handleCancelSubscription = async () => {
        try {
            // Command: 서적 구독 취소
            await apiService.post('/subscriptions/cancel', { subscriberId, bookId });
            alert('서적 구독이 취소되었습니다.');
            fetchSubscriptionStatus();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('구독 취소 실패:', error);
            alert('구독 취소에 실패했습니다. 관리자에게 문의하세요.');
        }
    };

    return (
        <div>
            <h2>구독 관리</h2>
            <p>나의 구독 현황: {subscriptionStatus}</p>
            <hr />
            <div>
                <h3>서적 구독</h3>
                <input
                    type='text'
                    value={bookId}
                    onChange={(e) => setBookId(e.target.value)}
                    placeholder='구독할 서적 ID 입력'
                />
                <button onClick={handleSubscribe}>구독 신청</button>
                <button onClick={handleCancelSubscription}>구독 취소</button>
            </div>
        </div>
    );
};

export default SubscriptionPage;