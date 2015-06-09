package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class AfficherMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //récupère l'action appelante
        Intent action = getIntent();
        String message = action.getStringExtra(ClientActivity.EXTRA_MESSAGE);

        //création d'une textview
        TextView tvMessage = new TextView(this);
        tvMessage.setTextSize(40);
        tvMessage.setText(message);

        //on ajoute le texte à l'activité
        setContentView(tvMessage);
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
}
