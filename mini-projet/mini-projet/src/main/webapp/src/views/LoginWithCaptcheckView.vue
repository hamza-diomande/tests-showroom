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
        <!-- Captcha Component -->
        <captcha-component
          ref="captcha"
          v-model:answer="form.captcha.answer"
          v-model:session="form.captcha.session"
          class="mb-3"
        />

        <Button type="submit">Me connecter</Button>
        <p class="p-error">{{form.error}}</p>
      </form>
    </div>
  </OfflineLayout>

</template>

<script>
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import OfflineLayout from '@/layouts/OfflineLayout.vue';
import CaptchaComponent from '@/components/CaptchaComponent.vue';

export default {
  name: 'LoginCaptcheckView',
  components: {
    OfflineLayout, InputText, Button, CaptchaComponent,
  },
  data() {
    return {
      form: {
        email: '',
        password: '',
        code: '',
        error: '',
        captcha: {
          answer: '',
          session: '',
        },
      },
    };
  },
  methods: {
    login() {
      const data = {
        email: this.form.email,
        password: this.form.password,
        captchaAnswer: this.form.captcha.answer,
        captchaSession: this.form.captcha.session,
      };
      this.$store.dispatch('auth/login', data)
        .then(() => this.$router.push('/'))
        .catch((e) => {
          this.form.error = 'Vérifier que le mot de passe et le l\'email saisis sont correctes';
          // Afficher la modalité de vérification;
          console.error(e);
        });
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
      float: center;
      margin: 0;
    }
  }

  </style>
