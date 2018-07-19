<template>
  <v-dialog
    :value="value"
    width="50%"
    persistent
    @input="$emit('input', $event)"
  >
    <v-card>
      <v-card-title class="title grey lighten-4">
        {{ title }}
      </v-card-title>
      <v-form class="pa-4">
        <v-text-field
          key="name"
          prepend-icon="title"
          placeholder="Version Name"
        />
        <v-range-slider
          :tick-labels="gameVersions"
          :value="[0, 1]"
          :max="gameVersions.length - 1"
          v-model="selectedVersions"
          label="Game Versions"
        />
        <v-layout justify-space-around>
          <v-flex xs3>
            <v-autocomplete
              :items="minForgeVersions"
              v-model="minForge"
              label="Min Forge"
              clearable
            />
          </v-flex>
          <v-flex xs3>
            <v-autocomplete
              :items="maxForgeVersions"
              v-model="maxForge"
              label="Max Forge"
              clearable
            />
          </v-flex>
        </v-layout>
      </v-form>
      <v-card-actions>
        <v-spacer/>
        <v-btn
          flat
          @click="close"
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
            :loading="loading"
            flat
            color="primary"
            @click="$emit('add')"
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
</template>

<script>
export default {
  name: 'PackVersionDialog',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: 'title'
    },
    loading: {
      type: Boolean,
      default: false
    },
    error: {
      type: Boolean,
      default: false
    },
    constants: {
      type: Object,
      default: () => ({ forge: [], game: [], java: [] })
    }
  },
  data () {
    return {
      selectedVersions: [0, 1],
      minForge: 0,
      maxForge: 9999
    }
  },
  computed: {
    gameVersions () {
      return toProps(this.constants.game, 'number')
    },
    forgeVersionsAvailable () {
      let ret = this.constants.forge.filter(it =>
        toProps(this.selectedGameVersions, 'name').includes(it.gameVersion.name)
      )
      console.log('forge versions avail: ' + JSON.stringify(ret))
      return toProps(ret, 'build').sort()
    },
    minForgeVersions () {
      return this.forgeVersionsAvailable.filter(it => it <= this.maxForge)
    },
    maxForgeVersions () {
      return this.forgeVersionsAvailable.filter(it => it >= this.minForge)
    },
    selectedGameVersions () {
      let min = this.selectedVersions[0]
      let max = this.selectedVersions[1]
      let ret = this.constants.game.slice(min, max + 1)
      console.log('selected versions: ' + JSON.stringify(ret))
      return ret
    }
  },
  methods: {
    close () {
      this.$emit('input', false)
    }
  }
}

function toProps (arr, prop) {
  return arr.map(it => it[prop])
}
</script>
