package com.plastprod.plastprodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sqlite.helper.Livraison;
import sqlite.helper.StatisCommentaire;

/**
 * Created by Laurent on 07/09/2015.
 */
public class CommentaireAdaptateur extends BaseExpandableListAdapter{

    private Context context;
    private List<StatisCommentaire> liste_com;

    public CommentaireAdaptateur(Context context, List<StatisCommentaire> liste) {
        this.context = context;
        this.liste_com = liste;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.liste_com.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final StatisCommentaire info = (StatisCommentaire)getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.item_commentaire, null);
        }

        TextView txtQuestion = (TextView) convertView.findViewById(R.id.tvQuestion);
        txtQuestion.setText(info.getQuestion());

        TextView txtCommentaire = (TextView) convertView.findViewById(R.id.tvReponseOuverte);
        txtCommentaire.setText(info.getCommentaire());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.liste_com.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.liste_com.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        StatisCommentaire com = (StatisCommentaire) getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.liste_commentaire, null);
        }

        TextView txtClient = (TextView) convertView.findViewById(R.id.id_client);
        txtClient.setText(com.getSociete());

        TextView txtNomContact = (TextView) convertView.findViewById(R.id.id_nom_contact);
        txtNomContact.setText(com.getClient());

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
