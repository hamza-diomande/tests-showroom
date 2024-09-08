<template>
  <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link>|
    <router-link to="/dummy">Dummy</router-link>|
    <button type="button" @click="disconnect">Déconnexion</button>
  </nav>
</template>
<script>
export default {
  name: 'HeaderComponent',
  methods: {
    disconnect() {
      this.$store.dispatch('auth/logout')
        .then(() => this.$router.push('login'));
    },
  },
};
</script>
