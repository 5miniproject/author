import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import { ThemeProvider } from '@mui/material/styles'; // ThemeProvider 임포트
import theme from './theme'; // 정의한 테마 임포트
// import './index.css'; // 이 파일은 이제 비워둠

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}> {/* ThemeProvider로 App 컴포넌트 감싸기 */}
      <App />
    </ThemeProvider>
  </React.StrictMode>,
);