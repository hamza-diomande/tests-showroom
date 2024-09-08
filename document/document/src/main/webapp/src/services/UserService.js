import AService from '@/services/AService';

export default class UserService extends AService {
  async getCurrentUser() {
    const response = await this.http.get(`${this.baseUrl}/api/users/current`);
    return response.data;
  }

  async getAllUsers() {
    const response = await this.http.get(`${this.baseUrl}/api/users`);
    return response.data;
  }
}
