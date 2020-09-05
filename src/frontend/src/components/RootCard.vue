<template>
  <v-card>
    <v-card-title>
      <span class="headline pa-3">{{ cardTitle }}</span>
      <v-spacer />
      <v-text-field v-model="search" append-icon="search" label="Search" />
    </v-card-title>

    <slot :search="search" />
  </v-card>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component({
  name: 'RootCard',
  props: {},
})
export default class RootCardComponent extends Vue {
  @Prop({
    type: String,
    default: 'cardTitle',
  })
  readonly cardTitle!: string;

  search = '';
}
</script>
