package com.plastprod.plastprodapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
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

    DatabaseHelper db;
    String auteur = "";

    public ClientAdaptateur(Context context, List<Societe> clients) {
        super(context, -1, clients);

        this.context = context;
        this.valeurs = clients;

        animation1 = AnimationUtils.loadAnimation(context, R.anim.anime1);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.anime2);

        ActionModeAffichee = false;

        auteur = Outils.recupererAuteur(context);
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
        //couleur de l'icône
        GradientDrawable drawable = (GradientDrawable) holder.icone.getBackground();
        drawable.setColor(Color.parseColor(client.getCouleur()));

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
            drawable = (GradientDrawable) holder.icone.getBackground();
            drawable.setColor(Color.parseColor(client.getCouleur()));

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
                        //mode = ((ClientActivity) context).startActionMode(new ClientActivity.ActionModeSpe(context));
                        mode = ((ClientActivity) context).startSupportActionMode(modeCallBack);
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

    static class ViewHolder {
        ImageView icone;
        TextView nom_client;
        TextView adresse;
        TextView nb_contact;
    }

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            MenuItem itemContact = menu.findItem(R.id.action_contact);
            MenuItem itemHisto = menu.findItem(R.id.action_histo);
            //depending on your conditions, either enable/disable

            if( NbLigneSelectionne > 1) {
                itemContact.setEnabled(false);
                itemHisto.setEnabled(false);
            }
            else{
                itemContact.setEnabled(true);
                itemHisto.setEnabled(true);
            }

            return true;
        }

        public void onDestroyActionMode(ActionMode mode){
            mode = null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu){
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_client_select, menu);
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item){

            ArrayList<Societe> liste_selectionne = new ArrayList<Societe>();

            // get items selected
            for (Societe client : ((ClientActivity) context).adaptateur.valeurs) {
                if( client.isSelectionne() ) {
                    liste_selectionne.add(client);
                }
            }

            switch (item.getItemId()) {
                case R.id.action_supprime:

                    //base de données
                    db = new DatabaseHelper(context);

                    //on parcours la liste des clients séléctionnée
                    for( Societe client : liste_selectionne){

                        //si le commercial en cours est l'auteur du client, on supprime
                        if( auteur.equals(client.getAuteur())) {
                            db.suppprimerSociete(client, true);
                        }
                    }

                    db.close();
                    mode.finish();

                    return true;

                case R.id.action_contact:

                    ((ClientActivity) context).afficherContacts(liste_selectionne.get(0));
                    return true;

                case R.id.action_histo:

                    return true;

                default:
                    return false;
            }
        }
    };
}