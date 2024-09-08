// src/utils/TwoFactorUserDTO.js

/**
 * Crée un DTO pour l'utilisateur.
 * @param {Object} data - Les données utilisateur.
 * @param {number} data.id - L'ID de l'utilisateur.
 * @param {string} data.identifier - L'identifiant de l'utilisateur.
 * @param {string} data.firstName - Le prénom de l'utilisateur.
 * @param {string} data.lastName - Le nom de famille de l'utilisateur.
 * @param {string} [data.company] - La société de l'utilisateur (optionnel).
 * @param {string} data.email - L'email de l'utilisateur.
 * @param {boolean} data.auth2Fa - Indicateur de 2FA (authentification à deux facteurs).
 * @param {string} [data.secret] - Le secret pour 2FA (optionnel).
 * @param {'USER_ROLE' | 'ADMIN_ROLE'} data.role - Le rôle de l'utilisateur.
 * @returns {Object} - L'objet DTO.
 */
export function createUserDTO({
  id,
  identifier,
  firstName,
  lastName,
  company = '',
  email,
  auth2Fa,
  secret = '',
  role,
}) {
  return {
    id,
    identifier,
    firstName,
    lastName,
    company,
    email,
    auth2Fa,
    secret,
    role,
  };
}

/**
 * Valide un DTO utilisateur.
 * @param {Object} userDTO - L'objet DTO utilisateur.
 * @returns {boolean} - True si valide, false sinon.
 */
export function validateUserDTO(userDTO) {
  return (
    typeof userDTO.id === 'number'
    && typeof userDTO.identifier === 'string'
    && typeof userDTO.firstName === 'string'
    && typeof userDTO.lastName === 'string'
    && (userDTO.company === undefined || typeof userDTO.company === 'string')
    && typeof userDTO.email === 'string'
    && typeof userDTO.auth2Fa === 'boolean'
    && (userDTO.secret === undefined || typeof userDTO.secret === 'string')
    && ['USER_ROLE', 'ADMIN_ROLE'].includes(userDTO.role)
  );
}
