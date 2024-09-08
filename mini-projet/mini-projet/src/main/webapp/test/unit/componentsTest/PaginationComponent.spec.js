import { mount } from '@vue/test-utils'; // Importation de la fonction `mount` pour monter le composant.
import { describe, it, expect } from 'vitest'; // Importation des fonctions de test de Vitest.
import PaginationComponent from '../../../src/components/PaginationComponent.vue'; // Importation du composant à tester.

describe('PaginationComponent', () => {
  it('renders pagination buttons correctly', () => {
    const wrapper = mount(PaginationComponent, {
      props: {
        currentPage: 1,
        totalPages: 5,
      },
    });

    // Vérifiez le rendu des éléments de pagination
    // console.log(wrapper.html());

    // Vérifie que le bouton "Précédent" est désactivé lorsque nous sommes sur la première page
    expect(wrapper.find('li.page-item:first-child').classes()).toContain('disabled');

    // Vérifie que le bouton "Suivant" est actif lorsque nous sommes sur la première page
    expect(wrapper.find('li.page-item:last-child').classes()).not.toContain('disabled');

    // Vérifie que les numéros de page sont rendus correctement
    // const pageNumbers = wrapper.findAll('li.page-item:not(:first-child):not(:last-child)');

    // Récupère tous les éléments de pagination
    const pageItems = wrapper.findAll('li.page-item');

    // Exclure le premier et le dernier élément
    const pageNumbers = pageItems.slice(1, -1);
    expect(pageNumbers).toHaveLength(5); // Total des pages
  });

  // Test de l'émission de l'événement "prev-page"
  it('emits "prev-page" event when the previous button is clicked', async () => {
    const wrapper = mount(PaginationComponent, {
      props: {
        currentPage: 2,
        totalPages: 5,
      },
    });

    await wrapper.find('li.page-item:first-child a.page-link').trigger('click'); // Simule le clic sur le bouton "Précédent"

    // Vérifie que l'événement "prev-page" a été émis
    expect(wrapper.emitted()['prev-page']).toBeTruthy();
  });

  // Test de l'émission de l'événement "next-page"
  it('emits "next-page" event when the next button is clicked', async () => {
    const wrapper = mount(PaginationComponent, {
      props: {
        currentPage: 1,
        totalPages: 5,
      },
    });

    // Simule le clic sur le bouton "Suivant"
    await wrapper.find('li.page-item:last-child a.page-link').trigger('click'); // Le bouton "Suivant" est le dernier <a.page-link>

    // Vérifie que l'événement "next-page" a été émis
    expect(wrapper.emitted()['next-page']).toBeTruthy();
  });

  // Test de l'émission de l'événement "go-to-page" pour un numéro de page spécifique
  it('emits "go-to-page" event when a page number is clicked', async () => {
    const wrapper = mount(PaginationComponent, {
      props: {
        currentPage: 1,
        totalPages: 5,
      },
    });

    // Simule le clic sur la page 3
    // await wrapper.find('li.page-item:nth-child(4) a.page-link').trigger('click');
    // Page 3 est le troisième <a.page-link> la page correspond au 4è element du tableau
    await wrapper.findAll('a.page-link')[3].trigger('click');
    /* const pageLinks = wrapper.findAll('a.page-link');
    pageLinks.forEach((link, index) => {
      console.log(`Link ${index}: ${link.text()}`);
    }); */

    // Vérifie que l'événement "go-to-page" a été émis avec le numéro de page correct
    expect(wrapper.emitted()['go-to-page']).toBeTruthy();
    expect(wrapper.emitted()['go-to-page'][0]).toEqual([3]);
  });

  // Test du style pour le bouton de page active
  it('applies the correct class for the active page', () => {
    const wrapper = mount(PaginationComponent, {
      props: {
        currentPage: 3,
        totalPages: 5,
      },
    });

    // Vérifie que la page active est correctement marquée
    const activePage = wrapper.find('li.page-item.active');
    expect(activePage.text()).toBe('3');
  });

  // Test de la pagination avec une seule page
  it('disables both "prev-page" and "next-page" buttons when there is only one page', () => {
    const wrapper = mount(PaginationComponent, {
      props: {
        currentPage: 1,
        totalPages: 1,
      },
    });

    // Vérifie que les boutons de navigation sont désactivés
    expect(wrapper.find('li.page-item:first-child').classes()).toContain('disabled');
    expect(wrapper.find('li.page-item:last-child').classes()).toContain('disabled');
  });
});
