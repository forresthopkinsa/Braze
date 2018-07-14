<template>
  <v-card
    flat
    dark
  >
    <v-progress-linear
      :active="loading"
      class="ma-0"
      height="3"
      indeterminate
    />
    <v-layout row>
      <v-flex
        sm7
        md8
        lg9
        xl10
      >
        <v-card-title class="subheader">
          {{ rowData }}
        </v-card-title>
      </v-flex>
      <v-flex
        sm5
        md4
        lg3
        xl2
      >
        <v-list>
          <v-list-tile
            v-for="version in versions"
            :key="version.index"
          >
            <v-list-tile-content>
              <v-list-tile-title v-text="version.name" />
            </v-list-tile-content>
            <v-list-tile-action>
              <v-btn
                icon
                ripple
                @click="$emit('edit', version)"
              >
                <v-icon>edit</v-icon>
              </v-btn>
            </v-list-tile-action>
          </v-list-tile>
        </v-list>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<script>
export default {
  name: 'ExpandDetails',
  props: {
    rowData: {
      type: Object,
      default: () => ({ slug: '', description: '', donate: '' })
    },
    loading: {
      type: Boolean,
      default: false
    },
    versions: {
      type: Array,
      default: () => []
    }
  }
}
</script>
