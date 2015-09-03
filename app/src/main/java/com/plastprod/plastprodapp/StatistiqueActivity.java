package com.plastprod.plastprodapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.StatParClient;


public class StatistiqueActivity extends ActionBarActivity implements ChoixDate.RetourListener {

    DatabaseHelper db;
    List<StatParClient> liste_stats_client = new ArrayList<StatParClient>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_statistique, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void modifierDate(GregorianCalendar date, String type){

        EditText lDate = null;
        String jour = "";
        String mois = "";
        int iMois = 0;

        if( type.equals("DateDeb") )
            lDate = (EditText) findViewById(R.id.edStatsDateDeb);
        else
            lDate = (EditText) findViewById(R.id.edStatsDateFin);

        if( date.get(Calendar.DAY_OF_MONTH) < 10)
            jour = "0" + String.valueOf(date.get(Calendar.DAY_OF_MONTH));
        else
            jour = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

        //le premier mois commence à 0
        iMois = date.get(Calendar.MONTH);
        iMois++;

        if( iMois < 10 )
            mois = "0" + String.valueOf(iMois);
        else
            mois = String.valueOf(iMois);

        lDate.setText(jour + "/" + mois + "/" + date.get(Calendar.YEAR));
    }

    public void recupererDateDebut(View vue){

        DialogFragment dialog = new ChoixDate();

        // on passe le type en argument
        Bundle bundle = new Bundle();
        bundle.putString("Type", "DateDeb");

        dialog.setArguments(bundle);

        //affiche le date picker
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void recupererDateFin(View vue){

        DialogFragment dialog = new ChoixDate();

        // on passe le type en argument
        Bundle bundle = new Bundle();
        bundle.putString("Type", "DateFin");

        dialog.setArguments(bundle);

        //affiche le date picker
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void recupererStat(View vue){

        db = new DatabaseHelper(getApplicationContext());

        liste_stats_client = db.getStatsParClient();

        db.close();

    }
}
