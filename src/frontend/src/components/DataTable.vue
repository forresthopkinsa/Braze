<template>
  <v-data-table
    :loading="loading"
    :pagination.sync="pagination"
    :search="search"
    :headers="headers"
    :items="truncatedItems"
    item-key="slug"
  >
    <template slot="items" slot-scope="props">
      <td
        v-for="header in headers"
        :key="header.value"
        style="cursor: pointer"
        @click="expand(props)"
      >
        {{ props.item[header.value] }}
      </td>
    </template>
    <template slot="expand" slot-scope="props">
      <slot v-bind="props" />
    </template>
  </v-data-table>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { BasicElement } from '../model';

export interface DataTableItem {
  item: BasicElement;
  index: number;
  selected: boolean;
  expanded: boolean;
}

@Component({
  name: 'DataTable',
  props: {
    loading: {
      type: Boolean,
      default: false,
    },
    search: {
      type: String,
      default: 'search',
    },
    headers: {
      type: Array,
      default: () => [{ text: 'text', value: 'value' }],
    },
  },
})
export default class DataTableComponent extends Vue {
  @Prop({
    // depends on these objects having a 'slug' field; description will be truncated IF it's supplied
    type: Array,
    default: () => [
      {
        slug: 'slug',
        name: 'name',
        author: 'author',
        description: 'description',
      },
    ],
  })
  items!: BasicElement[];

  pagination = {
    rowsPerPage: document.documentElement.clientHeight >= 850 ? 10 : 5,
  };

  // if description is more than 64 characters, truncate it
  get truncatedItems() {
    // todo: use Vuetify's truncation
    return this.items.map(it => {
      if (it.description != null && it.description.length > 64) {
        it.description = `${it.description.substring(0, 63)}...`;
      }
      return it;
    });
  }

  expand(scope: DataTableItem) {
    scope.expanded = !scope.expanded;
    console.log(scope);
    this.$emit('expand', {
      index: scope.index,
      slug: scope.item.slug,
      expanded: scope.expanded,
    });
  }
}
</script>
