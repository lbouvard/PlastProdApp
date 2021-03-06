package sqlite.helper;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Societe implements Serializable {

    private int id;
    private String nom;
    private String adresse1;
    private String adresse2;
    private String codePostal;
    private String ville;
    private String pays;
    private String type;
    private String commentaire;
    private String auteur;
    private String couleur;

    private int nb_contact;
    private boolean EstSelectionne;
    private boolean ASupprimer;

    //Constructeurs
    public Societe(){
        this.EstSelectionne = false;
        this.nb_contact = 0;
    }

    public Societe(String auteur) {
        this.auteur = auteur;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getCode_postal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNb_contact() {
        return nb_contact;
    }

    public void setNb_contact(int nb_contact) {
        this.nb_contact = nb_contact;
    }

    public boolean isSelectionne() {
        return EstSelectionne;
    }

    public void setIsSelectionne(boolean estSelectionne) {
        EstSelectionne = estSelectionne;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public boolean isASupprimer() {
        return ASupprimer;
    }

    public void setASupprimer(boolean ASupprimer) {
        this.ASupprimer = ASupprimer;
    }

    @Override
    public String toString(){
        return nom;
    }
}
