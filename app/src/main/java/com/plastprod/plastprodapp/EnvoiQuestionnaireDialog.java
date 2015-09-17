package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Satisfaction;
import sqlite.helper.Societe;

public class EnvoiQuestionnaireDialog extends DialogFragment {

    RetourListener mListener;
    DatabaseHelper db;
    Satisfaction questionnaire;
    String nom_contact;
    String adresse_contact;
    int id_societe;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // constructeur du dialogue
        final AlertDialog.Builder constructeur = new AlertDialog.Builder(getActivity());

        //on recupère le questionnaire à transmettre
        questionnaire =  (Satisfaction)getArguments().getSerializable("satisfaction");

        // on récupère la vue spécifique
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.activity_envoi_questionnaire_dialog, null);

        // on lie la vue au constructeur
        constructeur.setView(layout)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        genererMailQuestionnaire();
                        mListener.validerEnvoi(questionnaire, adresse_contact);
                    }
                })
                .setNegativeButton(R.string.btn_annuler, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.annulerEnvoi();
                    }
                });

        // création du dialogue
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

    @Override
    public void onStart(){

        super.onStart();

        // recupérer les produits
        List<Societe> liste_client = new ArrayList<Societe>();

        db = new DatabaseHelper(getActivity().getApplicationContext());
        liste_client = db.getSocietes("C");

        // on récupère la liste déroulante des clients
        Spinner spinner = (Spinner) this.getDialog().findViewById(R.id.dropdown_client);

        ArrayAdapter<Societe> adaptateur = new ArrayAdapter<Societe>(getDialog().getContext(),
                android.R.layout.simple_spinner_item, liste_client);
        adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //on applique l'adaptateur à la liste déroulante
        spinner.setAdapter(adaptateur);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Societe client = (Societe) parent.getItemAtPosition(position);
                id_societe = client.getId();
                remplirListeContact(client);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db.close();


    }

    public interface RetourListener {
        public void validerEnvoi(Satisfaction questionnaire, String adresse);
        public void annulerEnvoi();
    }

    private void remplirListeContact(Societe client){

        List<Contact> liste_contact = new ArrayList<Contact>();
        db = new DatabaseHelper(getActivity().getApplicationContext());

        liste_contact =  db.getContacts(client.getId(), null);

        // on récupère la liste déroulante des contacts
        Spinner spinner = (Spinner) this.getDialog().findViewById(R.id.dropdown_contact);

        ArrayAdapter<Contact> adaptateur = new ArrayAdapter<Contact>(getDialog().getContext(),
                android.R.layout.simple_spinner_item, liste_contact);
        adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //on applique l'adaptateur à la liste déroulante
        spinner.setAdapter(adaptateur);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Contact contact = (Contact) parent.getItemAtPosition(position);
                nom_contact = contact.toString();
                remplirEmail(contact);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db.close();
    }

    private void remplirEmail(Contact contact){

        //variables
        EditText edGenerique = null;

        //remise
        edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_email);
        edGenerique.setText(contact.getEmail());
    }

    private void genererMailQuestionnaire() {

        String corps;

        EditText mail = (EditText) this.getDialog().findViewById(R.id.edt_email);
        adresse_contact = mail.getText().toString();

        corps = questionnaire.getCorps().replace("_CONTACT_", nom_contact).replace("_LIEN_", questionnaire.getLien());

        questionnaire.setId_societe(id_societe);
        questionnaire.setContact(nom_contact);
        questionnaire.setCorps(corps);
    }
}
