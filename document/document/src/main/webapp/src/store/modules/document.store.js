export default (http, router, documentService) => ({
  state: {
    downloading: false, // Indicateur de téléchargement en cours
    error: null, // Message d'erreur en cas d'échec de téléchargement
  },
  mutations: {
    setDownloading(state, downloading) {
      state.downloading = downloading;
    },
    setError(state, error) {
      state.error = error;
    },
  },
  actions: {
    async downloadDocument({ commit }, { type }) {
      commit('setDownloading', true);
      let success = false;
      try {
        if (type === 'csv') {
          success = await documentService.getCsvDocument();
        } else if (type === 'pdf') {
          success = await documentService.getPdfDocument();
        } else {
          success = await documentService.getExcelDocument();
        }
        if (!success) {
          throw new Error(`Erreur lors du téléchargement du document ${type}`);
        }
      } catch (error) {
        commit('setError', error.message || 'Une erreur est survenue lors du téléchargement');
        console.error('Erreur de téléchargement :', error);
      } finally {
        commit('setDownloading', false);
      }
    },
    clearError({ commit }) {
      commit('setError', null);
    },
  },
  getters: {
    downloading: (state) => state.downloading,
    error: (state) => state.error,
  },
});
