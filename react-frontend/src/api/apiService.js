// frontend/src/api/apiService.js
import axios from 'axios';

// const API_BASE_URL = 'http://localhost:8088/'; // 백엔드 API Gateway URL
const API_BASE_URL = 'https://8088-5miniprojec-libraryproj-19bfadrkwwl.ws-us120.gitpod.io';

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