import React, { useState, useEffect } from 'react';
import useGridLogic from '../../hooks/useGridLogic'; 
import apiService from '../../api/apiService';
import ApproveAuthor from '../../components/ApproveAuthor';
import RejectAuthor from '../../components/RejectAuthor';

// 사용자 역할을 확인하는 임시 함수. 실제로는 백엔드에서 사용자 정보를 가져와야 합니다.
const hasRole = (role) => {
    return role === 'Admin';
};

const AuthorGridPage = () => {
    // useGridLogic 커스텀 훅을 사용하여 공통 로직을 가져옵니다.
    const {
        data: authors, 
        loading,
        selectedRow,
        changeSelectedRow, 
        fetchData, 
        showSnackbar,
        snackbar,
        hideSnackbar, // Snackbar close를 위한 함수
        setSelectedRow, // 선택된 행을 직접 설정하기 위한 함수
        // addNewRow: registerAuthorInHook, // 작가 등록은 특정 엔드포인트를 사용하므로 useGridLogic의 addNewRow를 사용하지 않음
        updateRow,
        deleteRow,
    } = useGridLogic('authors'); // 작가 목록 조회/수정/삭제에는 useGridLogic 활용

    const [openDialog, setOpenDialog] = useState(false); // 등록 다이얼로그
    const [editDialog, setEditDialog] = useState(false); // 수정 다이얼로그
    const [approveAuthorDialog, setApproveAuthorDialog] = useState(false); // 작가승인 다이얼로그
    const [rejectAuthorDialog, setRejectAuthorDialog] = useState(false); // 작가거부 다이얼로그
    const [newAuthor, setNewAuthor] = useState({ email: '', name: '', detail: '', portfolio: '', isApprove: false });

    // 선택된 작가의 출판한 책 정보 관련 상태
    const [publishedBooks, setPublishedBooks] = useState([]);
    const [publishedBooksLoading, setPublishedBooksLoading] = useState(false);

    useEffect(() => {
        if (selectedRow && selectedRow.id) {
            // eslint-disable-next-line no-console
            console.log('선택된 작가:', selectedRow); 
            fetchPublishedBooksByAuthor(selectedRow.id);
        } else {
            setPublishedBooks([]); 
        }
    }, [selectedRow]);

    const fetchPublishedBooksByAuthor = async (authorId) => {
        setPublishedBooksLoading(true);
        try {
            const response = await apiService.get(`/ebookStatisticsViews/search/findByAuthorId?authorId=${authorId}`);
            setPublishedBooks(response.data._embedded ? response.data._embedded.ebookStatisticsViews : []);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error(`작가 ID ${authorId}의 출판물 조회 실패:`, error);
            setPublishedBooks([]);
            showSnackbar('출판물 목록 조회에 실패했습니다.', 'error');
        } finally {
            setPublishedBooksLoading(false);
        }
    };

    // '등록' 버튼 클릭 시 새 작가 등록 모달 열기
    const addNewRow = () => {
        setNewAuthor({ email: '', name: '', detail: '', portfolio: '', isApprove: false });
        setOpenDialog(true);
    };

    const handleRegisterAuthor = async () => {
        try {
            // Command: 작가 등록 (특정 엔드포인트로 직접 호출)
            await apiService.post('/authors', newAuthor); 
            showSnackbar('작가 등록 요청이 완료되었습니다. 관리자의 승인을 기다려주세요.', 'success');
            setOpenDialog(false);
            setNewAuthor({ email: '', name: '', detail: '', portfolio: '', isApprove: false }); // 폼 초기화
            fetchData(); // 작가 목록 갱신
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('작가 등록 실패:', error);
            showSnackbar('작가 등록에 실패했습니다. 관리자에게 문의하세요.', 'error');
        }
    };
    
    const openEditDialog = () => {
        if (selectedRow) {
            setEditDialog(true);
        }
    };

    const handleSaveEdit = async () => {
        if (selectedRow) {
            await updateRow(selectedRow); // useGridLogic의 updateRow 사용
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
            {/* Snackbar (useGridLogic 제공) */}
            {snackbar.status && (
                <div style={{ position: 'fixed', bottom: '20px', right: '20px', padding: '15px', backgroundColor: snackbar.color === 'success' ? '#4CAF50' : '#F44336', color: 'white', borderRadius: '5px', zIndex: 999 }}>
                    {snackbar.message}
                    <button onClick={hideSnackbar} style={{ marginLeft: '20px', color: 'white', border: 'none', background: 'transparent', cursor: 'pointer' }}>Close</button>
                </div>
            )}

            <h2>작가 관리</h2>
            <hr />

            {/* 버튼 패널 */}
            <div style={{ marginBottom: '20px' }}>
                <button onClick={addNewRow} style={{ marginRight: '5px' }}>등록</button>
                <button onClick={openEditDialog} disabled={!selectedRow} style={{ marginRight: '5px' }}>수정</button>
                <button onClick={() => setApproveAuthorDialog(true)} disabled={!selectedRow || !hasRole('Admin')} style={{ marginRight: '5px'}}>작가승인</button>
                <button onClick={() => setRejectAuthorDialog(true)} disabled={!selectedRow || !hasRole('Admin')}>작가거부</button>
            </div>
            
            {/* 선택된 작가 정보 표시 (선택 해제 버튼 포함) */}
            {selectedRow && (
                <div style={{ padding: '15px', marginBottom: '20px', border: '1px solid #ccc', borderRadius: '5px', backgroundColor: '#f9f9f9' }}>
                    <h4 style={{ margin: '0 0 10px 0' }}>선택된 작가</h4>
                    <p style={{ margin: '5px 0' }}><strong>ID:</strong> {selectedRow.id}</p>
                    <p style={{ margin: '5px 0' }}><strong>Email:</strong> {selectedRow.email}</p>
                    <p style={{ margin: '5px 0' }}><strong>Name:</strong> {selectedRow.name}</p>
                    <button onClick={() => setSelectedRow(null)} style={{ marginTop: '10px', padding: '5px 10px' }}>선택 해제</button>
                </div>
            )}

            {/* 작가 목록 테이블 */}
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
                            <tr key="authors-loading-row"><td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>Loading...</td></tr>
                        ) : (
                            authors.map((author) => (
                                <tr
                                    key={author.id}
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

            {/* 선택된 작가의 출판한 책 목록 테이블 */}
            {selectedRow && publishedBooks && (
                <div style={{ marginTop: '30px' }}>
                    <h3>작가 ({selectedRow.name})의 출판물 목록</h3>
                    <div style={{ overflowX: 'auto' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
                            <thead>
                                <tr style={{ backgroundColor: '#f2f2f2' }}>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>Book ID</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>Subscription Count</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>Is Best Seller</th>
                                    <th style={{ padding: '12px', textAlign: 'left' }}>Cover Image</th>
                                </tr>
                            </thead>
                            <tbody>
                                {publishedBooksLoading ? (
                                    <tr key="published-books-loading-row">
                                        <td colSpan="4" style={{ textAlign: 'center', padding: '20px' }}>로딩 중...</td>
                                    </tr>
                                ) : publishedBooks.length > 0 ? (
                                    publishedBooks.map((book) => (
                                        <tr key={book.bookId} style={{ borderBottom: '1px solid #eee' }}>
                                            <td style={{ padding: '12px' }}>{book.bookId}</td>
                                            <td style={{ padding: '12px' }}>{book.subscriptionCount}</td>
                                            <td style={{ padding: '12px' }}>{book.isBestSeller ? 'Yes' : 'No'}</td>
                                            <td style={{ padding: '12px' }}>
                                                {book.coverImageUrl ? (
                                                    <img src={book.coverImageUrl} alt="Cover" style={{ width: '50px', height: 'auto', borderRadius: '4px' }} />
                                                ) : 'No Image'}
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr key="no-published-books-row">
                                        <td colSpan="4" style={{ textAlign: 'center', padding: '20px' }}>출판한 책이 없습니다.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

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
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Detail</label>
                            <input type="text" value={selectedRow.detail} onChange={(e) => setSelectedRow({...selectedRow, detail: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
                        </div>
                        <div style={{ marginBottom: '15px' }}>
                            <label style={{ display: 'block', marginBottom: '5px' }}>Portfolio</label>
                            <input type="text" value={selectedRow.portfolio} onChange={(e) => setSelectedRow({...selectedRow, portfolio: e.target.value})} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
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
                    ></ApproveAuthor>
                </div>
            )}
            
            {rejectAuthorDialog && selectedRow && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000 }}>
                    <RejectAuthor
                        onClose={() => setRejectAuthorDialog(false)}
                        onReject={rejectAuthor}
                    ></RejectAuthor>
                </div>
            )}
        </div>
    );
};

export default AuthorGridPage;