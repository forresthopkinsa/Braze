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
      <v-form ref="form" lazy-validation class="pa-4">
        <v-layout justify-space-between>
          <v-flex xs8>
            <v-text-field
              key="name"
              v-model="selection.name"
              :rules="[validateName]"
              prepend-icon="title"
              placeholder="Version Name*"
            />
          </v-flex>
          <v-spacer />
          <v-flex>
            <v-switch v-model="selection.recommended" label="Recommended" />
          </v-flex>
        </v-layout>
        <v-layout justify-space-between>
          <v-flex xs3>
            <v-select
              v-model="selectedGame"
              :items="constants.game"
              item-text="number"
              item-value="name"
              label="Game Version"
              clearable
            />
          </v-flex>
          <v-flex xs3>
            <v-autocomplete
              v-model="selection.forgeVersion"
              :items="forgeVersionsAvailable"
              item-text="build"
              item-value="name"
              label="Forge Version*"
              clearable
            />
          </v-flex>
          <v-flex xs3>
            <v-select
              v-model="selection.javaVersion"
              :items="constants.java"
              item-text="version"
              item-value="name"
              label="Java version"
              clearable
            />
          </v-flex>
        </v-layout>
        <v-slider
          v-model="selection.memory"
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
              v-model="selectedMod"
              :items="mods"
              item-text="name"
              item-value="slug"
              label="Mod"
              @input="updateVersions"
            />
          </v-flex>
          <v-flex xs4>
            <v-select
              v-model="selectedVersion"
              :items="modVersions"
              :loading="versionLoading"
              :disabled="versionLoading"
              label="Version"
              item-text="name"
              return-object
            />
          </v-flex>
          <v-flex xs1>
            <v-btn icon ripple @click="addMod">
              <v-icon>add_circle_outline</v-icon>
            </v-btn>
          </v-flex>
        </v-layout>
        <included-list :items="selection.modList" @delete="delMod" />
      </v-form>
      <v-card-actions>
        <v-spacer />
        <v-btn flat @click="close">
          Cancel
        </v-btn>
        <v-badge v-model="error" left color="error" overlap>
          <v-btn :loading="loading" flat color="primary" @click="submit">
            Save
          </v-btn>
          <v-icon slot="badge" dark small>
            error
          </v-icon>
        </v-badge>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import * as Util from '../util';
import IncludedList from '@/components/IncludedList.vue';

@Component({
  name: 'PackVersionDialog',
  components: { IncludedList },
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    error: {
      type: Boolean,
      default: false,
    },
  },
})
export default class PackVersionDialogComponent extends Vue {
  @Prop({
    type: String,
    default: '',
  })
  pack!: string;

  @Prop({
    type: Object,
    default: () => ({
      name: null,
      forgeVersion: null,
      javaVersion: null,
      recommended: false,
      memory: 0,
      modList: [],
    }),
  })
  preset!: PackVersion;

  @Prop({
    type: Object,
    default: () => ({ forge: [], game: [], java: [] }),
  })
  constants!: Constants;

  $refs!: Vue['$refs'] & {
    form: { validate: () => boolean };
  };

  selection: PackVersion = JSON.parse(JSON.stringify(this.preset));

  selectedGame = '';

  ramTicks = [
    'Any',
    '1 GiB',
    '2 GiB',
    '3 GiB',
    '4 GiB',
    '5 GiB',
    '6 GiB',
    '7 GiB',
    '8 GiB',
  ]
    .flatMap(item => [item, ''])
    .slice(0, -1);

  selectedMod = '';

  selectedVersion: { name?: string } = { name: undefined };

  loading = false;

  mods: SimpleMod[] = [];

  modVersions: IndexedModVersion[] = [];

  versionLoading = false;

  get forgeVersionsAvailable() {
    const gameVersion = this.selectedGame;
    const matches = (it: ForgeConstant) => {
      if (gameVersion === null || gameVersion.length === 0) {
        return true;
      }
      // noinspection JSUnresolvedVariable
      return gameVersion === it.gameVersion.name;
    };

    const ret = this.constants.forge.filter(matches);
    return ret.sort();
  }

  get packVersion() {
    let ram = this.selection.memory;
    if (ram === 0) ram = null;

    return {
      name: this.selection.name,
      index: -1,
      forgeVersion: this.selection.forgeVersion,
      javaVersion: this.selection.javaVersion,
      recommended: this.selection.recommended,
      memory: ram,
      modList: this.selection.modList,
    };
  }

  mounted() {
    Util.getMods(
      it => {
        this.mods = it.data;
      },
      e => {
        console.log(e);
      }
    );
  }

  updateVersions() {
    this.selectedVersion = { name: undefined };
    this.versionLoading = true;
    Util.getMod(
      this.selectedMod,
      it => {
        this.modVersions = it.data.versions;
        this.versionLoading = false;
      },
      e => {
        console.log(e);
        this.versionLoading = false;
      }
    );
  }

  addMod() {
    const mod = this.selectedMod;
    const ver = this.selectedVersion;
    console.log(ver);
    if (ver.name == null || ver.name === '') {
      this.snack('Mod version is required');
      return;
    }
    if (this.selection.modList.some(it => it.slug === mod)) {
      this.snack('Mod already included in pack');
      return;
    }
    this.selection.modList.push({ slug: mod, version: ver.name });
  }

  delMod(simpleModVersion: SimpleModVersion) {
    const index = this.selection.modList.indexOf(simpleModVersion);
    this.selection.modList.splice(index, 1);
  }

  static validateName(str: string) {
    if (!str) {
      return 'Name must not be blank';
    }
    if (/[^a-zA-Z\d\-._]/.test(str)) {
      return 'Name contains invalid characters; only alphanumeric, hyphens, underscores, periods';
    }
    return true;
  }

  submit() {
    console.log(this.packVersion);
    if (!this.$refs.form.validate()) {
      this.snack('Inputs are invalid');
      return;
    }
    this.loading = true;
    Util.postPackVersion(
      this.pack,
      this.packVersion,
      it => {
        this.loading = false;
        console.log(it);
        this.close();
      },
      e => {
        this.snack(e.response ? e.response.data.error : 'HTTP error');
        this.loading = false;
      }
    );
  }

  close() {
    this.$emit('input', false);
  }

  snack(msg: string) {
    this.$emit('snack', msg);
  }
}
</script>
