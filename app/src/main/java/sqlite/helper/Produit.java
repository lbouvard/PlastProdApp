package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Produit {

    int id;
    String nom;
    String description;
    String categorie;
    String code;
    Double prix;

    public Produit() {
    }

    public Produit(String nom, String description, String categorie, String code, Double prix) {
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.code = code;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getCode() {
        return code;
    }

    public Double getPrix() {
        return prix;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
