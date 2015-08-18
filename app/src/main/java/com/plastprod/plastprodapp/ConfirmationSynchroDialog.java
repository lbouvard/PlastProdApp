package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Laurent on 17/08/2015.
 */
public class ConfirmationSynchroDialog extends DialogFragment {

    RetourListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder constructeur = new AlertDialog.Builder(getActivity());
        constructeur.setTitle(R.string.titre_dialog_confirm);
        constructeur.setMessage(R.string.text_dialog_confirm);

        constructeur.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogPositiveClick(ConfirmationSynchroDialog.this);
            }
        });

        constructeur.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                mListener.onDialogNegativeClick(ConfirmationSynchroDialog.this);
            }
        });

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
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
