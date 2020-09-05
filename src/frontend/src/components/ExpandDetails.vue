<template>
  <v-card flat dark>
    <v-progress-linear
      :active="loading"
      class="ma-0"
      height="3"
      indeterminate
    />
    <v-layout row>
      <v-flex sm7 md8 lg9 xl10>
        <v-layout justify-space-between fill-height column>
          <v-card-title>
            <div>
              <div class="headline">{{ rowData.name }}</div>
              <div class="subheading">
                By <span class="font-italic">{{ rowData.author }}</span>
              </div>
              <br />
              <div class="body-2">{{ rowData.description }}</div>
              <div class="caption font-weight-light font-italic">
                <!-- todo: validate url -->
                <a :href="rowData.link">Website</a> •
                <a :href="rowData.donate">Donate</a>
              </div>
            </div>
          </v-card-title>
          <v-btn class="ma-3" outline fab @click="$emit('edit')">
            <v-icon>edit</v-icon>
          </v-btn>
        </v-layout>
      </v-flex>
      <v-flex sm5 md4 lg3 xl2>
        <v-list>
          <v-list-tile v-for="version in versions" :key="version.index">
            <v-list-tile-content>
              {{ version.name }}
            </v-list-tile-content>
            <v-list-tile-action>
              <v-btn icon ripple @click="$emit('editVersion', version)">
                <v-icon>edit</v-icon>
              </v-btn>
            </v-list-tile-action>
          </v-list-tile>
          <v-list-tile>
            <v-spacer />
            <v-btn outline @click="$emit('addVersion')">
              add version
            </v-btn>
            <v-spacer />
          </v-list-tile>
        </v-list>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name: 'ExpandDetails',
  props: {
    rowData: {
      type: Object,
      default: () => ({ slug: '', description: '', donate: '' }),
    },
    loading: {
      type: Boolean,
      default: false,
    },
    versions: {
      type: Array,
      default: () => [],
    },
  },
})
export default class ExpandDetailsComponent extends Vue {}
</script>
