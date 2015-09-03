package sqlite.helper;

/**
 * Created by Laurent on 02/09/2015.
 */
public class Statistique {

    int nbCommande;
    int nbDevis;
    int nbCommandeTermine;
    int nbCommandePrepare;
    int nbDevisEtCommande;

    public int getNbCommande() {
        return nbCommande;
    }

    public void setNbCommande(int nbCommande) {
        this.nbCommande = nbCommande;
    }

    public int getNbDevis() {
        return nbDevis;
    }

    public void setNbDevis(int nbDevis) {
        this.nbDevis = nbDevis;
    }

    public int getNbCommandeTermine() {
        return nbCommandeTermine;
    }

    public void setNbCommandeTermine(int nbCommandeTermine) {
        this.nbCommandeTermine = nbCommandeTermine;
    }

    public int getNbCommandePrepare() {
        return nbCommandePrepare;
    }

    public void setNbCommandePrepare(int nbCommandePrepare) {
        this.nbCommandePrepare = nbCommandePrepare;
    }

    public int getNbDevisEtCommande() {
        return nbDevisEtCommande;
    }

    public void setNbDevisEtCommande(int nbDevisEtCommande) {
        this.nbDevisEtCommande = nbDevisEtCommande;
    }
}
