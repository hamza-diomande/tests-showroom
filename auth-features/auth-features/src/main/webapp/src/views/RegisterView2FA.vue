<template>
  <OfflineLayout>
    <div>
      <h1>Register</h1>
      <form @submit.prevent="register">
        <div>
          <label for="identifier">Identifier :</label>
          <InputText type="text" id="identifier" autocomplete="identifier" v-model="form.identifier" required />
        </div>
        <div>
          <label for="firstname">First name :</label>
          <InputText type="text" id="firstname" autocomplete="firstname" v-model="form.firstname" required />
        </div>
        <div>
          <label for="lastname">Last name :</label>
          <InputText type="text" id="lastname" autocomplete="lastname" v-model="form.lastname" required />
        </div>
        <div>
          <label for="email">Email :</label>
          <InputText type="email" id="email" autocomplete="email" v-model="form.email" required />
        </div>
        <div>
          <label for="password">Password :</label>
          <InputText type="password" id="password" autocomplete="new-password" v-model="form.password" required />
        </div>
        <div>
          <label for="confirmPassword">Confirm Password :</label>
          <InputText type="password" id="confirmPassword" autocomplete="new-password" v-model="form.confirmPassword" required />
        </div>
        <div>
          <label for="company">Company :</label>
          <InputText type="text" id="company" autocomplete="organization" v-model="form.company" required />
        </div>
        <Button type="submit">Register</Button>
        <p class="p-error" v-if="form.error">{{ form.error }}</p>
      </form>
    </div>
  </OfflineLayout>
</template>

<script>
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import OfflineLayout from '@/layouts/OfflineLayout.vue';

export default {
  name: 'RegisterView2Fa',
  components: { OfflineLayout, InputText, Button },
  data() {
    return {
      form: {
        identifier: '',
        firstname: '',
        lastname: '',
        email: '',
        password: '',
        confirmPassword: '',
        company: '',
        error: '',
      },
    };
  },
  methods: {
    async register() {
      if (this.form.password !== this.form.confirmPassword) {
        this.form.error = 'Passwords do not match.';
        return;
      }
      const data = {
        email: this.form.email,
        password: this.form.password,
        company: this.form.company,
        lastName: this.form.lastname,
        firstName: this.form.firstname,
        identifier: this.form.identifier,
      };
      this.$store.dispatch('auth2Fa/register', data)
        .then(() => this.$router.push({ name: 'QrCode', params: { account: this.form.email } }))
        .catch((e) => { this.form.error = e.response?.data?.message || 'Registration failed.'; });
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
  }

  button {
    float: right;
    margin: 0;
  }

  .p-error {
    color: red;
  }
}
</style>
