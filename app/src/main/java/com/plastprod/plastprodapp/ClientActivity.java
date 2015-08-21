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

public class ClientActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    List<Societe> liste_client = new ArrayList<Societe>();
    DatabaseHelper db;
    ListView lvClient;
    ClientAdaptateur adaptateur;

    static final int RETOUR_MAJ = 1000;

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


            adaptateur = new ClientAdaptateur(this, liste_client, getApplicationContext());
            lvClient.setAdapter(adaptateur);
            lvClient.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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
    public boolean onPrepareOptionsMenu(Menu menu) {

        /*MenuItem itemContact = menu.findItem(R.id.action_contact);
        //depending on your conditions, either enable/disable
        item.setEnabled(false);
        super.onPrepareOptionsMenu(menu);
        return true;*/
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, FormulaireClient.class);

        //on récupère la classe qui contient les données du client
        Societe client = (Societe)((ListView) arg0).getAdapter().getItem(arg2);

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client);

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RETOUR_MAJ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                majListe();
            }
        }
    }

    public void ajouterClient(View vue){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, FormulaireClient.class);

        Societe client = null;

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client );

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }

    public void afficherContacts(Societe client){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, ContactActivity.class);

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client);

        //et on affiche le formulaire
        startActivity(activite);
    }

    public void afficherCommandes(Societe client){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, HistocdActivity.class);

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client);

        //et on affiche le formulaire
        startActivity(activite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_recherche:
                Outils.afficherToast(getApplicationContext(), "bouton recherche sélectionné.");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void majListe(){

        liste_client = db.getSocietes(null);
        adaptateur.clear();
        adaptateur.addAll(liste_client);
        adaptateur.notifyDataSetChanged();
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
