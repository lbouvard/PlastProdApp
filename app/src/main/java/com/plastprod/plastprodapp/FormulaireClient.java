package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import sqlite.helper.Societe;


public class FormulaireClient extends ActionBarActivity {

    EditText etGenerique;
    Societe client_en_cours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_client);

        //on recupère les données
        Intent intent = getIntent();
        client_en_cours = (Societe) intent.getSerializableExtra("Client");

        //Seulement si on est en modification/consultation
        if( client_en_cours != null ) {
            //nom
            etGenerique = (EditText) findViewById(R.id.etNom);
            etGenerique.setText(client_en_cours.getNom());
            //adresse
            etGenerique = (EditText) findViewById(R.id.etAdresse);
            etGenerique.setText(client_en_cours.getAdresse1());
            //complément adresse
            etGenerique = (EditText) findViewById(R.id.etComplement);
            etGenerique.setText(client_en_cours.getAdresse2());
            //code postal
            etGenerique = (EditText) findViewById(R.id.etCodePostal);
            etGenerique.setText(client_en_cours.getCode_postal());
            //ville
            etGenerique = (EditText) findViewById(R.id.etVille);
            etGenerique.setText(client_en_cours.getVille());
            //pays
            etGenerique = (EditText) findViewById(R.id.etPays);
            etGenerique.setText(client_en_cours.getPays());
            //commentaire
            etGenerique = (EditText) findViewById(R.id.etCommentaire);
            etGenerique.setText(client_en_cours.getCommentaire());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulaire_client, menu);
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

    //pour annuler les modification
    public void annulerModification(View vue){
        finish();
    }

    //pour enregistrer les modification
    public void enregistrerModification(View vue){

        //on recupère la variable globale pour recupérer l'auteur
        final Global jeton = (Global) getApplicationContext();

        Societe client = new Societe();

        client.setNom(findViewById(R.id.etNom).toString());
        client.setAdresse1(findViewById(R.id.etAdresse).toString());
        client.setAdresse2(findViewById(R.id.etComplement).toString());
        client.setCode_postal(findViewById(R.id.etCodePostal).toString());
        client.setVille(findViewById(R.id.etVille).toString());
        client.setPays(findViewById(R.id.etPays).toString());
        client.setCommentaire(findViewById(R.id.etCommentaire).toString());
        client.setAuteur(jeton.getUtilisateur().toString());

        if( client_en_cours != null ){
            client.setId( client_en_cours.getId());
            modifierClient(client);
        }
        else{
            creerClient(client);
        }

    }

    //pour modifier un client
    public void modifierClient(Societe client){

    }

    //pour créer un nouveau client
    public void creerClient(Societe client){

    }

}
