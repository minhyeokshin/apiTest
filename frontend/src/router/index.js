import {createRouter, createWebHistory} from 'vue-router';
import Home from '@/views/Home.vue';
import RedirectComplete from '@/views/RedirectComplete.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [

    {
      path: '/',
      name: 'Home',
      component: Home
    },
   
    // {
    //   path: '/api/sign/test',
    //   name: 'signService',
    //   component: () => import('../App.vue')
    // },
    // {
    //   path: '/api/create',
    //   name: 'createSignService',
    //   component: () => import('../App.vue')
    // },
    {
      path: '/redirect-complete',
      component: RedirectComplete
    }
  ]
});

export default router;