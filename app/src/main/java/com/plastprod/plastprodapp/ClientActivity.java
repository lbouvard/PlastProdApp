package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;

import android.widget.AbsListView.MultiChoiceModeListener;

public class ClientActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    List<Societe> liste_client = new ArrayList<Societe>();
    List<Societe> liste_client_selectionnee = new ArrayList<Societe>();
    DatabaseHelper db;
    ListView lvClient;
    ClientAdaptateur adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ){

            //connexion base
            db = new DatabaseHelper(getApplicationContext());

            //récupération des clients
            liste_client = db.getSocietes(null);

            lvClient = (ListView) findViewById(R.id.liste_client);
            lvClient.setOnItemClickListener(this);


            adaptateur = new ClientAdaptateur(this, liste_client);
            lvClient.setAdapter(adaptateur);
            lvClient.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        }
        else
            terminerSession();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // show description
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void terminerSession(){

        Intent activite = new Intent(this, AccueilActivity.class);
        activite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activite);
        finish();
    }

}
