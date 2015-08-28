package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import sqlite.helper.DatabaseHelper;

/**
 * Created by Laurent on 28/08/2015.
 */
public class ChoixClientDialog  extends DialogFragment {

    RetourListener mListener;
    DatabaseHelper db;
    int idSociete;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder constructeur = new AlertDialog.Builder(getActivity());
        constructeur.setTitle(R.string.titre_dialog_choix_client);

        db = new DatabaseHelper(getActivity().getApplicationContext());

        final Cursor c = db.getCurseurClient();
        constructeur.setCursor(c, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                c.moveToPosition(which);
                idSociete = c.getInt(c.getColumnIndexOrThrow("_id"));

                //on transmet l'identifiant du client à l'activité bon
                mListener.onDialogChoixClient(idSociete);
            }
        }, "Nom");

        /*constructeur.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogPositiveClick(ChoixClientDialog.this);
            }
        });

        constructeur.setNegativeButton(R.string.btn_annuler, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                mListener.onDialogNegativeClick(ChoixClientDialog.this);
            }
        });*/

        return constructeur.create();
    }

    @Override
    public void onAttach(Activity activite){
        super.onAttach(activite);

        try{
            mListener = (RetourListener)activite;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activite.toString() + "doit implémenter RetourListener");
        }

    }

    public interface RetourListener {
        public void onDialogChoixClient(int id_societe);
    }
}
