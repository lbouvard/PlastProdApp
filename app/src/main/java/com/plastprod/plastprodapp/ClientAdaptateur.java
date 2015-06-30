package com.plastprod.plastprodapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    int NbLigneSelectionne = 0;
    boolean ActionModeAffichee;

    Context context;
    List<Societe> valeurs;
    Animation animation1;
    Animation animation2;
    ImageView ivFlip;
    ActionMode mode;

    private String auteur = "";
    private String[] couleur = {"#fff22e8a", "#ff157ebf", "#ff0a5916", "#ffbfa004", "#ffd9c8bf", "#ff14beff", "#fff22111", "#ffbf00ff", "#ffffa100", "#fffeffff"};
    private int max = couleur.length;
    private int i = 0;
    private String couleur_en_cours = "";
    private String couleur_selection = "";

    public ClientAdaptateur(Context context, List<Societe> clients) {
        super(context, -1, clients);

        this.context = context;
        this.valeurs = clients;

        animation1 = AnimationUtils.loadAnimation(context, R.anim.anime1);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.anime2);

        ActionModeAffichee = false;

    }

    @Override
    public int getCount() {
        return valeurs.size();
    }

    @Override
    public Societe getItem(int position){
        return valeurs.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if( convertView == null) {
            convertView = inflater.inflate(R.layout.liste_client, parent, false);
            holder = new ViewHolder();
            holder.icone = (ImageView) convertView.findViewById(R.id.imgUser);
            holder.nom_client = (TextView) convertView.findViewById(R.id.nom_client);
            holder.adresse = (TextView) convertView.findViewById(R.id.adresse);
            holder.nb_contact = (TextView) convertView.findViewById(R.id.nb_contact);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        Societe client = getItem(position);

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

        GradientDrawable drawable = (GradientDrawable) holder.icone.getBackground();
        //set color
        drawable.setColor(Color.parseColor(couleur_en_cours));

        //texte
        holder.nom_client.setText(client.getNom());
        holder.adresse.setText(client.getAdresse1() + ", " + client.getCode_postal() + " " + client.getVille());
        holder.nb_contact.setText(String.valueOf(client.getNb_contact()));
        holder.icone.setTag("" + position);

        holder.icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ivFlip = (ImageView)v;
                ivFlip.clearAnimation();
                ivFlip.setAnimation(animation1);
                ivFlip.startAnimation(animation1);
                setAnimListeners(valeurs.get(Integer.parseInt(v.getTag().toString())));
            }

        });

        if( valeurs.get(position).isSelectionne()){
            holder.icone.setImageResource(R.drawable.pastille_selected);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gris));
        }
        else{
            holder.icone.setImageResource(R.drawable.pastille_client);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.blanc));
        }

        return convertView;
    }

    private void setAnimListeners(final Societe client) {
        Animation.AnimationListener animListner;
        animListner = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (animation == animation1) {
                    if (client.isSelectionne()) {
                        ivFlip.setImageResource(R.drawable.pastille_selected);
                    } else {
                        ivFlip.setImageResource(R.drawable.pastille_client);
                    }
                    ivFlip.clearAnimation();
                    ivFlip.setAnimation(animation2);
                    ivFlip.startAnimation(animation2);
                } else {
                    client.setIsSelectionne(!client.isSelectionne());
                    setCount();
                    setActionMode();
                }
            }

            // Set selected count
            private void setCount() {
                if (client.isSelectionne()) {
                    NbLigneSelectionne++;
                } else {
                    if (NbLigneSelectionne != 0) {
                        NbLigneSelectionne--;
                    }
                }
            }

            // Show/Hide action mode
            private void setActionMode() {
                if (NbLigneSelectionne > 0) {
                    if (!ActionModeAffichee) {
                        mode = ((ClientActivity) context).startActionMode(new ClientActivity.ActionModeSpe(context));
                        ActionModeAffichee = true;
                    }
                } else if (mode != null) {
                    mode.finish();
                    ActionModeAffichee = false;
                }

                // Set action mode title
                if (mode != null)
                    mode.setTitle(String.valueOf(NbLigneSelectionne));

                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
            }
        };

        animation1.setAnimationListener(animListner);
        animation2.setAnimationListener(animListner);
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

    static class ViewHolder {
        ImageView icone;
        TextView nom_client;
        TextView adresse;
        TextView nb_contact;
    }
}