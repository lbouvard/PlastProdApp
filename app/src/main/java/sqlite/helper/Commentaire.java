package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Commentaire {

    int id;
    int id_societe;
    String texte;

    public Commentaire(){
    }

    public Commentaire(String commentaire){
        this.texte = commentaire;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public int getId() {
        return id;
    }

    public int getId_societe() {
        return id_societe;
    }

    public void setId_societe(int id_societe) {
        this.id_societe = id_societe;
    }
}
