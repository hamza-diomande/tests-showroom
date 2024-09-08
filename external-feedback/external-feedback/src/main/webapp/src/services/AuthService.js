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

  async register(
    identifier,
    firstName,
    lastName,
    company,
    email,
    role,
    password,
    captchaAnswer,
    captchaSession,
  ) {
    const body = {
      identifier,
      firstName,
      lastName,
      company,
      email,
      role,
      password,
      captchaAnswer,
      captchaSession,
    };

    const response = await this.http.post(`${this.baseUrl}/api/auth/register`, body);
    return response.data;
  }
}
