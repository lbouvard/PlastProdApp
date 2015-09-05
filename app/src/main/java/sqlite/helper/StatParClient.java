package sqlite.helper;

/**
 * Created by Laurent on 02/09/2015.
 */
public class StatParClient {

    String nom;
    Statistique stat;
    String type;        // 'C' pour client, 'P' pour prospect

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Statistique getStat() {
        return stat;
    }

    public void setStat(Statistique stat) {
        this.stat = stat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
