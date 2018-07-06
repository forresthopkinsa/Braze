<template>
  <v-data-table
    :loading="loading"
    :pagination.sync="pagination"
    :search="search"
    :headers="headers"
    :items="truncatedItems"
  >
    <template
      slot="items"
      slot-scope="props"
    >
      <td
        v-for="header in headers"
        :key="header.value"
      >
        {{ props.item[header.value] }}
      </td>
    </template>
  </v-data-table>
</template>

<script>
export default {
  name: 'DataTable',
  props: {
    loading: {
      type: Boolean,
      default: false
    },
    search: {
      type: String,
      default: 'search'
    },
    headers: {
      type: Array,
      default: () => [ { text: 'text', value: 'value' } ]
    },
    items: {
      type: Array,
      default: () => [ { name: 'name', key: 'key', icon: 'icon', value: 'value' } ]
    }
  },
  data () {
    return {
      pagination: {
        rowsPerPage: (document.documentElement.clientHeight >= 850) ? 10 : 5
      }
    }
  },
  computed: {
    // if description is more than 64 characters, truncate it
    truncatedItems () {
      return this.items.map(it => {
        if (it.description != null && it.description.length > 64) {
          it.description = it.description.substring(0, 63) + '...'
        }
        return it
      })
    }
  }
}
</script>
