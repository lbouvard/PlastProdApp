package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RechercherActivity extends Activity {

    private TextView txtQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechercher);

        txtQuery = (TextView) findViewById(R.id.txtQuery);

        handleIntent(getIntent());
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_rechecher, menu);
        return true;
    }*/

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            txtQuery.setText("Recherche : " + query);
        }
    }
}
