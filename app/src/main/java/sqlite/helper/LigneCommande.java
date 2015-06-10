package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class LigneCommande {

    int id;
    int quantite;
    String code;
    String nom;
    String description;
    Double prix_unitaire;
    Double prix_total;

    public LigneCommande() {
    }

    public LigneCommande(int quantite, String code, String nom, String description, Double prix_unitaire, Double prix_total) {
        this.quantite = quantite;
        this.code = code;
        this.nom = nom;
        this.description = description;
        this.prix_unitaire = prix_unitaire;
        this.prix_total = prix_total;
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

    public Double getPrix_unitaire() {
        return prix_unitaire;
    }

    public Double getPrix_total() {
        return prix_total;
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

    public void setPrix_unitaire(Double prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    public void setPrix_total(Double prix_total) {
        this.prix_total = prix_total;
    }
}
