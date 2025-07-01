import React, { useState, useEffect } from 'react';
import apiService from '../../api/apiService';

const PointPage = () => {
    const [userId, setUserId] = useState('user-123'); // 예시 사용자 ID
    const [currentPoint, setCurrentPoint] = useState(0);
    const [amount, setAmount] = useState(0);
    const [history, setHistory] = useState([]);

    useEffect(() => {
        // eslint-disable-next-line no-console
        console.log('Fetching point data for user:', userId);
        fetchPointAndHistory();
    }, [userId]);

    const fetchPointAndHistory = async () => {
        try {
            // ReadModel: 포인트 조회
            const pointData = await apiService.get(`/points/${userId}`);
            setCurrentPoint(pointData.point);

            // ReadModel: 포인트 내역 조회 (가정)
            const historyData = await apiService.get(`/points/history/${userId}`);
            setHistory(historyData);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('포인트 및 내역 조회 실패:', error);
        }
    };

    const handleAddPoint = async () => {
        try {
            // Command: 포인트 추가
            await apiService.post('/points/add', { userId, amount });
            alert(`${amount} 포인트가 추가되었습니다.`);
            fetchPointAndHistory(); // 데이터 갱신
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('포인트 추가 실패:', error);
            alert('포인트 추가에 실패했습니다. 관리자에게 문의하세요.');
        }
    };

    const handleUsePoint = async () => {
        try {
            // Command: 포인트 사용
            await apiService.post('/points/use', { userId, amount });
            alert(`${amount} 포인트가 사용되었습니다.`);
            fetchPointAndHistory(); // 데이터 갱신
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('포인트 사용 실패:', error);
            alert('포인트 사용에 실패했습니다. 잔액을 확인하거나 관리자에게 문의하세요.');
        }
    };

    return (
        <div>
            <h2>포인트 관리</h2>
            <p>현재 포인트: {currentPoint} P</p>
            <hr />
            <div>
                <h3>포인트 추가/사용</h3>
                <input
                    type='number'
                    value={amount}
                    onChange={(e) => setAmount(Number(e.target.value))}
                    placeholder='포인트 입력'
                />
                <button onClick={handleAddPoint}>포인트 추가</button>
                <button onClick={handleUsePoint}>포인트 사용</button>
            </div>
            <hr />
            <h3>포인트 내역</h3>
            <ul>
                {history.map((item, index) => (
                    <li key={index}>
                        {item.description}: {item.change} P
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PointPage;