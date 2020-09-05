<template>
  <v-snackbar v-model="value.display" :color="value.color" :timeout="5000">
    {{ value.text }}
    <v-btn dark flat @click="value.display = false">
      Close
    </v-btn>
  </v-snackbar>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import { SnackbarState } from '../model';

@Component({
  name: 'Snackbar',
})
export default class SnackbarComponent extends Vue {
  @Prop({
    type: Object,
    default() {
      return {
        display: false,
        color: 'primary',
        text: '[default]',
      };
    },
  })
  readonly value!: SnackbarState;

  @Watch('value')
  onValueChanged(oldValue: SnackbarState, newValue: SnackbarState) {
    this.$emit('input', newValue);
  }
}
</script>
