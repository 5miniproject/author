import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // 아래 server 설정을 추가합니다.
  server: {
    host: '0.0.0.0', // 외부 접속을 허용하기 위해 모든 IP를 수신 대기
    allowedHosts: [
      '5173-5miniprojec-libraryproj-19bfadrkwwl.ws-us120.gitpod.io', // Gitpod에서 제공하는 URL
    ],
  },
});