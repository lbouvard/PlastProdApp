package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Objectif {

    int id;
    int id_compte;
    String annee;
    String type;
    String libelle;
    String valeur;

    public Objectif(){
    }

    public Objectif(String annee, String type, String libelle, String valeur) {
        this.annee = annee;
        this.type = type;
        this.libelle = libelle;
        this.valeur = valeur;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
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
