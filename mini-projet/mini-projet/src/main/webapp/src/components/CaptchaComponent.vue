<template>
  <div>
    <div
      id="captcha"
      ref="captcha"
      class="captcheck_container"
      @click="setCaptchaValue($event)"
      @keyup="setCaptchaValue($event)"
    />
  </div>
</template>
<script>
export default {
  name: 'CaptchaComponent',
  props: {
    session: {
      type: String,
      default: null,
    },
    answer: {
      type: String,
      default: null,
    },
  },
  emits: ['update:session', 'update:answer'],
  data() {
    return {
      loading: false,
    };
  },
  computed: {
    captchaSession: {
      get() {
        return this.session;
      },
      set(value) {
        this.$emit('update:session', value);
      },
    },
    captchaAnswer: {
      get() {
        return this.answer;
      },
      set(value) {
        this.$emit('update:answer', value);
      },
    },
  },
  methods: {
    async reload() {
      this.loading = true;
      await window.refreshCaptcha();
      this.loading = false;
    },

    setCaptchaValue(event) {
      const sessionInput = this.$refs.captcha.querySelector('input[name="captcheck_session_code"]');
      this.captchaSession = sessionInput?.value;

      const captcheckModeText = event.target.name;

      // Text Mode
      if (captcheckModeText === 'captcheck_selected_answer') {
        if (event.target.type === 'text' && event.target.localName === 'input') {
          this.captchaAnswer = event.target.value;
        }
      } else if (event.target.localName === 'img') {
        this.captchaAnswer = event.target.dataset.answer;
      }
    },
  },
};

</script>
