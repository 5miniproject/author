import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0', // 외부 접속을 허용하기 위해 모든 IP를 수신 대기
    port: 8080, // <-- 이 부분을 추가하여 포트를 8080으로 설정합니다.
    strictPort: true, // (선택 사항) 해당 포트가 사용 중일 경우 빌드 실패
    allowedHosts: [
      '8080-5miniprojec-libraryproj-19bfadrkwwl.ws-us120.gitpod.io', // Gitpod에서 제공하는 URL
    ],
  },
});