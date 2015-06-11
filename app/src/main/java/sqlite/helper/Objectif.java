package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Objectif {

    private int id;
    private String annee;
    private String type;
    private String libelle;
    private String valeur;
    private Compte compte;

    public Objectif(){
    }

    public int getId() {
        return id;
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

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }
}
