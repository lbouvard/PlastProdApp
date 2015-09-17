package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.List;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.StatisCommentaire;


public class CommentaireActivity extends ActionBarActivity {

    CommentaireAdaptateur adaptateur;
    ExpandableListView vue;
    List<StatisCommentaire> liste_com = new ArrayList<StatisCommentaire>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on utilise le layout de l'activité
        setContentView(R.layout.activity_commentaire);

        //on récupère la liste
        vue = (ExpandableListView) findViewById(R.id.lvListeCommentaire);

        //recupération des bons du client demandé
        db = new DatabaseHelper(getApplicationContext());
        liste_com = db.getSatisCommentaire();

        //et on alimente la liste
        adaptateur = new CommentaireAdaptateur(this, liste_com);
        //on lie l'adaptateur à la liste expensive
        vue.setAdapter(adaptateur);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_satisfaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_stats :

                Intent activite_stat= new Intent(this, SatisfactionActivity.class);
                startActivity(activite_stat);

                return true;

            case R.id.action_commentaire :

                // en cours !
                return true;

            case R.id.action_config :

                Intent activite_cfg = new Intent(this, MenuQuestionnaireActivity.class);
                startActivity(activite_cfg);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
