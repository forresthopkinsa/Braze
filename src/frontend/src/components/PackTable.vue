<template>
  <v-container>

    <root-card
      card-title="Pack Library"
    >
      <data-table
        slot-scope="props"
        :loading="loading"
        :search="props.search"
        :headers="headers"
        :items="packs"
      />
    </root-card>

    <add-btn @click="dialog = true" />

  </v-container>
</template>

<script>
import RootCard from '@/components/RootCard'
import DataTable from '@/components/DataTable'
import AddBtn from '@/components/AddBtn'

export default {
  name: 'PackTable',
  components: { AddBtn, DataTable, RootCard },
  data () {
    return {
      packs: [ { name: 'name', slug: 'slug' } ],
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Slug', value: 'slug' }
      ],
      loading: false,
      dialog: false
    }
  },
  mounted: function () {
    this.updateTable()
  },
  methods: {
    updateTable () {
      this.loading = true

      this.getPacks(it => {
        this.packs = it.data
        this.loading = false
      }, e => {
        console.log(e)
      })
    }
  }
}
</script>
