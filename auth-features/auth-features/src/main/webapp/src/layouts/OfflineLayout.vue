<template>
  <div class="grid min-h-full">
    <div id="left" class="col-12 md:col-8">
      <div class="p-6">
        <div id="header" class="mb-3">
          <img alt="logo" src="../assets/images/ocdm-logo.svg">
        </div>
        <slot />

      </div>
    </div>

    <div id="right" class="col-4 hidden md:inline-flex min-h-full" />
  </div>
</template>

<script>

export default {
  name: 'OfflineLayout',
};
</script>

<style lang="scss" scoped>

#left, #right {
  display: inline-block;
  vertical-align: top;
}

#left {
  nav {
    background-color: #efefef;
    border-bottom: 1px solid lightgray;

    a{
      text-decoration: none;
      margin-right: 20px;
    }
  }
}

#right {
  background-image: url("../assets/images/contact-unsplash.jpg");
  background-size: cover;
}
</style>
