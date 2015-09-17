package com.plastprod.plastprodapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Parametre;


public class AdminActivity extends ActionBarActivity {

    DatabaseHelper db;
    List<Parametre> liste_params = new ArrayList<Parametre>();

    EditText ssid;
    EditText adresseip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = new DatabaseHelper(getApplicationContext());

        liste_params = db.getParametres(0);

        //on renseigne les champs
        for(Parametre param : liste_params){

            if( param.getNom().equals("ADRESSEIP_SRV") ){
                adresseip = (EditText) findViewById(R.id.etAdresseIp);
                adresseip.setText(param.getValeur());
            }

            if( param.getNom().equals("SSID_SOCIETE") ){
                ssid = (EditText) findViewById(R.id.etSsid);
                ssid.setText(param.getValeur());
            }
        }

        db.close();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_admin, menu);

        //on masque les items
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_admin, menu);
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

    public void synchroniser_first(View vue){

        db = new DatabaseHelper(getApplicationContext());
        String ssid = db.getSsidWifi();

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo active = cm.getActiveNetworkInfo();

        if( active.isConnected() && active.getType() == ConnectivityManager.TYPE_WIFI && active.getExtraInfo().replace("\"","").equals(ssid) ) {
            new RestApi().execute(getApplicationContext(), getApplicationContext());
        }
        else{
            Outils.afficherToast(getApplicationContext(), "Vous devez être connecté en WIFI chez PlastProd.");
        }

        db.close();
    }

    public void enregistrer(View vue){

        db = new DatabaseHelper(getApplicationContext());

        for(Parametre param : liste_params){

            if( param.getNom().equals("ADRESSEIP_SRV") ){
                param.setValeur(adresseip.getText().toString());
                db.majParametre(param);
            }

            if( param.getNom().equals("SSID_SOCIETE") ){
                param.setValeur(ssid.getText().toString());
                db.majParametre(param);
            }
        }

        Toast notification = Toast.makeText(getApplicationContext(), "Les paramètres ont été modifiés avec succès.", Toast.LENGTH_LONG);
        notification.show();

        db.close();
    }
}
