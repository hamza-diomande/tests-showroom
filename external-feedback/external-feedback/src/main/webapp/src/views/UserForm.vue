<template>
  <OffLineLayout>
    <div class="user-form">
      <form @submit.prevent="submitForm">
        <div>
          <label for="identifier">Identifier:</label>
          <InputText type="text" id="identifier" v-model="user.identifier" required />
        </div>
        <div>
          <label for="firstName">First Name:</label>
          <InputText type="text" id="firstName" v-model="user.firstName" required />
        </div>
        <div>
          <label for="lastName">Last Name:</label>
          <InputText type="text" id="lastName" v-model="user.lastName" required />
        </div>
        <div>
          <label for="company">Company:</label>
          <InputText type="text" id="company" v-model="user.company" />
        </div>
        <div>
          <label for="email">Email:</label>
          <InputText type="email" id="email" v-model="user.email" required />
        </div>
        <div>
          <label for="role">Role:</label>
          <select id="role" v-model="user.role">
            <option value="USER_ROLE">User</option>
            <option value="ADMIN_ROLE">Admin</option>
          </select>
        </div>
        <div>
          <label for="password">Password:</label>
          <InputText type="password" id="password" v-model="user.password" required />
        </div>
        <captcha-component
          ref="captcha"
          v-model:answer="form.captcha.answer"
          v-model:session="form.captcha.session"
        />
        <Button type="submit">Submit</Button>
      </form>
    </div>
  </OffLineLayout>
</template>

<script>
import CaptchaComponent from '@/components/CaptchaComponent.vue';
import OffLineLayout from '@/layouts/OfflineLayout.vue';

import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

export default {
  name: 'UserForm',
  components: {
    InputText, Button, OffLineLayout, CaptchaComponent,
  },
  data() {
    return {
      user: {
        identifier: '',
        firstName: '',
        lastName: '',
        company: '',
        email: '',
        role: 'USER_ROLE',
        password: '',
      },
      form: {
        captcha: {
          answer: '',
          session: '',
        },
        error: '',
      },
    };
  },
  methods: {
    async submitForm() {
      const data = {
        identifier: this.user.identifier,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        role: this.user.role,
        password: this.user.password,
        company: this.user.company,
        captchaAnswer: this.form.captcha.answer,
        captchaSession: this.form.captcha.session,
      };
      try {
        await this.$store.dispatch('auth/register', data);
        this.$router.push({ name: 'login' });
      } catch (e) {
        this.form.error = e.response?.data?.message || 'Échec de l\'inscription.';
        console.error(this.form.error);
      }
      /* this.$store.dispatch('auth/register', data)
        .then(() => this.$router.push({ name: 'login' }))
        .catch((e) => { this.form.error = e.response?.data?.message
        || 'Registration failed.'; }); */
      // Here you can add your code to send the form data to your backend
    },
  },
};
</script>

<style scoped>
.user-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.user-form div {
  margin-bottom: 1rem;
}
.user-form label {
  display: block;
  margin-bottom: 0.5rem;
}
.user-form input,
.user-form select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.user-form button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
