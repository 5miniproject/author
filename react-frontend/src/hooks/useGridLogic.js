import { useState, useEffect, useCallback } from 'react';
import apiService from '../api/apiService';

/**
 * @param {string} path API 엔드포인트 경로 (예: 'authors')
 * @returns {object} 그리드 데이터 관리 관련 상태 및 함수
 */
const useGridLogic = (path) => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedRow, setSelectedRow] = useState(null);
    const [snackbar, setSnackbar] = useState({ status: false, timeout: 3000, color: 'success', message: '' });

    const fetchData = useCallback(async (query = {}) => {
        setLoading(true);
        try {
            // ReadModel 조회
            const response = await apiService.get(`/${path}`, { params: query });
            setData(response.data);
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('데이터 조회 실패:', error);
            showSnackbar('데이터 조회에 실패했습니다.', 'error');
        } finally {
            setLoading(false);
        }
    }, [path]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const addNewRow = (newRecord) => {
        // '등록' 버튼 클릭 시 새 레코드 추가 로직
        // 여기서는 API 호출 대신 UI에만 추가하는 예시
        setData(prevData => [...prevData, newRecord]);
        showSnackbar('새로운 항목이 등록되었습니다.', 'success');
    };
    
    const updateRow = async (updatedRecord) => {
        // '수정' 버튼 클릭 시 API 호출
        try {
            await apiService.put(`/${path}/${updatedRecord.id}`, updatedRecord);
            // 데이터 갱신
            setData(prevData => prevData.map(row => (row.id === updatedRecord.id ? updatedRecord : row)));
            showSnackbar('성공적으로 수정되었습니다.', 'success');
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('데이터 수정 실패:', error);
            showSnackbar('수정에 실패했습니다.', 'error');
        }
    };

    const deleteRow = async (recordToDelete) => {
        try {
            await apiService.delete(`/${path}/${recordToDelete.id}`);
            setData(prevData => prevData.filter(row => row.id !== recordToDelete.id));
            showSnackbar('성공적으로 삭제되었습니다.', 'success');
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('데이터 삭제 실패:', error);
            showSnackbar('삭제에 실패했습니다.', 'error');
        }
    };

    const changeSelectedRow = (row) => {
        setSelectedRow(row);
    };

    const showSnackbar = (message, color) => {
        setSnackbar({ status: true, timeout: 3000, color, message });
    };

    return {
        data,
        loading,
        selectedRow,
        changeSelectedRow,
        addNewRow,
        updateRow,
        deleteRow,
        fetchData,
        showSnackbar,
        snackbar,
        setSelectedRow,
    };
};

export default useGridLogic;