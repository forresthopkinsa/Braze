<template>
  <v-container>

    <root-card
      card-title="Pack Library"
    >
      <data-table
        slot-scope="card"
        :loading="tableLoading"
        :search="card.search"
        :headers="headers"
        :items="packs"
        @expand="expandHandler"
      >
        <expand-details
          slot-scope="props"
          :row-data="props.item"
          :loading="versionsLoading[props.index]"
          :versions="versions[props.index]"
          @edit="editVersion(props.item.slug, $event)"
          @add="addVersionDialog = true"
        />
      </data-table>
    </root-card>

    <pack-version-dialog
      :constants="constants"
      v-model="addVersionDialog"
      title="Add version"
      @snack="snack('error', $event)"
    />

    <add-btn @click="dialog = true" />

    <add-dialog
      v-model="dialog"
      :inputs="inputs"
      :loading="dialogLoading"
      :error="dialogError"
      title="Add Pack"
      @add="addPack"
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
import ExpandDetails from '@/components/ExpandDetails'
import PackVersionDialog from '@/components/PackVersionDialog'

export default {
  name: 'PackTable',
  components: { PackVersionDialog, ExpandDetails, Snackbar, AddDialog, AddBtn, DataTable, RootCard },
  data () {
    return {
      addVersionDialog: false,
      addVersionDialogText: '',
      packs: [],
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Slug', value: 'slug' }
      ],
      tableLoading: false,
      dialog: false,
      dialogLoading: false,
      dialogError: false,
      inputs: [
        { name: 'Name', key: 'name', icon: 'title', value: '' },
        { name: 'Slug', key: 'slug', icon: 'fingerprint', value: '' },
        { name: 'Author', key: 'author', icon: 'face', value: '' },
        { name: 'Description', key: 'description', icon: 'insert_comment', value: '' },
        { name: 'Link', key: 'link', icon: 'link', value: '' },
        { name: 'Donate', key: 'donate', icon: 'attach_money', value: '' }
      ],
      snackbar: { display: false, color: 'primary', text: '[default]' },
      versionsLoading: [],
      versions: [],
      constants: { forge: [], game: [], java: [] }
    }
  },
  mounted: function () {
    this.updateTable()
    this.getConstants(it => {
      this.constants = it.data
    }, e => {
      console.log(e)
    })
  },
  methods: {
    editVersion (slug, version) {
      this.addVersionDialog = true
    },
    updateTable () {
      this.tableLoading = true

      this.getPacks(it => {
        this.packs = it.data
        this.tableLoading = false
      }, e => {
        this.tableLoading = false
        this.snack('error', e.message)
      })
    },
    expandHandler (expansion) {
      let index = expansion.index
      this.$set(this.versionsLoading, index, true)

      this.getPack(expansion.slug, it => {
        this.$set(this.versions, index, it.data.versions)
        this.versionsLoading.splice(index, 1, false)
      }, e => {
        console.log(e)
      })
    },
    addPack (inputs) {
      this.dialogLoading = true
      let pack = {}

      for (let i in inputs) {
        let input = inputs[i]
        pack[input.key] = input.value
      }

      this.postPack(pack, it => {
        // console.log(it)
        this.updateTable()
        this.dialogLoading = false
        this.dialog = false
      }, e => {
        this.dialogLoading = false
        this.dialogError = true
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
