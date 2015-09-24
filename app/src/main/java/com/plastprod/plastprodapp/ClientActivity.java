package com.plastprod.plastprodapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;

public class ClientActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    List<Societe> liste_client = new ArrayList<Societe>();
    DatabaseHelper db;
    ListView lvClient;
    ClientAdaptateur adaptateur;
    String type;

    static final int RETOUR_MAJ = 1000;
    boolean init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        init = true;

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");

        setContentView(R.layout.activity_client);

        //client
        if( type.equals("C") ) {
            setTitle("Clients");
        }
        else{
            setTitle("Prospects");
        }

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ){

            //connexion base
            db = new DatabaseHelper(getApplicationContext());

            //récupération des clients
            liste_client = db.getSocietes(type);

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
    protected void onResume(){
        super.onResume();

        if( !init ){
            majListe();
        }
        else{
            init = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_recherche)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

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
        activite.putExtra("Type", type);

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

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            majRecherche(query);
        }
    }

    public void ajouterClient(View vue){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, FormulaireClient.class);

        Societe client = null;

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client );
        activite.putExtra("Type", type);

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }

    public void afficherContacts(Societe client){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, ContactActivity.class);

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client);

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }

    public void afficherCommandes(Societe client){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, HistoBonActivity.class);

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client);

        if( type.equals("C") ){
            activite.putExtra("Type", "CD");
            activite.putExtra("Bons", true);
        }
        else{
            activite.putExtra("Type", "DE");
            activite.putExtra("Bons", false);
        }

        //et on affiche le formulaire
        startActivity(activite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_recherche:
                //Outils.afficherToast(getApplicationContext(), "bouton recherche sélectionné.");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void majListe(){

        liste_client = db.getSocietes(type);
        adaptateur.clear();
        adaptateur.addAll(liste_client);
        adaptateur.notifyDataSetChanged();
    }

    public void majRecherche(String recherche){

        liste_client = db.rechercherSociete(type, recherche);
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
