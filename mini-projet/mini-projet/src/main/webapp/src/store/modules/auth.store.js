export default (http, router, authService, userService) => ({
  namespaced: true,
  state: {
    user: null,
    loading: false,
    requires2FA: false,
    currentAccount: null,
    qrCode: null,
    secret: null,
    jwt: null,
    users: [
      {
        id: 1, name: 'Alice', email: 'alice@example.com', role: 'Admin',
      },
      {
        id: 2, name: 'Bob', email: 'bob@example.com', role: 'User',
      },
      {
        id: 3, name: 'Charlie', email: 'charlie@example.com', role: 'User',
      },
      {
        id: 4, name: 'David', email: 'david@example.com', role: 'Admin',
      },
      {
        id: 5, name: 'Emma', email: 'emma@example.com', role: 'User',
      },
      {
        id: 6, name: 'Frank', email: 'frank@example.com', role: 'User',
      },
      {
        id: 7, name: 'Grace', email: 'grace@example.com', role: 'Admin',
      },
      {
        id: 8, name: 'Hannah', email: 'hannah@example.com', role: 'User',
      },
      {
        id: 9, name: 'Ian', email: 'ian@example.com', role: 'User',
      },
      {
        id: 10, name: 'Jane', email: 'jane@example.com', role: 'Admin',
      },
      {
        id: 11, name: 'Kevin', email: 'kevin@example.com', role: 'User',
      },
      {
        id: 12, name: 'Alice', email: 'alice@example.com', role: 'Admin',
      },
      {
        id: 13, name: 'Bohb', email: 'bob@example.com', role: 'User',
      },
      {
        id: 14, name: 'Charlhie', email: 'charlie@example.com', role: 'User',
      },
      {
        id: 15, name: 'David', email: 'david@example.com', role: 'Admin',
      },
      {
        id: 16, name: 'Emmha', email: 'emma@example.com', role: 'User',
      },
      {
        id: 17, name: 'Frahnk', email: 'frank@example.com', role: 'User',
      },
      {
        id: 18, name: 'Grahce', email: 'grace@example.com', role: 'Admin',
      },
      {
        id: 19, name: 'Hahnnah', email: 'hannah@example.com', role: 'User',
      },
      {
        id: 20, name: 'Iahn', email: 'ian@example.com', role: 'User',
      },
      {
        id: 21, name: 'Jane', email: 'jane@example.com', role: 'Admin',
      },
      {
        id: 22, name: 'Kevin', email: 'kevin@example.com', role: 'User',
      },
      // Ajoutez autant d'utilisateurs que nécessaire pour vos tests
    ],
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
    // Getter pour obtenir la liste complète des utilisateurs
    allUsers: (state) => state.users,
  },
  mutations: {
    /* loading(state) {
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
    }, */
    setLoading(state, isLoading) {
      state.loading = isLoading;
    },
    loginSuccess(state, user) {
      console.log('Mutation loginSuccess called with user:', user);
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
    updateUser(state, user) {
      state.user = user;
      console.log(' updateUser :', state.user);
    },
    removeUserFromList(state, userId) {
      if (state.users) {
        state.users = state.users.filter((user) => user.id !== userId);
      }
    },
    clearUser(state) {
      state.user = null;
    },
    setUser(state, user) {
      state.user = user;
    },
    setUsers(state, users) {
      state.users = users;
    },
    updateUserInList(state, updatedUser) {
      if (state.users) {
        const index = state.users.findIndex((user) => user.id === updatedUser.id);
        if (index !== -1) {
          state.users.splice(index, 1, updatedUser);
        }
      }
    },
  },
  actions: {
    /* async login(context, {
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
    }, */

    async login(context, { email, password }) {
      console.log('email', email);
      console.log('password', password);
      context.commit('setLoading', true);
      try {
        const result = await authService.login(email, password);
        console.log('result0', result);
        if (result === '2FA_REQUIRED') {
          console.log('result', result);
          context.commit('setRequires2FA', true);
          context.commit('setCurrentAccount', email);
          context.commit('setLoading', false);
        } else {
          console.log('result2', result);
          const jwt = `Bearer ${result}`;
          const httpCopy = http;
          httpCopy.defaults.headers.common.Authorization = jwt;
          localStorage.setItem('jwt', jwt);
          console.log('test');
          const user = await userService.getCurrentUser();
          console.log('testons', user);
          context.commit('loginSuccess', user);
          context.commit('setLoading', false);
          router.push('/profile');
        }
        return result;
      } catch (e) {
        context.commit('loginFailed');
        context.commit('setLoading', false);
        throw e;
      }
    },

    async verifyTOTP(context, { totpCode }) {
      context.commit('setLoading', true);
      try {
        console.log('totpCode', totpCode);

        const jwt = `Bearer ${await authService.verifyTOTP(context.getters.currentAccount, context.getters.secret, totpCode)}`;
        console.log('jwt', jwt);
        const httpCopy = http;
        httpCopy.defaults.headers.common.Authorization = jwt;
        console.log('jwt', jwt);
        if (jwt !== 'Bearer Invalid TOTP code') {
          localStorage.setItem('jwt', jwt);
          const user = await userService.getCurrentUser();
          console.log('testonsTotp', user);
          context.commit('loginSuccess', user);
          router.push('/profile');
        }

        return jwt;
      } catch (e) {
        context.commit('setLoading', false);
        throw e;
      }
    },

    async logout(context) {
      context.commit('logout');
    },

    /* async register(context, {
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
        // router.push('/login');
      } catch (error) {
        context.commit('setLoading', false);
        throw error;
      }
    }, */

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
    async deleteUser({ commit }, userId) {
      commit('setLoading', true);
      try {
        await userService.deleteUser(userId);
        commit('clearUser');
        commit('removeUserFromList', userId);
        commit('setLoading', false);
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },
    async updateUser({ commit }, updatedUser) {
      commit('setLoading', true);
      try {
        const user = await userService.updateUser(updatedUser);
        commit('setUser', user);
        commit('updateUserInList', user);
        commit('setLoading', false);
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },
  },
});
