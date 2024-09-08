export default (http, router, auth2FaService, user2FaService) => ({
  namespaced: true,
  state: {
    user: null,
    loading: false,
    requires2FA: false,
    qrCode: null,
    secret: null,
    currentAccount: null,
  },
  getters: {
    isAuthenticated: (state) => !!state.user,
    currentUser: (state) => state.user,
    isLoading: (state) => state.loading,
    requires2FA: (state) => state.requires2FA,
    qrCode: (state) => state.qrCode,
    secret: (state) => state.secret,
    currentAccount: (state) => state.currentAccount,
  },
  mutations: {
    setLoading(state, isLoading) {
      state.loading = isLoading;
    },
    setUser(state, user) {
      state.user = user;
    },
    setRequires2FA(state, requires2FA) {
      state.requires2FA = requires2FA;
    },
    clearUser(state) {
      state.user = null;
      const httpCopy = http;
      delete httpCopy.defaults.headers.common.Authorization;
      localStorage.removeItem('jwt');
    },
    setQRCode(state, qrCode) {
      state.qrCode = qrCode;
    },
    setSecret(state, secret) {
      state.secret = secret;
    },
    setCurrentAccount(state, account) {
      state.currentAccount = account;
    },
  },
  actions: {
    async login({ commit }, { email, password }) {
      commit('setLoading', true);
      try {
        const sessionCode = await auth2FaService.login(email, password);
        commit('setRequires2FA', true);
        commit('setLoading', false);
        return sessionCode; // Return the session code to be used for TOTP verification
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },

    async verifyTOTP({ commit }, { sessionCode, totpCode }) {
      commit('setLoading', true);
      try {
        const jwt = `Bearer ${await auth2FaService.verifyTOTP(sessionCode, totpCode)}`;
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = jwt;
        localStorage.setItem('jwt', jwt);
        const user = await user2FaService.getCurrentUser();
        commit('setUser', user);
        commit('setLoading', false);
        router.push('/');
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },

    async register({ commit, dispatch }, {
      identifier, firstName, lastName, email, password, company,
    }) {
      commit('setLoading', true);
      try {
        const user = await auth2FaService.register(
          identifier,
          firstName,
          lastName,
          email,
          password,
          company,
        );
        console.log(user);
        commit('setUser', user);
        commit('setCurrentAccount', email);
        await dispatch('setup'); // No need to pass account as it's already in state
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },

    async setup({ commit, state }) {
      commit('setLoading', true);
      try {
        const { secret, qrCode } = await auth2FaService.setup(state.currentAccount);
        commit('setSecret', secret);
        commit('setQRCode', qrCode);
        commit('setLoading', false);
        // Redirigez vers la page de visualisation du QR code avec les informations nécessaires
        router.push({ name: 'QrCode' });
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },

    logout({ commit }) {
      commit('clearUser');
      router.push('/login');
    },

    async recover({ commit }) {
      try {
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = localStorage.getItem('jwt');
        const user = await user2FaService.getCurrentUser();
        commit('setUser', user);
        router.push('/');
      } catch (error) {
        console.warn('Failed to recover session', error);
        commit('clearUser');
      }
    },
  },
});
