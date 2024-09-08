<template>
  <OnlineLayout>
    <div class="home">
      <img alt="Vue logo" src="../assets/logo.png">
      <HelloWorld msg="Welcome to Your Vue.js App" />
    </div>
  </OnlineLayout>

</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue';
import OnlineLayout from '@/layouts/OnlineLayout.vue';

export default {
  name: 'HomeView',
  components: {
    OnlineLayout, HelloWorld,
  },
};
</script>
