package sqlite.helper;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Satisfaction implements Serializable{

    int id;
    int id_societe;
    String nom;
    String date_envoi;
    String date_recu;
    String corps;
    String lien;
    String contact;

    public Satisfaction() {
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

    public String getDate_envoi() {
        return date_envoi;
    }

    public String getDate_recu() {
        return date_recu;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate_envoi(String date_envoi) {
        this.date_envoi = date_envoi;
    }

    public void setDate_recu(String date_recu) {
        this.date_recu = date_recu;
    }

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
