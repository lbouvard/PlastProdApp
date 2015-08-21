package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;


public class HistocdActivity extends ActionBarActivity {

    HistocdAdaptateur adaptateur;
    ExpandableListView vue;
    List<Bon> liste_bons = new ArrayList<Bon>();
    Societe client_en_cours;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on récupère la société passé en paramètre
        Intent intent = getIntent();
        client_en_cours = (Societe) intent.getSerializableExtra("Client");

        //on utilise le layout de l'activité
        setContentView(R.layout.activity_histocd);

        //on récupère la liste
        vue = (ExpandableListView) findViewById(R.id.lvListeCommande);

        //recupération des bons du client demandé
        db = new DatabaseHelper(getApplicationContext());
        liste_bons = db.getBons("CD", client_en_cours.getId(), -1);

        //et on alimente la liste
        adaptateur = new HistocdAdaptateur(this, liste_bons);
        //on lie l'adaptateur à la liste expensive
        vue.setAdapter(adaptateur);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_histocd, menu);

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_histocd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
