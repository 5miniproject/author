<script setup>
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'
</script>

<template>
    <ul>
        <VerticalNavSectionTitle :item="{ heading: '작가관리' }" />
        <VerticalNavLink
            :item="{
                title: '작가',
                to: '/authors',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '전자책통계열람',
                to: '/ebookStatisticsViews',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '구독관리' }" />
        <VerticalNavLink
            :item="{
                title: '요금제구독자',
                to: '/subscribers',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '서적 구독',
                to: '/subscribeBooks',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '구독 열람',
                to: '/subscriptionOpens',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '책 확인',
                to: '/checkBooks',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '포인트 관리' }" />
        <VerticalNavLink
            :item="{
                title: '포인트',
                to: '/points',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '집필관리' }" />
        <VerticalNavLink
            :item="{
                title: '원고',
                to: '/bookScripts',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '원고 열람',
                to: '/bookScriptsOpens',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '서재플랫폼' }" />
        <VerticalNavLink
            :item="{
                title: '서재',
                to: '/books',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '구독 권한 확인',
                to: '/checkSubsciptionPermissons',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'ai서비스' }" />
        <VerticalNavLink
            :item="{
                title: '출간물',
                to: '/publications',
            }"
        />
    </ul>
</template>
