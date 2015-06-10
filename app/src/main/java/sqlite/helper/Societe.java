package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Societe {

    int id;
    String nom;
    String adresse1;
    String adresse2;
    String code_postal;
    String ville;
    String pays;
    String commentaire;
    Date date_modif;
    Boolean bit_modif;

    //Constructeurs
    public Societe() {

    }

    public Societe(String nom, String adresse1, String adresse2, String code_postal, String ville, String pays, String commentaire, Date date_modif, Boolean modif) {
        this.nom = nom;
        this.adresse1 = adresse1;
        this.adresse2 = adresse2;
        this.code_postal = code_postal;
        this.ville = ville;
        this.pays = pays;
        this.commentaire = commentaire;
        this.date_modif = date_modif;
        this.bit_modif = modif;
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
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
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

    public Date getDate_modif() {
        return date_modif;
    }

    public void setDate_modif(Date date_modif) {
        this.date_modif = date_modif;
    }

    public Boolean getBit_modif() {
        return bit_modif;
    }

    public void setBit_modif(Boolean bit_modif) {
        this.bit_modif = bit_modif;
    }

    public int getId() {
        return id;
    }
}
