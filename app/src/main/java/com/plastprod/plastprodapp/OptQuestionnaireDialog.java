package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.Parametre;

/**
 * Created by Laurent on 28/08/2015.
 */
public class OptQuestionnaireDialog extends DialogFragment {

    RetourListener mListener;
    DatabaseHelper db;
    String titre;
    String choix;
    int type;

    Parametre valeurEnCours;

    public static OptQuestionnaireDialog newInstance(String titre, int type, Parametre param) {

        OptQuestionnaireDialog frag = new OptQuestionnaireDialog();

        Bundle parametre = new Bundle();
        parametre.putString("titre", titre);
        parametre.putInt("type", type);
        parametre.putSerializable("en_cours", param);

        frag.setArguments(parametre);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        titre = getArguments().getString("titre");
        type = getArguments().getInt("type");
        valeurEnCours = (Parametre)getArguments().getSerializable("en_cours");

        AlertDialog.Builder constructeur = new AlertDialog.Builder(getActivity());
        constructeur.setTitle(titre);

        db = new DatabaseHelper(getActivity().getApplicationContext());

        switch(type){
            case 1 :
                //Etape de la commande
                final Cursor c = db.getCurseurChoix("QETAPE");

                constructeur.setCursor(c, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c.moveToPosition(which);
                        choix = c.getString(c.getColumnIndex("Valeur"));
                        //on sauvegarde le choix de la valeur du paramètre
                        valeurEnCours.setValeur(choix);
                        db.majParametre(valeurEnCours);
                        db.close();

                        //pour indiquer la fin et demander la mise à jour de la liste
                        mListener.onFinDialogue();
                    }
                }, "Valeur");

                break;

            case 2 :
                //Délais
                final Cursor c2 = db.getCurseurChoix("QDELAIS");

                constructeur.setCursor(c2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c2.moveToPosition(which);
                        choix = c2.getString(c2.getColumnIndex("Valeur"));
                        //on sauvegarde le choix de la valeur du paramètre
                        valeurEnCours.setValeur(choix);
                        db.majParametre(valeurEnCours);
                        db.close();

                        //pour indiquer la fin et demander la mise à jour de la liste
                        mListener.onFinDialogue();
                    }
                }, "Valeur");

                break;

            case 3 :
                //Version
                final Cursor c3 = db.getCurseurChoix("QVERSION");

                constructeur.setCursor(c3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c3.moveToPosition(which);
                        choix = c3.getString(c3.getColumnIndex("Valeur"));
                        //on sauvegarde le choix de la valeur du paramètre
                        valeurEnCours.setValeur(choix.toUpperCase());
                        db.majParametre(valeurEnCours);
                        db.close();

                        //pour indiquer la fin et demander la mise à jour de la liste
                        mListener.onFinDialogue();
                    }
                }, "Valeur");

                break;

            default:
                break;
        }

        //db.close();

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
        public void onFinDialogue();
    }
}
