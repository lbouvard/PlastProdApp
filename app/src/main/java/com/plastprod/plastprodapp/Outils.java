package com.plastprod.plastprodapp;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Laurent on 15/06/2015.
 */
public class Outils {

    static int duree = Toast.LENGTH_LONG;
    static final String SSID = "WiredSSID";

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
}
