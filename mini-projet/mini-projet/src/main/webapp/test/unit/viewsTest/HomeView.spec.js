// homeView2.spec.js

import { mount } from '@vue/test-utils';
import {
  describe, it, expect,
} from 'vitest';
import HomeView from '../../../src/views/HomeView2.vue';

describe('HomeView', () => {
  it('renders all ClickableCard components with correct props', () => {
    const wrapper = mount(HomeView);

    const cards = wrapper.findAllComponents({ name: 'ClickableCard' });
    // console.log('Cards found:', cards);

    expect(cards).toHaveLength(wrapper.vm.features.length);

    wrapper.vm.features.forEach((feature, index) => {
      const card = cards.at(index);
      // console.log(feature);
      // console.log('Card at index', index, ':', card);

      expect(card.props('title')).toBe(feature.title);
      expect(card.props('iconClass')).toBe(feature.iconClass);
      expect(card.props('link')).toBe(feature.link);
      expect(card.props('description')).toBe(feature.description);
    });
  });
});
