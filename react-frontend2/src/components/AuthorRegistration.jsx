// src/components/AuthorRegistration.jsx
import React from 'react';
import { Box, Typography, Button, TextField, Select, MenuItem, InputAdornment, FormControl, InputLabel } from '@mui/material';
import { styled, useTheme } from '@mui/material/styles';

import AppBar from './common/AppBar'; // AppBar 컴포넌트 임포트

// MUI Icons (HTML SVG를 대체)
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown'; // CaretUpDownIcon 대체 (드롭다운 아이콘)

// Styled components for consistent styling (optional, can use sx prop directly)
const FormFieldContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  maxWidth: '480px', // max-w-[480px]
  flexWrap: 'wrap', // flex-wrap
  alignItems: 'flex-end', // items-end
  gap: theme.spacing(2), // gap-4
  px: theme.spacing(4), // px-4
  py: theme.spacing(1.5), // py-3
}));

const FormLabel = styled(Typography)(({ theme }) => ({
  color: theme.palette.text.primary, // text-[#0c1c17]
  fontSize: theme.typography.body1.fontSize, // text-base
  fontWeight: 500, // font-medium
  paddingBottom: theme.spacing(1), // pb-2
}));

const AuthorRegistration = () => {
  const theme = useTheme();

  const navLinks = [
    { label: 'Explore', href: '#' },
    { label: 'My Library', href: '#' },
    { label: 'Community', href: '#' },
  ];

  // 폼 제출 핸들러 (더미 함수)
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('작가 등록 신청이 제출되었습니다.');
    // 실제 로직: API 호출, 유효성 검사 등
  };

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
    }}>
      <Box sx={{ display: 'flex', height: '100%', flexGrow: 1, flexDirection: 'column' }}> {/* layout-container */}
        <AppBar
          logoType="novelVerse"
          navLinks={navLinks}
          showBell={true}
          profileImage="https://lh3.googleusercontent.com/aida-public/AB6AXuAKhuTFktum3eLZmg9K8Q3E1z2LyHpGlVEqmZm_kLUO3iFEqhuFnlrVGk596YMwUPcEbnO29n3nFIM2tFRE88Lr7tKOZRBaY-034iQHDqOAjgTmzP2T8s0jZd7PovP1S4GREzH1gfn_UgGQCvlMUYXTcJcCERtr3-tbH6zPgWafhSzzKfjOjjhS4oxTWDpzpMSWEPdxNpfIWlla2-Cit6m0dPB-GfMHIyHHnNkDDAgxSXvP6tMHdMjaP5iZ34P67WTg9ErAxNzFt0eY"
        />
        <Box sx={{ px: theme.spacing(40), display: 'flex', flex: 1, justifyContent: 'center', py: theme.spacing(5) }}> {/* px-40 flex flex-1 justify-center py-5 */}
          <Box sx={{ display: 'flex', flexDirection: 'column', width: '512px', maxWidth: '512px', py: theme.spacing(5), flex: 1 }}> {/* layout-content-container */}
            <Box sx={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', gap: theme.spacing(3), p: theme.spacing(4) }}> {/* flex flex-wrap justify-between gap-3 p-4 */}
              <Typography variant="h1" sx={{ minWidth: '288px' }}>Become an Author</Typography> {/* tracking-light text-[32px] font-bold leading-tight min-w-72 */}
            </Box>
            <form onSubmit={handleSubmit}>
              <FormFieldContainer>
                <FormControl variant="filled" fullWidth sx={{ flex: 1, minWidth: '160px' }}> {/* flex-col min-w-40 flex-1 */}
                  <FormLabel>Full Name</FormLabel>
                  <TextField
                    placeholder="Enter your full name"
                    variant="filled"
                    disableUnderline={true} // filledInputFieldClasses에 border-none 해당
                    InputProps={{ disableUnderline: true }} // MuiFilledInput의 언더라인 제거
                    sx={{
                      '& .MuiFilledInput-root': {
                        backgroundColor: theme.palette.secondary.main, // bg-[#e6f4ef]
                        borderRadius: '12px', // rounded-xl
                        height: '56px', // h-14
                        padding: '0 15px', // p-4 (p-[15px])
                        '&:hover': {
                          backgroundColor: theme.palette.secondary.main,
                        },
                        '&.Mui-focused': {
                          backgroundColor: theme.palette.secondary.main,
                        },
                        color: theme.palette.text.primary, // text-[#0c1c17]
                      },
                      '& .MuiInputBase-input::placeholder': {
                        color: theme.palette.novelVerse.placeholder, // placeholder:text-[#46a080]
                        opacity: 1,
                      },
                    }}
                  />
                </FormControl>
              </FormFieldContainer>
              <FormFieldContainer>
                <FormControl variant="filled" fullWidth sx={{ flex: 1, minWidth: '160px' }}>
                  <FormLabel>Email</FormLabel>
                  <TextField
                    type="email"
                    placeholder="Enter your email"
                    variant="filled"
                    InputProps={{ disableUnderline: true }}
                    sx={{
                      '& .MuiFilledInput-root': {
                        backgroundColor: theme.palette.secondary.main,
                        borderRadius: '12px',
                        height: '56px',
                        padding: '0 15px',
                        '&:hover': { backgroundColor: theme.palette.secondary.main },
                        '&.Mui-focused': { backgroundColor: theme.palette.secondary.main },
                        color: theme.palette.text.primary,
                      },
                      '& .MuiInputBase-input::placeholder': {
                        color: theme.palette.novelVerse.placeholder,
                        opacity: 1,
                      },
                    }}
                  />
                </FormControl>
              </FormFieldContainer>
              <FormFieldContainer>
                <FormControl variant="filled" fullWidth sx={{ flex: 1, minWidth: '160px' }}>
                  <FormLabel>Genre</FormLabel>
                  {/*
                    HTML에서 input과 SVG 아이콘으로 되어 있으나,
                    MUI에서는 Select 컴포넌트를 사용하는 것이 일반적이며 더 나은 사용자 경험을 제공합니다.
                    HTML의 select-button-svg와 동일한 외형을 맞추기 위해 iconComponent를 커스텀할 수 있습니다.
                  */}
                  <Select
                    value="" // 실제 값 관리 필요
                    displayEmpty
                    variant="filled"
                    IconComponent={ArrowDropDownIcon} // 기본 드롭다운 아이콘
                    inputProps={{ 'aria-label': 'Without label', disableUnderline: true }}
                    sx={{
                      '& .MuiFilledInput-root': {
                        backgroundColor: theme.palette.secondary.main,
                        borderRadius: '12px',
                        height: '56px',
                        padding: '0 15px',
                        '&:hover': { backgroundColor: theme.palette.secondary.main },
                        '&.Mui-focused': { backgroundColor: theme.palette.secondary.main },
                        color: theme.palette.text.primary,
                      },
                      '& .MuiSelect-select': {
                        paddingLeft: '15px !important', // p-[15px] 유지
                      },
                      '& .MuiSelect-icon': {
                        color: theme.palette.text.secondary, // 아이콘 색상
                        right: theme.spacing(2), // pr-4 (8px)
                      },
                    }}
                  >
                    <MenuItem value="" disabled>
                      <Typography sx={{ color: theme.palette.novelVerse.placeholder }}>Select your genre</Typography>
                    </MenuItem>
                    <MenuItem value="fantasy">Fantasy</MenuItem>
                    <MenuItem value="romance">Romance</MenuItem>
                    <MenuItem value="scifi">Science Fiction</MenuItem>
                  </Select>
                </FormControl>
              </FormFieldContainer>
              <FormFieldContainer>
                <FormControl variant="filled" fullWidth sx={{ flex: 1, minWidth: '160px' }}>
                  <FormLabel>About You</FormLabel>
                  <TextField
                    placeholder="Tell us about yourself and your writing experience"
                    variant="filled"
                    multiline // textarea 역할을 위해 multiline 활성화
                    rows={6} // min-h-36 에 해당하는 대략적인 높이
                    InputProps={{ disableUnderline: true }}
                    sx={{
                      '& .MuiFilledInput-root': {
                        backgroundColor: theme.palette.secondary.main,
                        borderRadius: '12px',
                        padding: '15px', // p-4 (p-[15px])
                        height: 'auto', // multiline일 때 height 자동 조절
                        minHeight: '144px', // min-h-36 (min-h-[144px])
                        alignItems: 'flex-start', // 텍스트가 위에서 시작되도록
                        '&:hover': { backgroundColor: theme.palette.secondary.main },
                        '&.Mui-focused': { backgroundColor: theme.palette.secondary.main },
                        color: theme.palette.text.primary,
                      },
                      '& .MuiInputBase-input::placeholder': {
                        color: theme.palette.novelVerse.placeholder,
                        opacity: 1,
                      },
                    }}
                  />
                </FormControl>
              </FormFieldContainer>
              <Box sx={{ display: 'flex', px: theme.spacing(4), py: theme.spacing(1.5), width: '100%' }}> {/* flex px-4 py-3 */}
                <Button
                  type="submit"
                  variant="customPrimary" // customPrimary variant 사용
                  fullWidth // flex-1에 해당
                  sx={{ height: '40px' }} // h-10
                >
                  Submit
                </Button>
              </Box>
            </form>
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default AuthorRegistration;