<template>
  <v-container>

    <root-card card-title="Mod Library">
      <data-table
        slot-scope="card"
        :loading="tableLoading"
        :search="card.search"
        :headers="headers"
        :items="mods"
      />
    </root-card>

    <add-btn @click="dialog = true" />

    <add-dialog
      v-model="dialog"
      :inputs="inputs"
      :loading="saveLoading"
      :error="error"
      title="Add Mod"
      @add="addMod"
    />

    <snackbar v-model="snackbar" />

  </v-container>
</template>

<script>
import RootCard from '@/components/RootCard'
import DataTable from '@/components/DataTable'
import AddBtn from '@/components/AddBtn'
import AddDialog from '@/components/AddDialog'
import Snackbar from '@/components/Snackbar'

export default {
  name: 'ModTable',
  components: { Snackbar, AddDialog, AddBtn, DataTable, RootCard },
  data () {
    return {
      mods: [],
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Slug', value: 'slug' },
        { text: 'Author', value: 'author' },
        { text: 'Description', value: 'description' }
      ],
      dialog: false,
      snackbar: { display: false, color: '', text: '' },
      error: false,
      tableLoading: false,
      saveLoading: false,
      inputs: [
        { name: 'Name', key: 'name', icon: 'title', value: '' },
        { name: 'Slug', key: 'slug', icon: 'fingerprint', value: '' },
        { name: 'Author', key: 'author', icon: 'face', value: '' },
        { name: 'Description', key: 'description', icon: 'insert_comment', value: '' },
        { name: 'Link', key: 'link', icon: 'link', value: '' },
        { name: 'Donate', key: 'donate', icon: 'attach_money', value: '' }
      ]
    }
  },

  mounted () {
    this.updateTable()
  },

  methods: {
    updateTable () {
      this.tableLoading = true

      this.getMods(it => {
        this.mods = it.data
        this.tableLoading = false
      }, e => {
        this.tableLoading = false
        this.snack('error', e.message)
      })
    },

    addMod (inputs) {
      this.saveLoading = true
      let mod = {}

      for (let i in inputs) {
        let input = inputs[i]
        mod[input.key] = input.value
      }

      this.postMod(mod, it => {
        console.log(it)
        this.updateTable()
        this.saveLoading = false
        this.dialog = false
      }, e => {
        this.saveLoading = false
        this.error = true
        this.snack('error', e.message)
      })
    },

    snack (color, text) {
      this.snackbar.color = color
      this.snackbar.text = text
      this.snackbar.display = true
    }
  }
}
</script>
