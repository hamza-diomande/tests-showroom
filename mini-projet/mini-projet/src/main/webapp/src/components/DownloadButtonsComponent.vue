<template>
  <div class="btn-group mb-3" role="group">
    <Button @click="downloadCsvDocument" class="btn btn-primary me-2" type="button">Télécharger CSV</Button>
    <Button @click="downloadExcelDocument" class="btn btn-primary me-2" type="button">Télécharger Excel</Button>
    <Button @click="downloadPdfDocument" class="btn btn-primary me-2" type="button">Télécharger PDF</Button>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import Button from 'primevue/button';

export default {
  name: 'DownloadButtons',
  components: { Button },
  methods: {
    ...mapActions(['downloadDocument']),
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
};
</script>

  <style scoped>
  /* Aucun style spécifique nécessaire pour Bootstrap */
  </style>
