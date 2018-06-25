<template>
  <v-app id="app">
    <v-navigation-drawer
      v-model="drawer"
      app>
      <v-list>
        <v-list-tile
          v-for="page in pages"
          :key="page.title">
          <v-list-tile-action>
            <v-icon>{{ page.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ page.title }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>

    <v-toolbar
      app
      dark
      color="orange darken-3">
      <v-toolbar-side-icon @click="drawer = !drawer"/>
      <v-toolbar-title>Braze</v-toolbar-title>
    </v-toolbar>

    <v-content>

      <router-view/>

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
      drawer: false,
      pages: [
        { title: 'Mods', icon: 'dashboard' },
        { title: 'Modpacks', icon: 'dashboard' }
      ]
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
