<template>
  <OnlineLayout>
    <div class="">
      <div class="card-header">
        <h2 class="card-title">{{ userId ? 'Modifier l\'utilisateur' : 'Ajouter un nouvel utilisateur' }}</h2>
      </div>
      <form @submit.prevent="submitForm">
        <div class="row">
          <!-- Identifier -->
          <div class="mb-3 col-md-12">
            <label for="identifier" class="form-label">Identifier:</label>
            <InputText type="text" id="identifier" v-model="user.identifier" class="form-control" required />
          </div>
        </div>

        <div class="row">
          <!-- First Name -->
          <div class="mb-3 col-md-6">
            <label for="firstName" class="form-label">First Name:</label>
            <InputText type="text" id="firstName" v-model="user.firstName" class="form-control" required />
          </div>

          <!-- Last Name -->
          <div class="mb-3 col-md-6">
            <label for="lastName" class="form-label">Last Name:</label>
            <InputText type="text" id="lastName" v-model="user.lastName" class="form-control" required />
          </div>
        </div>

        <div class="row">
          <!-- Company -->
          <div class="mb-3 col-md-12">
            <label for="company" class="form-label">Company:</label>
            <InputText type="text" id="company" v-model="user.company" class="form-control" />
          </div>
        </div>

        <div class="row">
          <!-- Email -->
          <div class="mb-3 col-md-12">
            <label for="email" class="form-label">Email:</label>
            <InputText type="email" id="email" v-model="user.email" class="form-control" required />
          </div>
        </div>

        <div class="row">
          <!-- Role -->
          <div class="mb-3 col-md-6">
            <label for="role" class="form-label">Role:</label>
            <select id="role" v-model="user.role" class="form-select">
              <option value="USER_ROLE">User</option>
              <option value="ADMIN_ROLE">Admin</option>
            </select>
          </div>

          <!-- Password -->
          <div class="mb-3 col-md-6">
            <label for="password" class="form-label">Password:</label>
            <InputText type="password" id="password" v-model="user.password" class="form-control" :disabled="userId" required />
          </div>
        </div>

        <!-- Captcha Component -->
        <div class="row">
          <div class="mb-3 col-md-12">
            <captcha-component
              ref="captcha"
              v-model:answer="form.captcha.answer"
              v-model:session="form.captcha.session"
              class="mb-3"
            />
          </div>
        </div>

        <!-- Submit Button -->
        <div class="row">
          <div class="col-md-12 text-end">
            <Button type="submit" class="btn btn-primary">Submit</Button>
          </div>
        </div>

        <!-- Error Message -->
        <div class="row">
          <div class="col-md-12">
            <p v-if="form.error" class="text-danger">{{ form.error }}</p>
          </div>
        </div>
      </form>
    </div>
  </OnlineLayout>
</template>

<script>
import CaptchaComponent from '@/components/CaptchaComponent.vue';
import OnlineLayout from '@/layouts/OnlineLayout.vue';

import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

export default {
  name: 'UserForm',
  components: {
    InputText,
    Button,
    OnlineLayout,
    CaptchaComponent,
  },
  data() {
    return {
      userId: this.$route.params.id || null,
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
  async created() {
    console.log('userIdCreated', this.userId);
    if (this.userId) {
      try {
        const userData = await this.$store.dispatch('user/fetchUserById', this.userId);
        this.user = {
          identifier: userData.identifier || '',
          firstName: userData.firstName || '',
          lastName: userData.lastName || '',
          company: userData.company || '',
          email: userData.email || '',
          role: userData.role || 'USER_ROLE',
          password: '', // Ne pas pré-remplir le mot de passe
        };
      } catch (error) {
        console.error('Erreur lors du chargement des données de l\'utilisateur:', error);
        this.form.error = 'Erreur lors du chargement des données.';
      }
    }
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
        if (this.userId) {
          await this.$store.dispatch('auth/updateUser', { id: this.userId, data });
        } else {
          await this.$store.dispatch('user/register', data);
        }
        this.$router.push({ name: 'users' });
      } catch (e) {
        this.form.error = e.response?.data?.message || 'L\'opération a échoué. Veuillez réessayer.';
        console.error('Erreur lors de l\'envoi du formulaire:', this.form.error);
      }
    }, /*
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
        if (this.userId) {
          await this.$store.dispatch('auth/updateUser', { id: this.userId, data });
        } else {
          await this.$store.dispatch('user/register', data);
        }
        this.$router.push({ name: 'users' });
      } catch (e) {
        this.form.error = e.response?.data?.message || 'Operation failed.';
        console.error(this.form.error);
      }

      /* try {
        await this.$store.dispatch('user/register', data);
        this.$router.push({ name: 'login' });
      } catch (e) {
        this.form.error = e.response?.data?.message || 'Registration failed.';
        console.error(this.form.error);
      }
    }, */
  },
};
</script>
<style scoped>
.user-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.user-form .form-label {
  font-weight: bold;
  margin-bottom: 0.25rem;
  display: inline-block;
}

.user-form .form-control,
.user-form .form-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.user-form .row {
  margin-bottom: 1rem;
}

.user-form .btn {
  padding: 0.5rem 1.5rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>

<!--<style scoped>
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
-->
