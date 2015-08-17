package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Satisfaction {

    int id;
    int id_societe;
    String nom;
    String date_envoi;
    String date_recu;

    public Satisfaction() {
    }

    public int getId() {
        return id;
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
}
