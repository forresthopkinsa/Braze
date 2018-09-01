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

const url = location.protocol + '//' + location.hostname + ':8081'
const root = url + '/braze/api/'
const constants = root + 'constants'
const mods = root + 'mods'
const packs = root + 'packs'

if (url !== location.origin) { // for dev environment
  axios.defaults.headers.common['Authorization'] = 'Basic dXNlcjpjaGFuZ2VtZQ=='
}

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
    getConstants (success, error) {
      get(constants, success, error)
    },
    getMods (success, error) {
      get(mods, success, error)
    },
    getMod (slug, success, error) {
      get(`${mods}/${slug}`, success, error)
    },
    getPack (slug, success, error) {
      get(`${packs}/${slug}`, success, error)
    },
    getPacks (success, error) {
      get(packs, success, error)
    },
    postMod (data, success, error) {
      post(mods, data, success, error)
    },
    postModVersion (slug, data, success, error) {
      post(`${mods}/${slug}`, data, success, error)
    },
    postPack (data, success, error) {
      post(packs, data, success, error)
    },
    postPackVersion (slug, data, success, error) {
      post(`${packs}/${slug}`, data, success, error)
    },
    slugify (str) {
      return str.toLowerCase()
        .replace(/[^\w\s-]/g, '') // remove non-word [a-z0-9_], non-whitespace, non-hyphen characters
        .replace(/[\s_-]+/g, '-') // swap any length of whitespace, underscore, hyphen characters with a single -
        .replace(/^-+|-+$/g, '') //  remove leading, trailing -
      // function written by kganser https://gist.github.com/mathewbyrne/1280286#gistcomment-1716050
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
    axios.post(url, data).then(success).catch(e => {
      console.error(e.response.data)
      error(e)
    })
  )
}

function loadAndThen (cb) {
  setTimeout(cb, latency)
}
