<template>
  <v-card>
    <v-card-title>
      <span class="headline pa-3">{{ cardTitle }}</span>
      <v-spacer/>
      <v-text-field
        v-model="search"
        append-icon="search"
        label="Search"
      />
    </v-card-title>

    <slot :search="search" />
  </v-card>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  name: 'RootCard',
  props: {
    cardTitle: {
      type: String,
      default: 'cardTitle'
    }
  },
})
export default class RootCardComponent extends Vue {
      search = ''
}
</script>
