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
import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;


public class ContactActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    List<Contact> liste_contact = new ArrayList<Contact>();
    //List<Contact> liste_contact_selectionnee = new ArrayList<Contact>();
    DatabaseHelper db;
    ListView lvContact;
    ContactAdaptateur adaptateur;

    Societe client_en_cours;
    int societe_id;

    static final int RETOUR_MAJ = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if( savedInstanceState != null ){
            client_en_cours = (Societe) savedInstanceState.getSerializable("Client");
        }
        else {
            if (client_en_cours == null) {
                Intent intent = getIntent();
                client_en_cours = (Societe) intent.getSerializableExtra("Client");
            }
        }

        setContentView(R.layout.activity_contact);

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ){

            //connexion base
            db = new DatabaseHelper(getApplicationContext());

            //récupération des clients
            liste_contact = db.getContacts(client_en_cours.getId(), null);

            lvContact = (ListView) findViewById(R.id.liste_contact);
            lvContact.setOnItemClickListener(this);


            adaptateur = new ContactAdaptateur(this, liste_contact, getApplicationContext(), client_en_cours);
            lvContact.setAdapter(adaptateur);
            lvContact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }
        else
            terminerSession();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable("Client", client_en_cours);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_ajout_contact:

                ajouterContact();
                return true;

            case R.id.action_recherche :

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, FormulaireContact.class);

        //on récupère la classe qui contient les données du client
        Contact personne = (Contact)((ListView) arg0).getAdapter().getItem(arg2);

        //on transmet l'objet à la nouvelle activité
        activite.putExtra("Contact", personne);
        activite.putExtra("IdtSociete", client_en_cours.getId());

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

    public void ajouterContact(){

        //on recupère l'activité du formulaire client
        Intent activite = new Intent(this, FormulaireContact.class);

        Contact contact = null;

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Contact", contact );
        activite.putExtra("IdtSociete", client_en_cours.getId());

        //et on affiche le formulaire
        startActivityForResult(activite, RETOUR_MAJ);
    }

    public void majListe(){

        liste_contact = db.getContacts(client_en_cours.getId(), null);
        adaptateur.clear();
        adaptateur.addAll(liste_contact);
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
