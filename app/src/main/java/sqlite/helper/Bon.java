package sqlite.helper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Bon implements Serializable{

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String numero_commande;
    private String date_commande;
    private String etat_commande;
    private String commentaire;
    private String type;
    private String suivi;
    private String transporteur;
    private String auteur;
    private String date_changement;
    private boolean aChange;

    private List<LigneCommande> lignesBon;

    private Societe client;
    private Contact commercial;

    private boolean ASupprimer;
    private int devis_id = 0;
    private int client_id;
    private int commercial_id;

    public Bon(String type) {
        this.type = type;
        this.lignesBon = new ArrayList<LigneCommande>();
    }

    public Bon(){
        this.lignesBon = new ArrayList<LigneCommande>();
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

    public List<LigneCommande> getLignesBon() {
        return lignesBon;
    }

    public void setLignesBon(List<LigneCommande> lignesBon) {
        this.lignesBon = lignesBon;
    }

    public Societe getClient() {
        return client;
    }

    public void setClient(Societe client) {
        this.client = client;
    }

    public Contact getCommercial() {
        return commercial;
    }

    public void setCommercial(Contact commercial) {
        this.commercial = commercial;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public boolean isASupprimer() {
        return ASupprimer;
    }

    public void setASupprimer(boolean ASupprimer) {
        this.ASupprimer = ASupprimer;
    }

    public boolean isAChange(){
        return aChange;
    }

    public void setAChange(boolean aChange) {
        this.aChange = aChange;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getCommercial_id() {
        return commercial_id;
    }

    public void setCommercial_id(int commercial_id) {
        this.commercial_id = commercial_id;
    }

    public BigDecimal calculerPrixTotal(){

        BigDecimal total = new BigDecimal(0.00);

        for(LigneCommande ligne : this.lignesBon){
            total = total.add(ligne.getPrixTotal());
        }

        return total;
    }

    public int getDevis_id() {
        return devis_id;
    }

    public void setDevis_id(int devis_id) {
        this.devis_id = devis_id;
    }

    public String getNumero_commande() {
        return numero_commande;
    }

    public void setNumero_commande(String numero_commande) {
        this.numero_commande = numero_commande;
    }
}
