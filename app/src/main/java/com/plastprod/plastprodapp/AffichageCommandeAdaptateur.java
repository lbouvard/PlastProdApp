package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sqlite.helper.LigneCommande;

/**
 * Created by Michael on 31/07/2015.
 */
public class AffichageCommandeAdaptateur extends BaseAdapter {

    // Une liste de personnes
    private List<LigneCommande> mListB;
    //Le contexte dans lequel est présent notre adapter
    private Context mContext;
    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;
    public int prix_total = 0;

    public AffichageCommandeAdaptateur(Context context, List<LigneCommande> aListB) {
        mContext = context;
        mListB = aListB;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mListB.size();
    }


    public Object getItem(int position) {
        return mListB.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layoutItem;
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "personne_layout.xml"
            layoutItem = (RelativeLayout) mInflater.inflate(R.layout.liste_article, parent, false);
        } else {
            layoutItem = (RelativeLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView a_Nom = (TextView) layoutItem.findViewById(R.id.nom_article);
        TextView a_Prix = (TextView) layoutItem.findViewById(R.id.prix);
        TextView a_Quantite = (TextView) layoutItem.findViewById(R.id.quantite);
        TextView a_Reference = (TextView) layoutItem.findViewById(R.id.reference);
        TextView prix_total = (TextView) layoutItem.findViewById(R.id.prix_total);

        //(3) : Renseignement des valeurs
        a_Nom.setText(mListB.get(position).getNom());
        a_Prix.setText("" + mListB.get(position).getPrixUnitaire());
        a_Quantite.setText("" + mListB.get(position).getQuantite());
        a_Reference.setText(mListB.get(position).getCode());
        prix_total.setText("" + mListB.get(position).getPrixTotal());

        //On retourne l'item créé.
        return layoutItem;
    }
}
