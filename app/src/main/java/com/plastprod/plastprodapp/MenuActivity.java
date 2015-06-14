package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sqlite.helper.Contact;


public class MenuActivity extends ActionBarActivity {

    Intent itActivite;
    Contact commercial;
    TextView tvNom;
    TextView tvMail;
    ListView lvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        itActivite = getIntent();
        commercial = (Contact) itActivite.getSerializableExtra("Commercial");

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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

    public void Deconnecter(View vue){
        //getParentActivityIntent();
    }
}
