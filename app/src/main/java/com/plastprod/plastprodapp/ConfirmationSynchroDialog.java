package com.plastprod.plastprodapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Laurent on 17/08/2015.
 */
public class ConfirmationSynchroDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder constructeur = new AlertDialog.Builder(getActivity());
        constructeur.setTitle(R.string.text_dialog_confirm);
        constructeur.setMessage(R.string.titre_dialog_confirm);

        constructeur.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        constructeur.setNegativeButton(R.string.valider, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        });

        return constructeur.create();
    }
}
