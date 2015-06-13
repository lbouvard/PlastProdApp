package com.plastprod.plastprodapp;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    }

    private void chargerDonneesBase(){

        try{

            db = new DatabaseHelper(getApplicationContext());

            //ajout de la societe plastprod
            Societe nouveau = new Societe();
            nouveau.setNom("PlastProd");
            nouveau.setAdresse1("1 rue du comodo");
            nouveau.setAdresse2("");
            nouveau.setCode_postal("54600");
            nouveau.setVille("Villers-Lès-nancy");
            nouveau.setPays("France");
            nouveau.setCommentaire("Société mère");
            nouveau.setAuteur("LB");
            db.ajouterClient(nouveau, "M");

            //ajout du contact
            Contact contact = new Contact();
            contact.setNom("Bouvard");
            contact.setPrenom("Laurent");
            contact.setPoste("Commercial");
            contact.setTel_fixe("+33383256594");
            contact.setTel_mobile("+33645986147");
            contact.setFax("");
            contact.setEmail("laurent.bouvard@plastprod.fr");
            contact.setAdresse("1 rue du comodo");
            contact.setCode_postal("54600");
            contact.setVille("Villers-Lès-nancy");
            contact.setPays("France");
            contact.setCommentaire("");
            contact.setAuteur("LB");
            contact.setSociete(nouveau);
            long id_compte = db.ajouterContact(contact);

            //ajout d'un compte
            Compte compte = new Compte();
            compte.setNom("bouvard.laurent");
            compte.setEmail("laurent.bouvard@plastprod.fr");
            compte.setActif(true);
            compte.setSalt("5703c8599affced67f20c76ff6ec0116");
            compte.setMdp("ZQGi8N+qt7Rt0o1Z/4hFodTwaXrj8BIYtj5zCbXMtXg2j5CKpoaoveoKPQodBS1oTs3XC+0bjwGLfj9mHjiX6Q==");
            compte.setContact_id((int)id_compte);
            db.ajouterCompte(compte);

        } catch (Exception e){
            Log.d("chargerDonneesBase", "Message : " + e.getMessage());
        }

    }

    public void Authentifier(){

        String identifiant;
        String motDePasse;

        try {

            EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
            identifiant = edt_utilisateur.getText().toString();
            EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);
            motDePasse = edt_motdepasse.getText().toString();

            //accès base
            db = new DatabaseHelper(getApplicationContext());

            String salt = db.getSalt(identifiant);

            if( salt != "" ) {
                Cryptage securite = new Cryptage(motDePasse, salt);
                String mdpEncode = securite.CrypterDonnees();

                Log.d("Info", mdpEncode);

                //Vérification des données d'identification
                Contact commercial = db.verifierIdentifiantCommercial(identifiant, motDePasse);

                //Réussite, on va au menu principal
                if( commercial != null ){
                    //intent
                }
                else{
                    //Message d'erreur à afficher
                    Context context = getApplicationContext();
                    CharSequence message = "Identifiant et/ou mot de passe incorrect";
                    int duree = Toast.LENGTH_SHORT;

                    Toast notification = Toast.makeText(context, message, duree);
                    notification.show();
                }
            }
        }
        catch (Exception e){
            Log.d("Erreur", "Message : " + e.getMessage());
        }
    }
}
