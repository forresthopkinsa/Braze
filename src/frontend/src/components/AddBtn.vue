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

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  name: 'AddBtn'
})
export default class AddBtnComponent extends Vue {
}
</script>
