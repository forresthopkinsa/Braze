<template>
  <v-list>
    <v-subheader>Mods included</v-subheader>
    <v-list-tile
      v-for="item in items"
      :key="item.slug"
      ripple
      @click="doNothing"
    >
      <v-layout>
        <v-flex xs8>
          {{ item.slug }}
        </v-flex>
        <v-flex xs4>
          {{ item.version }}
        </v-flex>
      </v-layout>
      <v-list-tile-action>
        <v-btn
          icon
          ripple
          @click="$emit('delete', item)"
        >
          <v-icon>delete</v-icon>
        </v-btn>
      </v-list-tile-action>
    </v-list-tile>
  </v-list>
</template>

<script>
export default {
  name: 'IncludedList',
  props: {
    items: {
      type: Array,
      default: () => [ { slug: 'NULL', version: 'NULL' } ]
    }
  },
  methods: {
    doNothing () { }
  }
}
</script>
