package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;


public class ClientActivity extends ActionBarActivity {

    List<Societe> liste_client = new ArrayList<Societe>();
    DatabaseHelper db;
    ListView lvClient;

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

            ClientAdaptateur adaptateur = new ClientAdaptateur(this, liste_client);
            lvClient.setAdapter(adaptateur);

            lvClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            ;
                            break;
                    }
                }
            });
        }
        else
            terminerSession();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_recherche) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
