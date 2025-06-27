import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/authors',
      component: () => import('../components/ui/AuthorGrid.vue'),
    },
    {
      path: '/ebookStatisticsViews',
      component: () => import('../components/EbookStatisticsViewView.vue'),
    },
    {
      path: '/subscribers',
      component: () => import('../components/ui/SubscriberGrid.vue'),
    },
    {
      path: '/subscribeBooks',
      component: () => import('../components/ui/SubscribeBookGrid.vue'),
    },
    {
      path: '/subscriptionOpens',
      component: () => import('../components/SubscriptionOpenView.vue'),
    },
    {
      path: '/checkBooks',
      component: () => import('../components/CheckBookView.vue'),
    },
    {
      path: '/points',
      component: () => import('../components/ui/PointGrid.vue'),
    },
    {
      path: '/bookScripts',
      component: () => import('../components/ui/BookScriptGrid.vue'),
    },
    {
      path: '/bookScriptsOpens',
      component: () => import('../components/BookScriptsOpenView.vue'),
    },
    {
      path: '/books',
      component: () => import('../components/ui/BookGrid.vue'),
    },
    {
      path: '/checkSubsciptionPermissons',
      component: () => import('../components/CheckSubsciptionPermissonView.vue'),
    },
    {
      path: '/publications',
      component: () => import('../components/ui/PublicationGrid.vue'),
    },
  ],
})

export default router;
