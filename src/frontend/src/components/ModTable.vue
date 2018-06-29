<template>
  <div>
    <v-content>
      <v-container>
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
      </v-container>
    </v-content>

    <v-btn
      fab
      bottom
      right
      fixed >
      <v-icon>add</v-icon>
    </v-btn>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ModTable',
  data () {
    return {
      mods: [],
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Slug', value: 'slug' },
        { text: 'Author', value: 'author' },
        { text: 'Description', value: 'description' }
      ]
    }
  },
  mounted: function () {
    axios
      .get('/braze/api/mods')
      .then(it => { this.mods = it.data })
  }
}
</script>
