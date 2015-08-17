package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Evenement {

    private int id;
    private String date_debut;
    private String date_fin;
    private String recurrent;
    private String frequence;
    private String titre;
    private String emplacement;
    private String disponibilite;
    private String commentaire;
    private int est_prive;
    private int compte_id;

    private boolean ASupprimer;

    public Evenement() {
    }

    public int getId() {
        return id;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public String getReccurent() {
        return recurrent;
    }

    public String getFrequence() {
        return frequence;
    }

    public String getTitre() {
        return titre;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public int getEst_prive() {
        return est_prive;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public void setReccurent(String reccurent) {
        this.recurrent = reccurent;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setEst_prive(int est_prive) {
        this.est_prive = est_prive;
    }

    /*public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }*/

    public void setId(int id) {
        this.id = id;
    }

    public boolean isASupprimer() {
        return ASupprimer;
    }

    public void setASupprimer(boolean ASupprimer) {
        this.ASupprimer = ASupprimer;
    }

    public int getCompte_id() {
        return compte_id;
    }

    public void setCompte_id(int compte_id) {
        this.compte_id = compte_id;
    }
}
