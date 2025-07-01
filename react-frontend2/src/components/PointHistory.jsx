// src/components/PointHistory.jsx
import React from 'react';
import { Box, Typography, Button } from '@mui/material'; // MUI 컴포넌트 임포트
import { styled, useTheme } from '@mui/material/styles'; // useTheme 훅 추가

import AppBar from './common/AppBar'; // AppBar 컴포넌트 임포트

// MUI Icons (HTML SVG를 대체)
import GroupIcon from '@mui/icons-material/Group'; // UsersIcon 대체
import StarIcon from '@mui/icons-material/Star'; // StarIcon 대체
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth'; // CalendarIcon 대체
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline'; // ChatCircleDotsIcon 대체


const PointEarnMethodContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  gap: theme.spacing(2), // gap-4
  backgroundColor: theme.palette.background.default, // bg-[#f8fcfa]
  px: theme.spacing(4), // px-4
  minHeight: '72px', // min-h-[72px]
  py: theme.spacing(1), // py-2
}));

const IconWrapper = styled(Box)(({ theme }) => ({
  color: theme.palette.text.primary,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  borderRadius: theme.shape.borderRadius, // rounded-lg
  backgroundColor: theme.palette.secondary.main, // bg-[#e6f4ef]
  flexShrink: 0, // shrink-0
  width: '48px', // size-12
  height: '48px', // size-12
}));

const PointEarnMethodTextContainer = styled(Box)({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
});

// 포인트 획득 방법 항목을 위한 공통 컴포넌트
const PointEarnMethod = ({ icon: Icon, title, description }) => {
  return (
    <PointEarnMethodContainer>
      <IconWrapper>
        <Icon sx={{ fontSize: '24px' }} /> {/* size-24px */}
      </IconWrapper>
      <PointEarnMethodTextContainer>
        <Typography variant="body1" sx={{ fontWeight: 500, lineHeight: 'normal', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {title}
        </Typography>
        <Typography variant="body2" sx={{ lineHeight: 'normal', overflow: 'hidden', textOverflow: 'ellipsis', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', color: 'text.secondary' }}>
          {description}
        </Typography>
      </PointEarnMethodTextContainer>
    </PointEarnMethodContainer>
  );
};


const PointHistory = () => {
  const theme = useTheme(); // 테마 객체 접근

  const navLinks = [
    { label: 'My Library', href: '#' },
    { label: 'Browse', href: '#' },
    { label: 'Explore', href: '#' },
  ];

  return (
    <Box sx={{
      position: 'relative',
      display: 'flex',
      width: '100%',
      minHeight: '100vh',
      flexDirection: 'column',
      backgroundColor: theme.palette.background.default, // bg-[#f8fcfa]
      overflowX: 'hidden',
      fontFamily: theme.typography.fontFamily,
    }}> {/* */}
      <Box sx={{ display: 'flex', height: '100%', flexGrow: 1, flexDirection: 'column' }}> {/* layout-container */}
        <AppBar
          logoType="novelVerse"
          navLinks={navLinks}
          showSearch={true}
          showBell={true}
          profileImage="https://lh3.googleusercontent.com/aida-public/AB6AXuDC6U2nTtoplLCKpOPsh-OmFX5oti6Gg16KrIOKnDjXEa9DHXSoTLL5UhO2rbUurB--jMzrmO5Ugn-xV4_gh12eokAJjKFflUn8EuN489JyvnKDrc7XfRVHL6R_yE1Lw0zsRAFn8nyssH92IvURdm6FqJFPqk6LKjPFXUnXdCAa2atAzah4-PETwaAneqkDe86EWLCRNRDnsB1rRf6GFXroFe8szsY6zPFB_264aPRF5gauE0KM0Fkq6E5RXuVw1-1YYJQ72eW0WHmp" //
        />
        <Box sx={{ px: theme.spacing(40), display: 'flex', flex: 1, justifyContent: 'center', py: theme.spacing(5) }}> {/* px-40 flex flex-1 justify-center py-5 */}
          <Box sx={{ display: 'flex', flexDirection: 'column', maxWidth: '960px', flex: 1 }}> {/* layout-content-container */}
            <Box sx={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', gap: theme.spacing(3), p: theme.spacing(4) }}> {/* flex flex-wrap justify-between gap-3 p-4 */}
              <Box sx={{ display: 'flex', minWidth: '288px', flexDirection: 'column', gap: theme.spacing(3) }}> {/* min-w-72 flex-col gap-3 */}
                <Typography variant="h1" sx={{ lineHeight: 'tight', letterSpacing: '-0.015em' }}>Points</Typography> {/* tracking-light text-[32px] font-bold leading-tight */}
                <Typography variant="body2">Earn points to unlock exclusive content and rewards.</Typography> {/* text-sm font-normal leading-normal */}
              </Box>
            </Box>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: theme.spacing(4), p: theme.spacing(4) }}> {/* flex flex-wrap gap-4 p-4 */}
              <Box sx={{ display: 'flex', minWidth: '158px', flex: 1, flexDirection: 'column', gap: theme.spacing(2), borderRadius: '8px', p: theme.spacing(6), backgroundColor: theme.palette.secondary.main }}> {/* min-w-[158px] flex-1 flex-col gap-2 rounded-xl p-6 bg-[#e6f4ef] */}
                <Typography variant="body1" sx={{ fontWeight: 500 }}>Your Points</Typography> {/* text-base font-medium leading-normal */}
                <Typography variant="h4" sx={{ letterSpacing: '-0.015em', fontWeight: 900 }}>1,250</Typography> {/* tracking-light text-2xl font-bold leading-tight */}
              </Box>
            </Box>
            <Typography variant="h4" sx={{ px: theme.spacing(4), pb: theme.spacing(3), pt: theme.spacing(5) }}>Ways to Earn Points</Typography> {/* text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 pb-3 pt-5 */}
            <PointEarnMethod
              icon={GroupIcon}
              title="Refer a Friend"
              description="Earn 50 points for every friend you refer to NovelVerse."
            /> {/* */}
            <PointEarnMethod
              icon={StarIcon}
              title="Leave a Review"
              description="Earn 25 points for every review you leave on a book."
            /> {/* */}
            <PointEarnMethod
              icon={CalendarMonthIcon}
              title="Daily Reading Challenges"
              description="Earn 100 points for completing daily reading challenges."
            /> {/* */}
            <PointEarnMethod
              icon={ChatBubbleOutlineIcon}
              title="Community Discussions"
              description="Earn 200 points for participating in community discussions."
            /> {/* */}
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default PointHistory;