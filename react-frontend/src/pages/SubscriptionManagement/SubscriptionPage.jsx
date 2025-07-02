import React, { useState, useEffect } from 'react';
import useGridLogic from '../../hooks/useGridLogic';
import apiService from '../../api/apiService';

const SubscriptionPage = () => {
    // useGridLogic 훅을 사용하여 구독자 목록을 관리합니다.
    const {
        data: subscribers, // 구독자 목록
        loading: subscribersLoading, // 구독자 목록 로딩 상태
        selectedRow: selectedSubscriber, // 선택된 구독자
        changeSelectedRow: changeSelectedSubscriber, // 선택된 구독자 변경 함수
        fetchData: fetchSubscribers, // 구독자 목록 새로고침 함수
        showSnackbar,
        snackbar,
        hideSnackbar, // useGridLogic에서 hideSnackbar를 가져옵니다.
        // useGridLogic에서 제공하는 CRUD 함수들을 가져옵니다.
        addNewRow, // <-- 새로운 구독자 추가 (로컬 상태만 변경)
        updateRow, // <-- 구독자 정보 수정
        deleteRow, // <-- 구독자 삭제
        setSelectedRow: setSelectedSubscriber, // 선택된 구독자 직접 설정 (모달에서 사용)
    } = useGridLogic('subscribers'); // API 경로를 'subscribers'로 설정

    const [bookId, setBookId] = useState('');
    const [subscribedBooks, setSubscribedBooks] = useState([]); // 선택된 구독자의 구독 목록
    const [subscribedBooksLoading, setSubscribedBooksLoading] = useState(false); // 구독 목록 로딩 상태

    const [openSubscriberDialog, setOpenSubscriberDialog] = useState(false); // 구독자 등록 모달
    const [editSubscriberDialog, setEditSubscriberDialog] = useState(false); // 구독자 수정 모달
    const [currentEditingSubscriber, setCurrentEditingSubscriber] = useState(null); // 수정 중인 구독자 객체

    // 새 구독자 등록 폼의 상태
    const [newSubscriber, setNewSubscriber] = useState({
        email: '',
        name: '',
        isKt: false,
    });

    // 헬퍼 함수: _links.self.href에서 ID 추출
    const extractIdFromHref = (href) => {
        if (!href) return null;
        const parts = href.split('/');
        return parseInt(parts[parts.length - 1], 10);
    };

    // 선택된 구독자가 변경될 때마다 해당 구독자의 구독 목록을 가져옵니다.
    useEffect(() => {
        if (selectedSubscriber) {
            // eslint-disable-next-line no-console
            console.log('선택된 구독자:', selectedSubscriber);
            fetchSubscribedBooks(selectedSubscriber.id);
            setCurrentEditingSubscriber({ ...selectedSubscriber }); // 수정 모달 초기값 설정
        } else {
            setSubscribedBooks([]); // 구독자 선택 해제 시 목록 초기화
            setCurrentEditingSubscriber(null);
        }
    }, [selectedSubscriber]);

    const fetchSubscribedBooks = async (subscriberId) => {
        setSubscribedBooksLoading(true);
        try {
            const response = await apiService.get(`/subscriptionOpens/search/findBySubscriberId?subscriberId=${subscriberId}`);
            const fetchedSubscriptions = response.data._embedded ? response.data._embedded.subscriptionOpens : [];

            const updatedSubscriptions = await Promise.all(
                fetchedSubscriptions.map(async (rawSubscription) => {
                    // 핵심 변경: _links.self.href에서 ID 추출
                    const subscriptionId = rawSubscription._links?.self?.href 
                                         ? extractIdFromHref(rawSubscription._links.self.href) 
                                         : null;

                    // rawSubscription의 모든 속성을 복사하고, id 필드를 직접 추가/덮어씁니다.
                    const subscriptionWithId = {
                        ...rawSubscription,
                        id: subscriptionId, // 여기에 추출된 ID를 할당
                    };
                    
                    try {
                        const checkBookResponse = await apiService.get(`/checkBooks/${subscriptionWithId.bookId}`);
                        const checkBookData = checkBookResponse.data;
                        return {
                            ...subscriptionWithId, // ID가 포함된 객체를 사용하여 반환
                            isBestSeller: checkBookData.isBestSeller,
                            title: checkBookData.title,
                        };
                    } catch (checkBookError) {
                        console.error(`CheckBook (ID: ${subscriptionWithId.bookId}) 정보 조회 실패:`, checkBookError);
                        return { 
                            ...subscriptionWithId, // ID가 포함된 객체를 사용하여 반환
                            isBestSeller: rawSubscription.isBestSeller || false, 
                            title: rawSubscription.title || '알 수 없는 책' 
                        };
                    }
                })
            );
            setSubscribedBooks(updatedSubscriptions);
        } catch (error) {
            console.error('선택된 구독자의 구독 목록 조회 실패:', error);
            setSubscribedBooks([]);
            showSnackbar('구독 목록 조회에 실패했습니다.', 'error');
        } finally {
            setSubscribedBooksLoading(false);
        }
    };

    const handleRegisterSubscriber = async () => {
        try {
            const response = await apiService.post('/subscribers', newSubscriber);
            showSnackbar('구독자 등록이 완료되었습니다.', 'success');
            setOpenSubscriberDialog(false);
            setNewSubscriber({ email: '', name: '', isKt: false });
            fetchSubscribers();
        } catch (error) {
            console.error('구독자 등록 실패:', error);
            const errorMessage = error.response?.data?.message || '구독자 등록에 실패했습니다.';
            showSnackbar(errorMessage, 'error');
        }
    };

    const handleOpenEditSubscriberDialog = () => {
        if (selectedSubscriber) {
            setEditSubscriberDialog(true);
        } else {
            showSnackbar('수정할 구독자를 선택해주세요.', 'warning');
        }
    };

    const handleSaveSubscriberEdit = async () => {
        if (!currentEditingSubscriber || !currentEditingSubscriber.id) {
            showSnackbar('유효한 구독자가 선택되지 않았습니다.', 'error');
            return;
        }
        try {
            await updateRow(currentEditingSubscriber);
            showSnackbar('구독자 정보가 성공적으로 수정되었습니다.', 'success');
            setEditSubscriberDialog(false);
            changeSelectedSubscriber(currentEditingSubscriber); 
        } catch (error) {
            console.error('구독자 수정 실패:', error);
            const errorMessage = error.response?.data?.message || '구독자 정보 수정에 실패했습니다.';
            showSnackbar(errorMessage, 'error');
        }
    };

    const handleDeleteSubscriber = () => {
        if (!selectedSubscriber) {
            showSnackbar('삭제할 구독자를 선택해주세요.', 'warning');
            return;
        }
        if (window.confirm(`정말 "${selectedSubscriber.name}" 구독자를 삭제하시겠습니까?`)) {
            deleteRow(selectedSubscriber);
            changeSelectedSubscriber(null);
        }
    };

    const handleSubscribe = async () => {
        if (!selectedSubscriber) {
            showSnackbar('구독할 구독자를 선택해주세요.', 'warning');
            return;
        }
        if (!bookId) {
            showSnackbar('구독할 서적 ID를 입력해주세요.', 'warning');
            return;
        }

        try {
            await apiService.post('/subscribeBooks', { 
                subscriberId: selectedSubscriber.id,
                bookId: bookId,
            });
            showSnackbar('서적 구독 신청이 완료되었습니다.', 'success');
            setBookId(''); 
            fetchSubscribedBooks(selectedSubscriber.id); 
            fetchSubscribers(); 
        } catch (error) {
            console.error('구독 신청 실패:', error);
            const errorMessage = error.response?.data?.message || '구독 신청에 실패했습니다.';
            showSnackbar(errorMessage, 'error');
        }
    };

    const handleCancelSubscription = async (subscriptionId) => {
        if (!selectedSubscriber) {
            showSnackbar('구독 취소할 구독자를 선택해주세요.', 'warning');
            return;
        }
        
        // subscriptionId의 유효성 검사 (숫자이고 0보다 큰지)
        if (subscriptionId === null || subscriptionId === undefined || isNaN(Number(subscriptionId)) || Number(subscriptionId) <= 0) {
            showSnackbar('취소할 구독 항목의 ID가 유효하지 않습니다. 콘솔 확인 요망.', 'error');
            console.error('handleCancelSubscription: Received invalid subscriptionId for cancellation.', subscriptionId);
            return;
        }

        if (!window.confirm('정말 이 구독을 취소하시겠습니까?')) {
            return;
        }

        try {
            await apiService.delete(`/subscribeBooks/${subscriptionId}`); // SubscriptionOpen의 ID로 직접 삭제 요청
            showSnackbar('서적 구독이 취소되었습니다.', 'success');
            fetchSubscribedBooks(selectedSubscriber.id); 
            fetchSubscribers(); 
        } catch (error) {
            console.error('구독 취소 실패:', error);
            const errorMessage = error.response?.data?.message || '구독 취소에 실패했습니다. 관리자에게 문의하세요.';
            showSnackbar(errorMessage, 'error');
        }
    };

    const handlePurchaseSubscription = async () => {
        if (!selectedSubscriber) {
            showSnackbar('구독권을 구매할 구독자를 선택해주세요.', 'warning');
            return;
        }
        if (selectedSubscriber.isPurchased) {
            showSnackbar('이미 구독권을 구매한 구독자입니다.', 'info');
            return;
        }
        if (!window.confirm(`"${selectedSubscriber.name}"님, 월 9,900원을 내고 구독권을 구매하시겠습니까?`)) {
            return;
        }

        try {
            await apiService.put(`/subscribers/${selectedSubscriber.id}/purchasesubscription`, {
                isPurchased: true, 
            });
            showSnackbar('구독권 구매가 성공적으로 처리되었습니다.', 'success');
            fetchSubscribers();
            changeSelectedSubscriber({ ...selectedSubscriber, isPurchased: true });
        } catch (error) {
            console.error('구독권 구매 실패:', error);
            const errorMessage = error.response?.data?.message || '구독권 구매에 실패했습니다.';
            showSnackbar(errorMessage, 'error');
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            {/* 1. Snackbar */}
            {snackbar.status && (
                <div style={{ position: 'fixed', bottom: '20px', right: '20px', padding: '15px', backgroundColor: snackbar.color === 'success' ? '#4CAF50' : '#F44336', color: 'white', borderRadius: '5px', zIndex: 999 }}>
                    {snackbar.message}
                    {/* Snackbar Close 버튼 클릭 핸들러 */}
                    <button onClick={hideSnackbar} style={{ marginLeft: '20px', color: 'white', border: 'none', background: 'transparent', cursor: 'pointer' }}>Close</button>
                </div>
            )}

            <h2>구독 관리</h2>
            <hr />

            {/* 2. 구독자 관리 버튼 패널 */}
            <div style={{ marginBottom: '20px' }}>
                <button onClick={() => setOpenSubscriberDialog(true)} style={{ marginRight: '5px' }}>구독자 등록</button>
                <button onClick={handleOpenEditSubscriberDialog} disabled={!selectedSubscriber} style={{ marginRight: '5px' }}>구독자 수정</button>
                <button onClick={handlePurchaseSubscription} disabled={!selectedSubscriber || selectedSubscriber.isPurchased} style={{ marginRight: '5px', backgroundColor: '#6c757d', color: 'white' }}>구독권 구매</button>
                <button onClick={handleDeleteSubscriber} disabled={!selectedSubscriber} style={{ marginRight: '5px', backgroundColor: '#dc3545', color: 'white' }}>구독자 삭제</button>
            </div>

            {/* 3. 구독자 목록 테이블 */}
            <h3>구독자 목록</h3>
            <div style={{ overflowX: 'auto', marginBottom: '20px' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                    <thead>
                        <tr style={{ borderBottom: '2px solid #ddd' }}>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Id</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Email</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Name</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Is Purchased</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Register Date</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Notification</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Is Kt</th>
                        </tr>
                    </thead>
                    <tbody>
                        {subscribersLoading ? (
                            <tr key="subscribers-loading-row"><td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>Loading Subscribers...</td></tr>
                        ) : (
                            subscribers.map((subscriber) => (
                                <tr
                                    key={subscriber.id}
                                    onClick={() => changeSelectedSubscriber(subscriber)}
                                    style={{ cursor: 'pointer', backgroundColor: selectedSubscriber?.id === subscriber.id ? '#e0f7fa' : 'white', borderBottom: '1px solid #eee' }}
                                >
                                    <td style={{ padding: '12px' }}>{subscriber.id}</td>
                                    <td style={{ padding: '12px' }}>{subscriber.email}</td>
                                    <td style={{ padding: '12px' }}>{subscriber.name}</td>
                                    <td style={{ padding: '12px' }}>{subscriber.isPurchased ? 'Yes' : 'No'}</td>
                                    <td style={{ padding: '12px' }}>{subscriber.registerDate ? new Date(subscriber.registerDate).toLocaleDateString() : ''}</td>
                                    <td style={{ padding: '12px' }}>{subscriber.notification}</td>
                                    <td style={{ padding: '12px' }}>{subscriber.isKt ? 'Yes' : 'No'}</td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* 4. 선택된 구독자 상세 정보 (선택 해제 버튼 포함) */}
            {selectedSubscriber && (
                <div style={{ marginTop: '20px', padding: '20px', border: '1px solid #ccc', borderRadius: '8px', backgroundColor: '#f9f9f9' }}>
                    <h3>선택된 구독자: {selectedSubscriber.name} (ID: {selectedSubscriber.id})</h3>
                    <p>이메일: {selectedSubscriber.email}</p>
                    <p>구독권 구매 여부: {selectedSubscriber.isPurchased ? '예' : '아니오'}</p>
                    <p>알림: {selectedSubscriber.notification}</p>
                    <button onClick={() => changeSelectedSubscriber(null)} style={{ marginTop: '15px', padding: '5px 10px', backgroundColor: '#e9e9e9', border: '1px solid #ccc', borderRadius: '4px', cursor: 'pointer' }}>선택 해제</button>
                </div>
            )}

            {/* 5. 서적 구독 신청 기능 */}
            <div style={{ marginTop: '30px' }}>
                <h3>서적 구독 신청</h3>
                <input
                    type='text'
                    value={bookId}
                    onChange={(e) => setBookId(e.target.value)}
                    placeholder='구독할 서적 ID 입력'
                    disabled={!selectedSubscriber}
                />
                <button onClick={handleSubscribe} disabled={!selectedSubscriber || !bookId}>구독 신청</button>
            </div>

            {/* 6. 선택된 구독자의 구독 목록 */}
            {selectedSubscriber && (
                <div style={{ marginTop: '30px' }}>
                    <h3>'{selectedSubscriber.name}' 님의 구독 목록</h3>
                    <div style={{ overflowX: 'auto' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                            <thead>
                                <tr style={{ borderBottom: '2px solid #ddd' }}>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>구독 ID</th> {/* 구독 ID 컬럼 추가 */}
                                    <th style={{ padding: '12px', textAlign: 'left' }}>책 ID</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>제목</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>상태</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>구독일</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>구독료</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>베스트셀러</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>액션</th>
                                </tr>
                            </thead>
                            <tbody>
                                {subscribedBooksLoading ? (
                                    <tr key="subscribed-books-loading-row"><td colSpan="9" style={{ textAlign: 'center', padding: '20px' }}>Loading Subscribed Books...</td>
                                    </tr>
                                ) : subscribedBooks.length === 0 ? (
                                    <tr key="no-subscribed-books-row"><td colSpan="9" style={{ textAlign: 'center', padding: '20px' }}>구독 중인 책이 없습니다.</td></tr>
                                ) : (
                                    subscribedBooks.map((subscription) => ( // 'book' 대신 'subscription'으로 변수명 변경
                                        <tr key={subscription.id} style={{ borderBottom: '1px solid #eee' }}>
                                            <td style={{ padding: '12px' }}>{subscription.id}</td> {/* 구독 ID 표시 */}
                                            <td style={{ padding: '12px' }}>{subscription.bookId}</td>
                                            <td style={{ padding: '12px' }}>
                                                {subscription.isBestSeller && <span style={{ color: 'red', fontWeight: 'bold', marginRight: '5px' }}>[BEST]</span>}
                                                {subscription.title || `(제목 없음) ID: ${subscription.bookId}`} {/* 제목이 없을 경우 표시 */}
                                            </td>
                                            <td style={{ padding: '12px' }}>{subscription.status}</td>
                                            <td style={{ padding: '12px' }}>{subscription.subscriptionDate ? new Date(subscription.subscriptionDate).toLocaleDateString() : ''}</td>
                                            <td style={{ padding: '12px' }}>{subscription.subscriptionFee}</td>
                                            <td style={{ padding: '12px' }}>{subscription.isBestSeller ? 'Yes' : 'No'}</td>
                                            <td style={{ padding: '12px' }}>
                                                <button onClick={() => handleCancelSubscription(subscription.id)} style={{ backgroundColor: '#dc3545', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '4px', cursor: 'pointer' }}>
                                                    구독 취소
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

            {/* 구독자 등록 모달 */}
            {openSubscriberDialog && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '400px', backgroundColor: 'white', padding: '30px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: 0 }}>구독자 등록</h3>
                        <button onClick={() => setOpenSubscriberDialog(false)}>&times;</button>
                    </div>
                    <div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Email</label><input type="email" value={newSubscriber.email} onChange={(e) => setNewSubscriber({...newSubscriber, email: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Name</label><input type="text" value={newSubscriber.name} onChange={(e) => setNewSubscriber({...newSubscriber, name: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>KT 고객 여부</label>
                            <input type="checkbox" checked={newSubscriber.isKt} onChange={(e) => setNewSubscriber({...newSubscriber, isKt: e.target.checked})} style={{ marginRight: '5px' }} />
                        </div>
                        <button onClick={handleRegisterSubscriber} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px' }}>등록</button>
                    </div>
                </div>
            )}

            {/* 구독자 수정 모달 */}
            {editSubscriberDialog && currentEditingSubscriber && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '400px', backgroundColor: 'white', padding: '30px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: 0 }}>구독자 수정</h3>
                        <button onClick={() => setEditSubscriberDialog(false)}>&times;</button>
                    </div>
                    <div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Id</label><input type="text" value={currentEditingSubscriber.id} disabled style={{ width: '100%', padding: '8px', boxSizing: 'border-box', backgroundColor: '#f0f0f0' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Email</label><input type="email" value={currentEditingSubscriber.email} onChange={(e) => setCurrentEditingSubscriber({...currentEditingSubscriber, email: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Name</label><input type="text" value={currentEditingSubscriber.name} onChange={(e) => setCurrentEditingSubscriber({...currentEditingSubscriber, name: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Is Purchased</label>
                            <input type="checkbox" checked={currentEditingSubscriber.isPurchased} onChange={(e) => setCurrentEditingSubscriber({...currentEditingSubscriber, isPurchased: e.target.checked})} style={{ marginRight: '5px' }} />
                        </div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>Notification</label><input type="text" value={currentEditingSubscriber.notification || ''} onChange={(e) => setCurrentEditingSubscriber({...currentEditingSubscriber, notification: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label style={{ display: 'block', marginBottom: '5px' }}>KT 고객 여부</label>
                            <input type="checkbox" checked={currentEditingSubscriber.isKt} onChange={(e) => setCurrentEditingSubscriber({...currentEditingSubscriber, isKt: e.target.checked})} style={{ marginRight: '5px' }} />
                        </div>
                        <button onClick={handleSaveSubscriberEdit} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px' }}>수정</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default SubscriptionPage;