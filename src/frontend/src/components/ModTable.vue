<template>
  <v-container>
    <v-card>
      <v-card-title>
        <span class="headline pa-3">Mod Library</span>
        <v-spacer/>
        <v-text-field
          v-model="search"
          append-icon="search"
          label="Search"
        />
      </v-card-title>

      <v-data-table
        :loading="tableLoading"
        :pagination.sync="pagination"
        :search="search"
        :headers="headers"
        :items="truncatedMods"
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
    </v-card>

    <v-btn
      fab
      bottom
      right
      fixed
      color="accent"
      @click="dialog = true"
    >
      <v-icon>add</v-icon>
    </v-btn>

    <v-dialog
      v-model="dialog"
      width="50%"
    >
      <v-card>
        <v-card-title
          class="title grey lighten-4"
        >
          Add Mod
        </v-card-title>
        <v-form class="pa-4">
          <v-text-field
            v-for="input in inputs"
            v-model="input.value"
            :key="input.name"
            :prepend-icon="input.icon"
            :placeholder="input.name"
          />
        </v-form>
        <v-card-actions>
          <v-spacer/>
          <v-btn
            flat
            @click="closeDialog"
          >
            Cancel
          </v-btn>
          <v-badge
            v-model="error"
            left
            color="error"
            overlap
          >
            <v-btn
              :loading="saveLoading"
              flat
              color="primary"
              @click="addMod"
            >
              Save
            </v-btn>
            <v-icon
              slot="badge"
              dark
              small
            >
              error
            </v-icon>
          </v-badge>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar
      v-model="snackbar"
      :color="snackColor"
      :timeout="5000"
    >
      {{ snackText }}
      <v-btn
        dark
        flat
        @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>
  </v-container>
</template>

<script>
import axios from 'axios'

const root = 'http://localhost:8081'
const latency = 1600 // artifical latency

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
      ],
      dialog: false,
      snackbar: false,
      snackColor: '',
      snackText: '',
      error: false,
      search: '',
      tableLoading: false,
      saveLoading: false,
      pagination: {
        rowsPerPage: (document.documentElement.clientHeight >= 850) ? 10 : 5
      },
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

  computed: {
    // if description is more than 64 characters, truncate it
    truncatedMods () {
      return this.mods.map(it => {
        if (it.description.length > 64) {
          it.description = it.description.substring(0, 63) + '...'
        }
        return it
      })
    }
  },

  mounted () {
    this.updateTable()
  },

  methods: {
    updateTable () {
      this.tableLoading = true

      axios
        .get(`${root}/braze/api/mods`)
        .then(it => {
          loadAndThen(() => {
            this.mods = it.data
            this.tableLoading = false
          })
        })
    },

    addMod () {
      this.saveLoading = true
      let mod = {}

      for (let i in this.inputs) {
        let input = this.inputs[i]
        mod[input.key] = input.value
      }

      axios
        .post(`${root}/braze/api/mods`, mod)
        .then(it => {
          loadAndThen(() => {
            console.log(it)
            this.updateTable()
            this.saveLoading = false
            this.error = false
            this.closeDialog()
          })
        })
        .catch(it => {
          this.saveLoading = false
          this.error = true
          this.snack('error', it.message)
        })
    },

    closeDialog () {
      this.dialog = false
      this.inputs.forEach(it => { it.value = '' })
      this.error = false
    },

    snack (color, text) {
      this.snackColor = color
      this.snackText = text
      this.snackbar = true
    }
  }
}

function loadAndThen (cb) {
  setTimeout(cb, latency)
}
</script>
