// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import 'typeface-roboto'
import 'vuetify/dist/vuetify.min.css'
import Vue from 'vue'
import Vuetify from 'vuetify'
import App from './App'
import router from './router'
import colors from 'vuetify/es5/util/colors'
import axios from 'axios'

const url = 'http://localhost:8081'
const root = url + '/braze/api/'
const mods = root + 'mods'
const packs = root + 'packs'

const latency = 1600 // artifical latency

Vue.config.productionTip = false

Vue.use(Vuetify, {
  theme: {
    'primary': colors.orange.darken2,
    'accent': colors.blue.base
  }
})

Vue.mixin({
  methods: {
    getMods (success, error) {
      get(mods, success, error)
    },
    getPacks (success, error) {
      get(packs, success, error)
    },
    postMod (data, success, error) {
      post(mods, data, success, error)
    },
    postPack (data, success, error) {
      post(packs, data, success, error)
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

function get (url, success, error) {
  loadAndThen(() =>
    axios.get(url).then(success).catch(error)
  )
}

function post (url, data, success, error) {
  loadAndThen(() =>
    axios.post(url, data).then(success).catch(error)
  )
}

function loadAndThen (cb) {
  setTimeout(cb, latency)
}
