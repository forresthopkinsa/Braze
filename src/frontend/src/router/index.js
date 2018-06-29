import Vue from 'vue'
import Router from 'vue-router'
import ModTable from '@/components/ModTable'
import PackTable from '@/components/PackTable'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/mods',
      name: 'Mods',
      component: ModTable
    },
    {
      path: '/packs',
      name: 'Packs',
      component: PackTable
    }
  ]
})
