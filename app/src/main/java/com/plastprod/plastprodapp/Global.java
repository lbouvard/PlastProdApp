package com.plastprod.plastprodapp;

import android.app.Application;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import sqlite.helper.Contact;

/**
 * Created by Laurent on 15/06/2015.
 */
public class Global extends Application {

    private Contact utilisateur;
    private String nom_utilisateur;
    private String mail_utilisateur;
    private Date date_connexion;
    private Date expiration;
    private int indice_bon;

    public Global() {
        this.nom_utilisateur = "";
        this.mail_utilisateur = "";
        this.utilisateur = null;
    }

    public Contact getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Contact utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setDate_connexion(Date date_connexion) {

        this.date_connexion = date_connexion;

        GregorianCalendar calendrier = new java.util.GregorianCalendar();
        calendrier.setTime(date_connexion);

        //calcul de la date d'expiration
        calendrier.add(Calendar.MINUTE, 15);

        setExpiration(calendrier.getTime());
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public String getMail_utilisateur() {
        return mail_utilisateur;
    }

    public void setMail_utilisateur(String mail_utilisateur) {
        this.mail_utilisateur = mail_utilisateur;
    }

    public int getIndice_bon() {
        return indice_bon;
    }

    public void setIndice_bon(int indice_bon) {
        this.indice_bon = indice_bon;
    }
}
