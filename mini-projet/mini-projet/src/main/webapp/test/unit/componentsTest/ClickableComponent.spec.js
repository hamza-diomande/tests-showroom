/* eslint-disable import/no-extraneous-dependencies */
// tests/ClickableCard.spec.js
import { mount } from '@vue/test-utils';
import {
  describe, it, expect,
} from 'vitest';
import ClickableCard from '../../../src/components/ClickableCard.vue';

describe('ClickableCard', () => {
  it('renders correctly', () => {
    const wrapper = mount(ClickableCard, {
      propsData: {
        title: 'Test Card',
        iconClass: 'fa fa-star',
        link: '/test-link',
        description: 'This is a test description',
      },
    });

    // Vérification du rendu initial
    expect(wrapper.text()).toContain('Test Card');
    expect(wrapper.find('.icon').exists()).toBe(true);
  });

  it('applies hover effect', async () => {
    const wrapper = mount(ClickableCard, {
      propsData: {
        title: 'Test Card',
        iconClass: 'fa fa-star',
        link: '/test-link',
        // description: 'This is a test description',
      },
    });

    // Vérifier que la classe hovered est ajoutée au survol
    await wrapper.find('.card').trigger('mouseover');
    expect(wrapper.find('.card').classes('hovered')).toBe(true);

    // Vérifier que la classe hovered est retirée lorsque la souris quitte
    await wrapper.find('.card').trigger('mouseout');
    expect(wrapper.find('.card').classes('hovered')).toBe(false);
  });

  it('applies focus effect', async () => {
    const wrapper = mount(ClickableCard, {
      propsData: {
        title: 'Test Card',
        iconClass: 'fa fa-star',
        link: '/test-link',
        description: 'This is a test description',
      },
    });

    // Vérifier que la classe focused est ajoutée au focus
    await wrapper.find('.card-link').trigger('focusin');
    expect(wrapper.find('.card').classes('focused')).toBe(true);

    // Vérifier que la classe focused est retirée lorsque le focus est perdu
    await wrapper.find('.card-link').trigger('focusout');
    expect(wrapper.find('.card').classes('focused')).toBe(false);
  });
  it('renders router-link with correct "to" prop', () => {
    const wrapper = mount(ClickableCard, {
      props: {
        title: 'Test Title',
        iconClass: 'test-icon',
        link: '/test-link',
        description: 'Test Description',
      },
    });

    // Vérifie que le composant RouterLink existe
    // console.log(wrapper.html());
    const routerLink = wrapper.findComponent({ name: 'RouterLink' });
    expect(routerLink.exists()).toBe(true);

    // Vérifie que le composant RouterLink a bien le prop "to" avec la valeur attendue
    expect(routerLink.props('to')).toBe('/test-link');
  });
});
