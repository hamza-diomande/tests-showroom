<template>
  <div class="container mt-4">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">Liste des utilisateurs</h2>
      </div>
      <div class="card-body">
        <!--Composant de filtrage-->
        <FilterComponent
          v-model:search-query="searchQuery" />

        <!--Composant Tableau-->
        <DataTable
          :columns="tableColumns"
          :data="filteredAndSortedUsers"
          :sortByKey="sortByKey"
          :sortDirection="sortDirection"
          @sort="handleSort"
          :showActionsColumn="true"
          @edit="editUser"
          @delete="deleteUser" />

        <!-- Composant de Pagination -->
        <PaginationComponent
          :currentPage="currentPage"
          :totalPages="totalPages"
          @prev-page="prevPage"
          @next-page="nextPage"
          @go-to-page="goToPage" />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import DataTable from '../components/DataTableComponent.vue';
import PaginationComponent from '../components/PaginationComponent.vue';
import FilterComponent from '../components/FilterComponent.vue';

export default {
  name: 'UserView',
  components: {
    FilterComponent,
    PaginationComponent,
    DataTable,
  },
  data() {
    return {
      searchQuery: '',
      sortByKey: '',
      sortDirection: 'asc',
      perPage: 10,
      currentPage: 1,
      tableColumns: [
        { key: 'name', label: 'Nom' },
        { key: 'email', label: 'Email' },
        { key: 'role', label: 'Rôle' },
      ],
    };
  },
  computed: {

    ...mapGetters('auth', ['allUsers']),
    filteredAndSortedUsers() {
      if (!this.allUsers) return [];

      // eslint-disable-next-line vue/max-len
      const filteredUsers = this.allUsers.filter((user) => user.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        || user.email.toLowerCase().includes(this.searchQuery.toLowerCase())
        || user.role.toLowerCase().includes(this.searchQuery.toLowerCase()));

      if (this.sortByKey) {
        filteredUsers.sort((a, b) => {
          const modifier = this.sortDirection === 'desc' ? -1 : 1;
          if (a[this.sortByKey] < b[this.sortByKey]) return -1 * modifier;
          if (a[this.sortByKey] > b[this.sortByKey]) return 1 * modifier;
          return 0;
        });
      }

      const startIndex = (this.currentPage - 1) * this.perPage;
      return filteredUsers.slice(startIndex, startIndex + this.perPage);
    },
    totalPages() {
      if (!this.allUsers) return 0;
      // eslint-disable-next-line vue/max-len
      const filteredUsers = this.allUsers.filter((user) => user.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      || user.email.toLowerCase().includes(this.searchQuery.toLowerCase())
      || user.role.toLowerCase().includes(this.searchQuery.toLowerCase()));
      return Math.ceil(filteredUsers.length / this.perPage);
    },

  },
  methods: {
    handleSort(key) {
      if (this.sortByKey === key) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortByKey = key;
        this.sortDirection = 'asc';
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage += 1;
      }
    },
    goToPage(page) {
      this.currentPage = page;
    },
    async editUser(userId) {
      try {
        // eslint-disable-next-line no-restricted-globals, no-alert
        const confirmed = confirm('Êtes-vous sûr de vouloir modifier cet utilisateur ?');
        if (confirmed) {
          const userData = await this.$store.dispatch('user/fetchUserById', userId);
          console.log('Données utilisateur récupérées :', userData);

          if (userData) {
            this.$router.push({
              name: 'userForm',
              params: { id: userId },
            });
          } else {
            this.verificationError = 'Les données de l\'utilisateur sont introuvables.';
          }
        }
      } catch (error) {
        console.error('Erreur lors de la tentative de modification de l\'utilisateur :', error);
        this.verificationError = 'Une erreur est survenue pendant la tentative de modification. Veuillez réessayer.';
      }
    },
    /* async editUser(userId) {
      try {
        console.log('userId', userId);
        // Confirmer l'action de modification
        // eslint-disable-next-line no-restricted-globals, no-alert
        const confirmed = confirm('Êtes-vous sûr de vouloir modifier cet utilisateur ?');
        if (confirmed) {
        // Récupérer les données de l'utilisateur à partir du store avant de rediriger
          const response = await this.$store.dispatch('user/fetchUserById', userId);
          console.log('value', response);
        }
      } catch (error) {
        console.error('Erreur lors de la tentative de modification de l\'utilisateur :', error);
        this.verificationError = 'Une erreur est survenue pendant la tentative de modification';
      }
    }, */
    async deleteUser(userId) {
      try {
        // eslint-disable-next-line no-restricted-globals, no-alert
        const confirmed = confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?');
        if (confirmed) {
          console.log('on va supprimer');
          const success = await this.$store.dispatch('auth/deleteUser', userId);
          if (success) {
            this.$router.push('/users'); // Redirige ou met à jour l'affichage après la suppression
          }
        }
      } catch (error) {
        console.error('Error during user deletion:', error);
        this.verificationError = 'Une erreur est survenue pendant la suppression';
      }
    },
  },
};
</script>

