package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sqlite.helper.Contact;


public class MenuActivity extends ActionBarActivity {

    Intent itActivite;
    Contact commercial;
    TextView tvNom;
    TextView tvMail;
    ListView lvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ){

            final Global jeton = (Global) getApplicationContext();

            commercial = jeton.getUtilisateur();

            //Alimente les textes
            tvNom = (TextView) findViewById(R.id.nom_utilisateur);
            tvNom.setText(commercial.toString());
            tvMail = (TextView) findViewById(R.id.mail_utilisateur);
            tvMail.setText(commercial.getEmail());
            lvMenu = (ListView) findViewById(R.id.liste_menu);

            String[] valeurs = new String[] { "Gestion des clients",
                    "Gestion des prospects", "Gestion des commandes", "Suivi commercial", "Suivi des performances" };

            MenuAdaptateur adaptateur = new MenuAdaptateur(this, valeurs);
            lvMenu.setAdapter(adaptateur);

            lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            ouvrirClient();
                            break;
                        case 1:
                            ouvrirProspect();
                            break;
                        case 2:
                            ouvrirBon();
                            break;
                        case 3:
                            ouvrirSuivi();
                            break;
                        case 4:
                            ouvrirPerf();
                            break;
                    }
                }
            });

        }
        else {
            terminerSession();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if( !Outils.VerifierSession(getApplicationContext()) ){
            terminerSession();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    /*
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
    }*/

    public void terminerSession(){

        Intent activite = new Intent(this, AccueilActivity.class);
        activite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activite);
        finish();
    }

    public void Deconnecter(View vue){

        terminerSession();
    }

    public void ouvrirClient(){
        Intent activite = new Intent(this, ClientActivity.class);
        startActivity(activite);
    }

    public void ouvrirProspect(){
        Intent activite = new Intent(this, ProspectActivity.class);
        startActivity(activite);
    }

    public void ouvrirBon(){
        Intent activite = new Intent(this, BonActivity.class);
        startActivity(activite);
    }

    public void ouvrirSuivi(){
        Intent activite = new Intent(this, SuiviActivity.class);
        startActivity(activite);
    }

    public void ouvrirPerf(){
        Intent activite = new Intent(this, PerfActivity.class);
        startActivity(activite);
    }
}
