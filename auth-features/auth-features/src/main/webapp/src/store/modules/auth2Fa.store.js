export default (http, router, auth2FaService, user2FaService) => ({
  namespaced: true,
  state: {
    user: null,
    loading: false,
    requires2FA: false,
    currentAccount: null,
    qrCode: null,
    secret: null,
    jwt: null,
  },
  getters: {
    isAuthenticated: (state) => !!state.user,
    currentUser: (state) => state.user,
    isLoading: (state) => state.loading,
    requires2FA: (state) => state.requires2FA,
    currentAccount: (state) => state.currentAccount,
    qrCode: (state) => state.qrCode,
    secret: (state) => state.secret,
    jwt: (state) => state.jwt,
  },
  mutations: {
    setLoading(state, isLoading) {
      state.loading = isLoading;
    },
    loginSuccess(state, user) {
      state.user = user;
      state.requires2FA = false;
    },
    loginFailed(state) {
      state.loading = false;
    },

    logout(state) {
      state.user = null;
      const httpCopy = http;
      delete httpCopy.defaults.headers.common.Authorization;
      localStorage.removeItem('jwt');
    },
    registerSuccess(state, user) {
      state.loading = false;
      state.user = user;
      // console.log(user);
    },
    registerFailure(state) {
      state.loading = false;
    },
    setRequires2FA(state, requires2FA) {
      state.requires2FA = requires2FA;
    },
    setCurrentAccount(state, account) {
      state.currentAccount = account;
      // console.log(account);
    },
    setQRCode(state, qrCode) {
      state.qrCode = qrCode;
      console.log('store QR :', state.qrCode);
    },
    setSecret(state, secret) {
      state.secret = secret;
    },
    setJwt(state, jwt) {
      state.jwt = jwt;
    },
  },
  actions: {
    async login(context, { email, password }) {
      console.log('email', email);
      console.log('password', password);
      context.commit('setLoading', true);
      try {
        const result = await auth2FaService.login(email, password);
        console.log('result0', result);
        if (result === '2FA_REQUIRED') {
          console.log('result', result);
          context.commit('setRequires2FA', true);
          context.commit('setCurrentAccount', email);
          context.commit('setLoading', false);
        } else {
          console.log('result2', result);
          const jwt = `Bearer ${result.jwt}`;
          const httpCopy = http;
          httpCopy.defaults.headers.common.Authorization = jwt;
          localStorage.setItem('jwt', jwt);
          const user = await user2FaService.getCurrentUser();
          context.commit('loginSuccess', user);
          context.commit('setLoading', false);
        }
        return result;
      } catch (e) {
        context.commit('loginFailed');
        context.commit('setLoading', false);
        throw e;
      }
    },

    /* sync login(context, {
      email, password, sessionCode, answer,
    }) {
      context.commit('loading');
      try {
        const userLog = await auth2FaService.login(email, password, sessionCode, answer);
        // const httpCopy = http;
        // httpCopy.defaults.headers.common.Authorization = jwt;
        // localStorage.setItem('jwt', jwt);
        const user = await user2FaService.getCurrentUser();
        console.log('User log :', userLog.id);
        console.log('User :', user);
        /* if (user.secret === userLog.secret) {
          context.commit('loginSuccess', user);
        }
        // router.push('/');
      } catch (e) {
        context.commit('loginFailed', e);
        throw e;
      }
    }, */

    async verifyTOTP(context, { totpCode }) {
      context.commit('setLoading', true);
      try {
        console.log('totpCode', totpCode);

        const jwt = `Bearer ${await auth2FaService.verifyTOTP(context.getters.currentAccount, context.getters.secret, totpCode)}`;
        console.log('jwt', jwt);
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = jwt;
        localStorage.setItem('jwt', jwt);
        // const user = await user2FaService.getCurrentUser();
        context.commit('loginSuccess', context.getters.user);
        router.push('/');
        return jwt;
      } catch (e) {
        context.commit('setLoading', false);
        throw e;
      }
    },

    async register(context, {
      email, password, identifier, firstName, lastName, company,
    }) {
      context.commit('setLoading', true);
      try {
        // eslint-disable-next-line vue/max-len
        const user = await auth2FaService.register(identifier, firstName, lastName, email, password, company);
        context.commit('registerSuccess', user);
        context.commit('setCurrentAccount', email);
        // console.log('bonjour');
        // Redirigez vers la page de visualisation du QR code si nécessaire
        // router.push({ name: 'QrCode', params: { account: email } });
      } catch (e) {
        context.commit('setLoading', false);
        throw e;
      }
    },
    async setup(context) {
      console.log('store', context.getters.currentAccount);
      context.commit('setLoading', true);
      try {
        const { secret, qrCode } = await auth2FaService.setup(context.getters.currentAccount);
        context.commit('setQRCode', qrCode);
        context.commit('setSecret', secret);
        console.log('secret :', context.getters.secret);
        console.log('qrCode :', context.getters.qrCode);

        context.commit('setLoading', false);
        // Redirigez vers la page de visualisation du QR code si nécessaire
      } catch (e) {
        context.commit('setLoading', false);
        throw e;
      }
    },

    async logout(context) {
      context.commit('logout');
      router.push('/login');
    },

    async recover(context) {
      try {
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = localStorage.getItem('jwt');
        const user = await user2FaService.getCurrentUser();
        context.commit('loginSuccess', user);
        router.push('/');
      } catch (e) {
        console.warn('Failed to recover session');
        context.commit('logout');
      }
    },
  },
});
