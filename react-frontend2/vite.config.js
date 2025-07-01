import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // 모든 네트워크 인터페이스에서 접근 허용
    allowedHosts: [
      '5173-5miniprojec-libraryproj-19bfadrkwwl.ws-us120.gitpod.io', // Gitpod URL 추가
      // 필요하다면 다른 개발 호스트를 여기에 추가
    ],
  },
})