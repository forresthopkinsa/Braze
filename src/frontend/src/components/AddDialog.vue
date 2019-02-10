<template>
  <v-dialog :value="value" width="50%" @input="$emit('input', $event)">
    <v-card>
      <v-card-title class="title grey lighten-4">
        {{ title }}
      </v-card-title>
      <v-form class="pa-4">
        <v-text-field
          v-for="input in inputs"
          :key="input.name"
          v-model="input.value"
          :prepend-icon="input.icon"
          :placeholder="input.name"
          :disabled="input.readonly"
        />
      </v-form>
      <v-card-actions>
        <v-spacer />
        <v-btn flat @click="close">
          Cancel
        </v-btn>
        <v-badge v-model="error" left color="error" overlap>
          <v-btn
            :loading="loading"
            flat
            color="primary"
            @click="$emit('add', inputs)"
          >
            Save
          </v-btn>
          <v-icon slot="badge" dark small>
            error
          </v-icon>
        </v-badge>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name: 'AddDialog',
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: 'title',
    },
    inputs: {
      type: Array,
      default: () => [{ name: 'Name', key: 'name', icon: 'error', value: '' }],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    error: {
      type: Boolean,
      default: false,
    },
  },
  watch: {
    value(newValue, oldValue) {
      if (!newValue) {
        this.inputs.forEach(it => {
          it.value = '';
        });
        this.error = false;
      }
    },
  },
})
export default class AddDialogComponent extends Vue {
  close() {
    this.$emit('input', false);
  }
}
</script>
