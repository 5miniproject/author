import React, { useState, useEffect } from 'react';
import useGridLogic from '../../hooks/useGridLogic';
import apiService from '../../api/apiService';
import ScriptPublishRequest from '../../components/ScriptPublishRequest';

// BookScriptsOpen 컴포넌트가 없으므로 임시로 빈 컴포넌트를 만듭니다.
const BookScriptsOpen = ({ onSearch }) => (
    <div style={{ padding: '15px', border: '1px solid #eee', marginBottom: '10px' }}>
        검색 컴포넌트 (BookScriptsOpen)
        {/* 검색 필드 및 버튼 추가 */}
        <button onClick={() => onSearch({ /* 검색 조건 */ })}>Search</button>
    </div>
);

// 사용자 역할을 확인하는 임시 함수. 실제로는 백엔드에서 사용자 정보를 가져와야 합니다.
const hasRole = (role) => {
    // 'Author' 역할만 true로 가정합니다.
    return role === 'Author';
};

const ManuscriptGridPage = () => {
    // useGridLogic 커스텀 훅을 사용하여 공통 로직을 가져옵니다.
    const {
        data: manuscripts,
        loading,
        selectedRow,
        changeSelectedRow,
        fetchData,
        showSnackbar,
        snackbar,
        setSelectedRow, // 수정 다이얼로그에서 selectedRow를 직접 변경하기 위해 추가
        updateRow, // 수정 로직 재사용
        deleteRow, // 삭제 로직 재사용
    } = useGridLogic('bookScripts'); // path는 Vue 코드에 따라 'bookScripts'로 설정

    const [openDialog, setOpenDialog] = useState(false); // 등록 다이얼로그
    const [editDialog, setEditDialog] = useState(false); // 수정 다이얼로그
    const [scriptPublishRequestDialog, setScriptPublishRequestDialog] = useState(false); // 출간 요청 다이얼로그
    
    const [newManuscript, setNewManuscript] = useState({
        authorId: '',
        contents: '',
        status: 'draft',
        title: '',
        authorname: '',
    });

    useEffect(() => {
        if (selectedRow) {
            // eslint-disable-next-line no-console
            console.log('선택된 원고:', selectedRow);
        }
    }, [selectedRow]);

    const openAddNewDialog = () => {
        setNewManuscript({ authorId: '', contents: '', status: 'draft', title: '', authorname: '' });
        setOpenDialog(true);
    };

    const openEditDialog = () => {
        if (selectedRow) {
            setEditDialog(true);
        }
    };
    
    const handleRegisterManuscript = async () => {
        try {
            // Command: 원고등록 (BookScript 등록)
            await apiService.post('/bookScripts', newManuscript); // API 엔드포인트는 Vue 코드의 path: 'bookScripts' 기반
            showSnackbar('원고가 성공적으로 등록되었습니다.', 'success');
            setOpenDialog(false);
            fetchData(); // 데이터 갱신
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('원고 등록 실패:', error);
            showSnackbar('원고 등록에 실패했습니다.', 'error');
        }
    };
    
    const handleSaveEdit = async () => {
        if (selectedRow) {
            // Command: 원고수정 (BookScript 수정)
            await updateRow(selectedRow); // useGridLogic의 updateRow 사용
            setEditDialog(false);
        }
    };

    const scriptPublishRequest = async (params) => {
        if (!selectedRow) {
            showSnackbar('원고를 선택해주세요.', 'warning');
            return;
        }

        const id = selectedRow.id;
        if (!id) {
            showSnackbar('선택된 원고의 ID가 없습니다.', 'error');
            return;
        }

        try {
            const endpoint = `/bookScripts/${id}/scriptpublishrequest`;
            await apiService.put(endpoint, params); // params는 다이얼로그에서 넘어온 추가 데이터

            showSnackbar('도서 출간 요청이 성공적으로 처리되었습니다.', 'success');
            setScriptPublishRequestDialog(false);
            fetchData(); // 요청 성공 후 목록을 갱신합니다.
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('도서 출간 요청 실패:', error);
            showSnackbar('도서 출간 요청에 실패했습니다. 백엔드 상태를 확인하세요.', 'error');
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

            {/* 2. Button Panel */}
            <div style={{ marginBottom: '20px' }}>
                <button onClick={openAddNewDialog} style={{ marginRight: '5px' }}>등록</button>
                <button onClick={openEditDialog} disabled={!selectedRow} style={{ marginRight: '5px' }}>수정</button>
                <button onClick={() => setScriptPublishRequestDialog(true)} disabled={!selectedRow || !hasRole('Author')}>도서 출간 요청</button>
            </div>

            {/* 3. Search Panel */}
            <BookScriptsOpen onSearch={fetchData} />

            {/* 4. Data Table */}
            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                    <thead>
                        <tr style={{ borderBottom: '2px solid #ddd' }}>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Id</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>AuthorId</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Contents</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Status</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>CreatedAt</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>UpdatedAt</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Title</th>
                            <th style={{ padding: '12px', textAlign: 'left' }}>Authorname</th>
                            <th style={{ padding: '12px', textAlign: 'center' }}>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="9" style={{ textAlign: 'center', padding: '20px' }}>Loading...</td></tr>
                        ) : (
                            manuscripts.map((script, index) => (
                                <tr key={script.id || index}
                                    onClick={() => changeSelectedRow(script)}
                                    style={{ cursor: 'pointer', backgroundColor: selectedRow?.id === script.id ? '#e0f7fa' : 'white', borderBottom: '1px solid #eee' }}
                                ><td style={{ padding: '12px' }}>{script.id}</td>
                                    <td style={{ padding: '12px' }}>{script.authorId}</td>
                                    <td style={{ padding: '12px' }}>{script.contents?.substring(0, 50)}...</td>
                                    <td style={{ padding: '12px' }}>{script.status}</td>
                                    <td style={{ padding: '12px' }}>{script.createdAt}</td>
                                    <td style={{ padding: '12px' }}>{script.updatedAt}</td>
                                    <td style={{ padding: '12px' }}>{script.title}</td>
                                    <td style={{ padding: '12px' }}>{script.authorname}</td>
                                    <td style={{ padding: '12px', textAlign: 'center' }}>
                                        <button onClick={(e) => { e.stopPropagation(); deleteRow(script); }} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'red' }}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {selectedRow && (
                <div style={{ marginTop: '20px', padding: '20px', border: '1px solid #ccc', borderRadius: '8px', backgroundColor: '#f9f9f9' }}>
                    <h3 style={{ marginTop: '0' }}>선택된 원고 상세 정보</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: '150px 1fr', gap: '10px' }}>
                        <strong>Id:</strong><span>{selectedRow.id}</span>
                        <strong>AuthorId:</strong><span>{selectedRow.authorId}</span>
                        <strong>Title:</strong><span>{selectedRow.title}</span>
                        <strong>Authorname:</strong><span>{selectedRow.authorname}</span>
                        <strong>Status:</strong><span>{selectedRow.status}</span>
                        <strong>CreatedAt:</strong><span>{selectedRow.createdAt}</span>
                        <strong>UpdatedAt:</strong><span>{selectedRow.updatedAt}</span>
                        <strong>Contents:</strong><span style={{ whiteSpace: 'pre-wrap' }}>{selectedRow.contents}</span>
                    </div>
                    <button onClick={() => changeSelectedRow(null)} style={{ marginTop: '15px', padding: '5px 10px', backgroundColor: '#e9e9e9', border: '1px solid #ccc', borderRadius: '4px', cursor: 'pointer' }}>선택 해제</button>
                </div>
            )}

            {/* 5. Register Dialog */}
            {openDialog && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '400px', backgroundColor: 'white', padding: '30px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: 0 }}>BookScript 등록</h3>
                        <button onClick={() => setOpenDialog(false)}>&times;</button>
                    </div>
                    <div>
                        <div style={{ marginBottom: '15px' }}><label>AuthorId</label><input type="text" value={newManuscript.authorId} onChange={(e) => setNewManuscript({...newManuscript, authorId: e.target.value})} style={{ width: '100%', padding: '8px' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label>Authorname</label><input type="text" value={newManuscript.authorname} onChange={(e) => setNewManuscript({...newManuscript, authorname: e.target.value})} style={{ width: '100%', padding: '8px' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label>Title</label><input type="text" value={newManuscript.title} onChange={(e) => setNewManuscript({...newManuscript, title: e.target.value})} style={{ width: '100%', padding: '8px' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label>Contents</label><textarea value={newManuscript.contents} onChange={(e) => setNewManuscript({...newManuscript, contents: e.target.value})} style={{ width: '100%', padding: '8px', minHeight: '100px' }}></textarea></div>
                        <button onClick={handleRegisterManuscript} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none' }}>등록</button>
                    </div>
                </div>
            )}

            {/* 6. Edit Dialog */}
            {editDialog && selectedRow && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '400px', backgroundColor: 'white', padding: '30px', boxShadow: '0 4px 12px rgba(0,0,0,0.15)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                        <h3 style={{ margin: 0 }}>BookScript 수정</h3>
                        <button onClick={() => setEditDialog(false)}>&times;</button>
                    </div>
                    <div>
                        <div style={{ marginBottom: '15px' }}><label>AuthorId</label><input type="text" value={selectedRow.authorId} onChange={(e) => setSelectedRow({...selectedRow, authorId: e.target.value})} style={{ width: '100%', padding: '8px' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label>Title</label><input type="text" value={selectedRow.title} onChange={(e) => setSelectedRow({...selectedRow, title: e.target.value})} style={{ width: '100%', padding: '8px' }} /></div>
                        <div style={{ marginBottom: '15px' }}><label>Contents</label><textarea value={selectedRow.contents} onChange={(e) => setSelectedRow({...selectedRow, contents: e.target.value})} style={{ width: '100%', padding: '8px', minHeight: '100px' }}></textarea></div>
                        <div style={{ marginBottom: '15px' }}><label>Authorname</label><input type="text" value={selectedRow.authorname} onChange={(e) => setSelectedRow({...selectedRow, authorname: e.target.value})} style={{ width: '100%', padding: '8px' }} /></div>
                        <button onClick={handleSaveEdit} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none' }}>수정</button>
                    </div>
                </div>
            )}

            {/* 7. Script Publish Request Dialog */}
            {scriptPublishRequestDialog && selectedRow && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000 }}>
                    <ScriptPublishRequest
                        onClose={() => setScriptPublishRequestDialog(false)}
                        onPublishRequest={scriptPublishRequest}
                    />
                </div>
            )}
        </div>
    );
};

export default ManuscriptGridPage;