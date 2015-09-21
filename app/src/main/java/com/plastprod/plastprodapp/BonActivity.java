package com.plastprod.plastprodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.DatabaseHelper;

public class BonActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    List<Bon> liste_bon = new ArrayList<Bon>();
    DatabaseHelper db;
    ListView lvCommande;
    BonAdaptateur adaptateur;
    String type;
    int id_societe;

    boolean revientDePause = false;

    static final int RETOUR_MAJ = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        id_societe = intent.getIntExtra("Id", -1);

        setContentView(R.layout.activity_bon);

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ) {

            //connexion base
            db = new DatabaseHelper(getApplicationContext());
            //récupération des bons
            liste_bon = db.getBons(type, id_societe, -1, false );

            adaptateur = new BonAdaptateur(getApplicationContext(), liste_bon);

            lvCommande = (ListView) findViewById(R.id.liste_bon);
            lvCommande.setOnItemClickListener(this);
            lvCommande.setAdapter(adaptateur);

            //on grise le bouton actif
            Button btn_actif = (Button)findViewById(R.id.button_commandes);
            btn_actif.setBackgroundColor(getResources().getColor(R.color.gris));
            Button btn_inactif = (Button)findViewById(R.id.button_devis);
            btn_inactif.setBackgroundColor(getResources().getColor(R.color.blanc));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if( revientDePause ) {
            majListe(id_societe);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        revientDePause = true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //MenuItem itemContact = menu.findItem(R.id.action_contact);
        //depending on your conditions, either enable/disable
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_ajout_bon:

                ajouterBon();
                return true;

            case R.id.action_recherche :

                //voir ClientActivity pour la recherche et androidmanifest pour la config
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RETOUR_MAJ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                majListe(id_societe);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, AffichageBon.class);

        //on récupère la classe qui contient les données du client
        Bon bon = (Bon)((ListView) arg0).getAdapter().getItem(arg2);

        //on transmet l'objet à  la nouvelle activité
        activite.putExtra("Bon", bon);
        activite.putExtra("IdtClient", id_societe);
        activite.putExtra("Type", type);

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }

    public void afficherDevis(View vue){

        type = "DE";

        //on grise le bouton actif
        Button btn_actif = (Button)findViewById(R.id.button_devis);
        btn_actif.setBackgroundColor(getResources().getColor(R.color.gris));
        Button btn_inactif = (Button)findViewById(R.id.button_commandes);
        btn_inactif.setBackgroundColor(getResources().getColor(R.color.blanc));

        majListe(id_societe);
    }

    public void afficherCommandes(View vue){

        type = "CD";

        //on grise le bouton actif
        Button btn_actif = (Button)findViewById(R.id.button_commandes);
        btn_actif.setBackgroundColor(getResources().getColor(R.color.gris));
        Button btn_inactif = (Button)findViewById(R.id.button_devis);
        btn_inactif.setBackgroundColor(getResources().getColor(R.color.blanc));

        majListe(id_societe);
    }

    public void majListe(int id_societe){

        db = new DatabaseHelper(getApplicationContext());

        liste_bon = db.getBons(type, id_societe, -1, false);
        adaptateur.clear();
        adaptateur.addAll(liste_bon);
        adaptateur.notifyDataSetChanged();

        db.close();
    }

    public void ajouterBon(){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, FormulaireBon.class);

        Bon bon = null;

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Bon", bon);
        activite.putExtra("IdtClient", id_societe);
        activite.putExtra("Type", type);

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }
}
