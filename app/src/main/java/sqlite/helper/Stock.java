package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Stock {

    int id;
    int quantite;
    Date date_entree;
    Date date_sortie;

    public Stock() {
    }

    public Stock(int quantite, Date date_entree) {
        this.quantite = quantite;
        this.date_entree = date_entree;
    }

    public int getId() {
        return id;
    }

    public int getQuantite() {
        return quantite;
    }

    public Date getDate_entree() {
        return date_entree;
    }

    public Date getDate_sortie() {
        return date_sortie;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setDate_entree(Date date_entree) {
        this.date_entree = date_entree;
    }

    public void setDate_sortie(Date date_sortie) {
        this.date_sortie = date_sortie;
    }
}
