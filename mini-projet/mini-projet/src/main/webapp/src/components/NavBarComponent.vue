<!-- src/components/NavBarComponent2.vue -->
<template>
  <b-navbar toggleable="md" class="custom-navbar">
    <b-navbar-brand href="#">NavBar</b-navbar-brand>
    <b-navbar-toggle target="nav-collapse" class="custom-navbar-toggle" />
    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item to="/">Home</b-nav-item>
        <b-nav-item v-for="item in navItems" :key="item.title" :to="item.link">
          {{ item.title }}
        </b-nav-item>
      </b-navbar-nav>
      <!-- Right aligned nav items -->
      <b-navbar-nav class="ms-auto mb-2 mb-lg-0">
        <b-nav-item-dropdown text="Lang" right>
          <b-dropdown-item href="#">EN</b-dropdown-item>
          <b-dropdown-item href="#">ES</b-dropdown-item>
          <b-dropdown-item href="#">RU</b-dropdown-item>
          <b-dropdown-item href="#">FA</b-dropdown-item>
        </b-nav-item-dropdown>
        <b-nav-item-dropdown right>
          <!-- Using 'button-content' slot -->
          <template #button-content>
            <em>User</em>
          </template>
          <b-dropdown-item href="#">Profile</b-dropdown-item>
          <b-dropdown-item href="#" @click="disconnect">Sign Out</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'NavBarComponent',
  computed: {
    ...mapGetters(['navItems']),
  },
  methods: {
    disconnect() {
      this.$store.dispatch('auth/logout')
        .then(() => this.$router.push('login'));
    },
  },
};
</script>

<style scoped>
.custom-navbar {
  height: 50px; /* Adjust the height as needed */
  background-color: #4976b4; /* Set the desired background color */
}

.custom-navbar .navbar-brand,
.custom-navbar .nav-link,
.custom-navbar .dropdown-item,
.custom-navbar .b-nav-item-dropdown > .dropdown-toggle {
  line-height: 50px; /* Adjust to vertically center the text */
  color: white; /* Set text color to white for better contrast */
}

.custom-navbar .nav-link:hover,
.custom-navbar .dropdown-item:hover {
  color: #d4e1f5; /* Set a hover color for better visibility */
}

.custom-navbar-toggle {
  height: 50px; /* Match the height of the navbar */
  display: flex;
  align-items: center; /* Center toggle button vertically */
}

@media (max-width: 767.98px) {
  .custom-navbar .navbar-collapse {
    background-color: #4976b4; /* Match the background color */
  }
}
</style>
