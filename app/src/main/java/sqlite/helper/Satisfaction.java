package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Satisfaction {

    int id;
    String nom;
    Date date_envoi;
    Date date_recu;

    public Satisfaction() {
    }

    public Satisfaction(String nom, Date date_envoi) {
        this.nom = nom;
        this.date_envoi = date_envoi;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Date getDate_envoi() {
        return date_envoi;
    }

    public Date getDate_recu() {
        return date_recu;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate_envoi(Date date_envoi) {
        this.date_envoi = date_envoi;
    }

    public void setDate_recu(Date date_recu) {
        this.date_recu = date_recu;
    }
}
