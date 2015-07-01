package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import sqlite.helper.Compte;
import sqlite.helper.Contact;
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
    }

    @Override
    protected void onResume(){
        super.onResume();

        EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
        EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);

        final Global jeton = (Global) getApplicationContext();

        if( jeton.getNom_utilisateur() != "")
            edt_utilisateur.setText(jeton.getNom_utilisateur());
        if( jeton.getMail_utilisateur() != "")
            edt_utilisateur.setText(jeton.getMail_utilisateur());

        edt_motdepasse.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void ByPass(View vue){

        db = new DatabaseHelper(getApplicationContext());

        //Vérification des données d'identification
        Contact commercial = db.getCommercial(4);

        //Réussite, on va au menu principal
        if( commercial != null ) {

            final Global jeton = (Global) getApplicationContext();
            jeton.setNom_utilisateur("dupond.jean");
            jeton.setUtilisateur(commercial);
            jeton.setDate_connexion(new Date());

            //nouvelle activité
            Intent activite = new Intent(this, MenuActivity.class);
            //on démarre la nouvelle activité
            startActivity(activite);
        }
    }

    public void Authentifier(View vue){

        String identifiant;
        String motDePasse;
        Boolean connexion_ok = false;
        CharSequence message = "";

        try {

            EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
            identifiant = edt_utilisateur.getText().toString();
            EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);
            motDePasse = edt_motdepasse.getText().toString();

            //accès base
            db = new DatabaseHelper(getApplicationContext());

            String salt = db.getSalt(identifiant);

            if( !salt.equals("") ) {
                Cryptage securite = new Cryptage(motDePasse, salt);
                String mdpEncode = securite.CrypterDonnees();

                Log.d("Info", mdpEncode);

                //Vérification des données d'identification
                Contact commercial = db.verifierIdentifiantCommercial(identifiant, mdpEncode);

                //Réussite, on va au menu principal
                if( commercial != null ){

                    connexion_ok = true;
                    final Global jeton = (Global) getApplicationContext();

                    if( identifiant.contains("@") )
                        jeton.setMail_utilisateur(identifiant);
                    else
                        jeton.setNom_utilisateur(identifiant);

                    jeton.setUtilisateur(commercial);

                    jeton.setDate_connexion(new Date());

                    //nouvelle activité
                    Intent activite = new Intent(this, MenuActivity.class);
                    //on passe les infos du commercial
                    //activite.putExtra("Commercial", commercial);

                    //on démarre la nouvelle activité
                    startActivity(activite);
                }
                else{
                    message = "Identifiant et/ou mot de passe incorrect";
                }
            }
            else{
                message = "Identifiant inconnu";
            }


            if( !connexion_ok) {
                //Message d'erreur à afficher
                Context context = getApplicationContext();
                int duree = Toast.LENGTH_LONG;

                Toast notification = Toast.makeText(context, message, duree);
                notification.show();
            }

            db.close();

        }
        catch (Exception e){
            Log.d("Erreur", "Message : " + e.getMessage());
        }
    }
}
