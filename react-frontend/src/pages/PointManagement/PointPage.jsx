import React, { useState, useEffect } from 'react';
import useGridLogic from '../../hooks/useGridLogic';
import apiService from '../../api/apiService';

const PointPage = () => {
    // 특정 사용자 ID 조회 관련 상태
    const [userId, setUserId] = useState(null); 
    const [inputUserId, setInputUserId] = useState(''); 
    const [currentPointBalance, setCurrentPointBalance] = useState(null); 

    // 모든 Point 객체 목록 조회 관련 상태 (useGridLogic 활용)
    const {
        data: allPointsData, 
        loading: allPointsLoading, 
        selectedRow: selectedPointRow, 
        setSelectedRow: setSelectedPointRow, 
        showSnackbar, 
        snackbar, 
    } = useGridLogic('points'); 

    // ID를 URL에서 추출하는 헬퍼 함수
    const extractIdFromHref = (href) => {
        if (!href) return null;
        const parts = href.split('/');
        return parseInt(parts[parts.length - 1], 10);
    };

    // 특정 사용자 ID의 현재 포인트 잔액을 가져오는 useEffect
    useEffect(() => {
        // userId가 유효할 때만 fetchCurrentPointBalance 호출
        if (userId !== null && userId !== '') { // null 또는 빈 문자열이 아닐 때만 호출
            // eslint-disable-next-line no-console
            console.log('Fetching single user point data for user:', userId);
            fetchCurrentPointBalance();
        } else {
            setCurrentPointBalance(null); // userId가 유효하지 않으면 포인트 잔액 초기화
        }
    }, [userId]); // userId가 변경될 때마다 실행

    const fetchCurrentPointBalance = async () => {
        try {
            // 백엔드 API 엔드포인트 경로 변경 반영
            const response = await apiService.get(`/points/${userId}`);
            // 응답에서 point 필드를 추출하여 상태 업데이트
            setCurrentPointBalance(response.data.point);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error(`포인트 조회 실패 (ID: ${userId}):`, error);
            setCurrentPointBalance(null); // 조회 실패 시 포인트 잔액 초기화
            
            if (error.response && error.response.status === 404) {
                 showSnackbar(`사용자 ID ${userId} 에 대한 포인트 정보가 없습니다.`, 'warning');
            } else {
                 showSnackbar(`포인트 조회 실패: 사용자 ID ${userId} 를 확인해주세요.`, 'error');
            }
        }
    };

    const handleInputUserIdChange = (e) => {
        setInputUserId(e.target.value);
    };

    const handleSetUserId = () => {
        const parsedId = parseInt(inputUserId, 10);
        if (isNaN(parsedId) || parsedId <= 0) {
            showSnackbar('유효한 사용자 ID (양의 정수)를 입력해주세요.', 'warning');
            setInputUserId(''); // 입력 필드 초기화
            setUserId(null); // userId 상태를 null로 설정
        } else {
            setUserId(parsedId); // userId 상태를 설정하여 useEffect 트리거
            showSnackbar(`사용자 ID가 ${parsedId}로 설정되었습니다.`, 'success'); // 포인트 조회 결과에 따라 스낵바 표시
        }
    };

    // 테이블 행 클릭 시 상단 userId 설정 함수
    const handleRowClick = (pointItem) => {
        const idFromHref = extractIdFromHref(pointItem._links.self.href);
        if (idFromHref) {
            setInputUserId(idFromHref.toString()); // 입력 필드에 ID 설정
            setUserId(idFromHref); // userId 상태 설정 (useEffect 트리거)
            showSnackbar(`테이블에서 사용자 ID ${idFromHref}를 선택했습니다.`, 'success');
        } else {
            showSnackbar('선택된 항목에서 유효한 사용자 ID를 찾을 수 없습니다.', 'warning');
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            {/* 1. Snackbar */}
            {snackbar.status && (
                <div style={{ position: 'fixed', bottom: '20px', right: '20px', padding: '15px', backgroundColor: snackbar.color === 'success' ? '#4CAF50' : '#F44336', color: 'white', borderRadius: '5px', zIndex: 999 }}>
                    {snackbar.message}
                    <button onClick={() => showSnackbar({ ...snackbar, status: false })} style={{ marginLeft: '20px', color: 'white', border: 'none', background: 'transparent', cursor: 'pointer' }}>Close</button>
                </div>
            )}

            <h2>포인트 관리</h2>
            <hr />
            
            <div style={{ marginBottom: '30px', padding: '15px', border: '1px solid #ddd', borderRadius: '8px', backgroundColor: '#f0f0f0' }}>
                <h3>특정 사용자 포인트 조회</h3>
                <div style={{ marginBottom: '15px' }}>
                    <p>현재 조회 중인 사용자 ID: <strong>{userId === null ? '설정되지 않음' : userId}</strong></p>
                    <input
                        type='number'
                        value={inputUserId}
                        onChange={handleInputUserIdChange}
                        placeholder='사용자 ID 입력'
                        min="1"
                        style={{ marginRight: '10px', padding: '8px', border: '1px solid #ccc', borderRadius: '4px' }}
                    />
                    <button 
                        onClick={handleSetUserId}
                        disabled={inputUserId === '' || isNaN(parseInt(inputUserId, 10)) || parseInt(inputUserId, 10) <= 0}
                        style={{ padding: '8px 15px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                    >
                        사용자 ID 설정
                    </button>
                </div>
                <p style={{ fontSize: '1.2em', fontWeight: 'bold' }}>
                    현재 포인트 잔액: {currentPointBalance === null ? '조회되지 않음' : `${currentPointBalance} P`}
                </p>
            </div>
            
            <hr />

            <h3>전체 포인트 목록 (사용자별 현재 포인트)</h3>
            {allPointsLoading ? (
                <div>로딩 중...</div>
            ) : allPointsData.length > 0 ? (
                <div style={{ overflowX: 'auto', marginTop: '10px' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                        <thead>
                            <tr style={{ backgroundColor: '#f2f2f2' }}>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>사용자 ID</th>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>현재 포인트</th>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>KT 고객</th>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>구독권 구매 여부</th>
                            </tr>
                        </thead>
                        <tbody>
                            {allPointsData.map((pointItem) => (
                                <tr 
                                    key={extractIdFromHref(pointItem._links.self.href)} 
                                    onClick={() => handleRowClick(pointItem)} 
                                    style={{ cursor: 'pointer', backgroundColor: (selectedPointRow && extractIdFromHref(selectedPointRow._links.self.href) === extractIdFromHref(pointItem._links.self.href)) ? '#e0f7fa' : 'white' }} 
                                >
                                    <td style={{ padding: '12px' }}>{extractIdFromHref(pointItem._links.self.href) || 'N/A'}</td>
                                    <td style={{ padding: '12px' }}>{pointItem.point} P</td>
                                    <td style={{ padding: '12px' }}>{pointItem.isKt ? 'Yes' : 'No'}</td>
                                    <td style={{ padding: '12px' }}>{pointItem.isPurchased ? 'Yes' : 'No'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div style={{ overflowX: 'auto', marginTop: '10px' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                        <thead>
                            <tr style={{ backgroundColor: '#f2f2f2' }}>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>사용자 ID</th>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>현재 포인트</th>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>KT 고객</th>
                                <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'left' }}>구독권 구매 여부</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr key="no-points-row">
                                <td colSpan="4" style={{ textAlign: 'center', padding: '20px' }}>포인트 정보가 없습니다.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default PointPage;