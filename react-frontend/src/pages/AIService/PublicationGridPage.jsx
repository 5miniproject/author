import React, { useState, useEffect } from 'react';
import useGridLogic from '../../hooks/useGridLogic';
import apiService from '../../api/apiService';

const PublicationGridPage = () => {
    // useGridLogic 커스텀 훅을 사용하여 출판물 목록을 관리합니다.
    const {
        data: publications,
        loading,
        selectedRow,
        changeSelectedRow,
        fetchData,
        showSnackbar,
        snackbar,
        deleteRow, 
    } = useGridLogic('publications'); // API 경로를 'publications'로 설정

    // 선택된 출판물의 상세 정보를 콘솔에 출력하는 효과
    useEffect(() => {
        if (selectedRow) {
            // eslint-disable-next-line no-console
            console.log('선택된 출판물:', selectedRow);
        }
    }, [selectedRow]);

    const handleRegenerateImage = async () => {
        if (!selectedRow) {
            showSnackbar('재생성할 출판물을 선택해주세요.', 'warning');
            return;
        }
        const id = selectedRow.id;
        try {
            // Command: regenerateI
            // 백엔드 컨트롤러에 정의된 PUT /publications/{id}/regenerate 엔드포인트 호출
            await apiService.put(`/publications/${id}/regenerate`, {}); // 빈 바디를 보냅니다.
            showSnackbar('표지 이미지 재생성 요청이 완료되었습니다.', 'success');
            // 재생성 요청 후 바로 데이터 갱신
            fetchData();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('이미지 재생성 요청 실패:', error);
            showSnackbar('이미지 재생성 요청에 실패했습니다.', 'error');
        }
    };
    
    const handleDeleteSelected = () => {
        if (!selectedRow) {
            showSnackbar('삭제할 출판물을 선택해주세요.', 'warning');
            return;
        }
        // 사용자의 확인을 받습니다.
        if (window.confirm(`정말 "${selectedRow.title}" 출판물을 삭제하시겠습니까?`)) {
            // useGridLogic의 deleteRow 함수를 호출하여 API 요청을 보냅니다.
            deleteRow(selectedRow);
        }
    };

    // ==============================================================
    // [추가된 부분]
    // '책 발간 요청' 버튼 클릭 핸들러를 추가합니다.
    // ==============================================================
    const handlePublishBook = async () => {
        if (!selectedRow) {
            showSnackbar('발간을 요청할 출판물을 선택해주세요.', 'warning');
            return;
        }
        const id = selectedRow.id;
        const authorId = selectedRow.authorId;
        
        try {
            // 요청하신 엔드포인트와 메서드(PUT)를 사용합니다.
            // 바디에 authorId를 포함하여 보냅니다.
            await apiService.put(`/publications/${id}/publishbook`, { authorId });
            showSnackbar('책 발간 요청이 성공적으로 처리되었습니다.', 'success');
            fetchData(); // 요청 성공 후 목록을 갱신합니다.
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('책 발간 요청 실패:', error);
            showSnackbar('책 발간 요청에 실패했습니다. 백엔드 상태를 확인하세요.', 'error');
        }
    };
    // ==============================================================
    // [추가된 부분 끝]
    // ==============================================================

    return (
        <div style={{ padding: '20px' }}>
            {/* 1. Snackbar */}
            {snackbar.status && (
                <div style={{ position: 'fixed', bottom: '20px', right: '20px', padding: '15px', backgroundColor: snackbar.color === 'success' ? '#4CAF50' : '#F44336', color: 'white', borderRadius: '5px', zIndex: 999 }}>
                    {snackbar.message}
                    <button onClick={() => showSnackbar({ ...snackbar, status: false })} style={{ marginLeft: '20px', color: 'white', border: 'none', background: 'transparent', cursor: 'pointer' }}>Close</button>
                </div>
            )}

            {/* 2. Button Panel */}
            <div style={{ marginBottom: '20px' }}>
                <button onClick={handleRegenerateImage} disabled={!selectedRow} style={{ marginRight: '5px' }}>이미지 재생성</button>
                <button onClick={handleDeleteSelected} disabled={!selectedRow} style={{ marginRight: '5px', backgroundColor: '#dc3545', color: 'white' }}>선택 삭제</button>
                {/* ==============================================================
                  [추가된 부분]
                  '책 발간 요청' 버튼을 추가합니다.
                  ============================================================== */}
                <button onClick={handlePublishBook} disabled={!selectedRow} style={{ marginRight: '5px', backgroundColor: '#28a745', color: 'white' }}>책 발간 요청</button>
                {/* ==============================================================
                  [추가된 부분 끝]
                  ============================================================== */}
            </div>

            {/* 3. Data Table */}
            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                    <thead>
                        <tr style={{ borderBottom: '2px solid #ddd' }}>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Id</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>AuthorId</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Title</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Authorname</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Status</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Category</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>SubscriptionFee</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Cover Image</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Plot URL</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="9" style={{ textAlign: 'center', padding: '20px' }}>Loading...</td></tr>
                        ) : (
                            publications.map((publication) => (
                                <tr
                                    key={publication.id}
                                    onClick={() => changeSelectedRow(publication)}
                                    style={{ cursor: 'pointer', backgroundColor: selectedRow?.id === publication.id ? '#e0f7fa' : 'white', borderBottom: '1px solid #eee' }}
                                >
                                    <td style={{ padding: '12px' }}>{publication.id}</td>
                                    <td style={{ padding: '12px' }}>{publication.authorId}</td>
                                    <td style={{ padding: '12px' }}>{publication.title}</td>
                                    <td style={{ padding: '12px' }}>{publication.authorname}</td>
                                    <td style={{ padding: '12px' }}>{publication.status}</td>
                                    <td style={{ padding: '12px' }}>{publication.category}</td>
                                    <td style={{ padding: '12px' }}>{publication.subscriptionFee}</td>
                                    <td style={{ padding: '12px' }}>
                                        {publication.coverImageUrl ? (
                                            <img src={`http://52.189.85.20${publication.coverImageUrl}`} alt="Cover" style={{ width: '50px', height: 'auto', borderRadius: '4px' }} />
                                        ) : 'No Image'}
                                    </td>
                                    <td style={{ padding: '12px' }}>
                                        {publication.plotUrl ? (
                                            <a href={`http://52.189.85.20${publication.plotUrl}`} target="_blank" rel="noopener noreferrer">Download PDF</a>
                                        ) : 'No Plot'}
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* 4. 선택된 출판물 상세 정보 패널 */}
            {selectedRow && (
                <div style={{ marginTop: '20px', padding: '20px', border: '1px solid #ccc', borderRadius: '8px', backgroundColor: '#f9f9f9' }}>
                    <h3 style={{ marginTop: '0' }}>선택된 출판물 상세 정보</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: '150px 1fr', gap: '10px' }}>
                        <strong>Id:</strong><span>{selectedRow.id}</span>
                        <strong>AuthorId:</strong><span>{selectedRow.authorId}</span>
                        <strong>Title:</strong><span>{selectedRow.title}</span>
                        <strong>Authorname:</strong><span>{selectedRow.authorname}</span>
                        <strong>Status:</strong><span>{selectedRow.status}</span>
                        <strong>Category:</strong><span>{selectedRow.category}</span>
                        <strong>Subscription Fee:</strong><span>{selectedRow.subscriptionFee}</span>
                        <strong>Plot:</strong><span style={{ whiteSpace: 'pre-wrap' }}>{selectedRow.plot}</span>
                        <strong>Cover Image URL:</strong><span>{selectedRow.coverImageUrl}</span>
                        <strong>Plot URL:</strong><span>{selectedRow.plotUrl}</span>
                    </div>
                    {selectedRow.coverImageUrl && (
                        <div style={{ marginTop: '20px', textAlign: 'center' }}>
                            <h4>생성된 표지 이미지</h4>
                            <img src={`http://52.189.85.20${selectedRow.coverImageUrl}`} alt="Generated Cover" style={{ maxWidth: '300px', maxHeight: '300px', border: '1px solid #ddd', borderRadius: '8px' }} />
                        </div>
                    )}
                    <button onClick={() => changeSelectedRow(null)} style={{ marginTop: '15px', padding: '5px 10px', backgroundColor: '#e9e9e9', border: '1px solid #ccc', borderRadius: '4px', cursor: 'pointer' }}>선택 해제</button>
                </div>
            )}
        </div>
    );
};

export default PublicationGridPage;