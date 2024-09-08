export default (http, router, userService) => ({
  namespaced: true,
  state: {
    user: null,
    loading: false,
    users: null,
  },
  getters: {
    currentUser: (state) => state.user,
    isLoading: (state) => state.loading,
    allUsers: (state) => state.users,
  },
  mutations: {
    setLoading(state, loading) {
      state.loading = loading;
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
    removeUserFromList(state, userId) {
      if (state.users) {
        state.users = state.users.filter((user) => user.id !== userId);
      }
    },
    clearUser(state) {
      state.user = null;
    },
  },
  actions: {
    async register({ commit }, userData) {
      commit('setLoading', true);
      try {
        const user = await userService.register(userData);
        commit('setUser', user);
        commit('setLoading', false);
        router.push('/users');
      } catch (error) {
        commit('setLoading', false);
        throw error;
      }
    },
    async fetchUserById({ commit }, userId) {
      try {
        const user = await userService.getUserById(userId);
        commit('clearUser');
        commit('setUser', user);
        return user;
      } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
      }
    },
    async fetchAllUsers({ commit }) {
      commit('setLoading', true);
      try {
        const users = await userService.getAllUsers();
        commit('setUsers', users);
        commit('setLoading', false);
      } catch (error) {
        commit('setLoading', false);
        console.error('Error fetching users:', error);
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
  },
});
// export default (http, router, userService) => ({
//   namespaced: true,
//   state: {
//     user: null,
//     loading: false,
//     users: null,
//   },
//   getters: {
//     currentUser: (state) => state.user,
//     isLoading: (state) => state.loading,
//     allUsers: (state) => state.users,
//   },
//   mutations: {
//     setLoading(state, loading) {
//       state.loading = loading;
//     },
//     setUser(state, user) {
//       state.user = user;
//     },
//     setUsers(state, users) {
//       state.users = users;
//     },
//     updateUserInList(state, updatedUser) {
//       if (state.users) {
//         const index = state.users.findIndex((user) => user.id === updatedUser.id);
//         if (index !== -1) {
//           state.users.splice(index, 1, updatedUser);
//         }
//       }
//     },
//     removeUserFromList(state, userId) {
//       if (state.users) {
//         state.users = state.users.filter((user) => user.id !== userId);
//       }
//     },
//     clearUser(state) {
//       state.user = null;
//     },
//   },
//   actions: {
//     async register({ commit }, userData) {
//       commit('setLoading', true);
//       try {
//         const user = await userService.register(userData);
//         commit('setUser', user);
//         commit('setLoading', false);
//         router.push('/users');
//       } catch (error) {
//         commit('setLoading', false);
//         throw error;
//       }
//     },
//     async fetchUserById({ commit }, userId) {
//       try {
//         const user = await userService.getUserById(userId);
//         if (user.id === userId) {
//           commit('clearUser');
//           commit('setUser', user);
//         }
//       } catch (error) {
//         console.error('Error fetching user:', error);
//         throw error;
//       }
//     },
//     async updateUser({ commit }, updatedUser) {
//       commit('setLoading', true);
//       try {
//         const user = await userService.updateUser(updatedUser);
//         commit('setUser', user);
//         commit('updateUserInList', user);
//         commit('setLoading', false);
//       } catch (error) {
//         commit('setLoading', false);
//         throw error;
//       }
//     },
//     async deleteUser({ commit }, userId) {
//       commit('setLoading', true);
//       try {
//         await userService.deleteUser(userId);
//         commit('clearUser');
//         commit('removeUserFromList', userId);
//         commit('setLoading', false);
//       } catch (error) {
//         commit('setLoading', false);
//         throw error;
//       }
//     },
//   },
// });

/* export default (http, router, userService) => ({
  namespaced: true,
  state: {
    user: null,
    loading: false,
    users: null,
  },
  getters: {
    currentUser: (state) => state.user,
    isLoading: (state) => state.loading,
    // Getter pour obtenir la liste complète des utilisateurs
    allUsers: (state) => state.users,
  },
  mutations: {
    registerSuccess(state, user) {
      state.loading = false;
      state.user = user;
      // console.log(user);
    },
    cleanUser(state) {
      state.loading = false;
      state.user = null;
      // console.log(user);
    },
    setUser(state, user) {
      state.user = user;
    },
    registerFailure(state) {
      state.loading = false;
    },
    updateUserSuccess(state, updatedUser) {
      state.loading = false;
      if (state.user && state.user.id === updatedUser.id) {
        state.user = updatedUser;
      }
      if (state.users) {
        const index = state.users.findIndex((user) => user.id === updatedUser.id);
        if (index !== -1) {
          state.users.splice(index, 1, updatedUser);
        }
      }
    },
    deleteUserSuccess(state, userId) {
      state.loading = false;
      if (state.user && state.user.id === userId) {
        state.user = null;
      }
      if (state.users) {
        state.users = state.users.filter((user) => user.id !== userId);
      }
    },
  },
  actions: {
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
        const user = await userService.register(
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
        router.push('/users');
      } catch (error) {
        context.commit('setLoading', false);
        throw error;
      }
    },
    async fetchUserById(context, userId) {
      try {
        const response = await userService.getUserById(userId);
        if (response.id === userId) {
          context.commit('cleanUser');
          context.commit('setUser', response);
        }
      } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
      }
    },
  },
}); */
