import { mount, flushPromises, RouterLinkStub } from '@vue/test-utils';
import {
  describe, it, expect, beforeEach,
} from 'vitest';
import { createStore } from 'vuex';
import UserView from '../../../src/views/UsersView.vue';
import FilterComponent from '../../../src/components/FilterComponent.vue';
import PaginationComponent from '../../../src/components/PaginationComponent.vue';
import DataTable from '../../../src/components/DataTableComponent.vue';

describe('UserView', () => {
  let wrapper;
  let store;

  beforeEach(async () => {
    // Création d'un store Vuex simulé avec une mutation pour mettre à jour les utilisateurs
    store = createStore({
      modules: {
        auth: {
          namespaced: true,
          state: {
            users: [
              {
                id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin',
              },
              {
                id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'User',
              },
              {
                id: 3, name: 'Alice Johnson', email: 'alice@example.com', role: 'User',
              },
              {
                id: 4, name: 'Bob Brown', email: 'bob@example.com', role: 'Admin',
              },
              {
                id: 5, name: 'Charlie Davis', email: 'charlie@example.com', role: 'User',
              },
              {
                id: 6, name: 'Daisy Evans', email: 'daisy@example.com', role: 'Admin',
              },
              {
                id: 7, name: 'Eva Green', email: 'eva@example.com', role: 'User',
              },
              {
                id: 8, name: 'Frank Harris', email: 'frank@example.com', role: 'Admin',
              },
              {
                id: 9, name: 'Grace King', email: 'grace@example.com', role: 'User',
              },
              {
                id: 10, name: 'Hannah Lee', email: 'hannah@example.com', role: 'User',
              },
              {
                id: 11, name: 'Ian Moore', email: 'ian@example.com', role: 'Admin',
              },
            ],
          },
          getters: {
            allUsers: (state) => state.users,
          },
          mutations: {
            SET_USERS(state, users) {
              state.users = users;
            },
          },
        },
      },
    });

    // Montage du composant UserView
    wrapper = mount(UserView, {
      global: {
        plugins: [store],
        stubs: {
          RouterLink: RouterLinkStub,
        },
      },
    });

    await flushPromises(); // Assurez-vous que les promises sont résolues
  });

  it('renders the FilterComponent', () => {
    expect(wrapper.findComponent(FilterComponent).exists()).toBe(true);
  });

  it('renders the TableComponent', () => {
    expect(wrapper.findComponent(DataTable).exists()).toBe(true);
  });

  it('renders the PaginationComponent', () => {
    expect(wrapper.findComponent(PaginationComponent).exists()).toBe(true);
  });

  it('filters users based on search query', async () => {
    const filterComponent = wrapper.findComponent(FilterComponent);
    await filterComponent.vm.$emit('update:search-query', 'Jane');
    await flushPromises();

    const tableComponent = wrapper.findComponent(DataTable);
    expect(tableComponent.props().data).toHaveLength(1);
    expect(tableComponent.props().data[0].name).toBe('Jane Smith');
  });

  it('sorts users by column', async () => {
    const tableComponent = wrapper.findComponent(DataTable);
    await tableComponent.vm.$emit('sort', 'name');
    await flushPromises();

    // Assurez-vous que la logique de tri est correctement appliquée
    expect(wrapper.vm.sortByKey).toBe('name');
    expect(wrapper.vm.sortDirection).toBe('asc'); // Vérifiez la direction du tri (cela dépend de la logique de tri)
  });

  it('paginates users correctly', async () => {
    const paginationComponent = wrapper.findComponent(PaginationComponent);

    // Vérifie l'état initial
    expect(wrapper.vm.currentPage).toBe(1);

    // Simule un clic pour aller à la page 2
    await paginationComponent.vm.$emit('go-to-page', 2);
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(2);

    // Simule un clic pour aller à la page précédente
    await paginationComponent.vm.$emit('prev-page');
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(1);

    // Simule un clic pour aller à la page suivante
    await paginationComponent.vm.$emit('next-page');
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(2);
  });
  it('paginates users correctly with a single page', async () => {
    // Modifier les données du store pour avoir une seule page
    store.commit('auth/SET_USERS', [
      {
        id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin',
      },
    ]);

    // Remonter le composant UserView avec les nouvelles données
    await wrapper.vm.$nextTick(); // Assurez-vous que le composant se met à jour

    const paginationComponent = wrapper.findComponent(PaginationComponent);

    // Essayer de passer à la page suivante quand il n'y a qu'une seule page
    await paginationComponent.vm.$emit('next-page');
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(1); // On devrait rester sur la page 1

    // Essayer de revenir à la page précédente quand on est déjà sur la première page
    await paginationComponent.vm.$emit('prev-page');
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(1); // On devrait rester sur la page 1
  });
});

