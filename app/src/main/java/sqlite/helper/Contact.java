package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Contact {

    int id;
    int id_societe;
    int id_compte;
    String nom;
    String prenom;
    String poste;
    String tel_fixe;
    String tel_mobile;
    String fax;
    String email;
    String adresse;
    String code_postal;
    String ville;
    String pays;
    String commentaire;
    Date date_modif;
    Boolean bit_modif;

    //Constructeurs
    public Contact(){
    }

    public Contact(String nom, String prenom, String poste, String tel_fixe, String tel_mobile, String fax, String email, String adresse, String code_postal, String ville, String pays, String commentaire, Date date_modif, Boolean bit_modif) {
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.tel_fixe = tel_fixe;
        this.tel_mobile = tel_mobile;
        this.fax = fax;
        this.email = email;
        this.adresse = adresse;
        this.code_postal = code_postal;
        this.ville = ville;
        this.pays = pays;
        this.commentaire = commentaire;
        this.date_modif = date_modif;
        this.bit_modif = bit_modif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getTel_fixe() {
        return tel_fixe;
    }

    public void setTel_fixe(String tel_fixe) {
        this.tel_fixe = tel_fixe;
    }

    public String getTel_mobile() {
        return tel_mobile;
    }

    public void setTel_mobile(String tel_mobile) {
        this.tel_mobile = tel_mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }

    public int getId_compte() {
        return id_compte;
    }

    public void setId_compte(int id_compte) {
        this.id_compte = id_compte;
    }
}
