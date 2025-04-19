package com.example.callerapp;

public class Contact {
    private int id;
    private String nom;
    private String pseudo;
    private String num;

    public Contact(int id, String nom, String pseudo, String num) {
        this.id = id;
        this.nom = nom;
        this.pseudo = pseudo;
        this.num = num;
    }

    public Contact(String nom, String pseudo, String num) {
        this.nom = nom;
        this.pseudo = pseudo;
        this.num = num;
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