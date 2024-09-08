<template>
  <div class="container-fluid min-vh-100 d-flex flex-column">
    <div class="row flex-grow-1">
      <!-- Left side -->
      <div id="left" class="col-12 col-md-8 d-flex align-items-center p-4">
        <div class="w-100">
          <div id="header" class="mb-3">
            <img alt="logo" src="../assets/images/ocdm-logo.svg" class="mb-2 logo-img">
            <h1 class="h4 mb-0 title">Base stack login</h1>
          </div>
          <slot />
        </div>
      </div>

      <!-- Right side -->
      <div id="right" class="col-md-4 d-none d-md-flex align-items-center bg-cover" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'OfflineLayout',
};
</script>

<style scoped>
/* Optional: Add custom styles for layout adjustments */
#right {
  background-image: url('../assets/images/contact-unsplash.jpg');
  background-size: cover;
  background-position: center;
}

#header {
  margin-top: -20px; /* Adjust this value to move the logo up if needed */
}

.logo-img {
  max-height: 80%; /* Adjust this value if necessary */
}

.title {
  margin-top: 10px; /* Adjust this value to move the title down */
}
</style>
