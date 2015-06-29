package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;

import android.widget.AbsListView.MultiChoiceModeListener;

public class ClientActivity extends ActionBarActivity {

    List<Societe> liste_client = new ArrayList<Societe>();
    DatabaseHelper db;
    ListView lvClient;
    ClientAdaptateur adaptateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        //utilisateur authentifié
        if( Outils.VerifierSession(getApplicationContext()) ){

            //connexion base
            db = new DatabaseHelper(getApplicationContext());

            //récupération des clients
            liste_client = db.getSocietes(null);

            lvClient = (ListView) findViewById(R.id.liste_client);

            adaptateur = new ClientAdaptateur(this, liste_client);
            lvClient.setAdapter(adaptateur);
            lvClient.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            lvClient.setMultiChoiceModeListener(new MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode,
                                                      int position, long id, boolean checked) {
                    // Capture total checked items
                    final int checkedCount = lvClient.getCheckedItemCount();
                    // Set the CAB title according to total checked items
                    mode.setTitle(checkedCount);
                    // Calls toggleSelection method from ListViewAdapter Class
                    adaptateur.toggleSelection(position);
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {

                        /*case R.id.delete:
                            // Calls getSelectedIds method from ListViewAdapter Class
                            SparseBooleanArray selected = adaptateur.getClientsSelectionnes();
                            // Captures all selected ids with a loop
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    WorldPopulation selecteditem = listviewadapter
                                            .getItem(selected.keyAt(i));
                                    // Remove selected items following the ids
                                    listviewadapter.remove(selecteditem);
                                }
                            }
                            // Close CAB
                            mode.finish();

                            return true;*/

                        default:
                            return false;
                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_client_select, menu);
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub
                    adaptateur.removeSelection();
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
           /* lvClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            ;
                            break;
                    }
                }
            });*/
        }
        else
            terminerSession();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_recherche) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void terminerSession(){

        Intent activite = new Intent(this, AccueilActivity.class);
        activite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activite);
        finish();
    }

}
