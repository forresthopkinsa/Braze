// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'material-design-icons-iconfont/dist/material-design-icons.css';
import 'typeface-roboto';
import 'vuetify/dist/vuetify.min.css';
import Vue from 'vue';
import Vuetify from 'vuetify';
import colors from 'vuetify/es5/util/colors';
import App from './components/App.vue';
import router from './router';

Vue.config.productionTip = false;

Vue.use(Vuetify, {
  theme: {
    primary: colors.orange.darken2,
    accent: colors.blue.base,
  },
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
});
