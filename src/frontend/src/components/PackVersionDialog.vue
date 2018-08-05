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
        <v-layout justify-space-between>
          <v-flex xs8>
            <v-text-field
              key="name"
              prepend-icon="title"
              placeholder="Version Name"
            />
          </v-flex>
          <v-spacer/>
          <v-flex>
            <v-switch
              label="Recommended"
            />
          </v-flex>
        </v-layout>
        <v-layout justify-space-between>
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
        <v-slider
          :tick-labels="ramTicks"
          :max="8096"
          label="RAM Requirement"
          step="512"
          tick-size="0"
          ticks="always"
          thumb-label
        />
        <v-layout justify-space-between>
          <v-flex xs6>
            <v-autocomplete
              :items="mods"
              v-model="selectedMod"
              item-text="name"
              item-value="slug"
              label="Mod"
              @input="updateVersions"
            />
          </v-flex>
          <v-flex xs4>
            <v-select
              :items="modVersions"
              :loading="versionLoading"
              :disabled="versionLoading"
              v-model="selectedVersion"
              label="Version"
              item-text="name"
              return-object
            />
          </v-flex>
          <v-flex xs1>
            <v-btn
              icon
              ripple
              @click="addMod"
            >
              <v-icon>add_circle_outline</v-icon>
            </v-btn>
          </v-flex>
        </v-layout>
        <included-list
          :items="modVersionsIncluded"
          @delete="delMod"
        />
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
import IncludedList from '@/components/IncludedList'

export default {
  name: 'PackVersionDialog',
  components: { IncludedList },
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
      selectedForge: 0,
      ramTicks: [
        'Any', '',
        '1 GiB', '',
        '2 GiB', '',
        '3 GiB', '',
        '4 GiB', '',
        '5 GiB', '',
        '6 GiB', '',
        '7 GiB', '',
        '8 GiB'
      ],
      selectedMod: {},
      selectedVersion: {},
      mods: [],
      modVersions: [],
      versionLoading: false,
      modVersionsIncluded: []
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
      // console.log('forge versions avail: ' + JSON.stringify(ret))
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
  mounted: function () {
    this.getMods(it => {
      // console.log(it.data)
      this.mods = it.data
    }, e => {
      console.log(e)
    })
  },
  methods: {
    updateVersions () {
      this.selectedVersion = {}
      this.versionLoading = true
      this.getMod(this.selectedMod, it => {
        // console.log(it.data)
        this.modVersions = it.data.versions
        this.versionLoading = false
      }, e => {
        console.log(e)
        this.versionLoading = false
      })
    },
    addMod () {
      let mod = this.selectedMod
      let ver = this.selectedVersion
      if (this.modVersionsIncluded.some(it => it.slug === mod)) {
        this.$emit('snack', 'Mod already included in pack')
        return
      }
      this.modVersionsIncluded.push({ slug: mod, version: ver.name })
    },
    delMod (simpleModVersion) {
      let index = this.modVersionsIncluded.indexOf(simpleModVersion)
      this.modVersionsIncluded.splice(index, 1)
    },
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
