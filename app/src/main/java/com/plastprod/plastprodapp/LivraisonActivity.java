package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Livraison;
import sqlite.helper.Societe;


public class LivraisonActivity extends ActionBarActivity {

    LivraisonAdaptateur adaptateur;
    ExpandableListView vue;
    List<Livraison> liste_suivi = new ArrayList<Livraison>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on utilise le layout de l'activité
        setContentView(R.layout.activity_livraison);

        //on récupère la liste
        vue = (ExpandableListView) findViewById(R.id.lvListeLivraison);

        //recupération des bons du client demandé
        db = new DatabaseHelper(getApplicationContext());
        liste_suivi = db.getSuiviLivraison();

        //et on alimente la liste
        adaptateur = new LivraisonAdaptateur(this, liste_suivi);
        //on lie l'adaptateur à la liste expensive
        vue.setAdapter(adaptateur);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_livraison, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
