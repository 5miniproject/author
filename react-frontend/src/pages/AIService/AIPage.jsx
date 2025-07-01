import React, { useState } from 'react';
import apiService from '../../api/apiService';

const AIPage = () => {
    const [authorId, setAuthorId] = useState('author-123'); // 예시 작가 ID
    const [manuscriptId, setManuscriptId] = useState('');
    const [aiServiceStatus, setAiServiceStatus] = useState('요청 대기 중');
    const [imageURL, setImageURL] = useState('');

    const handleGenerateImage = async () => {
        try {
            setAiServiceStatus('표지 이미지 생성 요청 중...');
            // Command: 표지이미지생성요청
            const response = await apiService.post('/ai/generate-cover', {
                authorId,
                manuscriptId,
            });
            setImageURL(response.data.imageURL); // 가정
            setAiServiceStatus('표지 이미지 생성 완료!');
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('표지 이미지 생성 요청 실패:', error);
            setAiServiceStatus('표지 이미지 생성 실패');
        }
    };

    const handleRegenerateImage = async () => {
        try {
            setAiServiceStatus('표지 이미지 재생성 요청 중...');
            // Command: 재생성요청
            const response = await apiService.post('/ai/regenerate-cover', {
                authorId,
                manuscriptId,
            });
            setImageURL(response.data.imageURL); // 가정
            setAiServiceStatus('표지 이미지 재생성 완료!');
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('표지 이미지 재생성 요청 실패:', error);
            setAiServiceStatus('표지 이미지 재생성 실패');
        }
    };

    return (
        <div>
            <h2>AI 서비스</h2>
            <p>AI가 원고의 내용을 기반으로 표지 이미지를 생성해 줍니다.</p>
            <hr />
            <div>
                <h3>표지 이미지 생성/재생성</h3>
                <input
                    type='text'
                    placeholder='원고 ID'
                    value={manuscriptId}
                    onChange={(e) => setManuscriptId(e.target.value)}
                />
                <button onClick={handleGenerateImage}>이미지 생성 요청</button>
                <button onClick={handleRegenerateImage}>이미지 재생성 요청</button>
            </div>
            <p>서비스 상태: {aiServiceStatus}</p>
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