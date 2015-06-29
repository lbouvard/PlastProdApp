package com.plastprod.plastprodapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import sqlite.helper.Societe;

/**
 * Created by Laurent on 14/06/2015.
 */
public class ClientAdaptateur extends ArrayAdapter<Societe> {

    private final Context context;
    private final List<Societe> valeurs;
    private String auteur = "";
    private String[] couleur = {"#fff22e8a", "#ff157ebf", "#ff0a5916", "#ffbfa004", "#ffd9c8bf", "#ff14beff", "#fff22111", "#ffbf00ff", "#ffffa100", "#fffeffff"};
    private int max = couleur.length;
    private int i = 0;
    private String couleur_en_cours = "";
    private SparseBooleanArray mSelectedItemsIds;

    public ClientAdaptateur(Context context, List<Societe> clients) {
        super(context, -1, clients);

        this.context = context;
        this.valeurs = clients;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View vueLigne = inflater.inflate(R.layout.liste_client, parent, false);

        ImageView icone = (ImageView) vueLigne.findViewById(R.id.imgUser);
        TextView nom_client = (TextView) vueLigne.findViewById(R.id.nom_client);
        TextView adresse = (TextView) vueLigne.findViewById(R.id.adresse);
        TextView nb_contact = (TextView) vueLigne.findViewById(R.id.nb_contact);

        Societe client = valeurs.get(position);

        //couleur icône
        if( position == 0) {
            changeCouleur();
            auteur = client.getAuteur();
        }
        else{
            if( !auteur.equals(client.getAuteur()) ){
                changeCouleur();
                auteur = client.getAuteur();
            }
        }

        GradientDrawable drawable = (GradientDrawable) icone.getBackground();
        //set color
        drawable.setColor(Color.parseColor(couleur_en_cours));

        //texte
        nom_client.setText(client.getNom());
        adresse.setText(client.getAdresse1() + ", " + client.getCode_postal() + " " + client.getVille());
        nb_contact.setText(String.valueOf(client.getNb_contact()));

        return vueLigne;
    }

    public void changeCouleur(){

        if( i < max ){
            couleur_en_cours = couleur[i];
            i++;
        }
        else {
            couleur_en_cours = couleur[0];
            i = 0;
        }
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getClientsSelectionnes() {
        return mSelectedItemsIds;
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
}