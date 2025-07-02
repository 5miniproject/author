// frontend/src/api/apiService.js
import axios from 'axios';

// const API_BASE_URL = 'https://8088-5miniprojec-libraryproj-19bfadrkwwl.ws-us120.gitpod.io';
// 백엔드 API Gateway Service의 이름은 'gateway'이고 포트는 '8080'입니다.
// 같은 네임스페이스에 프론트엔드와 백엔드가 배포된다고 가정합니다.
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8088/';


const apiService = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default {
    get: (url) => apiService.get(url),
    post: (url, data) => apiService.post(url, data),
    put: (url, data) => apiService.put(url, data),
    delete: (url) => apiService.delete(url),
};