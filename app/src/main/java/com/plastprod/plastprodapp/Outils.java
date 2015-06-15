package com.plastprod.plastprodapp;

import android.content.Context;

import java.util.Date;

/**
 * Created by Laurent on 15/06/2015.
 */
public class Outils {

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
}
