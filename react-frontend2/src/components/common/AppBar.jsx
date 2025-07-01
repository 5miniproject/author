// src/components/common/AppBar.jsx
import React from 'react';
import { AppBar as MuiAppBar, Toolbar, Button, Typography, Box, InputBase } from '@mui/material';
import { styled, useTheme } from '@mui/material/styles'; // useTheme 훅 추가

// MUI Icons (HTML SVG를 대체)
import NovelVerseIcon from '@mui/icons-material/AllInclusive'; // 적절한 아이콘으로 대체
import StoryVerseIcon from '@mui/icons-material/Book'; // 적절한 아이콘으로 대체
import NovelCraftIcon from '@mui/icons-material/Create'; // 적절한 아이콘으로 대체
import StoryCraftIcon from '@mui/icons-material/AutoStories'; // 적절한 아이콘으로 대체
import StoryReadsIcon from '@mui/icons-material/LocalLibrary'; // 적절한 아이콘으로 대체
import StoryWeaverIcon from '@mui/icons-material/Architecture'; // 적절한 아이콘으로 대체

import NotificationsIcon from '@mui/icons-material/Notifications';
import SearchIcon from '@mui/icons-material/Search';
import BookmarkIcon from '@mui/icons-material/BookmarkBorder';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCartOutlined';

// HTML SVG 로고들을 대체할 스타일 컴포넌트
const LogoContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  gap: theme.spacing(1), // gap-4 -> theme.spacing(1)은 8px, 16px은 theme.spacing(2)
  color: theme.palette.text.primary,
}));

const LogoText = styled(Typography)(({ theme }) => ({
  fontSize: theme.typography.h2.fontSize, // text-lg
  fontWeight: theme.typography.h2.fontWeight, // font-bold
  lineHeight: theme.typography.h2.lineHeight, // leading-tight
  letterSpacing: theme.typography.h2.letterSpacing, // tracking-[-0.015em]
}));

const NavLink = styled('a')(({ theme }) => ({
  color: theme.palette.text.primary,
  fontSize: theme.typography.body2.fontSize, // text-sm
  fontWeight: theme.typography.body2.fontWeight, // font-medium
  lineHeight: theme.typography.body2.lineHeight, // leading-normal
  textDecoration: 'none',
  '&:hover': {
    textDecoration: 'underline', // 예시로 hover 시 밑줄 추가
  },
}));

const CustomButton = styled(Button)(({ theme }) => ({
  textTransform: 'none',
  borderRadius: '8px', // rounded-xl
  height: '40px', // h-10
  padding: '0 16px', // px-4
  fontSize: theme.typography.button.fontSize, // text-sm
  fontWeight: theme.typography.button.fontWeight, // font-bold
  letterSpacing: theme.typography.button.letterSpacing, // tracking-[0.015em]
}));

const ProfileImage = styled(Box)(({ imageUrl }) => ({
  backgroundImage: `url("${imageUrl}")`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  borderRadius: '50%',
  width: '40px', // size-10
  height: '40px', // size-10
  aspectRatio: '1 / 1',
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  backgroundColor: theme.palette.secondary.main, // bg-[#e6f4ef]
  borderRadius: '8px', // rounded-xl
  height: '40px', // h-10
  paddingLeft: theme.spacing(2), // pl-4
  color: theme.palette.text.primary,
  '& .MuiInputBase-input': {
    padding: '0 16px 0 8px', // px-4, pl-2
    '&::placeholder': {
      color: theme.palette.novelVerse.placeholder,
      opacity: 1,
    },
  },
}));

/**
 * @param {Object} props - AppBar 컴포넌트의 props
 * @param {'novelVerse' | 'storyVerse' | 'novelCraft' | 'storyCraft' | 'storyReads' | 'storyWeaver'} props.logoType - 사용할 로고 타입
 * @param {Array<{ label: string, href: string }>} props.navLinks - 내비게이션 링크 배열
 * @param {boolean} [props.showSearch=false] - 검색창 표시 여부
 * @param {string} [props.searchValue=''] - 검색창 기본 값
 * @param {boolean} [props.showBell=false] - 알림 아이콘 표시 여부
 * @param {boolean} [props.showBookmark=false] - 북마크 아이콘 표시 여부
 * @param {boolean} [props.showCart=false] - 장바구니 아이콘 표시 여부
 * @param {string} [props.profileImage=''] - 프로필 이미지 URL
 * @param {React.ReactNode} [props.customButtons] - 사용자 정의 버튼 (예: "New Story", "Write", "Get Started")
 * @param {string} [props.headerBorderColor] - 헤더 하단 border 색상 (MUI Palette color name 또는 CSS hex value)
 */
