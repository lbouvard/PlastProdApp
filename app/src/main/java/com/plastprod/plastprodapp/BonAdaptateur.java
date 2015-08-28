package com.plastprod.plastprodapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sqlite.helper.Bon;

/**
 * Created by Michael on 28/07/2015.
 */
public class BonAdaptateur extends ArrayAdapter<Bon> {

    // Une liste de personnes
    private List<Bon> liste_bon;
    //Le contexte dans lequel est présent notre adapter
    private Context context;

    public BonAdaptateur(Context context, List<Bon> liste_bon) {
        super(context, -1, liste_bon);
        this.context = context;
        this.liste_bon = liste_bon;
    }

    public int getCount() {
        return liste_bon.size();
    }


    public Bon getItem(int position) {
        return liste_bon.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            convertView =  inflater.inflate(R.layout.liste_bon, parent, false);
        }

        //(2) : Récupération des TextView de notre layout
        TextView dateCommande = (TextView)convertView.findViewById(R.id.datecommande);
        TextView numeroBon = (TextView)convertView.findViewById(R.id.idbon);
        //TextView a_Contact = (TextView)convertView.findViewById(R.id.aContact);

        //(3) : Renseignement des valeurs
        dateCommande.setText( Outils.DateJourMoisAnnee(Outils.chaineVersDate(liste_bon.get(position).getDate_commande())) );
        numeroBon.setText(liste_bon.get(position).getType() + liste_bon.get(position).getId());

        //a_Contact.setText(liste_bon.get(position).getClient().getNom());

        //On retourne l'item créé.
        return convertView;
    }


}
