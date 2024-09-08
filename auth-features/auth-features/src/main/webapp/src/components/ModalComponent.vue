<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="close" @keydown.esc="close" tabindex="0">
    <div class="modal-container">
      <button type="button" class="close-button" @click="close" @keydown.enter="close" @keydown.space="close">X</button>
      <div class="modal-content">
        <slot />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    modelValue: {
      type: Boolean,
      required: true,
    },
  },
  computed: {
    isVisible: {
      get() {
        console.log('Getting isVisible:', this.modelValue); // Log pour le getter
        return this.modelValue;
      },
      set(value) {
        console.log('Setting isVisible:', value); // Log pour le setter
        this.$emit('update:modelValue', value);
      },
    },
  },
  watch: {
    modelValue(newVal) {
      console.log('modelValue changed:', newVal); // Log pour le watch
    },
  },
  mounted() {
    console.log('ModalComponent mounted'); // Log pour le hook mounted
  },
  methods: {
    close() {
      console.log('Closing modal'); // Log pour la méthode close
      this.isVisible = false;
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  position: relative;
}
.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  cursor: pointer;
}
.close-button:focus {
  outline: 2px solid #000;
}
</style>
