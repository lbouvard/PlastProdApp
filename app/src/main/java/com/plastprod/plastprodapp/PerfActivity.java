package com.plastprod.plastprodapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class PerfActivity extends ActionBarActivity {

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
}
