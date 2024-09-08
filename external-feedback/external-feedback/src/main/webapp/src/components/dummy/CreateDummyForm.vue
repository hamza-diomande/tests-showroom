<template>
  <form>
    <label for="dummy-name">Name (min5, max 50)</label>
    <input
      id="dummy-name"
      v-model="form.name"
      type="text"
    >

    <button
      type="button"
      @click="createDummy"
    >
      Create
    </button>

    <p
      v-if="form.error"
      class="error"
    >
      {{ form.error }}
    </p>
  </form>
</template>

<script>
export default {
  name: 'CreateDummyForm',
  emits: ['onCreateDummy'],
  data() {
    return {
      form: {
        name: '',
        error: '',
      },
    };
  },
  methods: {
    async createDummy() {
      try {
        const created = await this.$dummyService.create(this.form.name, this.form.privateField);
        if (created) {
          this.$emit('onCreateDummy', created);
          this.form.name = '';
        }
      } catch (err) {
        this.form.error = err.response.data;
      }
    },
  },
};
</script>

<style scoped>
form{
  margin: 10px 0;
}
input, label{
  margin-right: 10px;
}

.error {
  color: red;
}
</style>
