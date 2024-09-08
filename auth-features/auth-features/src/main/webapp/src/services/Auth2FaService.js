import AService from '@/services/AService';

export default class Auth2FaService extends AService {
  async login(email, password) {
    const body = {
      email,
      password,
    };

    const response = await this.http.post(`${this.baseUrl}/api/auth2Fa/login`, body);
    return response.data;
  }

  async register(identifier, firstName, lastName, email, password, company) {
    const body = {
      identifier,
      firstName,
      lastName,
      email,
      password,
      company,
    };
    const response = await this.http.post(`${this.baseUrl}/api/auth2Fa/register`, body);
    console.log('response', response);
    return response.data;
  }

  async setup(account) {
    const body = {
      account,
    };
    const response = await this.http.post(`${this.baseUrl}/api/auth2Fa/setup`, body);
    return response.data;
  }

  async verifyTOTP(email, secret, totpCode) {
    const body = {
      email,
      secret,
      totpCode,
    };
    const response = await this.http.post(`${this.baseUrl}/api/auth2Fa/verify`, body);
    return response.data;
  }
}
