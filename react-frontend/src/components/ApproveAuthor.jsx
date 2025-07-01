import React, { useState } from 'react';

const ApproveAuthor = ({ onClose, onApprove }) => {
    const [params, setParams] = useState({}); // 필요한 파라미터가 있다면 여기에 상태로 관리

    const handleApprove = () => {
        onApprove(params); // 부모 컴포넌트의 approveAuthor 함수 호출
        onClose(); // 다이얼로그 닫기
    };

    return (
        <div style={{ padding: '20px', backgroundColor: 'white' }}>
            <h3>작가 승인</h3>
            <p>선택된 작가를 승인하시겠습니까?</p>
            {/* 필요한 입력 필드 추가 */}
            <div style={{ marginTop: '20px', textAlign: 'right' }}>
                <button onClick={onClose} style={{ marginRight: '10px' }}>취소</button>
                <button onClick={handleApprove}>승인</button>
            </div>
        </div>
    );
};

export default ApproveAuthor;