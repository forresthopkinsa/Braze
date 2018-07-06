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

<script>
export default {
  name: 'RootCard',
  props: {
    cardTitle: {
      type: String,
      default: 'cardTitle'
    }
  },
  data () {
    return {
      search: ''
    }
  }
}
</script>
