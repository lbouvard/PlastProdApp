package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class ChoixClientActivity extends ActionBarActivity implements ChoixClientDialog.RetourListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choix_client);

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ) {
            //Dialogue pour choisir le client
            DialogFragment choix_client = new ChoixClientDialog();
            choix_client.show(getSupportFragmentManager(), "choix_client");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_client, menu);
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

    @Override
    public void onDialogChoixClient(int id_societe, String type){

        Intent activite = new Intent(this, BonActivity.class);

        if( type.equals("C") )
            activite.putExtra("Type", "CD");
        else if( type.equals("P") )
            activite.putExtra("Type", "DE");

        activite.putExtra("Id", id_societe);

        startActivity(activite);
    }
}
