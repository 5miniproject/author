import React, { useState, useEffect } from 'react';
import apiService from '../../api/apiService';

const AuthorDashboard = ({ isAdmin }) => {
    const [newAuthor, setNewAuthor] = useState({ name: '', email: '', detail: '', portfolio: '' });
    const [pendingAuthors, setPendingAuthors] = useState([]);

    useEffect(() => {
        if (isAdmin) {
            fetchPendingAuthors();
        }
    }, [isAdmin]);

    const fetchPendingAuthors = async () => {
        try {
            // ReadModel: 작가 목록 조회 (관리자용, 가정)
            const authors = await apiService.get('/authors/pending'); // 미승인 작가 목록
            setPendingAuthors(authors);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('미승인 작가 목록 조회 실패:', error);
        }
    };

    const handleRegisterAuthor = async () => {
        try {
            // Command: 작가 등록
            await apiService.post('/authors/register', newAuthor);
            alert('작가 등록 요청이 완료되었습니다. 관리자의 승인을 기다려주세요.');
            setNewAuthor({ name: '', email: '', detail: '', portfolio: '' });
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('작가 등록 실패:', error);
        }
    };

    const handleApproveReject = async (authorId, isApprove) => {
        try {
            const endpoint = isApprove ? `/authors/approve/${authorId}` : `/authors/reject/${authorId}`;
            const command = isApprove ? '승인' : '거부';
            // Command: 작가 승인/거부
            await apiService.post(endpoint, { isApprove });
            alert(`작가 ${command}이(가) 완료되었습니다.`);
            fetchPendingAuthors();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error(`${command} 실패:`, error);
        }
    };

    const handleDeleteAuthor = async (authorId) => {
        try {
            // Command: 작가 삭제
            await apiService.delete(`/authors/${authorId}`);
            alert('작가가 삭제되었습니다.');
            fetchPendingAuthors();
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('작가 삭제 실패:', error);
        }
    };

    return (
        <div>
            <h2>작가 관리</h2>
            {!isAdmin ? (
                <div>
                    <h3>작가 등록</h3>
                    <input
                        type='text'
                        placeholder='이름'
                        value={newAuthor.name}
                        onChange={(e) => setNewAuthor({ ...newAuthor, name: e.target.value })}
                    />
                    <input
                        type='email'
                        placeholder='이메일'
                        value={newAuthor.email}
                        onChange={(e) => setNewAuthor({ ...newAuthor, email: e.target.value })}
                    />
                    <button onClick={handleRegisterAuthor}>등록 요청</button>
                </div>
            ) : (
                <div>
                    <h3>미승인 작가 목록 (관리자)</h3>
                    <ul>
                        {pendingAuthors.map((author) => (
                            <li key={author.id}>
                                {author.name} ({author.email})
                                <button onClick={() => handleApproveReject(author.id, true)}>
                                    승인
                                </button>
                                <button onClick={() => handleApproveReject(author.id, false)}>
                                    거부
                                </button>
                                <button onClick={() => handleDeleteAuthor(author.id)}>삭제</button>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default AuthorDashboard;