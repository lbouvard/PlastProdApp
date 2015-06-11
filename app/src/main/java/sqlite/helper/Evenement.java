package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Evenement {

    private int id;
    private Date date_debut;
    private Date date_fin;
    private String reccurent;
    private String frequence;
    private String titre;
    private String emplacement;
    private String disponibilite;
    private String commentaire;
    private Boolean est_prive;
    private Compte compte;

    public Evenement() {
    }

    public int getId() {
        return id;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public String getReccurent() {
        return reccurent;
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

    public Boolean getEst_prive() {
        return est_prive;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public void setReccurent(String reccurent) {
        this.reccurent = reccurent;
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

    public void setEst_prive(Boolean est_prive) {
        this.est_prive = est_prive;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }
}