<!--<template>
  <div class="container mt-4">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">Liste des utilisateurs</h2>
      </div>
      <div class="card-body">-->
        <!-- Barre de recherche -->
       <!-- <div class="form-group">
          <label for="search">Recherche</label>
          <input id="search" type="text" class="form-control" v-model="searchQuery" placeholder="Recherche...">
        </div>-->
        <!-- Tableau des utilisateurs -->
      <!-- <table class="table table-bordered table-striped">-->
          <!-- Header du tableau -->
         <!-- <thead class="thead-dark">
            <tr>
              <th @click="sortBy('name')" :class="{ sorted: sortByKey === 'name', desc: sortByKey === 'name' && sortDirection === 'desc' }">Nom</th>
              <th @click="sortBy('email')" :class="{ sorted: sortByKey === 'email', desc: sortByKey === 'email' && sortDirection === 'desc' }">Email</th>
              <th @click="sortBy('role')" :class="{ sorted: sortByKey === 'role', desc: sortByKey === 'role' && sortDirection === 'desc' }">Rôle</th>-->
              <!-- Ajoutez d'autres colonnes pour les critères de tri supplémentaires -->
          <!--  </tr>
          </thead>-->
          <!-- Corps du tableau -->
         <!-- <tbody>
            <tr v-for="user in paginatedUsers" :key="user.id">
              <td>{{ user.name }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.role }}</td>-->
              <!-- Ajoutez d'autres colonnes selon les données de l'utilisateur -->
           <!-- </tr>
          </tbody>
        </table> -->

        <!-- Pagination -->
     <!--   <nav aria-label="Pagination">
          <ul class="pagination justify-content-end">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="prevPage">Précédent</a>
            </li>
            <li class="page-item" v-for="page in pages" :key="page" :class="{ active: currentPage === page }">
              <a class="page-link" href="#" @click.prevent="goToPage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <a class="page-link" href="#" @click.prevent="nextPage">Suivant</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>-->

<!-- <script>
import { mapGetters } from 'vuex';

export default {
  name: 'UserView',
  data() {
    return {
      searchQuery: '',
      sortByKey: '',
      sortDirection: 'asc',
      perPage: 10,
      currentPage: 1,
    };
  },
  computed: {
    ...mapGetters('auth', ['allUsers']),
    // Calcul de la liste d'utilisateurs paginée
    paginatedUsers() {
      if (!this.allUsers) return []; // Vérifie que this.allUsers est défini
      // Filtrage par recherche
      const filteredUsers = this.allUsers.filter((user) => user.name.toLowerCase()
        .includes(this.searchQuery.toLowerCase())
        || user.email.toLowerCase().includes(this.searchQuery.toLowerCase())
        || user.role.toLowerCase().includes(this.searchQuery.toLowerCase()));

      // Tri par colonne si une colonne est sélectionnée
      if (this.sortByKey) {
        filteredUsers.sort((a, b) => {
          const modifier = this.sortDirection === 'desc' ? -1 : 1;
          if (a[this.sortByKey] < b[this.sortByKey]) return -1 * modifier;
          if (a[this.sortByKey] > b[this.sortByKey]) return 1 * modifier;
          return 0;
        });
      }

      // Pagination
      const startIndex = (this.currentPage - 1) * this.perPage;
      return filteredUsers.slice(startIndex, startIndex + this.perPage);
    },
    // Calcul du nombre total de pages
    totalPages() {
      if (!this.allUsers) return 0; // Gérer le cas où this.allUsers n'est pas défini
      const filteredUsers = this.allUsers.filter((user) => user.name.toLowerCase()
        .includes(this.searchQuery.toLowerCase())
        || user.email.toLowerCase().includes(this.searchQuery.toLowerCase())
        || user.role.toLowerCase().includes(this.searchQuery.toLowerCase()));
      return Math.ceil(filteredUsers.length / this.perPage);
    },
    // Génération des numéros de page pour la pagination
    pages() {
      const pagesArray = [];
      for (let page = 1; page <= this.totalPages; page += 1) {
        pagesArray.push(page);
      }
      return pagesArray;
    },
  },
  methods: {
    // Fonction de tri
    sortBy(key) {
      if (this.sortByKey === key) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortByKey = key;
        this.sortDirection = 'asc';
      }
    },
    // Fonction pour aller à la page précédente
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1;
      }
    },
    // Fonction pour aller à la page suivante
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage += 1;
      }
    },
    // Fonction pour aller à une page spécifique
    goToPage(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style scoped>
/* Styles spécifiques au composant */
thead {
  background-color: #343a40; /* Couleur de fond du header du tableau */
  color: white; /* Couleur du texte du header du tableau */
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: #f8f9fa; /* Couleur de fond des lignes impaires du corps du tableau */
}

.sorted {
  background-color: #6c757d; /* Couleur de fond pour les entêtes triées */
}

th.sorted.desc::after {
  content: ' ▼'; /* Flèche descendante pour indiquer le tri décroissant */
}

th.sorted.asc::after {
  content: ' ▲'; /* Flèche ascendante pour indiquer le tri croissant */
}
</style> -->
