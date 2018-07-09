<template>
  <v-snackbar
    v-model="value.display"
    :color="value.color"
    :timeout="5000"
  >
    {{ value.text }}
    <v-btn
      dark
      flat
      @click="value.display = false"
    >
      Close
    </v-btn>
  </v-snackbar>
</template>

<script>
export default {
  name: 'Snackbar',
  props: {
    value: {
      type: Object,
      default () {
        return {
          display: false,
          color: 'primary',
          text: '[default]'
        }
      }
    }
  },
  watch: {
    value (oldValue, newValue) {
      this.$emit('input', newValue)
    }
  }
}
</script>
