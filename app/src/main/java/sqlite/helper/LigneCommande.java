package sqlite.helper;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Laurent on 10/06/2015.
 */
public class LigneCommande implements Serializable{

    private int id;
    private int id_bon;
    private int quantite;
    private String code;
    private String nom;
    private String description;
    private int remise;
    private Double prixUnitaire;
    private BigDecimal prixRemise;
    private BigDecimal prixTotal;

    private boolean ASupprimer;

    public LigneCommande() {
    }

    public int getId() {
        return id;
    }

    public int getQuantite() {
        return quantite;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_bon() {
        return id_bon;
    }

    public void setId_bon(int id_bon) {
        this.id_bon = id_bon;
    }

    public int getRemise() {
        return remise;
    }

    public void setRemise(int remise) {
        this.remise = remise;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getPrixRemise() {
        return prixRemise;
    }

    public void setPrixRemise(BigDecimal prixRemise) {
        this.prixRemise = prixRemise;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public void calculerPrixRemise(){

        if( this.remise > 0 ) {
            Double prix = this.prixUnitaire - (this.prixUnitaire * this.remise);

            this.prixRemise = new BigDecimal(prix.doubleValue());
            this.prixRemise = this.prixRemise.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        else{
            this.prixRemise = new BigDecimal(prixUnitaire.doubleValue());
        }
    }

    public void calculerPrixTotal(){

        if( remise > 0 ){
            calculerPrixRemise();
        }

        this.prixTotal = this.prixRemise.multiply(new BigDecimal(quantite));
        this.prixTotal = this.prixTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isASupprimer() {
        return ASupprimer;
    }

    public void setASupprimer(boolean ASupprimer) {
        this.ASupprimer = ASupprimer;
    }
}
