import AService from '@/services/AService';

export default class AuthService extends AService {
  /**
   * Effectue une requête HTTP POST avec gestion des erreurs.
   * @param {string} url - L'URL relative pour la requête.
   * @param {Object} body - Le corps de la requête.
   * @returns {Promise<Object>} - Les données de la réponse.
   * @throws {Error} - Si la requête échoue.
   */
  async postRequest(url, body) {
    try {
      const response = await this.http.post(`${this.baseUrl}${url}`, body);
      return response.data;
    } catch (error) {
      console.error(`Error during POST request to ${url}:`, error);
      throw error;
    }
  }

  /**
   * Authentifie un utilisateur avec son adresse e-mail et son mot de passe.
   * @param {string} email - L'adresse e-mail de l'utilisateur.
   * @param {string} password - Le mot de passe de l'utilisateur.
   * @returns {Promise<Object>} - Les données de l'utilisateur authentifié,
   * incluant le token de session.
   * @throws {Error} - Si la requête échoue.
   */
  login(email, password) {
    return this.postRequest('/api/auth/login', { email, password });
  }

  /**
   * Vérifie un code TOTP pour l'authentification à deux facteurs.
   * @param {string} email - L'adresse e-mail de l'utilisateur.
   * @param {string} secret - Le secret TOTP de l'utilisateur.
   * @param {string} totpCode - Le code TOTP généré par l'application de l'utilisateur.
   * @returns {Promise<Object>} - La réponse de l'API après la vérification du TOTP.
   * @throws {Error} - Si la vérification échoue.
   */
  verifyTOTP(email, secret, totpCode) {
    return this.postRequest('/api/auth/verify', { email, secret, totpCode });
  }
}

/* import AService from '@/services/AService';

export default class AuthService extends AService {
  async login(email, password) {
    const body = {
      email,
      password,
    };

    const response = await this.http.post(`${this.baseUrl}/api/auth/login`, body);
    return response.data;
  }

  verifyTOTP(email, secret, totpCode) {
    return this.http.post(`${this.baseUrl}/api/auth/verify`, { email, secret, totpCode })
      .then((response) => response.data)
      .catch((error) => {
        console.error('Error verifying TOTP:', error);
        throw error;
      });
  }
} */
