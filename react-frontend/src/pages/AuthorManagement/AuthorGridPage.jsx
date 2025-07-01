import React, { useState, useEffect } from 'react';
import useGridLogic from '../../hooks/useGridLogic';
import apiService from '../../api/apiService';
import ApproveAuthor from '../../components/ApproveAuthor';
import RejectAuthor from '../../components/RejectAuthor';

// EbookStatisticsView 컴포넌트가 없으므로 임시로 빈 컴포넌트를 만듭니다.
const EbookStatisticsView = ({ onSearch }) => (
    <div style={{ padding: '15px', border: '1px solid #eee', marginBottom: '10px' }}>
        검색 컴포넌트 (EbookStatisticsView)
        {/* 검색 필드 및 버튼 추가 */}
        <button onClick={() => onSearch({ /* 검색 조건 */ })}>Search</button>
    </div>
);

// 사용자 역할을 확인하는 임시 함수. 실제로는 백엔드에서 사용자 정보를 가져와야 합니다.
const hasRole = (role) => {
    // 현재는 'Admin' 역할을 true로 가정합니다.
    return role === 'Admin';
};

const AuthorGridPage = () => {
    // useGridLogic 커스텀 훅을 사용하여 공통 로직을 가져옵니다.
    const {
        data: authors, // <-- 이제 훅에서 가공된 배열이 반환되므로 authorsData를 authors로 사용합니다.
        loading,
        selectedRow,
        changeSelectedRow,
        fetchData,
        showSnackbar,
        snackbar,
        setSelectedRow,
        updateRow,
        deleteRow,
        registerRow // <-- 훅에서 새로 추가된 등록 함수를 가져옵니다.
    } = useGridLogic('authors');

    const [openDialog, setOpenDialog] = useState(false); // 등록 다이얼로그
    const [editDialog, setEditDialog] = useState(false); // 수정 다이얼로그
    const [approveAuthorDialog, setApproveAuthorDialog] = useState(false); // 작가승인 다이얼로그
    const [rejectAuthorDialog, setRejectAuthorDialog] = useState(false); // 작가거부 다이얼로그
    const [newAuthor, setNewAuthor] = useState({ email: '', name: '', detail: '', portfolio: '', isApprove: false });

    // Vue의 addNewRow와 유사한 기능
    const addNewRow = () => {
        setNewAuthor({ email: '', name: '', detail: '', portfolio: '', isApprove: false });
        setOpenDialog(true);
    };

    // ** <<< handleRegisterAuthor 함수가 수정되었습니다 >>> **
    const handleRegisterAuthor = async () => {
        // useGridLogic에서 반환된 registerRow 함수를 사용합니다.
        const success = await registerRow(newAuthor);
        if (success) {
            setOpenDialog(false);
            // fetchData(); // Optimistic update를 사용하므로 굳이 refetch할 필요는 없습니다.
        }
        // showSnackbar와 console.error는 registerRow 함수 내에서 처리됩니다.
    };
    
    const openEditDialog = () => {
        if (selectedRow) {
            setEditDialog(true);
        }
    };

    const handleSaveEdit = async () => {
        if (selectedRow) {
            await updateRow(selectedRow);
            setEditDialog(false);
        }
    };

    const approveAuthor = async () => {
        if (!selectedRow) return;
        try {
            const endpoint = `/authors/${selectedRow.id}/approveauthor`; 
            const payload = { isApprove: true }; 
            await apiService.put(endpoint, payload); 
            showSnackbar('작가 승인 성공적으로 처리되었습니다.', 'success');
            setApproveAuthorDialog(false);
            fetchData();
        } catch (error) {
            console.error('작가 승인 실패:', error);
            showSnackbar('작가 승인에 실패했습니다.', 'error');
        }
    };
    
    const rejectAuthor = async () => {
        if (!selectedRow) return;
        try {
            const endpoint = `/authors/${selectedRow.id}/rejectauthor`; 
            const payload = { isApprove: false }; 
            await apiService.put(endpoint, payload); 
            showSnackbar('작가 거부 성공적으로 처리되었습니다.', 'success');
            setRejectAuthorDialog(false);
            fetchData();
        } catch (error) {
            console.error('작가 거부 실패:', error);
            showSnackbar('작가 거부에 실패했습니다.', 'error');
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            {snackbar.status && (
                <div style={{ position: 'fixed', bottom: '20px', right: '20px', padding: '15px', backgroundColor: snackbar.color === 'success' ? '#4CAF50' : '#F44336', color: 'white', borderRadius: '5px' }}>
                    {snackbar.message}
                    <button onClick={() => showSnackbar({ ...snackbar, status: false })} style={{ marginLeft: '20px', color: 'white', border: 'none', background: 'transparent', cursor: 'pointer' }}>Close</button>
                </div>
            )}

            <div style={{ marginBottom: '20px' }}>
                <button onClick={addNewRow} style={{ marginRight: '5px' }}>등록</button>
                <button onClick={openEditDialog} disabled={!selectedRow} style={{ marginRight: '5px' }}>수정</button>
                <button onClick={() => setApproveAuthorDialog(true)} disabled={!selectedRow || !hasRole('Admin')} style={{ marginRight: '5px' }}>작가승인</button>
                <button onClick={() => setRejectAuthorDialog(true)} disabled={!selectedRow || !hasRole('Admin')}>작가거부</button>
            </div>
            
            {selectedRow && (
                <div style={{ padding: '15px', marginBottom: '20px', border: '1px solid #ccc', borderRadius: '5px', backgroundColor: '#f9f9f9' }}>
                    <h4 style={{ margin: '0 0 10px 0' }}>선택된 작가</h4>
                    <p style={{ margin: '5px 0' }}><strong>ID:</strong> {selectedRow.id}</p>
                    <p style={{ margin: '5px 0' }}><strong>Email:</strong> {selectedRow.email}</p>
                    <p style={{ margin: '5px 0' }}><strong>Name:</strong> {selectedRow.name}</p>
                    <button onClick={() => setSelectedRow(null)} style={{ marginTop: '10px', padding: '5px 10px' }}>선택 해제</button>
                </div>
            )}
            
            <EbookStatisticsView onSearch={fetchData} />

            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                    <thead>
                        <tr style={{ borderBottom: '2px solid #ddd' }}>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Id</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Email</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Name</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Detail</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Portfolio</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>IsApprove</th>
                            <th style={{ padding: '12px', textAlign: 'center' }}>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>Loading...</td></tr>
                        ) : (
                            authors.map((author, index) => (
                                <tr
                                    key={author.id || index}
                                    onClick={() => {
                                        changeSelectedRow(author);
                                        console.log('선택된 작가:', author); 
                                    }}
                                    style={{ cursor: 'pointer', backgroundColor: selectedRow?.id === author.id ? '#e0f7fa' : 'white', borderBottom: '1px solid #eee' }}
                                >
                                    <td style={{ padding: '12px' }}>{author.id}</td>
                                    <td style={{ padding: '12px' }}>{author.email}</td>
                                    <td style={{ padding: '12px' }}>{author.name}</td>
                                    <td style={{ padding: '12px' }}>{author.detail}</td>
                                    <td style={{ padding: '12px' }}>{author.portfolio}</td>
                                    <td style={{ padding: '12px' }}>{author.isApprove ? 'Yes' : 'No'}</td>
                                    <td style={{ padding: '12px', textAlign: 'center' }}>
                                        <button onClick={(e) => { e.stopPropagation(); deleteRow(author); }} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'red' }}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {openDialog && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '400px', backgroundColor: 'white', padding: '30px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: 0 }}>Author 등록</h3>
                        <button onClick={() => setOpenDialog(false)}>&times;</button>
                    </div>
                    <div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Email</label>
                            <input type="text" value={newAuthor.email} onChange={(e) => setNewAuthor({...newAuthor, email: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Name</label>
                            <input type="text" value={newAuthor.name} onChange={(e) => setNewAuthor({...newAuthor, name: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Detail</label>
                            <input type="text" value={newAuthor.detail} onChange={(e) => setNewAuthor({...newAuthor, detail: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Portfolio</label>
                            <input type="text" value={newAuthor.portfolio} onChange={(e) => setNewAuthor({...newAuthor, portfolio: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <button onClick={handleRegisterAuthor} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none' }}>등록</button>
                    </div>
                </div>
            )}

            {editDialog && selectedRow && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '400px', backgroundColor: 'white', padding: '30px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: 0 }}>Author 수정</h3>
                        <button onClick={() => setEditDialog(false)}>&times;</button>
                    </div>
                    <div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Email</label>
                            <input type="text" value={selectedRow.email} onChange={(e) => setSelectedRow({...selectedRow, email: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Name</label>
                            <input type="text" value={selectedRow.name} onChange={(e) => setSelectedRow({...selectedRow, name: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <button onClick={handleSaveEdit} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none' }}>수정</button>
                    </div>
                </div>
            )}

            {approveAuthorDialog && selectedRow && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000 }}>
                    <ApproveAuthor
                        onClose={() => setApproveAuthorDialog(false)}
                        onApprove={approveAuthor}
                    />
                </div>
            )}
            
            {rejectAuthorDialog && selectedRow && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000 }}>
                    <RejectAuthor
                        onClose={() => setRejectAuthorDialog(false)}
                        onReject={rejectAuthor}
                    />
                </div>
            )}
        </div>
    );
};

export default AuthorGridPage;