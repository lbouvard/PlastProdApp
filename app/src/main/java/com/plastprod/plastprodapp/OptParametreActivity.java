package com.plastprod.plastprodapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Parametre;
import sqlite.helper.Societe;

public class OptParametreActivity extends ActionBarActivity {

    DatabaseHelper db;
    List<Parametre> liste_param = new ArrayList<Parametre>();
    int id_commercial;

    EditText etGenerique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formulaire_parammail);

        // Param du commercial
        final Global jeton = (Global) getApplicationContext();
        id_commercial = jeton.getUtilisateur().getId();

        //recupération des paramètres du commercial
        db = new DatabaseHelper(getApplicationContext());
        liste_param = db.getParamMail(id_commercial);

        // Remplissage du formulaire
        etGenerique = (EditText) findViewById(R.id.etMail);
        etGenerique.setText(liste_param.get(0).getValeur());

        etGenerique = (EditText) findViewById(R.id.etDomaine);
        etGenerique.setText(liste_param.get(1).getValeur());

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_questionnaire, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return super.onOptionsItemSelected(item);
    }

    //pour annuler les modification
    public void annulerModification(View vue){
        finish();
    }

    //pour enregistrer les modification
    public void enregistrerModification(View vue){

        Parametre param;

        param = liste_param.get(0);
        param.setValeur( ((EditText) findViewById(R.id.etMail)).getText().toString() );

        param = liste_param.get(1);
        param.setValeur( ((EditText) findViewById(R.id.etDomaine)).getText().toString() );

        db = new DatabaseHelper(getApplicationContext());

        //sauvegarde des données
        for(Parametre parm : liste_param){
            db.majParametre(parm);
        }

        db.close();

        Outils.afficherToast(getApplicationContext(), "Les paramètres ont été mis à jour.");
        finish();
    }

}