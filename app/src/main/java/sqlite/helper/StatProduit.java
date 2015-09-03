package sqlite.helper;

/**
 * Created by Laurent on 02/09/2015.
 */
public class StatProduit {

    String nomProduit;
    String codeProduit;
    int nbProduitVendu;

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public int getNbProduitVendu() {
        return nbProduitVendu;
    }

    public void setNbProduitVendu(int nbProduitVendu) {
        this.nbProduitVendu = nbProduitVendu;
    }

    public String getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
    }
}
