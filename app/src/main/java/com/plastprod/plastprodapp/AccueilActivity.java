package com.plastprod.plastprodapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;


public class AccueilActivity extends ActionBarActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = new DatabaseHelper(getApplicationContext());

        Societe nouveau = new Societe();
        nouveau.setNom("Bouvard");
        nouveau.setAdresse1("1 rue du bac");
        nouveau.setAdresse2("ZA du bornant");
        nouveau.setCode_postal("54600");
        nouveau.setVille("Villers-lès-nancy");
        nouveau.setPays("France");
        nouveau.setCommentaire("Ceci est un commentaire");
        nouveau.setAuteur("LB");

        db.ajouterClient(nouveau, "C");

        Log.d("Lecture", "Récupération des clients");
        List<Societe> clients = db.getSocietes("WHERE Type = 'C'");

        for (Societe client : clients) {
            String log = "Id: " + client.getId() + " ,Name: " + client.getNom();
            // Writing Contacts to log
            Log.d("Info: ", log);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
