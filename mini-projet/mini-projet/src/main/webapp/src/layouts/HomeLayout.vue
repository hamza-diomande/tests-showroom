<template>
  <div class="home-layout">
    <!-- Header avec le logo de l'entreprise -->
    <header class="header">
      <img src="../assets/images/ocdm-logo.svg" alt="Logo de l'entreprise" class="logo">
    </header>

    <!-- Section de présentation du projet -->
    <section class="project-presentation">
      <div class="container my-5">
        <h1>Bienvenue sur notre projet de test</h1>
        <p>
          Notre projet de test met en lumière plusieurs fonctionnalités
          clés ainsi que des tests unitaires approfondis
          tant au niveau frontend qu'au niveau backend.
        </p>

        <h2>Fonctionnalités clés</h2>
        <ul>
          <li>La fonctionnalité d'authentification</li>
          <li>Le captcheck : interactions externe</li>
          <li>La fonctionnalité document</li>
        </ul>

      </div>
    </section>

    <slot />

    <!-- Autres sections ou composants peuvent être ajoutés ici -->

    <!-- Footer -->
    <!-- Footer -->
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 Votre Entreprise. Tous droits réservés.</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'HomeLayout',
};
</script>

  <style scoped>
  /* Styles spécifiques au layout */
  .header {
    background-color: #f8f9fa;
    padding: 20px;
    text-align: center;
  }

  .logo {
    max-width: 200px;
  }

  .project-presentation {
    background-color: #e9ecef;
    padding: 50px 0;
    text-align: center;
  }

  .footer {
    background-color: #343a40;
    color: #fff;
    padding: 20px 0;
    text-align: center;
  }
  </style>
