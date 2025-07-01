import React, { useState, useEffect } from 'react';
import apiService from '../../api/apiService';

const ManuscriptPage = () => {
    const [authorId, setAuthorId] = useState('author-123'); // 예시 작가 ID
    const [title, setTitle] = useState('');
    const [contents, setContents] = useState('');
    const [manuscriptId, setManuscriptId] = useState('');
    const [manuscripts, setManuscripts] = useState([]);

    useEffect(() => {
        fetchManuscripts();
    }, [authorId]);

    const fetchManuscripts = async () => {
        try {
            // ReadModel: 원고열람
            const response = await apiService.get(`/manuscripts/${authorId}`);
            setManuscripts(response.data);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('원고 목록 조회 실패:', error);
        }
    };

    const handleRegisterManuscript = async () => {
        try {
            // Command: 원고등록
            await apiService.post('/manuscripts/register', { authorId, title, contents });
            alert('원고가 성공적으로 등록되었습니다.');
            fetchManuscripts();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('원고 등록 실패:', error);
        }
    };

    const handleUpdateManuscript = async () => {
        try {
            // Command: 원고수정
            await apiService.put(`/manuscripts/${manuscriptId}`, { authorId, contents, title });
            alert('원고가 성공적으로 수정되었습니다.');
            fetchManuscripts();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('원고 수정 실패:', error);
        }
    };

    const handleDeleteManuscript = async (idToDelete) => {
        try {
            // Command: 원고삭제
            await apiService.delete(`/manuscripts/${idToDelete}`);
            alert('원고가 삭제되었습니다.');
            fetchManuscripts();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('원고 삭제 실패:', error);
        }
    };

    const handlePublishRequest = async (idToPublish) => {
        try {
            // Command: 도서출판요청
            await apiService.post(`/manuscripts/${idToPublish}/publish`, { authorId });
            alert('도서 출판이 요청되었습니다. 관리자의 승인을 기다려주세요.');
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('출판 요청 실패:', error);
        }
    };

    return (
        <div>
            <h2>원고 관리</h2>
            <p>현재 로그인된 작가 ID: {authorId}</p>
            <hr />
            <div>
                <h3>새 원고 등록</h3>
                <input
                    type='text'
                    placeholder='제목'
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
                <textarea
                    placeholder='내용'
                    value={contents}
                    onChange={(e) => setContents(e.target.value)}
                ></textarea>
                <button onClick={handleRegisterManuscript}>원고 등록</button>
            </div>
            <hr />
            <div>
                <h3>원고 수정</h3>
                <input
                    type='text'
                    placeholder='수정할 원고 ID'
                    value={manuscriptId}
                    onChange={(e) => setManuscriptId(e.target.value)}
                />
                <input
                    type='text'
                    placeholder='새 제목'
                    onChange={(e) => setTitle(e.target.value)}
                />
                <textarea
                    placeholder='새 내용'
                    onChange={(e) => setContents(e.target.value)}
                ></textarea>
                <button onClick={handleUpdateManuscript}>수정</button>
            </div>
            <hr />
            <h3>내 원고 목록</h3>
            <ul>
                {manuscripts.map((manuscript) => (
                    <li key={manuscript.id}>
                        <h4>{manuscript.title}</h4>
                        <p>{manuscript.contents.substring(0, 50)}...</p>
                        <button onClick={() => handleDeleteManuscript(manuscript.id)}>삭제</button>
                        <button onClick={() => handlePublishRequest(manuscript.id)}>
                            출판 요청
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ManuscriptPage;