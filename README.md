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

 ## 🔍 RecyclerView Avancé
L'application utilise un **RecyclerView** avec les fonctionnalités suivantes :

✅ **ViewHolder personnalisé**  
- Affichage des contacts avec :
  - Nom et pseudo
  - Numéro de téléphone
  - Avatar personnalisé
  - Boutons d'action (appel, modification, suppression)

✅ **Filtrage en temps réel**  
- Barre de recherche intégrée
- Filtrage par : nom, pseudo ou numéro

✅ **Gestions des clics**  
- Appel téléphonique au clic
- Modification/suppression via icônes
- Sélection de photo depuis la galerie

✅ **Chargement optimisé**  
- Utilisation de Picasso pour :
  - Chargement asynchrone des images
  - Cache mémoire et disque
  - Placeholder et erreur de chargement
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
