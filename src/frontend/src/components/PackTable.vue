<template>
  <v-data-table
    :headers="headers"
    :items="mods"
    class="elevation-1"
  >
    <template
      slot="items"
      slot-scope="props"
    >
      <td>{{ props.item.name }}</td>
      <td>{{ props.item.slug }}</td>
      <td>{{ props.item.author }}</td>
      <td>{{ props.item.description }}</td>
    </template>
  </v-data-table>
</template>

<script>
import axios from 'axios'
import Vue from 'vue'
import Vuetify from 'vuetify'

Vue.use(Vuetify)

export default {
  name: 'PackTable',
  data () {
    return {
      mods: [],
      headers: [
        {text: 'Name', value: 'name'},
        {text: 'Slug', value: 'slug'}
      ]
    }
  },
  mounted: function () {
    axios
      .get('/braze/api/modpacks')
      .then(it => { this.mods = it.data })
  }
}
</script>

<style scoped>

</style>
