export default (store) => (to) => {
  if (to.meta.requiresNotAuth && store.state.auth.user) {
    return false;
  }

  if (to.meta.requiresAuth && !store.state.auth.user) {
    return 'login';
  }

  return true;
};
