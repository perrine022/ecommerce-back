# Tradefood Backend

Ce projet est le backend de l'application **Tradefood**, développé par **Perrine Honoré**. Il s'agit d'une plateforme e-commerce robuste conçue pour gérer les produits, les commandes et les clients, avec une intégration poussée vers des outils tiers (Sellsy, Stripe).

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :
- **Java 17** ou version ultérieure
- **Maven 3.8+**
- **MySQL 8.0+**
- Un compte **Sellsy** et **Stripe** pour obtenir les clés API nécessaires.

## ⚙️ Configuration

1. **Base de données** :
   Créez une base de données MySQL localement :
   ```sql
   CREATE DATABASE tradefood;
   ```

2. **Fichier de configuration** :
   Vérifiez et adaptez le fichier `src/main/resources/application.properties` :
   ```properties
   # Base de données
   spring.datasource.url=jdbc:mysql://localhost:3306/tradefood
   spring.datasource.username=root
   spring.datasource.password=password

   # Sellsy API
   sellsy.client-id=VOTRE_CLIENT_ID
   sellsy.client-secret=VOTRE_CLIENT_SECRET

   # Stripe API
   stripe.api.key=VOTRE_STRIPE_KEY
   stripe.webhook.secret=VOTRE_WEBHOOK_SECRET

   # JWT
   jwt.secret=VOTRE_SECRET_JWT
   ```

## 🚀 Lancement du projet

Pour lancer l'application en mode développement :

```bash
# Utiliser le wrapper Maven fourni
./mvnw spring-boot:run
```

L'application sera disponible sur `http://localhost:8080`.

### Compilation et exécution du JAR

```bash
# Compiler le projet
./mvnw clean package

# Lancer l'exécutable
java -jar target/tradefood-0.0.1-SNAPSHOT.jar
```

## 🧪 Tests

Pour exécuter les tests unitaires et d'intégration :

```bash
./mvnw test
```

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

## 📂 Structure du projet

- `controller/` : Contrôleurs REST gérant les requêtes HTTP.
- `service/` : Couche de services contenant la logique métier.
- `entity/` : Entités JPA représentant les tables de la base de données.
- `repository/` : Interfaces Spring Data JPA pour l'accès aux données.
- `dto/` : Objets de transfert de données (Data Transfer Objects).
- `config/` : Classes de configuration (Sécurité, Sellsy, etc.).
- `security/` : Gestion de l'authentification JWT.

---
Développé par **Perrine Honoré** pour **Tradefood**.
