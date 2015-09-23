package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import java.util.Date;

import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;

public class AccueilActivity extends ActionBarActivity {

    //pour la base de donnée locale
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        CheckBox affiche_mdp = (CheckBox) findViewById(R.id.checkBox);
        final EditText mdp = (EditText) findViewById(R.id.edt_motdepasse);
        affiche_mdp.setChecked(false);

        //affiche ou masque le mot de passe
        affiche_mdp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mdp.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    mdp.setInputType(129);
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //on affiche le nom d'utilisateur ou son mail si il s'est déjà connecté
        EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
        EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);

        final Global jeton = (Global) getApplicationContext();

        if( jeton.getNom_utilisateur().equals("") )
            edt_utilisateur.setText(jeton.getNom_utilisateur());
        if( jeton.getMail_utilisateur().equals("") )
            edt_utilisateur.setText(jeton.getMail_utilisateur());

        //on enlève le mot de passe
        edt_motdepasse.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void Authentifier(View vue){

        String identifiant;
        String motDePasse;
        Boolean connexion_ok = false;
        CharSequence message = "";

        try {

            EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
            identifiant = edt_utilisateur.getText().toString();
            EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);
            motDePasse = edt_motdepasse.getText().toString();

            //accès base
            db = new DatabaseHelper(getApplicationContext());

            String salt = db.getSalt(identifiant);

            if( !salt.equals("") ) {
                Cryptage securite = new Cryptage(motDePasse, salt);
                String mdpEncode = securite.CrypterDonnees();

                //Log.d("Info", mdpEncode);

                //Vérification des données d'identification
                Contact commercial = db.verifierIdentifiantCommercial(identifiant, mdpEncode);

                //Réussite, on va au menu principal
                if( commercial != null ){

                    connexion_ok = true;
                    final Global jeton = (Global) getApplicationContext();

                    if( identifiant.contains("@") )
                        jeton.setMail_utilisateur(identifiant);
                    else
                        jeton.setNom_utilisateur(identifiant);

                    jeton.setUtilisateur(commercial);

                    jeton.setDate_connexion(new Date());

                    if( commercial.getNom().equals("ADMSYS") ){
                        //administration
                        Intent activite = new Intent(this, AdminActivity.class);
                        //on démarre l'activite
                        startActivity(activite);
                    }
                    else {
                        // On récupère l'indice du dernier bon pour incrémenter à chaque nouveau bon
                        jeton.setIndice_bon( db.getDernierIndiceBon() );

                        //nouvelle activité
                        Intent activite = new Intent(this, MenuActivity.class);

                        //on démarre la nouvelle activité
                        startActivity(activite);
                    }
                }
                else{
                    message = "Identifiant et/ou mot de passe incorrect";
                }
            }
            else{
                message = "Identifiant inconnu";
            }


            if( !connexion_ok) {
                //Message d'erreur à afficher
                Outils.afficherToast(getApplicationContext(), message.toString());
                /*Context context = getApplicationContext();
                int duree = Toast.LENGTH_LONG;

                Toast notification = Toast.makeText(context, message, duree);
                notification.show();*/
            }

            db.close();

        }
        catch (Exception e){
            Log.d("AccueilActivity", "Authentifier() : " + e.getMessage());
        }
    }
}
