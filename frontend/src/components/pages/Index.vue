<template>
  <v-container fluid>
    <v-row>
      <v-card
          v-for="(card, index) in cards"
          :key="index"
          class="mx-auto"
          style="height:300px; width:300px; margin-bottom:20px;"
          outlined
      >
        <v-list-item>
          <v-icon style="margin-top: 30%; margin-left: 37.5%;" color="primary" size="64">mdi-apps</v-icon>
        </v-list-item>
        <v-card-actions>
          <v-btn
              class="mx-auto"
              outlined
              rounded
              :to="card.link"
              style="font-weight:500; font-size:20px; border:solid 2px; max-width:250px; overflow:hidden; margin-top: 10%;"
          >
              {{ card.text }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'Index',
  data: () => ({
    cards: [
      {
          text: "작가",
          link: "/authors",
      },
      {
          text: "전자책통계열람",
          link: "/ebookStatisticsViews",
      },
      {
          text: "요금제구독자",
          link: "/subscribers",
      },
      {
          text: "서적 구독",
          link: "/subscribeBooks",
      },
      {
          text: "구독 열람",
          link: "/subscriptionOpens",
      },
      {
          text: "책 확인",
          link: "/checkBooks",
      },
      {
          text: "포인트",
          link: "/points",
      },
      {
          text: "원고",
          link: "/bookScripts",
      },
      {
          text: "원고 열람",
          link: "/bookScriptsOpens",
      },
      {
          text: "서재",
          link: "/books",
      },
      {
          text: "구독 권한 확인",
          link: "/checkSubsciptionPermissons",
      },
      {
          text: "출간물",
          link: "/publications",
      },
    ],
  }),
};
</script>
