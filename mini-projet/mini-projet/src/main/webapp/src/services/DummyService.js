import AService from '@/services/AService';

export default class DummyService extends AService {
  async getAll() {
    const response = await this.http.get(`${this.baseUrl}/api/dummy`);
    return response.data;
  }

  async test() {
    const response = await this.http.get(`${this.baseUrl}/api/dummy/test`);
    return response.data;
  }

  async create(name) {
    const body = {
      name,
    };
    const response = await this.http.post(`${this.baseUrl}/api/dummy`, body);
    return response.data;
  }

  async createWithCaptcha(name, captchaSession, captchaAnswer) {
    const body = {
      name,
      captchaSession,
      captchaAnswer,
    };
    const response = await this.http.post(`${this.baseUrl}/api/dummy`, body);
    return response.data;
  }
}
