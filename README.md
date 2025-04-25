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
- **Permissions**:
  - READ_EXTERNAL_STORAGE
  - POST_NOTIFICATIONS
  - CALL_PHONE

## 🖼 Captures d'écran
<div align="center">
  <img src="screenshots/login_screen.png" width="200" alt="Écran de connexion">
  <img src="screenshots/contact_list.png" width="200" alt="Liste contacts"> 
  <img src="screenshots/add_contact.png" width="200" alt="Ajout contact">
</div>

## 🚀 Installation
1. Cloner le dépôt :
```bash
git clone https://github.com/votre-username/CallerApp.git