/* import { mount, flushPromises } from '@vue/test-utils';
import {
  describe, it, expect, beforeEach,
} from 'vitest';

import { createStore } from 'vuex';
import { createRouter, createWebHistory } from 'vue-router';
// import { createApp } from 'vue';
import FilterComponent from '../../../src/components/FilterComponent.vue';
import TableComponent from '../../../src/components/TableComponent.vue';
import PaginationComponent from '../../../src/components/PaginationComponent.vue';
import UserView from '../../../src/views/UsersView.vue';

describe('UserView', () => {
  let wrapper;
  let store;
  let router;

  beforeEach(async () => {
    // Création d'un store Vuex simulé
    store = createStore({
      modules: {
        auth: {
          namespaced: true,
          getters: {
            allUsers: () => [
              {
                id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin',
              },
              {
                id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'User',
              },
              // Ajoutez plus de données utilisateur ici pour tester
            ],
          },
        },
      },
    });

    // Création d'un routeur simulé
    router = createRouter({
      history: createWebHistory(),
      routes: [],
    });

    // Montage du composant UserView
    wrapper = mount(UserView, {
      global: {
        plugins: [store, router],
      },
    });

    await flushPromises(); // Assurez-vous que les promises sont résolues
  });

  it('renders the FilterComponent', () => {
    expect(wrapper.findComponent(FilterComponent).exists()).toBe(true);
  });

  it('renders the TableComponent', () => {
    expect(wrapper.findComponent(TableComponent).exists()).toBe(true);
  });

  it('renders the PaginationComponent', () => {
    expect(wrapper.findComponent(PaginationComponent).exists()).toBe(true);
  });

  it('filters users based on search query', async () => {
    const filterComponent = wrapper.findComponent(FilterComponent);
    await filterComponent.setProps({ 'v-model:search-query': 'Jane' });
    await flushPromises();

    const tableComponent = wrapper.findComponent(TableComponent);
    expect(tableComponent.props().data).toHaveLength(1);
    expect(tableComponent.props().data[0].name).toBe('Jane Smith');
  });

  it('sorts users by column', async () => {
    const tableComponent = wrapper.findComponent(TableComponent);

    // Simule un clic pour trier par "name"
    await tableComponent.vm.$emit('sort', 'name');
    await flushPromises();

    // Vérifie le tri (la logique de tri devrait être testée plus en détail)
    expect(wrapper.vm.sortByKey).toBe('name');
    expect(wrapper.vm.sortDirection).toBe('asc'); // Vérifiez la direction du tri
  });

  it('paginates users correctly', async () => {
    // Teste la pagination
    const paginationComponent = wrapper.findComponent(PaginationComponent);
    // Change la page
    await paginationComponent.vm.$emit('go-to-page', 2);
    await flushPromises();

    expect(wrapper.vm.currentPage).toBe(2);

    // Teste la fonction de page précédente
    await paginationComponent.vm.$emit('prev-page');
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(1);

    // Teste la fonction de page suivante
    await paginationComponent.vm.$emit('next-page');
    await flushPromises();
    expect(wrapper.vm.currentPage).toBe(2);
  });
});
*/
