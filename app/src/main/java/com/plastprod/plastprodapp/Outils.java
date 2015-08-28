package com.plastprod.plastprodapp;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Laurent on 15/06/2015.
 */
public class Outils {

    static int duree = Toast.LENGTH_LONG;

    //Pour vérifier si la session de l'utilisateur est toujours bonne
    public static Boolean VerifierSession(Context contexte){

        Boolean estverifie = true;
        Date now = new Date();

        final Global jeton = (Global) contexte;

        if( now.compareTo(jeton.getExpiration()) > 0 ){
            //la session est expiré
            estverifie = false;
        }

        return estverifie;
    }

    public static void afficherToast(Context contexte, String message){

        Toast notification = Toast.makeText(contexte, message, duree);
        notification.show();

    }

    public static String recupererAuteur(Context contexte){

        final Global jeton = (Global) contexte;

        return jeton.getUtilisateur().toString();

    }

    public static String premiereLettreEnMajuscule(String chaine){

        String sortie;

        sortie = chaine.substring(0, 1).toUpperCase() + chaine.substring(1);

        return  sortie;
    }

    public static String miseEnMajuscule(String chaine){

        String sortie;

        sortie = chaine.toUpperCase();

        return sortie;
    }

    public static boolean verifierCodePostal(String cp){

        boolean retour = false;

        if( cp.matches("^\\d{4,10}$") ){
            retour = true;
        }

        return  retour;
    }

    public static boolean verifierNumero(String numero){

        boolean retour = false;

        if( numero.matches("^\\+(?:[0-9] ?){6,14}[0-9]$") ){
            retour = true;
        }

        return retour;
    }

    public static boolean verifierEmail(String mail){

        boolean retour = false;

        mail = mail.toUpperCase();

        if( mail.matches("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b") ){
            retour = true;
        }

        return retour;
    }

    public static Date chaineVersDate(String chaine){

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = format.parse(chaine);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String DateJourMoisAnnee(Date date){

        String datefinale = "";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

        datefinale = dateformat.format(date);

        return datefinale;
    }
}
