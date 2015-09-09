package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sqlite.helper.Bon;
import sqlite.helper.LigneCommande;
import sqlite.helper.Livraison;

/**
 * Created by Laurent on 07/09/2015.
 */
public class LivraisonAdaptateur extends BaseExpandableListAdapter{

    private Context context;
    private List<Livraison> liste_suivi;
    private SimpleDateFormat date_base = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat date_ihm = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private Date date_util = new Date();
    private String date_chaine = "";

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

        final Livraison info = (Livraison)getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.item_livraison, null);
        }



        TextView txtDepartUsine = (TextView) convertView.findViewById(R.id.depart_societe);
        try {
            this.date_util = date_base.parse(info.getDateDispo());
            this.date_chaine = date_ihm.format(this.date_util);
            txtDepartUsine.setText(this.date_chaine);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        TextView txtDispoTransporteur = (TextView) convertView.findViewById(R.id.arrive_transporteur);
        try {
            this.date_util = date_base.parse(info.getDateEnvoi());
            this.date_chaine = date_ihm.format(this.date_util);
            txtDispoTransporteur.setText(this.date_chaine);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        TextView txtArrivePrevu = (TextView) convertView.findViewById(R.id.arrive_prevu);
        try {
            this.date_util = date_base.parse(info.getDateRecu());
            this.date_chaine = date_ihm.format(this.date_util);
            txtArrivePrevu.setText(this.date_chaine);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        TextView txtLienSuivi = (TextView) convertView.findViewById(R.id.lien_suivi);
        txtLienSuivi.setText(info.getTrack());

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
