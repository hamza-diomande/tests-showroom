<template>
  <HomeLayout>
    <div class="container my-4">
      <div class="row">
        <div class="col-12 col-md-4 mb-3" v-for="feature in features" :key="feature.title">
          <ClickableCard
            :title="feature.title"
            :iconClass="feature.iconClass"
            :link="feature.link"
            :description="feature.description"
            @click="selectFeature(feature)"
          />
        </div>
      </div>
    </div>
  </HomeLayout>
</template>

<script>
import ClickableCard from '../components/ClickableCard.vue';
import HomeLayout from '../layouts/HomeLayout.vue';

export default {
  name: 'HomeView2',
  components: {
    ClickableCard,
    HomeLayout,
  },
  data() {
    return {
      features: [
        {
          title: 'Authentification',
          iconClass: 'bi bi-key',
          link: '/login',
          description: 'Pour cette fonctionnalité nous avons mis en place une authentification 2 facteurs Totp et une authentification simple',
        },
        { title: 'Captcheck', iconClass: 'bi bi-shield-shaded captcha-icon', link: '/users' },
        { title: 'Document', iconClass: 'bi bi-file-text', link: '/feature3' },
        { title: 'Fonctionnalité 4', iconClass: 'bi bi-camera', link: '/' },
        { title: 'Fonctionnalité 5', iconClass: 'bi bi-cloud', link: '/' },
        { title: 'Fonctionnalité 6', iconClass: 'bi bi-cup', link: '/' },
      ],
    };
  },
  methods: {
    selectFeature(feature) {
      this.$store.dispatch('home/selectFeature', feature); // this.$router.push(feature.link);
    },
  },
};
</script>

<style>
@import 'bootstrap/dist/css/bootstrap.min.css';
@import 'bootstrap-icons/font/bootstrap-icons.css';
</style>
