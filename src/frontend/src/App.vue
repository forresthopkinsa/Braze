<template>
  <v-app id="app">
    <v-navigation-drawer
      v-model="drawer"
      clipped
      app
    >
      <v-list>
        <v-list-tile
          v-for="page in pages"
          :key="page.title"
          :to="page.url"
          ripple
        >
          <v-list-tile-action>
            <v-icon>{{ page.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>
              {{ page.title }}
            </v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>

    <v-toolbar
      app
      dark
      clipped-left
      color="primary"
    >
      <v-toolbar-side-icon @click="drawer = !drawer"/>
      <v-toolbar-title>Braze</v-toolbar-title>
    </v-toolbar>

    <v-content>
      <router-view/>
    </v-content>

    <v-footer
      app
      dark
      color="secondary"
    >
      <v-flex text-xs-center>
        <span>Open Source</span>
      </v-flex>
    </v-footer>

  </v-app>
</template>

<script>
import UploadButton from 'vuetify-upload-button'

export default {
  name: 'App',
  components: {
    'upload-btn': UploadButton
  },
  data () {
    return {
      drawer: (document.documentElement.clientWidth >= 1264),
      pages: [
        { title: 'Mods', icon: 'folder', url: '/mods' },
        { title: 'Packs', icon: 'apps', url: '/packs' }
      ]
    }
  }
}
</script>
