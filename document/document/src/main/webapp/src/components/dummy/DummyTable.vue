<template>
  <div v-if="dummies.length > 0">
    <table aria-label="Saved Dummies">
      <tr>
        <th>Id</th>
        <th>Name</th>
      </tr>
      <tr
        v-for="dummy of dummies"
        :key="dummy.id"
      >
        <td>{{ dummy.id }}</td>
        <td>{{ dummy.name }}</td>
      </tr>
    </table>
  </div>
  <div v-else>
    <p>No dummy to show</p>
  </div>
</template>

<script>
export default {
  name: 'DummyTable',
  props: {
    dummies: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {};
  },
};

</script>

<style scoped>
table {
  margin: auto;
  width: 100%;
  text-align: center;
}

table th {
    padding: 5px;
    background-color: lightgray;
}

table tr:nth-child(odd){
    background-color: #eeeded;
}

table, th, td {
  border: 1px solid gray;
  border-collapse: collapse;
}

</style>
