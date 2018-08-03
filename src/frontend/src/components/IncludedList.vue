<template>
  <v-list>
    <v-subheader>Mods included</v-subheader>
    <v-list-tile
      v-for="item in items"
      :key="item.name"
      ripple
      @click="true"
    >
      <v-layout>
        <v-flex xs8>
          {{ item.name }}
        </v-flex>
        <v-flex xs4>
          {{ item.version }}
        </v-flex>
      </v-layout>
      <v-list-tile-action><v-btn
        icon
        ripple
        @click="$emit('edit', version)"
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
      default: () => [ { name: 'name', version: 'version' } ]
    }
  }
}
</script>
