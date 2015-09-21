package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        format.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        try {
            date = format.parse(chaine);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String DateJourMoisAnnee(Date date){

        String datefinale = "";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        dateformat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        datefinale = dateformat.format(date);

        return datefinale;
    }

    public static String dateComplete(Date date){

        String datefinale = "";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);
        dateformat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        datefinale = dateformat.format(date);

        return datefinale;
    }

    public static int[] decoupeDate(String date){

        int[] info = new int[3];

        String[] decoupage = date.split("/");

        info[0] = getJour(decoupage[0]);
        info[1] = getMois(decoupage[1]);
        info[2] = Integer.valueOf(decoupage[2]);

        return info;
    }

    public static int getMois(String mois){

        int retour = 0;
        int valeur = 0;

        valeur = Integer.valueOf(mois);
        retour = valeur - 1;

        return retour;
    }

    public static  int getJour(String jour){

        int retour = 0;

        retour = Integer.valueOf(jour);

        return retour;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, GridLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String dateNow() {

        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        // pour les requêtes sql
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        return formatter.format(calendar.getTime());
    }
}
