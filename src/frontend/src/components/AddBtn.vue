<template>
  <v-btn
    fab
    bottom
    right
    fixed
    color="accent"
    @click="$emit('click')"
  >
    <v-icon>add</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: 'AddBtn'
}
</script>
