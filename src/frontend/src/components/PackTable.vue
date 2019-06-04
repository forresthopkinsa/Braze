<template>
  <v-container>
    <root-card card-title="Pack Library">
      <data-table
        slot-scope="card"
        :loading="tableLoading"
        :search="card.search"
        :headers="headers"
        :items="packs"
        @expand="expandHandler"
      >
        <expand-details
          slot-scope="props"
          :row-data="props.item"
          :loading="versionsLoading[props.index]"
          :versions="versions[props.index]"
          @edit="editPack(props.item)"
          @editVersion="editVersion(props.item.slug, $event)"
          @addVersion="addVersion(props.item.slug)"
        />
      </data-table>
    </root-card>

    <pack-version-dialog
      :key="addVersionDialog.key"
      v-model="addVersionDialog.display"
      :constants="constants"
      :pack="addVersionDialog.pack"
      :preset="addVersionDialog.version"
      @snack="snack('error', $event)"
    />

    <add-btn @click="addPack" />

    <add-dialog
      v-model="dialog"
      :inputs="inputs"
      :loading="dialogLoading"
      :error="dialogError"
      title="Add Pack"
      @add="submitPack"
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
import PackVersionDialog from '@/components/PackVersionDialog.vue';
import * as Util from '../util';
import { DynamicInput, Expansion } from '../model';

type LoosePackVersion = Util.Overwrite<
  PackVersion,
  {
    forgeVersion: ForgeVersion | null;
    name: string | null;
  }
>;

interface VersionDialog {
  display: boolean;
  pack: string | null;
  version: LoosePackVersion | null;
  key: number;
}

@Component({
  name: 'PackTable',
  components: {
    PackVersionDialog,
    ExpandDetails,
    Snackbar,
    AddDialog,
    AddBtn,
    DataTable,
    RootCard,
  },
})
export default class PackTable extends Vue {
  addVersionDialog: VersionDialog = {
    display: false,
    pack: null,
    version: null,
    key: 0,
  };

  packs: SimplePack[] = [];

  headers = [
    { text: 'Name', value: 'name' },
    { text: 'Slug', value: 'slug' },
    { text: 'Author', value: 'author' },
  ];

  tableLoading = false;

  dialog = false;

  dialogLoading = false;

  dialogError = false;

  inputs: DynamicInput[] = [];

  snackbar = { display: false, color: 'primary', text: '[default]' };

  versionsLoading: boolean[] = [];

  versions = [];

  constants: Constants = { forge: [], game: [], java: [] };

  mounted() {
    this.updateTable();
    Util.getConstants(
      it => {
        this.constants = it.data;
      },
      e => {
        console.log(e);
      }
    );
  }

  editVersion(slug: string, version: PackVersion) {
    this.addVersionDialog.key += 1;
    this.addVersionDialog.pack = slug;
    this.addVersionDialog.version = version;
    this.addVersionDialog.display = true;
  }

  addVersion(slug: string) {
    this.addVersionDialog.key += 1;
    this.addVersionDialog.pack = slug;
    this.addVersionDialog.version = {
      name: null,
      forgeVersion: null,
      javaVersion: null,
      recommended: false,
      memory: 0,
      modList: [],
    };
    this.addVersionDialog.display = true;
  }

  updateTable() {
    this.tableLoading = true;

    Util.getPacks(
      it => {
        this.packs = it.data;
        this.tableLoading = false;
      },
      e => {
        this.tableLoading = false;
        this.snack('error', e.message);
      }
    );
  }

  expandHandler(expansion: Expansion) {
    const { index } = expansion;
    this.$set(this.versionsLoading, index, true);

    Util.getPack(
      expansion.slug,
      it => {
        this.$set(this.versions, index, it.data.versions);
        this.versionsLoading.splice(index, 1, false);
      },
      e => {
        console.log(e);
      }
    );
  }

  addPack() {
    this.inputs = [
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
    this.dialog = true;
  }

  editPack(data: SimplePack) {
    this.inputs = [
      { name: 'Name', key: 'name', icon: 'title', value: data.name },
      {
        name: 'Slug',
        key: 'slug',
        icon: 'fingerprint',
        value: data.slug,
        readonly: true,
      },
      { name: 'Author', key: 'author', icon: 'face', value: data.author },
      {
        name: 'Description',
        key: 'description',
        icon: 'insert_comment',
        value: data.description,
      },
      { name: 'Link', key: 'link', icon: 'link', value: data.link },
      {
        name: 'Donate',
        key: 'donate',
        icon: 'attach_money',
        value: data.donate,
      },
    ];
    this.dialog = true;
  }

  submitPack(inputs: DynamicInput[]) {
    this.dialogLoading = true;

    const pack: SimplePack = Util.inputsToObject(inputs);

    Util.postPack(
      pack,
      () => {
        // console.log(it)
        this.updateTable();
        this.dialogLoading = false;
        this.dialog = false;
      },
      e => {
        this.dialogLoading = false;
        this.dialogError = true;
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
