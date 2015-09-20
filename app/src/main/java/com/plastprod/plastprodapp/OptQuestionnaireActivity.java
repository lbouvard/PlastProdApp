package com.plastprod.plastprodapp;

import android.support.v4.app.DialogFragment;
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
import sqlite.helper.Parametre;

public class OptQuestionnaireActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, OptQuestionnaireDialog.RetourListener {

    OptQuestionnaireAdaptateur adaptateur;
    DatabaseHelper db;
    List<Parametre> liste_param = new ArrayList<Parametre>();
    ListView liste;
    int id_commercial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questionnaire);

        //on récupère la liste
        liste = (ListView) findViewById(R.id.lvListeParametre);

        // Param du commercial
        final Global jeton = (Global) getApplicationContext();
        id_commercial = jeton.getUtilisateur().getId();

        //recupération des paramètres du commercial
        db = new DatabaseHelper(getApplicationContext());
        liste_param = db.getParamQuestionnaire(id_commercial);

        //et on alimente la liste
        adaptateur = new OptQuestionnaireAdaptateur(this, liste_param);
        //on lie l'adaptateur à la liste
        liste.setAdapter(adaptateur);
        liste.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on récupère la classe qui contient les données du client
        Parametre parm = (Parametre)((ListView) arg0).getAdapter().getItem(arg2);

        if( parm.getNom().equals("QETAPE")){

            DialogFragment choix_commande = OptQuestionnaireDialog.newInstance("Etape de la commande", 1, parm);
            choix_commande.show(getSupportFragmentManager(), "dialog");
        }
        else if( parm.getNom().equals("QDELAIS")){

            DialogFragment choix_commande = OptQuestionnaireDialog.newInstance("Envoie après", 2, parm);
            choix_commande.show(getSupportFragmentManager(), "dialog");
        }
        else if( parm.getNom().equals("QVERSION")){

            DialogFragment choix_commande = OptQuestionnaireDialog.newInstance("Version du questionnaire", 3, parm);
            choix_commande.show(getSupportFragmentManager(), "dialog");
        }

    }

   /* public void validerSaisie(){

        for(Parametre parm : liste_param){
            db.majParametre(parm);
        }
    }*/

    @Override
    public void onFinDialogue(){

        //liste_param = db.getParamQuestionnaire(id_commercial);
        adaptateur.majResultat();
    }

}