package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.LigneCommande;
import sqlite.helper.Livraison;

/**
 * Created by Laurent on 07/09/2015.
 */
public class LivraisonAdaptateur extends BaseExpandableListAdapter{

    private Context context;
    private List<Livraison> liste_suivi;

    public LivraisonAdaptateur(Context context, List<Livraison> liste ) {
        this.context = context;
        this.liste_suivi = liste;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.liste_suivi.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final LigneCommande ligne = (LigneCommande)getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.item_histobon, null);
        }

        TextView txtNomArticle = (TextView) convertView.findViewById(R.id.nom_article);
        txtNomArticle.setText(ligne.getNom());

        TextView txtPrixUnitaire = (TextView) convertView.findViewById(R.id.prix_article);
        txtPrixUnitaire.setText(ligne.getPrixRemise().toString() + " €");

        TextView txtQuantite = (TextView) convertView.findViewById(R.id.quantite_article);
        txtQuantite.setText(String.valueOf(ligne.getQuantite()) );

        TextView txtPrixTotal = (TextView) convertView.findViewById(R.id.total_article);
        txtPrixTotal.setText(ligne.getPrixTotal().toString() + " €");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.liste_suivi.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.liste_suivi.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        Livraison commande = (Livraison) getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.liste_livraison, null);
        }

        TextView txtIdtCommande = (TextView) convertView.findViewById(R.id.id_commande);
        txtIdtCommande.setText("Commande CD" + commande.getIdt());
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        TextView txtNomClient = (TextView) convertView.findViewById(R.id.nom_client);
        txtNomClient.setText( String.valueOf(commande.getNomSociete()));

        TextView txtTransporteur = (TextView) convertView.findViewById(R.id.transporteur);
        txtTransporteur.setText( String.valueOf(commande.getTransporteur()));

        ImageView imgSelecteur = (ImageView) convertView.findViewById(R.id.selecteur);

        if( isExpanded ){
            imgSelecteur.setBackgroundResource(R.drawable.ic_expand_less_black_48dp);
        }else{
            imgSelecteur.setBackgroundResource(R.drawable.ic_expand_more_black_48dp);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