const AppBar = ({
  logoType,
  navLinks,
  showSearch = false,
  searchValue = '',
  showBell = false,
  showBookmark = false,
  showCart = false,
  profileImage,
  customButtons,
  headerBorderColor, // now uses theme.palette colors or direct hex
}) => {
  const theme = useTheme(); // 테마 객체 접근

  const renderLogo = () => {
    let IconComponent;
    let logoName;
    let textColor = theme.palette.text.primary; // 기본 텍스트 색상

    switch (logoType) {
      case 'novelVerse':
        IconComponent = NovelVerseIcon;
        logoName = 'NovelVerse';
        break;
      case 'storyVerse':
        IconComponent = StoryVerseIcon;
        logoName = 'StoryVerse';
        break;
      case 'novelCraft':
        IconComponent = NovelCraftIcon;
        logoName = 'NovelCraft';
        break;
      case 'storyCraft':
        IconComponent = StoryCraftIcon;
        logoName = 'StoryCraft';
        textColor = theme.palette.novelVerse.black; // ai서비스.html의 StoryCraft 로고 텍스트 색상
        break;
      case 'storyReads':
        IconComponent = StoryReadsIcon;
        logoName = 'StoryReads';
        break;
      case 'storyWeaver':
        IconComponent = StoryWeaverIcon;
        logoName = 'StoryWeaver';
        break;
      default:
        IconComponent = NovelVerseIcon;
        logoName = 'NovelVerse';
    }

    return (
      <LogoContainer sx={{ color: textColor }}>
        <IconComponent sx={{ fontSize: '1rem' }} /> {/* size-4 (16px) 에 맞게 조절 */}
        <LogoText sx={{ color: textColor }}>{logoName}</LogoText>
      </LogoContainer>
    );
  };

  return (
    <MuiAppBar
      position="static"
      elevation={0} // border-b만 남기기 위해 그림자 제거
      sx={{
        backgroundColor: theme.palette.background.default, // bg-[#f8fcfa] 또는 bg-white
        borderBottom: 1, // border-b
        borderColor: headerBorderColor || theme.palette.novelVerse.headerBorder, // border-solid border-b-[#e6f4ef] 또는 ai서비스.html의 #f1f3f2
        px: theme.spacing(10), // px-10 (40px)
        py: theme.spacing(3), // py-3 (12px)
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        whiteSpace: 'nowrap',
      }}
    >
      <Toolbar disableGutters sx={{ minHeight: 'auto', display: 'flex', alignItems: 'center', gap: theme.spacing(10) }}> {/* gap-8 (32px), gap-8 for header main content */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: theme.spacing(2) }}> {/* gap-4 (16px) for logo and text */}
          {renderLogo()}
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: theme.spacing(9) }}> {/* gap-9 (36px) for navigation links */}
          {navLinks.map((link, index) => (
            <NavLink key={index} href={link.href}>
              {link.label}
            </NavLink>
          ))}
        </Box>
      </Toolbar>
      <Box sx={{ flex: 1, display: 'flex', justifyContent: 'flex-end', alignItems: 'center', gap: theme.spacing(8) }}> {/* flex-1 justify-end gap-8 (32px) */}
        {showSearch && (
          <Box sx={{ display: 'flex', flexDirection: 'column', minWidth: '160px', height: '40px', maxWidth: '256px' }}> {/* min-w-40 !h-10 max-w-64 */}
            <Box sx={{ display: 'flex', width: '1', flex: 1, alignItems: 'stretch', borderRadius: '8px', height: '100%', backgroundColor: theme.palette.secondary.main }}> {/* flex w-full flex-1 items-stretch rounded-xl h-full */}
              <SearchIcon sx={{ color: theme.palette.text.secondary, pl: theme.spacing(2), display: 'flex', alignItems: 'center', justifyContent: 'center' }} /> {/* pl-4, rounded-l-xl border-r-0 */}
              <StyledInputBase
                placeholder="Search"
                value={searchValue}
                readOnly // 검색 결과 페이지에서는 readOnly로 설정 (예시)
                sx={{
                    flex: 1, // flex-1
                    minWidth: 0, // min-w-0
                    overflow: 'hidden', // overflow-hidden
                    borderRadius: '0 8px 8px 0 !important', // rounded-xl rounded-l-none border-l-0
                    borderLeft: 'none',
                    backgroundColor: 'transparent', // 배경색은 부모에서 처리
                    height: '100%',
                    '& .MuiInputBase-input': {
                        paddingLeft: theme.spacing(1), // pl-2
                    }
                }}
              />
            </Box>
          </Box>
        )}
        <Box sx={{ display: 'flex', gap: theme.spacing(2) }}> {/* gap-2 (8px) */}
          {customButtons} {/* */}
          {showBell && (
            <CustomButton
              variant="customSecondary" // customSecondary variant 사용
              sx={{ minWidth: 0, px: theme.spacing(2.5), borderRadius: '9999px' }} // min-w-0 px-2.5, rounded-full
            >
              <NotificationsIcon sx={{ fontSize: '20px' }} /> {/* size-20px */}
            </CustomButton>
          )}
          {showBookmark && (
            <CustomButton
              variant="customSecondary" // customSecondary variant 사용
              sx={{ minWidth: 0, px: theme.spacing(2.5), borderRadius: '8px' }} // min-w-0 px-2.5, rounded-xl
            >
              <BookmarkIcon sx={{ fontSize: '20px' }} /> {/* size-20px */}
            </CustomButton>
          )}
          {showCart && (
            <CustomButton
              variant="customSecondary" // customSecondary variant 사용
              sx={{ minWidth: 0, px: theme.spacing(2.5), borderRadius: '8px' }} // min-w-0 px-2.5, rounded-xl
            >
              <ShoppingCartIcon sx={{ fontSize: '20px' }} /> {/* size-20px */}
            </CustomButton>
          )}
        </Box>
        {profileImage && (
          <ProfileImage imageUrl={profileImage} />
        )}
      </Box>
    </MuiAppBar>
  );
};

export default AppBar;