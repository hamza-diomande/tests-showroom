export default (http, router, authService, userService) => ({
  namespaced: true,
  state: {
    user: null,
    loading: false,
  },
  getters: {},
  mutations: {
    loading(state) {
      state.loading = true;
    },
    loginSuccess(state, user) {
      state.loading = false;
      state.user = user;
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
  },
  actions: {
    async login(context, {
      email, password, sessionCode, answer,
    }) {
      context.commit('loading');
      try {
        const jwt = `Bearer ${await authService.login(email, password, sessionCode, answer)}`;
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = jwt;
        localStorage.setItem('jwt', jwt);
        const user = await userService.getCurrentUser();
        context.commit('loginSuccess', user);
        router.push('/');
      } catch (e) {
        context.commit('loginFailed', e);
        throw e;
      }
    },

    async register(context, {
      identifier,
      firstName,
      lastName,
      company,
      email,
      role,
      password,
      captchaAnswer,
      captchaSession,
    }) {
      context.commit('setLoading', true);
      try {
        const user = await authService.register(
          identifier,
          firstName,
          lastName,
          company,
          email,
          role,
          password,
          captchaAnswer,
          captchaSession,
        );
        context.commit('registerSuccess', user);
        router.push('/login');
      } catch (error) {
        context.commit('setLoading', false);
        throw error;
      }
    },

    async logout(context) {
      context.commit('logout');
    },

    async recover(context) {
      try {
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = localStorage.getItem('jwt');
        const user = await userService.getCurrentUser();
        context.commit('loginSuccess', user);
        router.push('/');
      } catch (e) {
        console.warn('Failed to recover session');
        context.commit('logout');
      }
    },
  },
});
