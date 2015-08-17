package sqlite.helper;

import java.math.BigDecimal;

/**
 * Created by Laurent on 10/06/2015.
 */
public class LigneCommande {

    private int id;
    private int id_bon;
    private int quantite;
    private String code;
    private String nom;
    private String description;
    private Double remise;
    private Double prixUnitaire;
    private BigDecimal prixRemise;
    private BigDecimal prixTotal;

    private boolean ASupprimer;

    public LigneCommande() {
    }

    public LigneCommande(int quantite, String code, String nom, String description, Double prix_unitaire) {
        this.quantite = quantite;
        this.code = code;
        this.nom = nom;
        this.description = description;
        this.remise = 0.0;
        this.prixUnitaire = prix_unitaire;
        calculerPrixTotal();
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

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
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

        if( remise != 0.0 ){
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
