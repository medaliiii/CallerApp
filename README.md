# 📱 CallerApp - Gestion de Contacts Android

Application mobile native Android (Java) pour gérer vos contacts avec validation des numéros via API, notifications et galerie photo.

## ✨ Fonctionnalités
- **Authentification utilisateur** (connexion/déconnexion)
- **Gestion complète des contacts** (CRUD)
- **Validation des numéros** via API NumVerify
- **Avatar personnalisé** généré via DiceBear API
- **Recherche instantanée** dans la liste
- **Notifications locales** (bienvenue, actions)
- **Appel téléphonique** intégré
- **Galerie photo** pour contacts

## 🛠 Stack Technique
- **Langage**: Java
- **Base de données**: SQLite (Room pourrait être utilisé en évolution)
- **API Consommées**:
  - NumVerify (validation numéro)
  - DiceBear (génération avatar)
- **Librairies**:
  - Retrofit (REST client)
  - Picasso (image loading)
## 🔐 Permissions Android
- **Réseau**:
  - `INTERNET` (accès au web)
- **Contacts**:
  - `CALL_PHONE` (passer des appels)
- **Stockage**:
  - `READ_EXTERNAL_STORAGE` (accès galerie)
  - `READ_MEDIA_IMAGES` (Android 13+)
- **Notifications**:
  - `POST_NOTIFICATIONS` (Android 13+)


## 🚀 Installation
1. Cloner le dépôt :
```bash
git clone https://github.com/medaliiii/CallerApp

Ouvrir avec Android Studio

Configurer :

Clé API NumVerify dans Ajout.java

Exécuter sur émulateur/device (Android 6.0+)
