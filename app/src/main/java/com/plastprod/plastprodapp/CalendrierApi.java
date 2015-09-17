package com.plastprod.plastprodapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Evenement;
import sqlite.helper.Parametre;

/**
 * Created by Laurent on 08/09/2015.
 */
public class CalendrierApi extends AsyncTask<Context, Void, Void> {

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CALENDRIER_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
            CalendarContract.Calendars.ACCOUNT_TYPE                   // 4
    };

    public static final String[] EVENEMENT_PROJECTION = new String[] {
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.AVAILABILITY,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.ACCESS_LEVEL,
            CalendarContract.Events.DELETED,
            CalendarContract.Events._ID
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    /*private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_ACCOUNT_TYPE = 4;*/
    private static final int PROJECTION_DATE_DEB = 0;
    private static final int PROJECTION_DATE_FIN = 1;
    private static final int PROJECTION_FREQ = 2;
    private static final int PROJECTION_TITRE = 3;
    private static final int PROJECTION_EMPLACEMENT = 4;
    private static final int PROJECTION_DESCRIPTION = 5;
    private static final int PROJECTION_DISPONIBILITE = 6;
    private static final int PROJECTION_JOUR_ENTIER = 7;
    private static final int PROJECTION_PRIVE = 8;
    private static final int PROJECTION_SUPPRIME = 9;
    private static final int PROJECTION_EVENEMENT_ID = 10;


    // variables
    long calendrier_id;
    //connexion base
    DatabaseHelper db;
    //Contexte
    Context context;
    //Commercial
    Contact commercial;

    protected Void doInBackground(Context... arg) {

        //initialisation
        context = arg[0];

        db = new DatabaseHelper(context);
        commercial = ((Global) context).getUtilisateur();

        List<Parametre> paramCalendrier = new ArrayList<Parametre>();
        List<Evenement> liste_evenement = new ArrayList<Evenement>();

        paramCalendrier = db.getParamCalendrier(commercial.getId());

        if (paramCalendrier.size() > 0){
            recupererCalendrierId(paramCalendrier.get(0).getValeur(), paramCalendrier.get(1).getValeur());

            liste_evenement = recupererEvenements(paramCalendrier.get(0).getId());

            //enregistrerEvenements();
        }

        db.close();

        return null;
    }

    public void recupererCalendrierId(String email, String type) {

        // on récupère les comptes et on recupère l'id du calendrier paramétré dans l'application
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        String[] selectionArgs = new String[]{email, type, email};

        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, CALENDRIER_PROJECTION, selection, selectionArgs, null);

        if( cur.getCount() > 0) {
            cur.moveToFirst();

            /*while (cur.moveToNext()) {
                String displayName = null;
                String accountName = null;
                String ownerName = null;
                String accountType = null;

                // Get the field values
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                accountType = cur.getString(PROJECTION_ACCOUNT_TYPE);
            }*/

            calendrier_id = cur.getLong(PROJECTION_ID_INDEX);

            Log.d("Rendez-vous", "Calendrier " + String.valueOf(calendrier_id) + " trouvé!");
        }

        cur.close();
    }

    public List<Evenement> recupererEvenements(int id_param){

        List<Evenement> liste = new ArrayList<Evenement>();
        long date_plus_un_derniere_maj = 0;
        Cursor cur = null;
        String selection = "";
        String[] selectionArgs;

        // on regarde le nombre de jours depuis la dernière maj du calendrier
        String date_temporaire = db.getDerniereRecuperationEvenements(id_param);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Date lDate = date.parse(date_temporaire);
            date_plus_un_derniere_maj = lDate.getTime();
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        ContentResolver cr = context.getContentResolver();

        Uri uri = CalendarContract.Events.CONTENT_URI;

        if( date_plus_un_derniere_maj == 0 ) {
            date_plus_un_derniere_maj = System.currentTimeMillis();
        }

        selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " >= ?))";

        selectionArgs = new String[]{String.valueOf(calendrier_id), String.valueOf(date_plus_un_derniere_maj)};

        cur = cr.query(uri, EVENEMENT_PROJECTION, selection, selectionArgs, null);

        if( cur.getCount() > 0 ){
            while( cur.moveToNext()){

                Evenement event = new Evenement();
                event.setId(cur.getInt(PROJECTION_EVENEMENT_ID));
                event.setDate_debut(getDate(cur.getLong(PROJECTION_DATE_DEB)));
                event.setDate_fin(getDate(cur.getLong(PROJECTION_DATE_FIN)));
                event.setReccurent("");
                event.setFrequence("");
                event.setTitre(cur.getString(PROJECTION_TITRE));
                event.setEmplacement(cur.getString(PROJECTION_EMPLACEMENT));
                event.setCommentaire(cur.getString(PROJECTION_DESCRIPTION));
                event.setDisponibilite(getDisponibilite(cur.getString(PROJECTION_DISPONIBILITE)));
                event.setEst_prive( etatEntier(cur.getString(PROJECTION_PRIVE)) );
                event.setASupprimer(etatLogique(cur.getString(PROJECTION_SUPPRIME)));
                event.setCompte_id(commercial.getId());

                liste.add(event);
            }
        }

        cur.close();

        return liste;
    }

    /*private String getHeure(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }*/

    private String getDate(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private String getDisponibilite(String valeur){

        String dispo = "";

        switch(valeur){
            case "0":
                dispo = "Occupé";
                break;
            case "1":
                dispo = "Disponible";
                break;
            case "2":
                dispo = "Inconnu";
                break;
        }

        return dispo;
    }

    private boolean etatLogique(String valeur){

        boolean etat = true;

        if( valeur.equals("0")){
            etat = false;
        }

        return etat;
    }

    private int etatEntier(String valeur){

        int etat = 1;

        if( valeur.equals("0")){
            etat = 0;
        }

        return etat;
    }
}
