<template>
  <OfflineLayout>
    <div class="container-fluid">
      <form @submit.prevent="login">
        <div class="b-form-group">
          <label for="email">Email :</label>
          <InputText type="text" id="email" class="b-form-input" autocomplete="email" v-model="form.email" placeholder="Email" required />
        </div>

        <div class="b-form-group">
          <label for="password">Password :</label>
          <InputText type="password" id="password" class="b-form-input" autocomplete="password" v-model="form.password" placeholder="Password" required />
        </div>

        <Button type="submit">Me connecter</Button>
        <p class="p-error">{{form.error}}</p>
      </form>
    </div>

    <!-- PrimeVue Dialog for TOTP Verification -->
    <Dialog
      header="Entrez le code de vérification TOTP"
      v-model:visible="showModal"
      :modal="true"
      :closable="false"
      :dismissable-mask="true"
      :baseZIndex="10000"
      class="totp-dialog"
    >
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="code">Code</label>
          <InputText
            id="code"
            v-model="form.code"
            placeholder="Code"
            required
          />
          <p class="p-error">{{ verificationError }}</p> <!-- Affichage du message d'erreur -->
        </div>
        <div class="dialog-buttons">
          <Button type="submit" label="Verifier" />
          <Button type="button" label="Annuler" @click="cancelModal" class="p-button-secondary" />
        </div>
      </form>
    </Dialog>
  </OfflineLayout>

</template>

<script>
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import OfflineLayout from '@/layouts/OfflineLayout.vue';

export default {
  name: 'LoginView',
  components: { OfflineLayout, InputText, Button },
  data() {
    return {
      form: {
        email: '',
        password: '',
        code: '',
        error: '',
      },
      showModal: false,
      verificationError: '',
    };
  },
  methods: {
    /* login() {
      const data = {
        email: this.form.email,
        password: this.form.password,
      };

      this.$store.dispatch('auth/login', data)
        .then(() => this.$router.push('/'))
        .catch((e) => {
          this.form.error = e;
        });
    }, */
    login() {
      const data = {
        email: this.form.email,
        password: this.form.password,
      };
      this.$store.dispatch('auth/login', data)
        .then((result) => {
          if (result === '2FA_REQUIRED') {
            // Si 2FA est requis, montrer la modalité de 2FA
            this.showModal = true;
          } else {
            // Sinon, rediriger l'utilisateur vers sa page de profil
            this.$router.push('/profile'); // Assurez-vous que la route 'UserProfile' existe
          }
        })
        .catch((e) => {
          this.form.error = 'Vérifier que le mot de passe et l\'email saisis sont correctes';
          // Afficher la modalité de vérification;
          console.error(e);
        });
    },
    resetModal() {
      this.form.code = '';
    },
    handleSubmit() {
      if (!this.checkFormValidity()) {
        return;
      }
      const data = {
        totpCode: this.form.code,
      };
      this.$store.dispatch('auth/verifyTOTP', data)
        .then((isVerified) => {
          if (isVerified !== 'Bearer Invalid TOTP code') {
            this.resetModal();
            this.showModal = false;
            this.$router.push('/profile');
          } else {
            this.verificationError = 'Code de vérification invalide';
          }
        })
        .catch((er) => {
          this.verificationError = 'Une erreur est survenue pendant la vérification';
          console.error('Error:', er);
        });
    },
    cancelModal() {
      this.showModal = false;
    },

    checkFormValidity() {
      const code = this.form.code.trim(); // Obtenez le code du formulaire et supprimez les espaces
      // Expression régulière pour vérifier que le
      // code est composé uniquement de chiffres et a une longueur de 6 caractères
      const codePattern = /^\d{6}$/;

      if (codePattern.test(code)) {
      // Le code est valide
        return true;
      }
      // Le code est invalide
      this.verificationError = 'Le code de vérification doit contenir exactement 6 chiffres.';
      return false;
    },

    /* async handleSubmit2() {
      // Exit when the form isn't valid
      if (!this.checkFormValidity()) {
        return;
      }
      try {
        // Effectuer la vérification du code via Vuex
        const data = {
          totpCode: this.form.code,
        };
        console.log('totpCode', data.totpCode);

        const isVerified = await this.$store.dispatch('auth/verifyTOTP', data);
        console.log('isverified', isVerified);
        if (isVerified !== 'Bearer Invalid TOTP code') {
          // Réinitialiser les erreurs de vérification
          this.verificationError = '';
          // Fermer la modale
          this.resetModal();
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
      // Push the name to submitted names
      // this.submittedNames.push(this.name);
      // Hide the modal manually
      this.$nextTick(() => {
        this.$bvModal.hide('modal-prevent-closing');
      });
    }, */
  },
};
</script>

<!-- <style lang="scss" scoped>
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
    float: center;
    margin: 0;
  }
}

</style>-->

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
  }

  button {
    float: center;
    margin: 0;
  }

  .p-error {
    color: red;
    font-size: 0.875rem;
    margin-top: 0.5rem;
  }
}

.totp-dialog .p-error {
  color: red;
  font-size: 0.875rem;
  margin-top: 0.5rem;
}
</style>
