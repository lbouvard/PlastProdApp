package sqlite.helper;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Compte {

    int id;
    String nom;
    String mdp;
    String email;
    String salt;
    Boolean actif;

    //Constructeurs
    public Compte(){
    }

    public Compte(String nom, String mdp, String email, String salt, Boolean actif) {
        this.nom = nom;
        this.mdp = mdp;
        this.email = email;
        this.salt = salt;
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public int getId() {
        return id;
    }
}
