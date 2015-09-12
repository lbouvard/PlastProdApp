package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Reponse {

    int id;
    int id_satisfaction;
    String question;
    String reponse;
    String categorie;
    String type;
    int niveau;

    public Reponse() {
    }

    public Reponse(String question, String reponse, String categorie) {
        this.question = question;
        this.reponse = reponse;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getReponse() {
        return reponse;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getId_satisfaction() {
        return id_satisfaction;
    }

    public void setId_satisfaction(int id_satisfaction) {
        this.id_satisfaction = id_satisfaction;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
}
