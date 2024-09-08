<template>
  <OnlineLayout>
    <div class="container">
      <div class="btn-group mb-3" role="group">
        <Button @click="downloadCsvDocument" class="btn btn-btn btn-primary me-2" type="button">Télécharger CSV</Button>
        <Button @click="downloadExcelDocument" class="btn btn-primary me-2" type="button">Télécharger Excel</Button>
        <Button @click="downloadPdfDocument" class="btn btn-primary me-2" type="button">Télécharger PDF</Button>
      </div>

      <div class="table-responsive">
        <table class="table table-striped">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">ID</th>
              <th scope="col">Identifiant</th>
              <th scope="col">Nom</th>
              <th scope="col">Email</th>
              <th scope="col">Role</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(user, index) in users" :key="user.id">
              <th scope="row">{{ index + 1 }}</th>
              <td>{{ user.id }}</td>
              <td>{{ user.identifier }}</td>
              <td>{{ user.lastName }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.role }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </OnlineLayout>
</template>

<script>
import { mapActions } from 'vuex';
import Button from 'primevue/button';
import OnlineLayout from '@/layouts/OnlineLayout.vue';

export default {
  name: 'UsersView',
  components: { Button, OnlineLayout },
  data() {
    return {
      users: [],
    };
  },
  methods: {
    ...mapActions(['downloadDocument']),
    async fetchUsers() {
      try {
        // Assurez-vous que cette méthode utilise votre
        // service utilisateur pour récupérer les utilisateurs
        this.users = await this.$userService.getAllUsers();
      } catch (error) {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    },
    async downloadCsvDocument() {
      await this.downloadDocumentType('csv');
    },
    async downloadExcelDocument() {
      await this.downloadDocumentType('excel');
    },
    async downloadPdfDocument() {
      await this.downloadDocumentType('pdf');
    },
    async downloadDocumentType(type) {
      try {
        await this.downloadDocument({ type });
      } catch (error) {
        console.error('Erreur de téléchargement :', error);
      }
    },
  },
  created() {
    this.fetchUsers();
  },
};
</script>

<style>
/* Aucun style spécifique nécessaire pour Bootstrap */
</style>
