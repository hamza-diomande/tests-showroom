export default (store) => (error) => {
  if (error.response.status === 401) {
    store.dispatch('auth/logout');
  }
  return Promise.reject(error);
};
