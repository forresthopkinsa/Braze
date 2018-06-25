import Vue from 'vue'
import Router from 'vue-router'
import ModTable from '@/components/ModTable'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'ModTable',
      component: ModTable
    }
  ]
})
