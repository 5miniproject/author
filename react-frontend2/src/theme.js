import { createTheme } from '@mui/material/styles';

export const appColors = {
  primary: '#019863',
  secondary: '#e6f4ef',
  textPrimary: '#0c1c17',
  textSecondary: '#46a080',
  background: '#f8fcfa',
  border: '#cde9df',
  headerBorder: '#e6f4ef',
  placeholder: '#46a080',
  white: '#f8fcfa',
  black: '#131614',
  lightGray: '#f1f3f2',
  darkGray: '#6d7e75',
  borderAi: '#dee3e0',
};

const theme = createTheme({
  palette: {
    primary: {
      main: appColors.primary,
    },
    secondary: {
      main: appColors.secondary,
    },
    text: {
      primary: appColors.textPrimary,
      secondary: appColors.textSecondary,
    },
    background: {
      default: appColors.background,
    },
    // HTML에 있는 다른 색상들을 여기에 추가하여 palette를 확장할 수 있습니다.
    novelVerse: {
      white: appColors.white,
      black: appColors.black,
      lightGray: appColors.lightGray,
      darkGray: appColors.darkGray,
      borderAi: appColors.borderAi,
      headerBorder: appColors.headerBorder, // theme palette에 직접 포함
      border: appColors.border, // theme palette에 직접 포함
      placeholder: appColors.placeholder, // theme palette에 직접 포함
    },
  },
  typography: {
    fontFamily: ['Inter', '"Noto Sans"', 'sans-serif'].join(','),
    h2: { // HTML의 h2 태그에 해당하는 스타일
      fontSize: '1.125rem', // 18px
      fontWeight: 700, // bold
      lineHeight: 'normal',
      letterSpacing: '-0.015em',
      color: appColors.textPrimary,
    },
    h3: { // HTML의 h3 태그에 해당하는 스타일
        fontSize: '1.125rem', // 18px
        fontWeight: 700, // bold
        lineHeight: 'normal',
        letterSpacing: '-0.015em',
        color: appColors.textPrimary,
    },
    h4: { // 22px
      fontSize: '1.375rem',
      fontWeight: 700,
      lineHeight: 'normal',
      letterSpacing: '-0.015em',
      color: appColors.textPrimary,
    },
    h1: { // 32px
      fontSize: '2rem',
      fontWeight: 700,
      lineHeight: 'tight',
      letterSpacing: '-0.015em', // tracking-light에 해당하는 값은 MUI typography에 직접 없음. 커스텀 필요.
      color: appColors.textPrimary,
    },
    body1: { // text-base
      fontSize: '1rem', // 16px
      fontWeight: 400, // normal
      lineHeight: 'normal',
      color: appColors.textPrimary,
    },
    body2: { // text-sm
      fontSize: '0.875rem', // 14px
      fontWeight: 400, // normal
      lineHeight: 'normal',
      color: appColors.textSecondary,
    },
    button: { // text-sm font-bold tracking-[0.015em]
      fontSize: '0.875rem',
      fontWeight: 700,
      lineHeight: 'normal',
      letterSpacing: '0.015em',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none', // 기본 uppercase 방지
          borderRadius: '8px', // rounded-xl에 해당
          height: '40px', // h-10
          paddingLeft: '16px', // px-4
          paddingRight: '16px', // px-4
          minWidth: '84px',
          maxWidth: '480px',
          overflow: 'hidden',
          whiteSpace: 'nowrap',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        },
        // primary: {
        //   backgroundColor: appColors.primary,
        //   color: appColors.white,
        //   '&:hover': {
        //     backgroundColor: appColors.primary, // hover 시 색상 유지 또는 변경
        //   },
        // },
        // containedSecondary: { // secondary 버튼을 위한 variant
        //   backgroundColor: appColors.secondary,
        //   color: appColors.textPrimary,
        //   '&:hover': {
        //     backgroundColor: appColors.secondary, // hover 시 색상 유지 또는 변경
        //   },
        // },
      },
      variants: [ // 커스텀 버튼 variants 정의
        {
          props: { variant: 'customPrimary' },
          style: {
            backgroundColor: appColors.primary,
            color: appColors.white,
            '&:hover': {
              backgroundColor: appColors.primary,
            },
          },
        },
        {
          props: { variant: 'customSecondary' },
          style: {
            backgroundColor: appColors.secondary,
            color: appColors.textPrimary,
            '&:hover': {
              backgroundColor: appColors.secondary,
            },
          },
        },
        { // ai서비스.html의 Regenerate Image/Summary 버튼
          props: { variant: 'aiSecondary' },
          style: {
            backgroundColor: appColors.lightGray,
            color: appColors.black,
            borderRadius: '9999px', // rounded-full
            height: '40px',
            paddingLeft: '16px',
            paddingRight: '16px',
            fontSize: '0.875rem',
            fontWeight: 700,
            lineHeight: 'normal',
            letterSpacing: '0.015em',
          }
        },
        { // ai서비스.html의 Publish 버튼
          props: { variant: 'aiPublish' },
          style: {
            backgroundColor: '#fbfdfc', // white-custom과 유사하지만 미묘하게 다른 색상
            color: appColors.black,
            borderRadius: '9999px', // rounded-full
            height: '48px', // h-12
            paddingLeft: '20px', // px-5
            paddingRight: '20px', // px-5
            fontSize: '1rem', // text-base
            fontWeight: 700,
            lineHeight: 'normal',
            letterSpacing: '0.015em',
          }
        },
      ]
    },
    MuiInputBase: { // input, textarea, select 공통 스타일
      styleOverrides: {
        root: {
          borderRadius: '12px !important', // rounded-xl
          height: '56px', // h-14
          padding: '15px !important',
          fontSize: '1rem', // text-base
          fontWeight: 400, // font-normal
          lineHeight: 'normal',
          color: appColors.textPrimary,
          '& fieldset': { // TextField의 outline border 제거
            border: 'none',
          },
        },
        input: {
          '&::placeholder': {
            color: appColors.placeholder,
            opacity: 1, // 브라우저 기본 opacity를 덮어씀
          },
        },
      },
    },
    MuiOutlinedInput: { // TextField의 기본 아웃라인 스타일 오버라이드
      styleOverrides: {
        root: {
          borderRadius: '12px !important',
        },
        notchedOutline: {
          borderColor: appColors.border, // border
        },
        input: {
          padding: '15px !important', // TextField 기본 padding 제거
        },
      },
    },
    MuiFilledInput: { // filled input (배경색 있는 input)
      styleOverrides: {
        root: {
          backgroundColor: appColors.secondary, // bg-[#e6f4ef]
          '&:hover': {
            backgroundColor: appColors.secondary,
          },
          '&.Mui-focused': {
            backgroundColor: appColors.secondary,
          },
          borderRadius: '12px !important',
          '&::before, &::after': { // Underline 제거
            display: 'none',
          }
        },
        input: {
          padding: '15px !important',
        },
      }
    },
    MuiSelect: {
      styleOverrides: {
        select: {
          // background-image:--select-button-svg 에 해당하는 부분
          // MUI Select의 기본 화살표를 커스텀 SVG로 바꾸는 것은 복잡할 수 있습니다.
          // 기본 화살표를 숨기고 커스텀 아이콘을 <InputAdornment> 등으로 추가하는 것을 고려할 수 있습니다.
          // 여기서는 일단 기본 화살표를 사용하거나, 매우 간단하게만 처리합니다.
          // HTML의 커스텀 SVG는 CSS 변수로 처리되었으므로, MUI에서는 직접 SVG 아이콘 컴포넌트를 사용하는 것이 일반적입니다.
        },
        icon: {
            color: appColors.textSecondary, // 기본 화살표 색상 변경
        }
      },
    },
    MuiRadio: {
      styleOverrides: {
        root: {
          color: appColors.border, // 기본 체크 안된 상태
          '&.Mui-checked': {
            color: appColors.primary, // 체크된 상태
          },
          // HTML의 background-image:--radio-dot-svg 는 MUI Radio 아이콘 오버라이드로 구현
          // 이는 복잡하므로, 기본 Material Design Radio 아이콘을 사용하고 색상만 맞추는 것을 권장합니다.
        },
      },
    },
  },
});

export default theme;