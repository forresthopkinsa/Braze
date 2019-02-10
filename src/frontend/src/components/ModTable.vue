<template>
  <v-container>
    <root-card card-title="Mod Library">
      <data-table
        slot-scope="card"
        :loading="tableLoading"
        :search="card.search"
        :headers="headers"
        :items="mods"
        @expand="expandHandler"
      >
        <expand-details
          slot-scope="props"
          :row-data="props.item"
          :loading="versionsLoading[props.index]"
          :versions="versions[props.index]"
        />
      </data-table>
    </root-card>

    <add-btn @click="dialog = true" />

    <add-dialog
      v-model="dialog"
      :inputs="inputs"
      :loading="saveLoading"
      :error="error"
      title="Add Mod"
      @add="addMod"
    />

    <snackbar v-model="snackbar" />
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import RootCard from '@/components/RootCard.vue';
import DataTable from '@/components/DataTable.vue';
import AddBtn from '@/components/AddBtn.vue';
import AddDialog from '@/components/AddDialog.vue';
import Snackbar from '@/components/Snackbar.vue';
import ExpandDetails from '@/components/ExpandDetails.vue';
import * as Util from '../util';

@Component({
  name: 'ModTable',
  components: {
    ExpandDetails,
    Snackbar,
    AddDialog,
    DataTable,
    AddBtn,
    RootCard,
  },
})
export default class ModTableComponent extends Vue {
  mods = [];

  headers = [
    { text: 'Name', value: 'name' },
    { text: 'Slug', value: 'slug' },
    { text: 'Author', value: 'author' },
    { text: 'Description', value: 'description' },
  ];

  dialog = false;

  snackbar = { display: false, color: '', text: '' };

  error = false;

  tableLoading = false;

  saveLoading = false;

  versionsLoading: boolean[] = [];

  versions = [];

  inputs = [
    { name: 'Name', key: 'name', icon: 'title', value: '' },
    { name: 'Slug', key: 'slug', icon: 'fingerprint', value: '' },
    { name: 'Author', key: 'author', icon: 'face', value: '' },
    {
      name: 'Description',
      key: 'description',
      icon: 'insert_comment',
      value: '',
    },
    { name: 'Link', key: 'link', icon: 'link', value: '' },
    { name: 'Donate', key: 'donate', icon: 'attach_money', value: '' },
  ];

  mounted() {
    this.updateTable();
  }

  updateTable() {
    this.tableLoading = true;

    Util.getMods(
      (it: any) => {
        this.mods = it.data;
        this.tableLoading = false;
      },
      (e: Error) => {
        this.tableLoading = false;
        this.snack('error', e.message);
      }
    );
  }

  expandHandler(expansion: any) {
    const { index } = expansion;
    this.$set(this.versionsLoading, index, true);

    Util.getMod(
      expansion.slug,
      (it: any) => {
        this.$set(this.versions, index, it.data.versions);
        this.versionsLoading.splice(index, 1, false);
      },
      (e: Error) => {
        console.log(e);
      }
    );
  }

  addMod(inputs: any) {
    this.saveLoading = true;
    const mod: any = {};

    for (const i in inputs) {
      const input = inputs[i];
      mod[input.key] = input.value;
    }

    Util.postMod(
      mod,
      (it: any) => {
        console.log(it);
        this.updateTable();
        this.saveLoading = false;
        this.dialog = false;
      },
      (e: Error) => {
        this.saveLoading = false;
        this.error = true;
        this.snack('error', e.message);
      }
    );
  }

  snack(color: string, text: string) {
    this.snackbar.color = color;
    this.snackbar.text = text;
    this.snackbar.display = true;
  }
}
</script>
