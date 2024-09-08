<template>
  <OnlineLayout>
    <div class="container mt-4">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">Profil utilisateur</h2>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label for="name"><strong>Identifiant :</strong></label>
            <p id="name">{{ currentUser ? currentUser.identifier : 'Chargement...' }}</p>
          </div>
          <div class="form-group">
            <label for="name"><strong>Nom :</strong></label>
            <p id="name">{{ currentUser ? currentUser.lastName : 'Chargement...' }}</p>
          </div>
          <div class="form-group">
            <label for="name"><strong>Prenom :</strong></label>
            <p id="name">{{ currentUser ? currentUser.firstName : 'Chargement...' }}</p>
          </div>
          <div class="form-group">
            <label for="2faValue"><strong>Authentification à deux facteurs :</strong></label>
            <p id="2faValue">{{ auth2faStatus }}</p>
          </div>
          <button type="button" :class="['btn', currentUser && currentUser.auth2Fa ? 'btn-danger' : 'btn-success']" @click="toggle2FA">{{ toggleButtonLabel }}</button>
        </div>
      </div>
    </div>

    <!-- PrimeVue Dialog
    <Dialog
      header="Scannez le QR Code"
      v-model:visible="showQRPopup"
      :modal="true"
      :closable="true"
      :dismissable-mask="true"
      :baseZIndex="10000"
    >
      <div class="text-center">
        <div class="qr-code-container">
          <img :src="qrCode" alt="QR Code" class="img-fluid" />
        </div>
        <p>
          Scannez ce QR code avec une application d'authentification comme Google Authenticator,
          Authy, ou Microsoft Authenticator pour activer l'authentification à deux facteurs.
        </p>
      </div>
    </Dialog>-->

    <!-- PrimeVue Dialog for QR Code -->
    <Dialog
      header="Scannez le QR Code"
      v-model:visible="showQRPopup"
      :modal="true"
      :closable="false"
      :dismissable-mask="true"
      :baseZIndex="10000"
      class="qr-dialog"
    >
      <div class="text-center">
        <div class="qr-code-container">
          <img :src="qrCode" alt="QR Code" class="img-fluid" />
        </div>
        <p>
          Scannez ce QR code avec une application d'authentification comme Google Authenticator,
          Authy, ou Microsoft Authenticator pour activer l'authentification à deux facteurs.
        </p>
        <div class="dialog-buttons">
          <button type="button" class="btn btn-primary" @click="showConfirmScanPopup">FERMER</button>
          <!-- <button type="button" class="btn btn-secondary"
           @click="cancelQRScan">Annuler</button>-->
        </div>
      </div>
    </Dialog>

    <!-- PrimeVue Dialog for Confirmation -->
    <Dialog
      header="Confirmez"
      v-model:visible="showConfirmPopup"
      :modal="true"
      :closable="true"
      :dismissable-mask="true"
      :baseZIndex="10000"
      class="confirm-dialog"
    >
      <div class="text-center">
        <p>Avez-vous bien scanné le QR code avec l'application d'authentification ?</p>
        <div class="dialog-buttons">
          <button type="button" class="btn btn-primary" @click="confirmScan">Oui</button>
          <button type="button" class="btn btn-secondary" @click="cancelConfirm">Non</button>
        </div>
      </div>
    </Dialog>

    <!-- PrimeVue Dialog for 2FA Deactivation Confirmation -->
    <Dialog
      header="Confirmez la désactivation"
      v-model:visible="showDeactivateConfirmPopup"
      :modal="true"
      :closable="true"
      :dismissable-mask="true"
      :baseZIndex="10000"
      class="deactivate-confirm-dialog"
    >
      <div class="text-center">
        <p>Êtes-vous sûr de vouloir désactiver l'authentification à deux facteurs ?</p>
        <div class="dialog-buttons">
          <button type="button" class="btn btn-primary" @click="confirmDeactivate2FA">Oui, désactiver</button>
          <button type="button" class="btn btn-secondary" @click="cancelDeactivateConfirm">Annuler</button>
        </div>
      </div>
    </Dialog>
  </OnlineLayout>

</template>

<script>

