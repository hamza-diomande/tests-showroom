import { mount } from '@vue/test-utils'; // Importation de la fonction `mount` pour monter le composant.
import { describe, it, expect } from 'vitest'; // Importation des fonctions de test de Vitest.
import DataTable from '../../../src/components/DataTableComponent.vue'; // Importation du composant à tester.

describe('DataTable', () => {
  // Test du rendu du tableau avec les colonnes et les données
  /* it('renders table with columns and rows', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
      { id: 2, name: 'Jane Doe', email: 'jane@example.com' },
    ];

    const wrapper = mount(TableComponent, {
      props: {
        columns,
        data,
      },
    });

    // Vérifie que les en-têtes de colonnes sont rendus correctement
    columns.forEach((column, index) => {
      const th = wrapper.findAll('th').at(index);
      expect(th.text()).toBe(column.label);
    });

    // Vérifie que les cellules de données sont rendues correctement
    data.forEach((row, rowIndex) => {
      Object.values(row).forEach((value, colIndex) => {
        // Utilisez .findAll('tr').at(rowIndex + 1)
        // pour obtenir les lignes de données, +1 pour ignorer l'en-tête
        const td = wrapper.findAll('tr').at(rowIndex + 1).findAll('td').at(colIndex);
        console.log('TD', td.text());
        expect(td.text()).toBe(String(value));
        // Convertir value en chaîne de caractères pour la comparaison
      });
    });
  });
*/

  it('renders table with columns and rows', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
      { id: 2, name: 'Jane Doe', email: 'jane@example.com' },
    ];

    const wrapper = mount(DataTable, {
      props: {
        columns,
        data,
      },
    });

    // Vérifie que les en-têtes de colonnes sont rendus correctement
    columns.forEach((column, index) => {
      const th = wrapper.findAll('th').at(index);
      expect(th.text()).toBe(column.label);
    });

    // Vérifie que les cellules de données sont rendues correctement
    data.forEach((row, rowIndex) => {
      columns.forEach((column, colIndex) => {
        const td = wrapper.findAll('tr').at(rowIndex + 1).findAll('td').at(colIndex);
        expect(td.text()).toBe(String(row[column.key]));
        // Associe les données avec les clés de colonne
      });
    });
  });

  // Test du tri des colonnes
  it('emits "sort" event with correct key when a column header is clicked', async () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
    ];

    const wrapper = mount(DataTable, {
      props: {
        columns,
        data,
      },
    });

    // Trouvez l'en-tête de la colonne "Name" et simulez un clic sur celui-ci
    const nameHeader = wrapper.findAll('th').at(0); // Trouvez le premier en-tête (Name) via le wrapper
    await nameHeader.trigger('click'); // Simule le clic sur l'en-tête

    // Vérifie que l'événement "sort" a été émis avec la clé correcte
    expect(wrapper.emitted().sort).toBeTruthy(); // Vérifiez que l'événement "sort" a bien été émis
    expect(wrapper.emitted().sort[0]).toEqual(['name']); // Vérifiez que la clé émise est correcte
  });

  // Test de la classe CSS triée
  it('applies the correct class for sorted columns', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
    ];

    const wrapper = mount(DataTable, {
      props: {
        columns,
        data,
        sortByKey: 'name',
        sortDirection: 'asc',
      },
    });

    const nameHeader = wrapper.findAll('th').at(0).element; // Trouvez le premier en-tête (Name) via le wrapper
    expect(nameHeader.classList).toContain('sorted'); // Vérifiez la présence de la classe "sorted"
    expect(nameHeader.classList).toContain('asc'); // Vérifiez la présence de la classe "asc"
  });

  // Test du tri décroissant
  it('applies the correct class for descending sorted columns', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
    ];

    const wrapper = mount(DataTable, {
      props: {
        columns,
        data,
        sortByKey: 'name',
        sortDirection: 'desc',
      },
    });

    const nameHeader = wrapper.findAll('th').at(0).element; // Trouvez le premier en-tête (Name) via le wrapper
    expect(nameHeader.classList).toContain('sorted'); // Vérifiez la présence de la classe "sorted"
    expect(nameHeader.classList).toContain('desc'); // Vérifiez la présence de la classe "desc"
  });
});

/* import { mount } from '@vue/test-utils';
// Importation de la fonction `mount` pour monter le composant.
import { describe, it, expect } from 'vitest'; // Importation des fonctions de test de Vitest.
import TableComponent from '../../../src/components/TableComponent.vue';
// Importation du composant à tester.

describe('TableComponent', () => {
  // Test du rendu du tableau avec les colonnes et les données
   it('renders table with columns and rows', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
      { id: 2, name: 'Jane Doe', email: 'jane@example.com' },
    ];

    const wrapper = mount(TableComponent, {
      props: {
        columns,
        data,
      },
    });

    // Vérifie que les colonnes sont rendues correctement
    columns.forEach((column) => {
      expect(wrapper.text()).toContain(column.label);
    });

    // Vérifie que les lignes sont rendues correctement
    data.forEach((row) => {
      Object.values(row).forEach((value) => {
        expect(wrapper.text()).toContain(value);
      });
    });
  });
  it('renders table with columns and rows', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
      { id: 2, name: 'Jane Doe', email: 'jane@example.com' },
    ];

    const wrapper = mount(TableComponent, {
      props: {
        columns,
        data,
      },
    });

    // Vérifie que les en-têtes de colonnes sont rendus correctement
    columns.forEach((column, index) => {
      const th = wrapper.findAll('th').at(index);
      expect(th.text()).toBe(column.label);
    });

    // Vérifie que les cellules de données sont rendues correctement
    data.forEach((row, rowIndex) => {
      Object.values(row).forEach((value, colIndex) => {
        const td = wrapper.findAll('tr').at(rowIndex + 1).findAll('td').at(colIndex);
         // +1 pour ignorer l'en-tête
        expect(td.text()).toBe(value);
      });
    });
  });

  // Test du tri des colonnes
  it('emits "sort" event with correct key when a column header is clicked', async () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
    ];

    const wrapper = mount(TableComponent, {
      props: {
        columns,
        data,
      },
    });

    // Simule un clic sur l'en-tête de la colonne "Name"
    const nameHeader = wrapper.find('th').text(); // Trouve le premier en-tête (Name)
    await nameHeader.trigger('click'); // Simule le clic sur l'en-tête

    // Vérifie que l'événement "sort" a été émis avec la clé correcte
    expect(wrapper.emitted().sort).toBeTruthy();
    expect(wrapper.emitted().sort[0]).toEqual(['name']);
  });

  // Test de la classe CSS triée
  it('applies the correct class for sorted columns', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
    ];

    const wrapper = mount(TableComponent, {
      props: {
        columns,
        data,
        sortByKey: 'name',
        sortDirection: 'asc',
      },
    });

    const nameHeader = wrapper.find('th').element;
    expect(nameHeader.classList).toContain('sorted');
    expect(nameHeader.classList).toContain('asc');
  });

  // Test du tri décroissant
  it('applies the correct class for descending sorted columns', () => {
    const columns = [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
    ];
    const data = [
      { id: 1, name: 'John Doe', email: 'john@example.com' },
    ];

    const wrapper = mount(TableComponent, {
      props: {
        columns,
        data,
        sortByKey: 'name',
        sortDirection: 'desc',
      },
    });

    const nameHeader = wrapper.find('th').element;
    expect(nameHeader.classList).toContain('sorted');
    expect(nameHeader.classList).toContain('desc');
  });
});
*/
