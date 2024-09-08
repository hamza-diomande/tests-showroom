import AService from '@/services/AService';
import { createUserDTO, validateUserDTO } from '@/utils/TwoFactorUserDTO';

export default class UserService extends AService {
  /**
   * Effectue une requête HTTP.
   * @param {string} method - La méthode HTTP (GET, POST, PUT, DELETE, etc.).
   * @param {string} url - L'URL relative de l'API.
   * @param {Object} [data=null] - Les données envoyées dans le corps
   * de la requête (pour POST, PUT, etc.).
   * @param {Object} [params=null] - Les paramètres de requête ajoutés à l'URL.
   * @returns {Promise<Object>} - La réponse des données de l'API.
   * @throws {Error} - Si la requête échoue.
   */
  async request(method, url, data = null, params = null) {
    try {
      const response = await this.http.request({
        method,
        url: `${this.baseUrl}${url}`,
        data,
        params,
      });
      return response.data;
    } catch (error) {
      console.error(`Request failed: ${method.toUpperCase()} ${url}`, error);
      throw error;
    }
  }

  /**
   * Récupère les informations de l'utilisateur courant.
   * @returns {Promise<Object>} - Les données de l'utilisateur courant.
   * @throws {Error} - Si la requête échoue.
   */
  getCurrentUser() {
    return this.request('get', '/api/users/current');
  }

  /**
   * Active l'authentification à deux facteurs (2FA) pour un utilisateur donné.
   * @param {number} userId - L'ID de l'utilisateur pour lequel activer la 2FA.
   * @returns {Promise<Object>} - Les données de confirmation d'activation de la 2FA.
   * @throws {Error} - Si la requête échoue.
   */
  activate2FA(userId) {
    return this.request('post', '/api/users/activate-2fa', null, { userId });
  }

  /**
   * Désactive l'authentification à deux facteurs (2FA) pour un utilisateur donné.
   * @param {number} userId - L'ID de l'utilisateur pour lequel désactiver la 2FA.
   * @returns {Promise<Object>} - Les données de confirmation de désactivation de la 2FA.
   * @throws {Error} - Si la requête échoue.
   */
  deactivate2FA(userId) {
    return this.request('post', '/api/users/deactivate-2fa', null, { userId });
  }

  /**
   * Enregistre un nouvel utilisateur.
   * @param {Object} userData - Les données de l'utilisateur à enregistrer.
   * @param {string} userData.identifier - L'identifiant unique de l'utilisateur.
   * @param {string} userData.firstName - Le prénom de l'utilisateur.
   * @param {string} userData.lastName - Le nom de famille de l'utilisateur.
   * @param {string} userData.company - L'entreprise de l'utilisateur.
   * @param {string} userData.email - L'adresse e-mail de l'utilisateur.
   * @param {string} userData.role - Le rôle de l'utilisateur.
   * @param {string} userData.password - Le mot de passe de l'utilisateur.
   * @param {string} userData.captchaAnswer - La réponse du CAPTCHA.
   * @param {string} userData.captchaSession - La session du CAPTCHA.
   * @returns {Promise<Object>} - Les données de l'utilisateur enregistré.
   * @throws {Error} - Si la requête échoue.
   */
  async register(userData) {
    const response = await this.request('post', '/api/auth/register', userData);
    return response;
  }

  /**
   * Récupère la liste de tous les utilisateurs.
   * @returns {Promise<Array>} - Un tableau contenant les données de tous les utilisateurs.
   * @throws {Error} - Si la requête échoue.
   */
  getAllUsers() {
    return this.request('get', '/api/users');
  }

  /**
   * Met à jour les informations d'un utilisateur.
   * @param {Object} userDto - Les données de l'utilisateur à mettre à jour.
   * @returns {Promise<Object>} - Les données de l'utilisateur mises à jour.
   * @throws {Error} - Si les données utilisateur ne sont pas valides ou si la requête échoue.
   */
  async updateUser(userDto) {
    if (!validateUserDTO(userDto)) {
      throw new Error('Invalid user data');
    }

    const updatedUser = await this.request('put', '/api/users/update-user', userDto);
    return createUserDTO(updatedUser);
  }

  /**
   * Supprime un utilisateur par ID.
   * @param {number} userId - L'ID de l'utilisateur à supprimer.
   * @returns {Promise<void>} - Une promesse vide si la suppression est réussie.
   * @throws {Error} - Si la requête échoue.
   */
  deleteUser(userId) {
    return this.request('delete', `/api/users/${userId}`);
  }

  /**
   * Récupère les informations d'un utilisateur par ID.
   * @param {number} userId - L'ID de l'utilisateur à récupérer.
   * @returns {Promise<Object>} - Les données de l'utilisateur récupéré.
   * @throws {Error} - Si la requête échoue.
   */
  getUserById(userId) {
    return this.request('get', `/api/users/${userId}`);
  }
}

/* import AService from '@/services/AService';
import { createUserDTO, validateUserDTO } from '@/utils/TwoFactorUserDTO';

export default class UserService extends AService {
  async getCurrentUser() {
    const response = await this.http.get(`${this.baseUrl}/api/users/current`);
    return response.data;
  }

  async activate2FA(userId) {
    try {
      const response = await this.http.post(`${this.baseUrl}/api/users/activate-2fa`, null, {
        params: { userId },
      });
      return response.data;
    } catch (error) {
      console.error('Failed to activate 2FA', error);
      throw error;
    }
  }

  async deactivate2FA(userId) {
    try {
      const response = await this.http.post(`${this.baseUrl}/api/users/deactivate-2fa`, null, {
        params: { userId },
      });
      return response.data;
    } catch (error) {
      console.error('Failed to deactivate 2FA', error);
      throw error;
    }
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

  async getAllUsers() {
    const response = await this.http.get(`${this.baseUrl}/api/users`);
    return response.data;
  }

  async updateUser(userDto) {
    if (!validateUserDTO(userDto)) {
      throw new Error('Invalid user data');
    }

    try {
      const response = await this.http.put(`${this.baseUrl}/api/users/update-user`, userDto);
      return createUserDTO(response.data);
    } catch (error) {
      console.error(`Impossible de mettre à jour les informations
      de l'utilisatur ${userDto.id}`, error);
      throw error;
    }
  }

  /**
   * Supprime un utilisateur par ID.
   * @param {number} userId - L'ID de l'utilisateur à supprimer.
   * @returns {Promise<void>}
   *//*
  async deleteUser(userId) {
    try {
      await this.http.delete(`${this.baseUrl}/api/users/${userId}`);
    } catch (error) {
      console.error(`impossible de supprimer l'utilisateur ${userId} :`, error);
      throw error;
    }
  }

  async getUserById(userId) {
    try {
      const response = await this.http.get(`${this.baseUrl}/api/users/`, null, {
        params: { userId },
      });
      return response.data;
    } catch (error) {
      console.error(`Impossible de trouver l'utilisateur ${userId} :`, error);
      throw error;
    }
  }
} */