import OnlineLayout from '@/layouts/OnlineLayout.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'ProfileView',
  components: { OnlineLayout },
  data() {
    return {
      auth2faStatus: '',
      isProcessing: false,
      showQRPopup: false, // État pour contrôler la visibilité de la popup
      showConfirmPopup: false, // État pour contrôler la visibilité de la popup de confirmation
      qrCode: '', // QR code à afficher dans la popup
      secret: '',
      showDeactivateConfirmPopup: false,
    };
  },
  computed: {
    ...mapGetters('auth', ['currentUser']),
    toggleButtonLabel() {
      return this.currentUser && this.currentUser.auth2Fa ? 'Désactiver l\'authentification 2 facteurs' : 'Activer l\'authentification 2 facteurs';
    },
  },
  watch: {
    currentUser: {
      immediate: true,
      handler() {
        this.updateAuth2faStatus();
      },
    },
  },
  mounted() {
    console.log('Current User:', this.currentUser);
    this.updateAuth2faStatus();
  },
  methods: {
    updateAuth2faStatus() {
      console.log('Updating 2FA Status...');
      this.auth2faStatus = this.currentUser && this.currentUser.auth2Fa ? 'Activée' : 'Désactivée';
      console.log('2FA Status:', this.auth2faStatus);
    },

    async toggle2FA() {
    // Exemple : Appeler une action Vuex pour basculer l'état de auth2fa
    // this.$store.dispatch('toggle2FA');
    // Ou muter directement l'état si c'est une mutation Vuex
    // this.$store.commit('toggle2FA');
      this.isProcessing = true;
      try {
        if (!this.currentUser.auth2Fa) {
          const userId = this.currentUser.id;
          const { secret, qrCode } = await this.$userService.activate2FA(userId);

          // Mise à jour du store
          const updatedUser = { ...this.currentUser, auth2Fa: true, secret };
          this.$store.commit('auth/updateUser', updatedUser);
          this.$store.commit('auth/setSecret', secret);
          this.$store.commit('auth/setQRCode', qrCode);

          // Mise à jour de l'UI
          this.auth2faStatus = 'Activée';
          this.qrCode = qrCode;
          this.secret = secret;
          this.showQRPopup = true; // Afficher la popup avec le QR code
        } else {
          this.showDeactivateConfirmPopup = true;
        }
      } catch (error) {
        console.error('Erreur lors de l\'activation ou désactivation de la 2FA', error);
      } finally {
        this.isProcessing = false;
      }
    },

    showConfirmScanPopup() {
      this.showQRPopup = false; // Fermer la popup du QR code
      this.showConfirmPopup = true; // Afficher la popup de confirmation
    },

    async confirmScan() {
      // Logique pour vérifier que le QR code a été scanné
      // Si la vérification est réussie, déconnecter l'utilisateur ou effectuer une autre action
      this.showConfirmPopup = false; // Fermer la popup de confirmation
      try {
        // Exemple : vérifier l'authentification 2FA en appelant le service
        // await this.$userService.verify2FA(this.secret);
        this.$store.dispatch('auth/logout')
          .then(() => this.$router.push('login'));
        // Déconnecter l'utilisateur si nécessaire
        // this.$authService.logout();
        // Afficher un message de succès ou rediriger l'utilisateur si nécessaire
      } catch (error) {
        console.error('Erreur lors de la déconnexion', error);
        // Optionnel : afficher un message d'erreur à l'utilisateur
      }
    },
    async deactivate2FA() {
      try {
        const userId = this.currentUser.id;
        const user = await this.$userService.deactivate2FA(userId);
        this.$store.commit('auth/updateUser', user);
        this.$store.commit('auth/setSecret', null);
        this.$store.commit('auth/setQRCode', null);
        this.auth2faStatus = 'Désactivée';
      } catch (error) {
        console.error('Erreur lors de la désactivation de la 2FA', error);
      }
    },

    cancelConfirm() {
      this.showConfirmPopup = false; // Fermer la popup de confirmation
      this.showQRPopup = true; // Réouvrir la popup du QR code
    },

    confirmDeactivate2FA() {
      this.showDeactivateConfirmPopup = false; // Fermer la popup de confirmation
      this.deactivate2FA(); // Appeler la méthode pour désactiver la 2FA
    },

    cancelDeactivateConfirm() {
      this.showDeactivateConfirmPopup = false; // Fermer la popup de confirmation
    },
    /* async activate2FA() {

    }, */
  },
};

</script>

<style scoped>

/* Center the QR code in the dialog */
.qr-code-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.qr-code-container img {
  max-width: 100%;
  height: auto;
}
</style>
