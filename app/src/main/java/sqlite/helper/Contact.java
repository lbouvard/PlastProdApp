package sqlite.helper;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Contact implements Serializable{

    private int id;
    private String nom;
    private String prenom;
    private String poste;
    private String tel_fixe;
    private String tel_mobile;
    private String fax;
    private String email;
    private String adresse;
    private String code_postal;
    private String ville;
    private String pays;
    private String commentaire;
    private String auteur;
    private Societe societe;

    private int id_societe;
    private int id_utilisateur;

    private boolean EstSelectionne;
    private boolean ASupprimer;

    //Constructeurs
    public Contact(){
        this.EstSelectionne = false;
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

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
        this.id_societe = societe.getId();
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelectionne() {
        return EstSelectionne;
    }

    public void setIsSelectionne(boolean estSelectionne) {
        EstSelectionne = estSelectionne;
    }

    public boolean isASupprimer() {
        return ASupprimer;
    }

    public void setASupprimer(boolean ASupprimer) {
        this.ASupprimer = ASupprimer;
    }

    @Override
    public String toString(){
        return this.nom + " " + this.prenom;
    }

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}
