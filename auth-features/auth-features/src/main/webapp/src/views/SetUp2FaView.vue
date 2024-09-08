<template>
  <OfflineLayout>
    <div>
      <!-- Button is removed -->
      <div v-if="qrCode">
        <!--<vue-qrcode :value="qrCode" />-->
        <h1>Scan the QR Code</h1>
        <p>Please scan the QR code with your authenticator app to complete the setup.</p>
        <img :src="qrCode" alt="TOTP QR Code" />
        <p>Secret: {{ secret }}</p>
      </div>
    </div>
    <form @submit.prevent="login">
      <Button type="submit">Login</Button>
    </form>
  </OfflineLayout>
</template>

<script>
import OfflineLayout from '@/layouts/OfflineLayout.vue';
import Button from 'primevue/button';
import { mapState } from 'vuex';

export default {
  name: 'SetUp2FaView',
  components: { OfflineLayout, Button },
  data() {
    return {
      account: null,
      qrCode: '',
      secret: '',
    };
  },
  computed: {
    // eslint-disable-next-line no-undef
    // ...mapState(['currentAccount']),
    ...mapState({
      currentAccount: (state) => state.auth2Fa.currentAccount,
      valueQRCode: (state) => state.auth2Fa.qrCode,
      valueSecret: (state) => state.auth2Fa.secret,
    }),
  },
  created() {
    this.fetchQRCode();
  },
  methods: {
    async fetchQRCode() {
      try {
        console.log('test', this.currentAccount);
        await this.$store.dispatch('auth2Fa/setup');
        // console.log('setUp :', this.valueQRCode);
        this.qrCode = this.valueQRCode;
        this.secret = this.valueSecret;
      } catch (e) {
        console.error('Error fetching QR code:', e);
      }
      // Appel à l'API pour configurer l'authentification à deux facteurs
      // const { qrCode, secret } = await this.$store.dispatch('auth2fa/setup',
      // { account: this.currentAccount });
    },
    async login() {
      this.$router.push({ name: 'login2Fa' }); // Pour naviguer avec une nouvelle entrée dans l'historique de navigation
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
</style>
