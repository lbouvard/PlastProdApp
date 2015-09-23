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
import sqlite.helper.Contact;

public class FormulaireContact extends ActionBarActivity {

    EditText etGenerique;
    Contact contact_en_cours;
    int societe_id;
    DatabaseHelper db;

    Context context;
    int duree = Toast.LENGTH_LONG;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_contact);

        context = getApplicationContext();

        Intent intent = getIntent();
        contact_en_cours = (Contact) intent.getSerializableExtra("Contact");
        societe_id = intent.getIntExtra("IdtSociete", -1);

        if( contact_en_cours != null){
            //nom
            etGenerique = (EditText) findViewById(R.id.etNom);
            etGenerique.setText(contact_en_cours.getNom());
            //prénom
            etGenerique = (EditText) findViewById(R.id.etPrenom);
            etGenerique.setText(contact_en_cours.getPrenom());
            //poste
            etGenerique = (EditText) findViewById(R.id.etPoste);
            etGenerique.setText(contact_en_cours.getPoste());
            //téléphone
            etGenerique = (EditText) findViewById(R.id.etFixe);
            etGenerique.setText(contact_en_cours.getTel_fixe());
            //mobile
            etGenerique = (EditText) findViewById(R.id.etMobile);
            etGenerique.setText(contact_en_cours.getTel_mobile());
            //fax
            etGenerique = (EditText) findViewById(R.id.etFax);
            etGenerique.setText(contact_en_cours.getFax());
            //email
            etGenerique = (EditText) findViewById(R.id.etMail);
            etGenerique.setText(contact_en_cours.getEmail());
            //adresse
            etGenerique = (EditText) findViewById(R.id.etAdresse);
            etGenerique.setText(contact_en_cours.getAdresse());
            //code postal
            etGenerique = (EditText) findViewById(R.id.etCodePostal);
            etGenerique.setText(contact_en_cours.getCode_postal());
            //ville
            etGenerique = (EditText) findViewById(R.id.etVille);
            etGenerique.setText(contact_en_cours.getVille());
            //pays
            etGenerique = (EditText) findViewById(R.id.etPays);
            etGenerique.setText(contact_en_cours.getPays());
            //commentaire
            etGenerique = (EditText) findViewById(R.id.etCommentaire);
            etGenerique.setText(contact_en_cours.getCommentaire());
        }
        else{
            //code postal
            etGenerique = (EditText) findViewById(R.id.etPays);
            etGenerique.setText("FRANCE");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_formulaire_contact, menu);

        if( contact_en_cours ==  null) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_formulaire_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_supprime:

                db = new DatabaseHelper(context);

                db.supprimerContact(contact_en_cours);

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

        Contact contact = new Contact();

        contact.setNom(Outils.miseEnMajuscule(((EditText) findViewById(R.id.etNom)).getText().toString()));
        contact.setPrenom( Outils.premiereLettreEnMajuscule(((EditText) findViewById(R.id.etPrenom)).getText().toString()) );
        contact.setPoste( Outils.premiereLettreEnMajuscule(((EditText) findViewById(R.id.etPoste)).getText().toString()) );

        if( !Outils.verifierNumero( ((EditText) findViewById(R.id.etFixe)).getText().toString()) ){
            Toast notification = Toast.makeText(context, "Veuillez entrer un numéro au format international (Ex : +33321457891).", duree);
            notification.show();
            return;
        }
        contact.setTel_fixe(((EditText) findViewById(R.id.etFixe)).getText().toString());

        if( !Outils.verifierNumero( ((EditText) findViewById(R.id.etMobile)).getText().toString()) ){
            Toast notification = Toast.makeText(context, "Veuillez entrer un numéro au format international (Ex : +33621457891).", duree);
            notification.show();
            return;
        }
        contact.setTel_mobile(((EditText) findViewById(R.id.etMobile)).getText().toString());

        if( !((EditText) findViewById(R.id.etFax)).getText().toString().equals("") ) {
            if (!Outils.verifierNumero(((EditText) findViewById(R.id.etFax)).getText().toString())) {
                Toast notification = Toast.makeText(context, "Veuillez entrer un numéro au format international (Ex : +33321457891).", duree);
                notification.show();
                return;
            }
            contact.setFax(((EditText) findViewById(R.id.etFax)).getText().toString());
        }

        if( !Outils.verifierEmail(((EditText) findViewById(R.id.etMail)).getText().toString()) ){
            Toast notification = Toast.makeText(context, "Veuillez entrer une adresse Email valide.", duree);
            notification.show();
            return;
        }
        contact.setEmail( (((EditText) findViewById(R.id.etMail)).getText().toString()).toLowerCase() );
        contact.setAdresse( (((EditText) findViewById(R.id.etAdresse)).getText().toString()).toUpperCase() );

        if( !Outils.verifierCodePostal( ((EditText) findViewById(R.id.etCodePostal)).getText().toString()) ){
            Toast notification = Toast.makeText(context, "Veuillez entrer un code postal valide.", duree);
            notification.show();
            return;
        }
        contact.setCode_postal(((EditText) findViewById(R.id.etCodePostal)).getText().toString());
        contact.setVille( Outils.miseEnMajuscule(((EditText) findViewById(R.id.etVille)).getText().toString()) );
        contact.setPays( Outils.miseEnMajuscule(((EditText) findViewById(R.id.etPays)).getText().toString()) );
        contact.setCommentaire(((EditText) findViewById(R.id.etCommentaire)).getText().toString());
        contact.setId_societe(societe_id);

        if( contact_en_cours != null ){

            //si le commercial est bien l'auteur, on modifie sinon on affiche un message
            if( contact_en_cours.getAuteur().equals(jeton.getUtilisateur().toString())) {
                contact.setAuteur(jeton.getUtilisateur().toString());
                contact.setId(contact_en_cours.getId());
                modifierContact(contact);
            }
            else{
                //Message d'erreur à afficher
                Toast notification = Toast.makeText(context, "Vous devez être le propriétaire de ce client pour le modifier.", duree);
                notification.show();
            }
        }
        else{
            contact.setAuteur(jeton.getUtilisateur().toString());
            creerContact(contact);
        }

    }

    //pour modifier un contact
    public void modifierContact(Contact contact){

        db = new DatabaseHelper(getApplicationContext());

        db.majContact(contact);

        Toast notification = Toast.makeText(context, "Le contact a été modifié avec succès.", duree);
        notification.show();

        db.close();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);

    }

    //pour créer un nouveau client
    public void creerContact(Contact contact){

        db = new DatabaseHelper(getApplicationContext());

        db.ajouterContact(contact);

        Toast notification = Toast.makeText(context, "Le client a été ajouté avec succès.", duree);
        notification.show();

        db.close();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);

        finish();
    }
}
