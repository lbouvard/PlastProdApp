package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.LigneCommande;

/**
 * Created by Laurent on 21/08/2015.
 */
public class HistocdAdaptateur extends BaseExpandableListAdapter{

    private Context context;
    private List<Bon> liste_bon;

    public HistocdAdaptateur(Context context, List<Bon> liste ) {
        this.context = context;
        this.liste_bon = liste;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.liste_bon.get(groupPosition).getLignesBon().get(childPosititon);
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

            convertView = infalInflater.inflate(R.layout.item_histocd, null);
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
        return this.liste_bon.get(groupPosition).getLignesBon().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.liste_bon.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.liste_bon.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        Bon commande = (Bon) getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.liste_histocd, null);
        }

        TextView txtIdtCommande = (TextView) convertView.findViewById(R.id.id_commande);
        txtIdtCommande.setText("Commande CD" + commande.getId());
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        TextView txtMontant = (TextView) convertView.findViewById(R.id.montant);
        txtMontant.setText( String.valueOf(commande.calculerPrixTotal()) + " €" );

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
