package com.plastprod.plastprodapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Parametre;
import sqlite.helper.Satisfaction;


public class MenuQuestionnaireActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, EnvoiQuestionnaireDialog.RetourListener {

    DatabaseHelper db;
    Satisfaction mQuestionnaire;        //pour l'envoi du mail
    static final int RETOUR_MAJ = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_questionnaire);

        // Find the ListView resource.
        ListView menu = (ListView) findViewById( R.id.liste_menu_questionnaire );

        // Create and populate a List of planet names.
        String[] options = new String[] { "Envoi manuel", "Paramètres automatique" };
        ArrayList<String> liste_options = new ArrayList<String>();
        liste_options.addAll(Arrays.asList(options));

        // Create ArrayAdapter using the planet list.
        ArrayAdapter adaptateur = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liste_options);

        // Set the ArrayAdapter as the ListView's adapter.
        menu.setAdapter( adaptateur );
        menu.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on récupère l'item séléctionné
        String valeur = (String)arg0.getItemAtPosition(arg2);

        if( valeur.equals("Envoi manuel") ){

            //identifiant du commercial
            final Global jeton = (Global) getApplicationContext();
            int id_commercial = jeton.getUtilisateur().getId();

            //on récupère les infos du questionnaire
            db = new DatabaseHelper(getApplicationContext());
            Satisfaction sat = db.recupererQuestionnaire(id_commercial);

            Bundle bundle = new Bundle();
            bundle.putSerializable("satisfaction", sat);

            DialogFragment envoiQ = new EnvoiQuestionnaireDialog();
            envoiQ.setArguments(bundle);
            envoiQ.show(getSupportFragmentManager(), "envoi_questionnaire_dialog");
        }
        else{

            Intent activite = new Intent(this, OptQuestionnaireActivity.class);
            startActivity(activite);
        }
    }

    public void validerEnvoi(Satisfaction questionnaire, String adresse){

        mQuestionnaire = questionnaire;
        envoyerEmail(questionnaire, adresse);
    }

    public void annulerEnvoi(){

    }

    public void envoyerEmail(Satisfaction questionnaire, String adresse)
    {
        //questionnaire.setDate_envoi(dateEnCours());

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{adresse});
        email.putExtra(Intent.EXTRA_SUBJECT, "Questionnaire de satisfaction");
        email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(questionnaire.getCorps()));

        startActivityForResult(Intent.createChooser(email, "Envoi du questionnaire..."), RETOUR_MAJ);
    }

    private String dateEnCours() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RETOUR_MAJ) {

            db = new DatabaseHelper(getApplicationContext());

            //on sauvegarde l'envoi du questionnaire
            mQuestionnaire.setDate_envoi(dateEnCours());
            db.ajouterSatisfaction(mQuestionnaire);
            db.close();

            Outils.afficherToast(getApplicationContext(), "Le message contenant le questionnaire a été envoyé.");
        }
    }
}
