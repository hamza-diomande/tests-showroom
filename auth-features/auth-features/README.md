# OCDM auth-feature

## Prérequis
- JAVA 17
- Maven 3
- Node 18.16.0
- NPM 9.5.1

## Installation
Télécharger les dépendances via `mvn clean install`

## Executer le projet avec une BDD H2 embedded (rien à installer)
Créer une configuration par défaut dans votre IDE favori pour lancer l'application spring-boot via le main de la classe **AuthFeaturesApplication** avec les profiles suivants :
- dev-auth
- h2-auth

Ou lancer le projet avec `mvn spring-boot:run -Dspring-boot.run.profiles=dev-auth,h2-auth`

Par defaut le serveur se lance sur le port 1987.
Le front end est servi de manière statique à la racine de l'url du backend.

### Executer le projet avec une BDD PostgreSQL
Prérequis :
- un serveur PostgreSQL (testé avec PostgreSQL v15.2)
  - créer un utilisateur BDD accessible via mot de passe
  - créer un schéma vide
  - mettre à jour `application-dev.yml` avec les informations de connexion à la BDD

Suivre la même procédure que pour lancer sous H2 mais avec le profil `dev` uniquement (au lieu de `dev` et `h2`)

## Commandes du projet front end
Il est possible de lancer le frontend avec npm sur un port spécifique. Cela nécessite que le backend soit déjà lancé et permet de visualiser les modifications apportées au code Vue.js sans avoir besoin de rebuilder et de relancer le serveur (hot reload)

Les commandes suivantes sont à lancer depuis le répertoire `src/frontend`

### Installation
```
npm install
```

### Compilation et hot reload
Par defaut le front sera disponible sur le port 1504.
```
npm run serve
```

### Compilation et minification pour la production
```
npm run build
```

### Correction du code pour le linter
```
npm run lint
```

## Installation alternative avec docker compose
### Lancer un environnement de dev
```
docker compose -f docker-compose-dev.yml up
```

### Stopper l'environnement
```
docker compose -f docker-compose-dev.yml down
```

Cet environnement utilise une base de données PostgreSQL 15.1 avec persistance des données dans le répertoire `pgdata` à la racine du projet.


### Utilisateur
Un utilisateur admin est créé automatique au lancement de l'application :

Email : `admin@admin.com`  
Mot de passe : `admin`
