# Tradefood Backend

Ce projet est le backend de l'application **Tradefood**, développé par **Perrine Honoré**. Il s'agit d'une plateforme e-commerce robuste conçue pour gérer les produits, les commandes et les clients, avec une intégration poussée vers des outils tiers.

## 🚀 Fonctionnalités principales

- **Gestion des utilisateurs** : Inscription, connexion et gestion sécurisée des profils (JWT).
- **Catalogue de produits** : Gestion complète des produits.
- **Panier d'achat** : Système de panier persistant.
- **Gestion des commandes** : Tunnel d'achat complet.
- **Paiements en ligne** : Intégration avec **Stripe** pour des transactions sécurisées.
- **Synchronisation CRM** : Intégration avec **Sellsy** pour la synchronisation des contacts, produits et commandes.

## 🛠 Technologies utilisées

Le projet repose sur une stack moderne basée sur Java et l'écosystème Spring :

- **Langage** : Java 17
- **Framework** : Spring Boot 3.4.1
- **Sécurité** : Spring Security & JSON Web Token (JWT)
- **Base de données** : MySQL
- **Persistence** : Spring Data JPA / Hibernate
- **Paiement** : Stripe Java SDK
- **Intégration API** : Spring Webflux (WebClient) pour les appels Sellsy
- **Mapping** : ModelMapper
- **Build** : Maven

## ⚙️ Configuration

Le projet nécessite une configuration via le fichier `application.properties` pour les éléments suivants :
- Connexion à la base de données MySQL.
- Identifiants API Sellsy.
- Clés API Stripe.
- Secret JWT pour la signature des tokens.

---
Développé par **Perrine Honoré** pour **Tradefood**.
