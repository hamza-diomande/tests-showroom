<template>
  <nav aria-label="Pagination">
    <ul class="pagination justify-content-end">
      <!-- Bouton précédent -->
      <li class="page-item" :class="{ disabled: currentPage === 1 }">
        <a class="page-link" href="#" @click.prevent="prevPage" aria-label="Previous">Précédent</a>
      </li>
      <!-- Boutons de pages -->
      <li
        class="page-item"
        v-for="page in pages"
        :key="page"
        :class="{ active: currentPage === page }"
      >
        <a class="page-link" href="#" @click.prevent="goToPage(page)">
          {{ page }}
        </a>
      </li>
      <!-- Bouton suivant -->
      <li class="page-item" :class="{ disabled: currentPage === totalPages }">
        <a class="page-link" href="#" @click.prevent="nextPage" aria-label="Next">Suivant</a>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'PaginationComponent',
  props: {
    currentPage: {
      type: Number,
      required: true,
      validator(value) {
        // Assure que currentPage est entre 1 et totalPages
        return value > 0;
      },
    },
    totalPages: {
      type: Number,
      required: true,
      validator(value) {
        // Assure que totalPages est au moins 1
        return value > 0;
      },
    },
  },
  computed: {
    pages() {
      const pagesArray = [];
      // eslint-disable-next-line no-plusplus
      for (let page = 1; page <= this.totalPages; page++) {
        pagesArray.push(page);
      }
      return pagesArray;
    },
  },
  methods: {
    prevPage() {
      if (this.currentPage > 1) {
        this.$emit('prev-page');
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.$emit('next-page');
      }
    },
    goToPage(page) {
      this.$emit('go-to-page', page);
    },
  },
};
</script>

<style scoped>
.page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
  color: white;
}
.page-item.disabled .page-link {
  pointer-events: none;
  opacity: 0.5;
}
</style>
