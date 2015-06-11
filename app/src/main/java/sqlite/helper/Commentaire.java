package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Commentaire {

    private int id;
    private String texte;
    private Societe client;

    public Commentaire(){
        this.texte = "";
    }

    public int getId() {
        return id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }


    public Societe getClient() {
        return client;
    }

    public void setClient(Societe client) {
        this.client = client;
    }
}
