import React, { useState } from 'react';
import apiService from '../../api/apiService';

const AIPage = () => {
    // API 호출에 필요한 출판물 ID 상태
    const [publicationId, setPublicationId] = useState('');
    const [aiServiceStatus, setAiServiceStatus] = useState('요청 대기 중');
    const [imageURL, setImageURL] = useState('');

    // ==============================================================
    // [수정된 부분 1]
    // 이미지/줄거리 최초 생성 기능은 백엔드 이벤트로 처리되므로,
    // 프론트엔드에서는 직접적인 Command가 아닌 Regenerate 기능만 남깁니다.
    // 기존 handleGenerateImage는 사용되지 않으므로 제거합니다.
    // ==============================================================
    
    // ==============================================================
    // [수정된 부분 2]
    // handleRegenerateImage 함수를 백엔드 API 명세에 맞게 수정합니다.
    // - HTTP Method: POST -> PUT
    // - Endpoint: /ai/regenerate-cover -> /publications/{id}/regenerate
    // ==============================================================
    const handleRegenerateImage = async () => {
        if (!publicationId) {
            setAiServiceStatus('출판물 ID를 입력해주세요.');
            return;
        }

        try {
            setAiServiceStatus('표지 이미지 재생성 요청 중...');
            // Command: regenerateI
            // 백엔드 컨트롤러에 정의된 PUT /publications/{id}/regenerate 엔드포인트 호출
            await apiService.put(`/publications/${publicationId}/regenerate`, {});
            
            // 성공 응답 후 상태 업데이트
            // 백엔드 응답에 imageURL이 포함될 경우를 가정하여 업데이트
            // const newImageUrl = response.data.coverImageUrl; // 백엔드 응답을 확인하세요.
            // setImageURL(newImageUrl); 
            
            setAiServiceStatus('표지 이미지 재생성 완료!');
            // 이미지 URL은 API 호출 후 다시 조회하거나, 응답에서 받아와야 합니다.
            // 여기서는 임시로 상태를 직접 업데이트합니다.
            // 실제 구현에서는 업데이트된 출판물 정보를 다시 GET으로 조회하는 것이 좋습니다.
            
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('표지 이미지 재생성 요청 실패:', error);
            setAiServiceStatus('표지 이미지 재생성 실패');
        }
    };

    // ==============================================================
    // [추가된 부분]
    // 줄거리/PDF 재생성 버튼에 대한 로직을 추가합니다.
    // 백엔드에 명시적인 API가 없으므로 알림만 띄웁니다.
    // ==============================================================
    const handleRegeneratePlot = () => {
        setAiServiceStatus('줄거리/PDF 재생성 기능은 백엔드에 명시된 API가 없습니다.');
    };
    
    return (
        <div>
            <h2>AI 서비스</h2>
            <p>AI가 출판물에 대한 표지 이미지를 재생성해 줍니다.</p>
            <hr />
            <div>
                <h3>표지 이미지 재생성</h3>
                <input
                    type='text'
                    placeholder='출판물 ID'
                    value={publicationId}
                    onChange={(e) => setPublicationId(e.target.value)}
                />
                <button onClick={handleRegenerateImage}>이미지 재생성 요청</button>
                {/* 백엔드에 API가 없으므로 알림만 띄우는 버튼입니다. */}
                <button onClick={handleRegeneratePlot}>줄거리/PDF 재생성 요청</button>
            </div>
            <p style={{ fontWeight: 'bold', color: aiServiceStatus.includes('실패') ? 'red' : 'green' }}>
                서비스 상태: {aiServiceStatus}
            </p>
            {imageURL && (
                <div style={{ marginTop: '20px' }}>
                    <h4>생성된 이미지:</h4>
                    <img src={imageURL} alt='Generated Cover' style={{ maxWidth: '300px', border: '1px solid #ccc' }} />
                </div>
            )}
        </div>
    );
};

export default AIPage;