<template>
  <v-container>

    <root-card
      card-title="Pack Library"
    >
      <template slot-scope="props">
        <data-table
          :loading="loading"
          :search="props.search"
          :headers="headers"
          :items="packs"
        />
      </template>
    </root-card>

    <v-btn
      color="accent"
      fab
      bottom
      right
      fixed >
      <v-icon>add</v-icon>
    </v-btn>
  </v-container>
</template>

<script>
import axios from 'axios'
import RootCard from '@/components/RootCard'
import DataTable from '@/components/DataTable'

export default {
  name: 'PackTable',
  components: { DataTable, RootCard },
  data () {
    return {
      packs: [ { name: 'name', slug: 'slug' } ],
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Slug', value: 'slug' }
      ],
      loading: false
    }
  },
  mounted: function () {
    axios
      .get('/braze/api/modpacks')
      .then(it => { this.packs = it.data })
      .catch(it => { console.log(it) })
  }
}
</script>
