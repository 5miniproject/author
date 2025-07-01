import { useState, useEffect, useCallback } from 'react';
import apiService from '../api/apiService';

/**
 * HAL 형식의 링크 URL에서 리소스 ID를 추출하는 헬퍼 함수
 * 예: "http://localhost:8082/authors/1" -> "1"
 */
const extractIdFromLink = (link) => {
    if (!link) return null;
    const parts = link.split('/');
    return parts[parts.length - 1];
};

/**
 * @param {string} path API 엔드포인트 경로 (예: 'authors')
 * @returns {object} 그리드 데이터 관리 관련 상태 및 함수
 */
const useGridLogic = (path) => {
    // data 상태는 이제 HAL 객체가 아닌, 실제 데이터 배열을 저장합니다.
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedRow, setSelectedRow] = useState(null);
    const [snackbar, setSnackbar] = useState({ status: false, timeout: 3000, color: 'success', message: '' });

    const showSnackbar = (message, color) => {
        setSnackbar({ status: true, timeout: 3000, color, message });
    };

    const fetchData = useCallback(async (query = {}) => {
        setLoading(true);
        try {
            // ReadModel 조회
            const response = await apiService.get(`/${path}`, { params: query });
            
            // API 응답에서 실제 데이터 배열을 추출하고 ID를 추가합니다.
            const rawData = response.data?._embedded?.[path] || [];
            
            // 각 항목에 ID 속성을 추가하여 데이터에 map, filter 사용이 용이하도록 만듭니다.
            const processedData = rawData.map(item => ({
                ...item,
                id: extractIdFromLink(item._links?.self?.href)
            }));
            
            setData(processedData); // <-- 가공된 배열 데이터를 상태에 저장합니다.
        } catch (error) {
            // eslint-disable-next-line no-console
            console.error('데이터 조회 실패:', error);
            showSnackbar('데이터 조회에 실패했습니다.', 'error');
            setData([]); // 에러 발생 시 데이터 초기화
        } finally {
            setLoading(false);
        }
    }, [path]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);
    
    // ** <<< 새로 추가된 함수입니다 >>> **
    // API에 항목을 등록하고, 성공 시 UI 상태를 즉시 업데이트합니다.
    const registerRow = async (newRecord) => {
        try {
            // 1. API에 POST 요청을 보냅니다.
            const response = await apiService.post(`/${path}`, newRecord);
            
            // 2. 응답으로 받은 새로운 항목을 추출하고 ID를 추가합니다.
            const rawNewItem = response.data;
            const processedNewItem = {
                ...rawNewItem,
                id: extractIdFromLink(rawNewItem._links?.self?.href)
            };
            
            // 3. UI 상태를 즉시 업데이트합니다. (Optimistic Update)
            setData(prevData => [...prevData, processedNewItem]);
            
            showSnackbar('성공적으로 등록되었습니다.', 'success');
            return true; // 성공 시 true 반환
        } catch (error) {
            console.error('등록 실패:', error);
            showSnackbar('등록에 실패했습니다.', 'error');
            return false; // 실패 시 false 반환
        }
    };

    const updateRow = async (updatedRecord) => {
        // '수정' 버튼 클릭 시 API 호출
        try {
            // 이제 data 상태가 배열이므로 map() 사용이 안전합니다.
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
            // 이제 data 상태가 배열이므로 filter() 사용이 안전합니다.
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

    return {
        data,
        loading,
        selectedRow,
        changeSelectedRow,
        // <<< addNewRow 대신 registerRow를 반환합니다 >>>
        // addNewRow, 
        updateRow,
        deleteRow,
        fetchData,
        showSnackbar,
        snackbar,
        setSelectedRow,
        registerRow // <-- 새롭게 추가된 등록 함수를 반환합니다.
    };
};

export default useGridLogic;