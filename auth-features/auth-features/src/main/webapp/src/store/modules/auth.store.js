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
