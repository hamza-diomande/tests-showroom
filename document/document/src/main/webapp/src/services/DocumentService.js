import AService from '@/services/AService';

export default class DocumentService extends AService {
  async downloadDocument(type) {
    const endpoint = `${this.baseUrl}/api/genDoc/${type}`;
    const mimeType = DocumentService.getMimeType(type);
    let fileName = `users.${type}`;
    if (type === 'excel') {
      fileName = 'users.xlsx';
    }

    try {
      const response = await this.http.get(endpoint, {
        responseType: 'blob',
      });

      if (response.status !== 200) {
        throw new Error(`Erreur HTTP: ${response.status}`);
      }

      const blob = new Blob([response.data], { type: mimeType });
      DocumentService.createDownloadLink(blob, fileName);
      return true; // Téléchargement réussi
    } catch (error) {
      console.error(`Erreur lors du téléchargement du document ${type} :`, error);
      return false; // Téléchargement échoué
    }
  }

  static getMimeType(type) {
    switch (type) {
      case 'excel':
        return 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
      case 'csv':
        return 'text/csv';
      case 'pdf':
        return 'application/pdf';
      default:
        throw new Error('Type de document non supporté');
    }
  }

  static createDownloadLink(blob, fileName) {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }

  async getExcelDocument() {
    return this.downloadDocument('excel');
  }

  async getCsvDocument() {
    return this.downloadDocument('csv');
  }

  async getPdfDocument() {
    return this.downloadDocument('pdf');
  }
}
