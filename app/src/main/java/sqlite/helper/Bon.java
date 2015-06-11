package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Bon {

    int id;
    int id_societe;
    int id_contact;
    String date_commande;
    String etat_commande;
    String type;
    String suivi;
    String transporteur;
    String date_changement;
    Boolean change;

    public Bon() {
    }

    public Bon(String date_commande, String etat_commande, String type) {
        this.date_commande = date_commande;
        this.etat_commande = etat_commande;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getDate_commande() {
        return date_commande;
    }

    public String getEtat_commande() {
        return etat_commande;
    }

    public String getType() {
        return type;
    }

    public String getSuivi() {
        return suivi;
    }

    public String getTransporteur() {
        return transporteur;
    }

    public String getDate_changement() {
        return date_changement;
    }

    public Boolean getChange() {
        return change;
    }

    public void setDate_commande(String date_commande) {
        this.date_commande = date_commande;
    }

    public void setEtat_commande(String etat_commande) {
        this.etat_commande = etat_commande;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSuivi(String suivi) {
        this.suivi = suivi;
    }

    public void setTransporteur(String transporteur) {
        this.transporteur = transporteur;
    }

    public void setDate_changement(String date_changement) {
        this.date_changement = date_changement;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }

    public int getId_contact() {
        return id_contact;
    }

    public void setId_contact(int id_contact) {
        this.id_contact = id_contact;
    }
}
