<template>
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
            @click="cancel"
          >
            Cancel
          </v-btn>
          <v-btn
            flat
            color="primary"
            @click="addMod"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios'

const root = 'http://localhost:8081'

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
    this.getMods()
  },
  methods: {
    getMods () {
      axios
        .get(`${root}/braze/api/mods`)
        .then(it => { this.mods = it.data })
    },
    addMod () {
      this.dialog = false
      let mod = {}

      for (let i in this.inputs) {
        let input = this.inputs[i]
        mod[input.key] = input.value
      }

      this.clearForm()
      console.log(mod)

      axios
        .post(`${root}/braze/api/mods`, mod)
        .then(it => {
          console.log(it)
          this.getMods()
        })
    },
    cancel () {
      this.dialog = false
      this.clearForm()
    },
    clearForm () {
      this.inputs.forEach(it => { it.value = '' })
    }
  }
}
</script>
