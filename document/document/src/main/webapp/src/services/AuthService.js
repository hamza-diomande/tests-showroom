import AService from '@/services/AService';

export default class AuthService extends AService {
  async login(email, password) {
    const body = {
      email,
      password,
    };

    const response = await this.http.post(`${this.baseUrl}/api/auth/login`, body);
    return response.data;
  }
}
