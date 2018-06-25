<template>
  <v-container>

    <v-data-table
      :headers="headers"
      :items="mods"
      class="elevation-1">
      <template
        slot="items"
        slot-scope="props">
        <td>{{ props.item.name }}</td>
        <td>{{ props.item.slug }}</td>
      </template>
    </v-data-table>

  </v-container>
</template>

<script>
import axios from 'axios'
import Vue from 'vue'
import Vuetify from 'vuetify'

Vue.use(Vuetify)

export default {
  name: 'ModTable',
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
      .get('/solder/api/mod')
      .then(it => { this.mods = entrify(it.data.mods) })
  }
}

function entrify (mods) {
  console.log(mods)
  let entries = Object.entries(mods)
  console.log(entries)
  return entries.map(x => {
    return { name: x[1], slug: x[0] }
  })
}
</script>

<style scoped>

</style>
