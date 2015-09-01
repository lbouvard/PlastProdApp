package com.plastprod.plastprodapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.LigneCommande;
import sqlite.helper.Societe;


public class MenuActivity extends ActionBarActivity implements ConfirmationSynchroDialog.RetourListener, ArticleDialog.RetourListener {

    Intent itActivite;
    Contact commercial;
    TextView tvNom;
    TextView tvMail;
    ListView lvMenu;

    //pour la base de donnée locale
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ){

            final Global jeton = (Global) getApplicationContext();

            commercial = jeton.getUtilisateur();

            //Alimente les textes
            tvNom = (TextView) findViewById(R.id.nom_utilisateur);
            tvNom.setText(commercial.toString());
            tvMail = (TextView) findViewById(R.id.mail_utilisateur);
            tvMail.setText(commercial.getEmail());
            lvMenu = (ListView) findViewById(R.id.liste_menu);

            String[] valeurs = new String[] { "Gestion des clients",
                    "Gestion des prospects", "Gestion des commandes", "Suivi commercial", "Suivi des performances" };

            MenuAdaptateur adaptateur = new MenuAdaptateur(this, valeurs);
            lvMenu.setAdapter(adaptateur);

            lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            ouvrirClient();
                            break;
                        case 1:
                            ouvrirProspect();
                            break;
                        case 2:
                            ouvrirBon();
                            break;
                        case 3:
                            ouvrirSuivi();
                            break;
                        case 4:
                            ouvrirPerf();
                            break;
                    }
                }
            });

        }
        else {
            terminerSession();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if( !Outils.VerifierSession(getApplicationContext()) ){
            terminerSession();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        Synchroniser();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        Outils.afficherToast(getApplicationContext(), "Synchronisation annulée.");
    }

    public void terminerSession(){

        Intent activite = new Intent(this, AccueilActivity.class);
        activite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activite);
        finish();
    }

    public void Deconnecter(View vue){

        terminerSession();
    }

    public void ouvrirClient(){

        Intent activite = new Intent(this, ClientActivity.class);

        activite.putExtra("Type", "C");

        startActivity(activite);
    }

    public void ouvrirProspect(){

        Intent activite = new Intent(this, ClientActivity.class);

        activite.putExtra("Type", "P");

        startActivity(activite);
    }

    public void ouvrirBon(){

        Intent activite = new Intent(this, ChoixClientActivity.class);
        startActivity(activite);
    }

    public void ouvrirSuivi(){
        Intent activite = new Intent(this, SuiviActivity.class);
        startActivity(activite);
    }

    public void ouvrirPerf(){
        Intent activite = new Intent(this, PerfActivity.class);
        startActivity(activite);
    }

    public int Synchroniser(){
        new RestApi().execute(getApplicationContext());
        return 0;
    }

    public void ConfirmerSynchro(View vue){

        db = new DatabaseHelper(getApplicationContext());
        String ssid = db.getSsidWifi();

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo active = cm.getActiveNetworkInfo();

        if( active.isConnected() && active.getType() == ConnectivityManager.TYPE_WIFI && active.getExtraInfo().replace("\"","").equals(ssid) ) {
            DialogFragment confirmation = new ConfirmationSynchroDialog();
            confirmation.show(getSupportFragmentManager(), "confirmation_synchro");
        }
        else{
            Outils.afficherToast(getApplicationContext(), "Vous devez être connecté en WIFI chez PlastProd.");
        }

        db.close();
    }

   /* public void afficherdialog(View vue){

        db = new DatabaseHelper(getApplicationContext());
        LigneCommande liste = db.getArticle(5);

        Bundle bundle = new Bundle();
        bundle.putSerializable("article", liste);

        DialogFragment test = new ArticleDialog();
        test.setArguments(bundle);
        test.show(getSupportFragmentManager(), "article_dialog");
    }*/

    public void validateArticles(DialogFragment diag){
        Outils.afficherToast(getApplicationContext(), "Articles validés");
    }

    public void annuleArticles(DialogFragment diag){
        Outils.afficherToast(getApplicationContext(), "Annulé");
    }
}
