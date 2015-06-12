package sqlite.helper;

import java.util.Date;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Stock {

    private int id;
    private int quantite;
    private int delaisMoy;
    private int delais;

    public Stock() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getDelaisMoy() {
        return delaisMoy;
    }

    public void setDelaisMoy(int delaisMoy) {
        this.delaisMoy = delaisMoy;
    }

    public int getDelais() {
        return delais;
    }

    public void setDelais(int delais) {
        this.delais = delais;
    }
}
