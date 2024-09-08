<template>
  <OfflineLayout>
    <form @submit.prevent="login">
      <div>
        <label for="email">Email :</label>
        <InputText type="text" id="email" autocomplete="email" v-model="form.email" />
      </div>

      <div>
        <label for="password">Password :</label>
        <InputText type="password" id="password" autocomplete="password" v-model="form.password" />
      </div>

      <Button type="submit">Me connecter</Button>
      <p class="p-error">{{form.error}}</p>
    </form>
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
        error: '',
      },
    };
  },
  methods: {
    login() {
      const data = {
        email: this.form.email,
        password: this.form.password,
      };

      this.$store.dispatch('auth/login', data)
        .then(() => this.$router.push('/'))
        .catch((e) => {
          this.form.error = e;
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
    float: right;
    margin: 0;
  }
}

</style>
