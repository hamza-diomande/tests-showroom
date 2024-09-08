export default () => ({
  namespaced: true,
  state: {
    navItems: [],
  },

  getters: {
    navItems: (state) => state.navItems,
  },
  mutations: {
    SET_NAV_ITEMS(state, items) {
      state.navItems = items;
    },
    SET_FEATURE(state, feature) {
      state.navItems = feature.navItems;
    },
  },
  actions: {
    updateNavItems({ commit }, items) {
      commit('SET_NAV_ITEMS', items);
    },
    selectFeature({ commit, getters }, feature) {
      console.log('FEATURE', feature);
      const routes = [
        {
          title: 'Authentification',
          navItems: [
            { title: 'Login', link: '/login' },
            { title: 'Register', link: '/register' },
          ],
        },
        {
          title: 'Captcheck',
          navItems: [
            { title: 'Captcha Test 1', link: '/feature2/sub1' },
            { title: 'Captcha Test 2', link: '/feature2/sub2' },
          ],
        },
        {
          title: 'Document',
          navItems: [
            { title: 'Docs 1', link: '/feature3/sub1' },
            { title: 'Docs 2', link: '/feature3/sub2' },
          ],
        },
      ];

      const selectedFeature = routes.find((r) => r.title === feature.title);
      if (selectedFeature) {
        commit('SET_FEATURE', selectedFeature);
        console.log('items', getters.navItems);
      }
    },
  },
});
