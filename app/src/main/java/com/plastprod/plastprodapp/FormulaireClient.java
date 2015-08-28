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
    String type;

    private static boolean TOUT_BON;
    private static String TYPE_BON;
    private static String MSG_PROP;
    private static String MSG_MDF;
    private static String MSG_AJT;
    private static String MSG_EXISTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_client);

        context = getApplicationContext();

        //on recupère les données
        Intent intent = getIntent();
        client_en_cours = (Societe) intent.getSerializableExtra("Client");
        type = intent.getStringExtra("Type");

        //on initialise les message selon client/prospect
        initialiserConstante(type);

        //Seulement si on est en modification/consultation
        if( client_en_cours != null ) {
            //titre de l'activité
            setTitle("Modification");

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
        else{
            //titre de l'activité
            setTitle("Ajout");

            etGenerique = (EditText) findViewById(R.id.etPays);
            etGenerique.setText("FRANCE");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_formulaire_client, menu);

        if( client_en_cours ==  null) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_formulaire_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_contact:

                afficherContacts(client_en_cours);
                return true;

            case R.id.action_histo:

                afficherCommandes(client_en_cours);
                return true;

            case R.id.action_supprime:

                db = new DatabaseHelper(context);

                db.suppprimerSociete(client_en_cours, true);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

        client.setNom(Outils.miseEnMajuscule(((EditText) findViewById(R.id.etNom)).getText().toString()));
        client.setAdresse1( Outils.miseEnMajuscule(((EditText) findViewById(R.id.etAdresse)).getText().toString()) );
        client.setAdresse2( Outils.miseEnMajuscule(((EditText) findViewById(R.id.etComplement)).getText().toString()) );

        if( !Outils.verifierCodePostal( ((EditText) findViewById(R.id.etCodePostal)).getText().toString()) ){
            Toast notification = Toast.makeText(context, "Veuillez entrer un code postal valide.", duree);
            notification.show();
            return;
        }
        client.setCodePostal(((EditText) findViewById(R.id.etCodePostal)).getText().toString());
        client.setVille(Outils.miseEnMajuscule(((EditText) findViewById(R.id.etVille)).getText().toString()));
        client.setPays(Outils.miseEnMajuscule(((EditText) findViewById(R.id.etPays)).getText().toString()));
        client.setCommentaire(((EditText) findViewById(R.id.etCommentaire)).getText().toString());
        client.setType(type);

        if( client_en_cours != null ){

            //si le commercial est bien l'auteur, on modifie sinon on affiche un message
            if( client_en_cours.getAuteur().equals(jeton.getUtilisateur().toString())) {
                client.setAuteur(jeton.getUtilisateur().toString());
                client.setId(client_en_cours.getId());
                modifierClient(client);
            }
            else{
                //Message d'erreur à afficher
                Toast notification = Toast.makeText(context, MSG_PROP, duree);
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

        if( !db.clientExiste(client.getNom(), client.getId()) ) {
            db.majSociete(client);

            Toast notification = Toast.makeText(context, MSG_MDF, duree);
            notification.show();
        }
        else {
            Toast notification = Toast.makeText(context, MSG_EXISTE, duree);
            notification.show();
        }

        db.close();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    //pour créer un nouveau client
    public void creerClient(Societe client){

        db = new DatabaseHelper(getApplicationContext());

        if( !db.clientExiste(client.getNom(), -1) ) {
            db.ajouterClient(client, "C");

            Toast notification = Toast.makeText(context, MSG_AJT, duree);
            notification.show();
        }
        else{
            Toast notification = Toast.makeText(context, MSG_EXISTE, duree);
            notification.show();
        }

        db.close();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);

        finish();
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
        Intent activite = new Intent(this, HistoBonActivity.class);

        //on transmert l'objet à la nouvelle activité
        activite.putExtra("Client", client);
        activite.putExtra("Type", TYPE_BON);
        activite.putExtra("Bons", TOUT_BON);

        //et on affiche le formulaire
        startActivity(activite);
    }

    public void initialiserConstante(String type){

        if( type.equals("C")){
            TYPE_BON = "CD";
            TOUT_BON = true;
            MSG_PROP = "Vous devez être le propriétaire de ce client pour le modifier.";
            MSG_MDF = "Le client a été modifié avec succès.";
            MSG_AJT = "Le client a été ajouté avec succès.";
            MSG_EXISTE = "Attention, un client existe déjà sous ce nom.";
        }
        else{
            TYPE_BON = "DE";
            TOUT_BON = false;
            MSG_PROP = "Vous devez être le propriétaire de ce prospect pour le modifier.";
            MSG_MDF = "Le prospect a été modifié avec succès.";
            MSG_AJT = "Le prospect a été ajouté avec succès.";
            MSG_EXISTE = "Attention, un prospect existe déjà sous ce nom.";
        }

    }
}
