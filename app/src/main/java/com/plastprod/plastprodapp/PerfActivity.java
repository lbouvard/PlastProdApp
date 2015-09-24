package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Satisfaction;


public class PerfActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perf);

        // Find the ListView resource.
        ListView menu = (ListView) findViewById( R.id.liste_menu );

        // Create and populate a List of planet names.
        String[] options = new String[] { "Statistiques personelles", "Mes objectifs" };
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on récupère l'item séléctionné
        String valeur = (String)arg0.getItemAtPosition(arg2);

        if( valeur.equals("Statistiques personelles") ) {
            Outils.afficherToast(getApplicationContext(), "Satistiques personnelles en construction.");
        }
        else{
            Outils.afficherToast(getApplicationContext(), "Objectifs en construction.");
        }

    }
}
