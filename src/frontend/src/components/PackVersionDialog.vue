<template>
  <v-dialog
    :value="value"
    width="50%"
    persistent
    @input="$emit('input', $event)"
  >
    <v-card>
      <v-card-title class="title grey lighten-4">
        Add version for {{ pack }}
      </v-card-title>
      <v-form
        ref="form"
        lazy-validation
        class="pa-4"
      >
        <v-layout justify-space-between>
          <v-flex xs8>
            <v-text-field
              key="name"
              :rules="[validateName]"
              v-model="selection.name"
              prepend-icon="title"
              placeholder="Version Name*"
            />
          </v-flex>
          <v-spacer/>
          <v-flex>
            <v-switch
              v-model="selection.recommended"
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
              v-model="selection.forgeVersion"
              item-text="build"
              item-value="name"
              label="Forge Version*"
              clearable
            />
          </v-flex>
          <v-flex xs3>
            <v-select
              :items="constants.java"
              v-model="selection.javaVersion"
              item-text="version"
              item-value="name"
              label="Java version"
              clearable
            />
          </v-flex>
        </v-layout>
        <v-slider
          :tick-labels="ramTicks"
          :max="8096"
          v-model="selection.memory"
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
          :items="selection.modList"
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
            @click="submit"
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
    pack: {
      type: String,
      default: null
    },
    selection: {
      type: Object,
      default: () => ({
        name: null,
        forgeVersion: null,
        javaVersion: null,
        recommended: false,
        memory: 0,
        modList: []
      })
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
      selectedGame: '',
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
      loading: false,
      mods: [],
      modVersions: [],
      versionLoading: false
    }
  },
  computed: {
    forgeVersionsAvailable () {
      let gameVersion = this.selectedGame
      let matches = function (it) {
        if (gameVersion === null || gameVersion.length === 0) {
          return true
        } else {
          // noinspection JSUnresolvedVariable
          return gameVersion === it.gameVersion.name
        }
      }

      let ret = this.constants.forge.filter(matches)
      return ret.sort()
    },
    packVersion () {
      let ram = this.selection.memory
      if (ram === 0) ram = null

      return {
        name: this.selection.name,
        index: -1,
        forgeVersion: this.selection.forgeVersion,
        javaVersion: this.selection.javaVersion,
        recommended: this.selection.recommended,
        memory: ram,
        modList: this.selection.modList
      }
    }
  },
  mounted: function () {
    this.getMods(it => {
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
      if (this.selection.modList.some(it => it.slug === mod)) {
        this.snack('Mod already included in pack')
        return
      }
      this.selection.modList.push({ slug: mod, version: ver.name })
    },
    delMod (simpleModVersion) {
      let index = this.selection.modList.indexOf(simpleModVersion)
      this.selection.modList.splice(index, 1)
    },
    validateName (str) {
      if (!str) {
        return 'Name must not be blank'
      } else if (/[^a-zA-Z\d\-._]/.test(str)) {
        return 'Name contains invalid characters; only alphanumeric, hyphens, underscores, periods'
      } else {
        return true
      }
    },
    submit () {
      console.log(this.packVersion)
      if (!this.$refs.form.validate()) {
        this.snack('Inputs are invalid')
        return
      }
      this.loading = true
      this.postPackVersion(this.pack, this.packVersion, it => {
        this.loading = false
        console.log(it)
        this.close()
      }, e => {
        console.log(e)
        this.snack(e)
        this.loading = false
      })
    },
    close () {
      this.$emit('input', false)
    },
    log (msg) {
      console.log(msg)
    },
    snack (msg) {
      this.$emit('snack', msg)
    }
  }
}
</script>
