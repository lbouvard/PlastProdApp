package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import sqlite.helper.LigneCommande;

/**
 * Created by Michael on 31/07/2015.
 */
public class AffichageBonAdaptateur extends BaseAdapter {

    // Une liste de personnes
    List<LigneCommande> liste_articles;

    //Le contexte dans lequel est présent notre adapter
    private Context context;

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater inflater;

    public AffichageBonAdaptateur(Context context, List<LigneCommande> liste_articles) {
        this.context = context;
        this.liste_articles = liste_articles;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return liste_articles.size();
    }


    public Object getItem(int position) {
        return liste_articles.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout layoutItem;
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "personne_layout.xml"
            layoutItem = (RelativeLayout) inflater.inflate(R.layout.liste_article, parent, false);
        }
        else{
            layoutItem = (RelativeLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView tvNom = (TextView) layoutItem.findViewById(R.id.nom_article);
        TextView tvPrixUnitaire = (TextView) layoutItem.findViewById(R.id.prix_unitaire);
        TextView tvQuantite = (TextView) layoutItem.findViewById(R.id.quantite);
        TextView tvReference = (TextView) layoutItem.findViewById(R.id.ref_article);
        TextView tvPrixTotal = (TextView) layoutItem.findViewById(R.id.prix_total);

        //(3) : Renseignement des valeurs
        tvNom.setText(liste_articles.get(position).getNom());
        tvPrixUnitaire.setText(afficherPrixEuro(liste_articles.get(position).getPrixUnitaire()));
        tvQuantite.setText(String.valueOf(liste_articles.get(position).getQuantite()));
        tvReference.setText(liste_articles.get(position).getCode());
        tvPrixTotal.setText(liste_articles.get(position).getPrixTotal().toString() + " €");

        //On retourne l'item créé.
        return layoutItem;
    }

    private String afficherPrixEuro(double prix){

        BigDecimal intermed = new BigDecimal(0.00);
        intermed = intermed.add(new BigDecimal(prix));
        intermed.setScale(2, BigDecimal.ROUND_HALF_UP);

        return intermed.toString() + " €";
    }
}
