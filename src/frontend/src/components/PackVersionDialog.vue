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
        <v-layout justify-space-around>
          <v-flex xs3>
            <v-select
              :items="constants.game"
              v-model="selectedGame"
              item-text="number"
              item-value="name"
              label="Game Version"
              clearable
            />
          </v-flex>
          <v-flex xs3>
            <v-autocomplete
              :items="forgeVersionsAvailable"
              v-model="selectedForge"
              label="Forge Version"
              clearable
            />
          </v-flex>
          <v-flex xs3>
            <v-select
              :items="javaVersions"
              label="Java version"
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
            @click="log(packVersion)"
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
      versionName: '',
      selectedGame: '',
      selectedForge: 0
    }
  },
  computed: {
    javaVersions () {
      return toProps(this.constants.java, 'version')
    },
    forgeVersionsAvailable () {
      let gameVersion = this.selectedGame
      let matches = function (it) {
        if (gameVersion === null || gameVersion.length === 0) {
          return true
        } else {
          return gameVersion === it.gameVersion.name
        }
      }

      let ret = this.constants.forge.filter(matches)
      console.log('forge versions avail: ' + JSON.stringify(ret))
      return toProps(ret, 'build').sort()
    },
    packVersion () {
      let obj = {
        'name': null,
        'index': null,
        'forgeVersion': null,
        'javaVersion': null,
        'recommended': null,
        'memory': null,
        'modlist': []
      }
      obj.name = this.versionName

      return obj
    }
  },
  methods: {
    close () {
      this.$emit('input', false)
    },
    log (msg) {
      console.log(msg)
    }
  }
}

function toProps (arr, prop) {
  return arr.map(it => it[prop])
}
</script>
