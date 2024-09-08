<template>
  <div class="form-group">
    <label for="search">Recherche</label>
    <input
      type="text"
      class="form-control"
      v-model="localSearchQuery"
      @input="updateSearch"
      placeholder="Recherche..."
    />
  </div>
</template>

<script>
export default {
  name: 'FilterComponent',
  props: {
    searchQuery: {
      type: [String, null],
      required: true,
    },
  },
  data() {
    return {
      localSearchQuery: this.searchQuery,
    };
  },
  watch: {
    searchQuery(newQuery) {
      this.localSearchQuery = newQuery;
    },
  },
  methods: {
    updateSearch() {
      this.$emit('update:searchQuery', this.localSearchQuery);
    },
  },
};
</script>
