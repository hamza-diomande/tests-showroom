import AService from '@/services/AService';

export default class User2FaService extends AService {
  async getCurrentUser() {
    const response = await this.http.get(`${this.baseUrl}/api/users2Fa/current`);
    return response.data;
  }
}
