<template>
  <router-link
    :to="link"
    class="card-link"
    @mouseover="handleMouseOver"
    @mouseout="handleMouseLeave"
    @focusin="handleFocusIn"
    @focusout="handleFocusOut">
    <div class="card" :class="{ hovered: isHover, focused: isFocused }">
      <div class="card-body d-flex align-items-center">
        <i :class="iconClass" class="me-3 icon" />
        <h5 class="card-title mb-0">{{ title }}</h5>
      </div>
      <transition name="fade">
        <div v-if="shouldShowDescription" class="card-description">
          {{ description }}
        </div>
      </transition>
    </div>
  </router-link>
</template>

<script>
export default {
  name: 'ClickableCard',
  props: {
    title: {
      type: String,
      required: true,
    },
    iconClass: {
      type: String,
      required: true,
    },
    link: {
      type: String,
      required: true,
    },
    description: {
      type: String,
      default: undefined, // Ajouter une description par défaut
    },
  },
  data() {
    return {
      isHover: false,
      isFocused: false,
    };
  },
  computed: {
    shouldShowDescription() {
      return this.description && this.description !== '';
    },
  },
  methods: {
    handleMouseOver() {
      this.isHover = true;
    },
    handleMouseLeave() {
      this.isHover = false;
    },
    handleFocusIn() {
      this.isFocused = true;
    },
    handleFocusOut() {
      this.isFocused = false;
    },
  },
};
</script>

<style scoped>
.card-link {
  text-decoration: none;
}

.card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 15px;
  position: relative; /* Position relative pour les enfants positionnés absolument */
}

.card.hovered,
.card.focused {
  background-color: #f0f0f0; /* Couleur de fond au survol ou au focus */
}

.card:hover {
  transform: scale(1.05);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.icon {
  font-size: 24px; /* Taille de l'icône */
  color: #007bff; /* Couleur de l'icône (bleu Bootstrap) */
}

.card-description {
  font-size: 14px;
  color: #555;
  position: absolute;
  top: 100%; /* Position sous la carte */
  left: 50%; /* Centrage horizontal */
  transform: translateX(-50%); /* Ajustement du centrage horizontal */
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 10px;
  border-radius: 8px;
  min-width: 200px;
  text-align: center;
  opacity: 0; /* Initialement invisible */
  pointer-events: none; /* Pour ignorer les événements souris */
  z-index: 1000; /* Assurer que la description apparaît au-dessus des autres éléments */
}

.card.hovered .card-description,
.card.focused .card-description {
  opacity: 1; /* Rendre visible au survol ou au focus */
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to /* .fade-leave-active in <2.1.8 */ {
  opacity: 0;
}
</style>
