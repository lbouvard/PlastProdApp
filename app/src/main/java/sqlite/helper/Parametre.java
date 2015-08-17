package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Parametre {

    private int id;
    private String nom;
    private String type;
    private String libelle;
    private String valeur;
    private Compte compte;

    private int compte_id;

    public Parametre(){
        this.compte = null;
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

    /*public void setCompte(Compte compte) {
        this.compte = compte;
    }*/

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Compte() {
        return compte_id;
    }

    public void setId_Compte(int idtCompte) {
        this.compte_id = idtCompte;
    }
}
