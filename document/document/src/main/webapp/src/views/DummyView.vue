<template>
  <OnlineLayout>
    <CreateDummyForm @on-create-dummy="updateDummies" />
    <DummyTable :dummies="dummies" />
  </OnlineLayout>
</template>

<script>
import DummyTable from '@/components/dummy/DummyTable.vue';
import CreateDummyForm from '@/components/dummy/CreateDummyForm.vue';
import OnlineLayout from '@/layouts/OnlineLayout.vue';

export default {
  name: 'DummyView',
  components: { OnlineLayout, CreateDummyForm, DummyTable },
  data() {
    return {
      dummies: [],
    };
  },
  mounted() {
    this.updateDummies();
  },
  methods: {
    async updateDummies() {
      this.dummies = await this.$dummyService.getAll();
    },
  },
};
</script>

<style scoped>
#add-button {
  margin: 10px 0;
}
</style>
