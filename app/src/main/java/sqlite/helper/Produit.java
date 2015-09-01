package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Produit {

    private int id;
    private String nom;
    private String description;
    private String categorie;
    private String code;
    private Double prix;
    private int delais;

    public Produit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDelais() {
        return delais;
    }

    public void setDelais(int delais) {
        this.delais = delais;
    }

    @Override
    public String toString(){
        return code + " - " + nom;
    }
}
