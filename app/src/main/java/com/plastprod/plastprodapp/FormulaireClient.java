package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;


public class FormulaireClient extends ActionBarActivity {

    EditText etGenerique;
    Societe client_en_cours;
    DatabaseHelper db;

    Context context;
    int duree = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_client);

        context = getApplicationContext();

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

        client.setNom(((EditText) findViewById(R.id.etNom)).getText().toString());
        client.setAdresse1(((EditText) findViewById(R.id.etAdresse)).getText().toString());
        client.setAdresse2(((EditText) findViewById(R.id.etComplement)).getText().toString());
        client.setCode_postal(((EditText) findViewById(R.id.etCodePostal)).getText().toString());
        client.setVille(((EditText) findViewById(R.id.etVille)).getText().toString());
        client.setPays(((EditText) findViewById(R.id.etPays)).getText().toString());
        client.setCommentaire(((EditText) findViewById(R.id.etCommentaire)).getText().toString());
        client.setType("C");

        if( client_en_cours != null ){

            //si le commercial est bien l'auteur, on modifie sinon on affiche un message
            if( client_en_cours.getAuteur().equals(jeton.getUtilisateur().toString())) {
                client.setAuteur(jeton.getUtilisateur().toString());
                client.setId(client_en_cours.getId());
                modifierClient(client);
            }
            else{
                //Message d'erreur à afficher
                Toast notification = Toast.makeText(context, "Vous devez être le propriétaire de ce client pour le modifier.", duree);
                notification.show();
            }
        }
        else{
            client.setAuteur(jeton.getUtilisateur().toString());
            creerClient(client);
        }

    }

    //pour modifier un client
    public void modifierClient(Societe client){

        db = new DatabaseHelper(getApplicationContext());

        db.majSociete(client);

        db.close();

        Toast notification = Toast.makeText(context, "Le client a été modifié avec succès.", duree);
        notification.show();
    }

    //pour créer un nouveau client
    public void creerClient(Societe client){

        db = new DatabaseHelper(getApplicationContext());

        db.ajouterClient(client, "C");

        db.close();

        Toast notification = Toast.makeText(context, "Le client a été ajouté avec succès.", duree);
        notification.show();
    }

}
