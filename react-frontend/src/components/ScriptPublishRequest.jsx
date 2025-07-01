import React from 'react';

const ScriptPublishRequest = ({ onClose, onPublishRequest }) => {
    // Vue 템플릿에 'params'가 전달되는 것으로 보아, 필요한 파라미터를 받을 수 있습니다.
    // 여기서는 간단하게 onClose와 onPublishRequest 함수만 사용합니다.
    
    const handlePublish = () => {
        // 부모 컴포넌트의 scriptPublishRequest 함수를 호출
        onPublishRequest({}); // 필요한 파라미터가 있다면 객체에 담아 전달
    };

    return (
        <div style={{ padding: '20px', backgroundColor: 'white', boxShadow: '0 4px 12px rgba(0,0,0,0.15)', borderRadius: '8px' }}>
            <h3 style={{ marginBottom: '20px' }}>도서 출간 요청</h3>
            <p>선택된 원고의 출간을 요청하시겠습니까?</p>
            <div style={{ marginTop: '30px', textAlign: 'right' }}>
                <button onClick={onClose} style={{ marginRight: '10px', padding: '8px 16px', border: '1px solid #ccc', borderRadius: '4px' }}>취소</button>
                <button onClick={handlePublish} style={{ padding: '8px 16px', border: 'none', backgroundColor: '#007bff', color: 'white', borderRadius: '4px', cursor: 'pointer' }}>요청</button>
            </div>
        </div>
    );
};

export default ScriptPublishRequest;