<template>
  <form>
    <label for="dummy-name">Name (min5, max 50)</label>
    <input
      id="dummy-name"
      v-model="form.name"
      type="text"
    >
    <captcha-component
      ref="captcha"
      v-model:answer="form.captcha.answer"
      v-model:session="form.captcha.session"
    />
    <button
      type="button"
      @click="createDummyCaptcha"
    >
      Create
    </button>

    <p
      v-if="form.error"
      class="error"
    >
      {{ form.error }}
    </p>
  </form>
</template>

<script>
import CaptchaComponent from '@/components/CaptchaComponent.vue';

export default {
  name: 'CreateDummyForm',
  components: {
    CaptchaComponent,

  },
  emits: ['onCreateDummy'],
  data() {
    return {
      form: {
        captcha: {
          answer: '',
          session: '',
        },
        name: '',
        error: '',
      },
    };
  },
  methods: {
    async createDummy() {
      try {
        const created = await this.$dummyService.create(this.form.name, this.form.privateField);
        if (created) {
          this.$emit('onCreateDummy', created);
          this.form.name = '';
        }
      } catch (err) {
        this.form.error = err.response.data;
      }
    },
    async createDummyCaptcha() {
      try {
        const created = await this.$dummyService.createWithCaptcha(
          this.form.name,
          this.form.captcha.session,
          this.form.captcha.answer,
          this.form.privateField,
        );
        if (created) {
          this.$emit('onCreateDummy', created);
          this.form.name = '';
          this.form.captcha.answer = '';
          this.form.captcha.session = '';
          window.location.reload();
        }
      } catch (err) {
        this.form.error = err.response.data;
      }
    },
  },
};
</script>

<style scoped>
form{
  margin: 10px 0;
}
input, label{
  margin-right: 10px;
}

.error {
  color: red;
}
</style>
