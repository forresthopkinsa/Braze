<template>
  <v-app id="app">
    <v-navigation-drawer
      v-model="drawer"
      clipped
      app>
      <v-list>
        <v-list-tile
          v-for="page in pages"
          :key="page.title"
          :to="page.url"
          ripple >
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
      color="orange darken-3">
      <v-toolbar-side-icon @click="drawer = !drawer"/>
      <v-toolbar-title>Braze</v-toolbar-title>
    </v-toolbar>

    <v-content>
      <v-container>
        <router-view/>
      </v-container>
    </v-content>

    <v-footer
      app
      dark
      color="primary">
      <v-flex text-xs-center>
        <span>Open Source</span>
      </v-flex>
    </v-footer>

  </v-app>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
      drawer: true,
      pages: [
        { title: 'Mods', icon: 'folder', url: '/mods' },
        { title: 'Packs', icon: 'apps', url: '/packs' }
      ]
    }
  }
}
</script>
