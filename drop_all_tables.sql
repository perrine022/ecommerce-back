-- Script pour supprimer toutes les tables de la base de données tradefood
-- Désactivation des contraintes de clés étrangères pour permettre la suppression
SET FOREIGN_KEY_CHECKS = 0;

-- Liste des tables à supprimer (basée sur schema.sql)
DROP TABLE IF EXISTS user_marketing_campaigns;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS contact_sellsy_marketing_campaigns;
DROP TABLE IF EXISTS contacts_sellsy;
DROP TABLE IF EXISTS individuals_sellsy;
DROP TABLE IF EXISTS companies_sellsy;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Réactivation des contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Base de données nettoyée avec succès !' AS message;
