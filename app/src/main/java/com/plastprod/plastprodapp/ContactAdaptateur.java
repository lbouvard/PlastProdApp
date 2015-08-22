package com.plastprod.plastprodapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Societe;

/**
 * Created by Laurent on 14/06/2015.
 */
public class ContactAdaptateur extends ArrayAdapter<Contact> {

    int NbLigneSelectionne = 0;
    boolean ActionModeAffichee;

    Context context;
    List<Contact> valeurs;
    Societe client_en_cours;
    Animation animation1;
    Animation animation2;
    ImageView ivFlip;
    ActionMode mode;

    DatabaseHelper db;
    String auteur = "";

    public ContactAdaptateur(Context context, List<Contact> contacts, Context app, Societe client) {
        super(context, -1, contacts);

        this.context = context;
        this.valeurs = contacts;
        this.client_en_cours = client;

        animation1 = AnimationUtils.loadAnimation(context, R.anim.anime1);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.anime2);

        ActionModeAffichee = false;

        auteur = Outils.recupererAuteur(app);
    }

    @Override
    public int getCount() {
        return valeurs.size();
    }

    @Override
    public Contact getItem(int position) {
        return valeurs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.liste_contact, parent, false);
            holder = new ViewHolder();
            holder.icone = (ImageView) convertView.findViewById(R.id.imgUser);
            holder.nom_contact = (TextView) convertView.findViewById(R.id.nom_contact);
            holder.mail_contact = (TextView) convertView.findViewById(R.id.mail_contact);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        Contact contact = getItem(position);
        //couleur de l'icône
        GradientDrawable drawable = (GradientDrawable) holder.icone.getBackground();
        drawable.setColor(Color.parseColor(client_en_cours.getCouleur()));

        //texte
        holder.nom_contact.setText(contact.getPrenom() + " " + contact.getNom());
        holder.mail_contact.setText(contact.getEmail());
        holder.icone.setTag("" + position);

        holder.icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFlip = (ImageView) v;
                ivFlip.clearAnimation();
                ivFlip.setAnimation(animation1);
                ivFlip.startAnimation(animation1);
                setAnimListeners(valeurs.get(Integer.parseInt(v.getTag().toString())));
            }

        });

        if (valeurs.get(position).isSelectionne()) {
            holder.icone.setImageResource(R.drawable.pastille_selected);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gris));
        } else {
            holder.icone.setImageResource(R.drawable.pastille_client);
            drawable = (GradientDrawable) holder.icone.getBackground();
            drawable.setColor(Color.parseColor(client_en_cours.getCouleur()));

            convertView.setBackgroundColor(context.getResources().getColor(R.color.blanc));
        }

        return convertView;
    }

    private void setAnimListeners(final Contact contact) {
        Animation.AnimationListener animListner;
        animListner = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (animation == animation1) {
                    if (contact.isSelectionne()) {
                        ivFlip.setImageResource(R.drawable.pastille_selected);
                    } else {
                        ivFlip.setImageResource(R.drawable.pastille_client);
                    }
                    ivFlip.clearAnimation();
                    ivFlip.setAnimation(animation2);
                    ivFlip.startAnimation(animation2);
                } else {
                    contact.setIsSelectionne(!contact.isSelectionne());
                    setCount();
                    setActionMode();
                }
            }

            // Set selected count
            private void setCount() {
                if (contact.isSelectionne()) {
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
                        mode = ((ContactActivity) context).startSupportActionMode(modeCallBack);
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
        TextView nom_contact;
        TextView mail_contact;
    }

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            mode = null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contact_select, menu);
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            ArrayList<Contact> liste_selectionne = new ArrayList<Contact>();

            // get items selected
            for (Contact contact : ((ContactActivity) context).adaptateur.valeurs) {
                if (contact.isSelectionne()) {
                    liste_selectionne.add(contact);
                }
            }

            switch (item.getItemId()) {
                case R.id.action_supprime:

                    //base de données
                    db = new DatabaseHelper(context);

                    //on parcours la liste des clients séléctionnée
                    for (Contact contact : liste_selectionne) {

                        //si le commercial en cours est l'auteur du client, on supprime
                        if (auteur.equals(contact.getAuteur())) {
                            db.supprimerContact(contact);
                        }
                    }

                    db.close();

                    //maj listview
                    ((ContactActivity) context).majListe();

                    mode.finish();

                    return true;

                default:
                    return false;
            }
        }
    };
}