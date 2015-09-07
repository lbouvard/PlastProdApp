package sqlite.helper;

/**
 * Created by Laurent on 07/09/2015.
 */
public class Livraison {

    int idt;
    String nomSociete;
    String transporteur;
    String track;
    String dateDispo;
    String dateEnvoi;
    String dateRecu;

    public int getIdt() {
        return idt;
    }

    public void setIdt(int idt) {
        this.idt = idt;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getTransporteur() {
        return transporteur;
    }

    public void setTransporteur(String transporteur) {
        this.transporteur = transporteur;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getDateDispo() {
        return dateDispo;
    }

    public void setDateDispo(String dateDispo) {
        this.dateDispo = dateDispo;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getDateRecu() {
        return dateRecu;
    }

    public void setDateRecu(String dateRecu) {
        this.dateRecu = dateRecu;
    }
}
