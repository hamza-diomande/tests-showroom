import { mount } from '@vue/test-utils'; // Importation de la fonction `mount` pour monter le composant.
import { describe, it, expect } from 'vitest'; // Importation des fonctions de test de Vitest.
import FilterComponents from '../../../src/components/FilterComponent.vue'; // Importation du composant à tester.

describe('FilterComponent', () => {
  // Test du rendu du placeholder
  it('renders the input with the correct placeholder', () => {
    const wrapper = mount(FilterComponents, {
      props: {
        searchQuery: 'initial value',
      },
    });

    const input = wrapper.find('input'); // Recherche l'élément input dans le DOM du composant.
    expect(input.exists()).toBe(true); // Vérifie que l'élément input existe.
    expect(input.attributes('placeholder')).toBe('Recherche...'); // Vérifie que le placeholder est "Recherche...".
  });

  // Test de la mise à jour de la valeur de l'input et de l'émission d'événements
  it('updates the searchQuery when typing in the input', async () => {
    const wrapper = mount(FilterComponents, {
      props: {
        searchQuery: 'initial value',
      },
    });

    const input = wrapper.find('input');
    await input.setValue('new value'); // Simule la saisie de la valeur "new value" dans l'input.

    // Vérifie que l'événement "update:searchQuery" a été émis avec la bonne valeur.
    expect(wrapper.emitted()['update:searchQuery']).toBeTruthy();
    expect(wrapper.emitted()['update:searchQuery'][0]).toEqual(['new value']);
  });

  // Test de la réactivité de la prop searchQuery
  it('reflects the updated searchQuery when the prop changes', async () => {
    const wrapper = mount(FilterComponents, {
      props: {
        searchQuery: 'initial value',
      },
    });

    await wrapper.setProps({ searchQuery: 'updated value' }); // Change la prop searchQuery.

    const input = wrapper.find('input');
    expect(input.element.value).toBe('updated value'); // Vérifie que l'input a la nouvelle valeur.
  });

  // Test de gestion de la prop searchQuery manquante ou nulle
  it('handles null searchQuery prop gracefully', () => {
    const wrapper = mount(FilterComponents, {
      props: {
        searchQuery: null,
      },
    });

    const input = wrapper.find('input');
    expect(input.element.value).toBe(''); // Vérifie que l'input est vide.
  });
});
