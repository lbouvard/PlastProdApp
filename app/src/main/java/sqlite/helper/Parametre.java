package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Parametre {

    int id;
    int id_compte;
    String nom;
    String type;
    String libelle;
    String valeur;

    public Parametre(){

    }

    public Parametre(String nom, String type, String libelle, String valeur) {
        this.nom = nom;
        this.type = type;
        this.libelle = libelle;
        this.valeur = valeur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public int getId_compte() {
        return id_compte;
    }

    public void setId_compte(int id_compte) {
        this.id_compte = id_compte;
    }
}
