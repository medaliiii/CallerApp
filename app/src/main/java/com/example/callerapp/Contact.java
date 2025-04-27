    package com.example.callerapp;

    public class Contact {
        private int id;
        private String nom;
        private String pseudo;
        private String num;
        private String avatarUrl;
        private String localPhotoUri;

        public Contact(int id, String nom, String pseudo, String num,String avatarUrl) {
            this.id = id;
            this.nom = nom;
            this.pseudo = pseudo;
            this.num = num;
            this.avatarUrl = avatarUrl;
        }
        public String getLocalPhotoUri() { return localPhotoUri; }
        public void setLocalPhotoUri(String uri) { this.localPhotoUri = uri; }

        public Contact(String nom, String pseudo, String num) {
            this.nom = nom;
            this.pseudo = pseudo;
            this.num = num;
        }
        public String getAvatarUrl() {
            return avatarUrl;
        }
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPseudo() {
            return pseudo;
        }

        public void setPseudo(String pseudo) {
            this.pseudo = pseudo;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return "Contact{" +
                    ", nom='" + nom + '\'' +
                    ", pseudo='" + pseudo + '\'' +
                    ", num='" + num + '\'' +
                    '}';
        }
    }