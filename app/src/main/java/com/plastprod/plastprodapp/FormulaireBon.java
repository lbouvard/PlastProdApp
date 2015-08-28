package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import sqlite.helper.Bon;
import sqlite.helper.DatabaseHelper;

/**
 * Created by Michael on 29/07/2015.
 */
public class FormulaireBon extends ActionBarActivity {

    TextView etGenerique;
    Bon commande_en_cours;
    DatabaseHelper db;
    long idCommande = 0;

    //commande ou devis (CD ou DE)
    String type;

    Context context;
    int duree = Toast.LENGTH_LONG;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_bon);
        context = getApplicationContext();

        //on recupère les données
        Intent intent = getIntent();
        commande_en_cours = (Bon) intent.getSerializableExtra("Bon");
        type = intent.getStringExtra("Type");

        //ecriture des données sur le layout

            //Seulement si on est en modification/consultation
        if( commande_en_cours != null ) {
            //nom
            etGenerique = (TextView) findViewById(R.id.datecommande);
            etGenerique.setText(commande_en_cours.getDate_commande());

            etGenerique = (TextView) findViewById(R.id.etatc);
            etGenerique.setText(commande_en_cours.getEtat_commande());

            etGenerique = (TextView) findViewById(R.id.nomsociete);
            etGenerique.setText(commande_en_cours.getClient().getNom());

            etGenerique = (TextView) findViewById(R.id.auteur);
            etGenerique.setText(commande_en_cours.getClient().getAuteur());

            etGenerique = (TextView) findViewById(R.id.adresse);
            etGenerique.setText(commande_en_cours.getClient().getAdresse1());

            etGenerique = (TextView) findViewById(R.id.codepostal);
            etGenerique.setText(commande_en_cours.getClient().getCode_postal());

            etGenerique = (TextView) findViewById(R.id.ville);
            etGenerique.setText(commande_en_cours.getClient().getVille());

            idCommande = commande_en_cours.getId();

            //Pour la liste des articles
            ListView lvLigneCommande;
            AffichageCommandeAdaptateur adaptateur;


           /* //connexion base
            db = new DatabaseHelper(getApplicationContext());
            //récupération des clients
            liste_commande = db.getLignesCommande(idCommande);
            //liste_commande = db.getBons("DE",null);*/

            adaptateur = new AffichageCommandeAdaptateur(getApplicationContext(), commande_en_cours.getLignesBon());

            lvLigneCommande = (ListView) findViewById(R.id.liste_affichage_commande);
            lvLigneCommande.setAdapter(adaptateur);

        }

    }
}

