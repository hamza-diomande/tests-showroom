<template>
  <table class="table table-bordered table-striped">
    <thead class="thead-dark">
      <tr>
        <th
          v-for="column in columns"
          :key="column.key"
          @click="sortBy(column.key)"
          :class="{ sorted: sortByKey === column.key, desc: sortByKey === column.key && sortDirection === 'desc', asc: sortByKey === column.key && sortDirection === 'asc' }"
        >
          {{ column.label }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="row in data" :key="row.id">
        <td v-for="column in columns" :key="column.key">
          {{ row[column.key] }}
        </td>
        <!-- Ajout de la colonne "Actions" avec les boutons -->
        <td v-if="showActionsColumn">
          <button type="button" class="btn btn-primary btn-sm" @click="$emit('edit', row.id)">Modifier</button>
          <button type="button" class="btn btn-danger btn-sm" @click="$emit('delete', row.id)">Supprimer</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  name: 'DataTable',
  props: {
    columns: {
      type: Array,
      required: true,
    },
    data: {
      type: Array,
      required: true,
    },
    sortByKey: {
      type: String,
      default: '',
    },
    sortDirection: {
      type: String,
      default: 'asc',
    },
    showActionsColumn: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    sortBy(key) {
      this.$emit('sort', key);
    },
  },
};
</script>

<style scoped>
thead {
  background-color: #343a40;
  color: white;
}

.sorted {
  background-color: #6c757d;
}

th.sorted.desc::after {
  content: ' ▼';
}

th.sorted.asc::after {
  content: ' ▲';
}
</style>
