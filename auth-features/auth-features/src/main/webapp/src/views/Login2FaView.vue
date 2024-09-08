<template>
  <OfflineLayout>
    <div>
      <h1>Login with 2FA</h1>
      <form @submit.prevent="login">
        <div>
          <label for="email">Email:</label>
          <InputText type="text" v-model="email" id="email" autocomplete="email" required />
        </div>
        <div>
          <label for="password">Password:</label>
          <InputText type="password" id="password" autocomplete="password" v-model="password" required />
        </div>
        <Button type="submit">Login</Button>
      </form>
      <Modal v-model="showModal">
        <div>
          <label for="code">Enter Verification Code</label>
          <InputText type="text" id="code" v-model="verificationCode" required />
          <Button type="button" @click="verifyCode">Verify</Button>
          <p v-if="verificationError" class="error">{{ verificationError }}</p>
        </div>
      </Modal>
    </div>
  </OfflineLayout>
</template>

<script>
import OfflineLayout from '@/layouts/OfflineLayout.vue';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Modal from '@/components/ModalComponent.vue';

export default {
  name: 'Login2FaView',
  components: {
    Modal,
    Button,
    OfflineLayout,
    InputText,
  },
  data() {
    return {
      email: '',
      password: '',
      verificationCode: '',
      showModal: false,
      verificationError: '', // Nouvelle propriété pour l'erreur de vérification
      error: '',
    };
  },
  methods: {
    async login() {
      try {
        const data = {
          email: this.email,
          password: this.password,
        };

        // Effectuer la requête de login via Vuex
        const response = await this.$store.dispatch('auth2Fa/login', data);
        console.log('response', response);
        if (response === '2FA_REQUIRED') {
          // Afficher la popup pour saisir le code de vérification
          this.showModal = true;
        } else {
          // Rediriger l'utilisateur vers la page d'accueil
          this.$router.push({ name: 'Home' });
        }
      } catch (error) {
        // Gérer l'erreur
        console.error('Error:', error);
      }
    },
    async verifyCode() {
      try {
        // Effectuer la vérification du code via Vuex
        const data = {
          totpCode: this.verificationCode,
        };
        console.log('totpCode', data.totpCode);

        const isVerified = await this.$store.dispatch('auth2Fa/verifyTOTP', data);
        console.log('isverified', isVerified);

        if (isVerified !== 'Bearer Invalid TOTP code') {
          // Réinitialiser les erreurs de vérification
          this.verificationError = '';
          // Fermer la modale
          this.showModal = false;

          this.verificationCode = '';
          // Rediriger l'utilisateur vers la page d'accueil
          // this.$router.push({ name: 'Home' });
        } else {
          // Afficher un message d'erreur
          this.verificationError = 'Invalid verification code';
        }
      } catch (er) {
        // Gérer l'erreur
        this.error = 'An error occurred during verification';
        console.error('Error:', er);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
form {
  width: 100%;

  div {
    margin-bottom: 20px;
  }

  label {
    margin-bottom: 5px;
    display: block;
  }

  input {
    width: 100%;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
  }

  button {
    float: right;
    margin: 0;
  }
}

.error {
  color: red;
  margin-top: 10px;
}
</style>
