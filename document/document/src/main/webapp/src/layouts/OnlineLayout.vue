<template>
  <div>
    <HeaderComponent />
    <div class="container-fluid">
      <slot />
    </div>
  </div>
</template>
<script>
import HeaderComponent from '@/components/HeaderComponent.vue';

export default {
  name: 'OnlineLayout',
  components: { HeaderComponent },
};
</script>

<style lang="scss" scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  text-align: center;
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
