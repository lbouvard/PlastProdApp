package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;

import sqlite.helper.Bon;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.LigneCommande;

/**
 * Created by Michael on 29/07/2015.
 */
public class AffichageBon extends ActionBarActivity {

    //pour récupérer les composants
    TextView tvGenerique;
    ImageView imEtat;

    //commande passé lors de la visualisation et modification
    Bon bon_en_cours;
    DatabaseHelper db;

    //commande ou devis (CD ou DE)
    String type;

    Context context;

    //ligne des totaux
    String quantiteTotale;
    String prixTotal;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_bon);
        context = getApplicationContext();

        //on recupère les données passé en argument
        Intent intent = getIntent();
        bon_en_cours = (Bon) intent.getSerializableExtra("Bon");
        type = intent.getStringExtra("Type");

        setTitle(type + bon_en_cours.getId());

        // Seulement si on est en modification/consultation
        if( bon_en_cours != null ) {

            //etat
            imEtat = (ImageView) findViewById(R.id.etat_commande);
            verifierEtat(bon_en_cours.getEtat_commande(), imEtat);

            tvGenerique = (TextView) findViewById(R.id.etat_texte_commande);
            tvGenerique.setText(bon_en_cours.getEtat_commande());

            //date commande
            tvGenerique = (TextView) findViewById(R.id.date_commande);
            tvGenerique.setText( Outils.dateComplete(Outils.chaineVersDate(bon_en_cours.getDate_commande())) );

            tvGenerique = (TextView) findViewById(R.id.nom_societe);
            tvGenerique.setText(bon_en_cours.getClient().getNom());

            tvGenerique = (TextView) findViewById(R.id.adresse_societe);
            tvGenerique.setText(bon_en_cours.getClient().getAdresse1());

            tvGenerique = (TextView) findViewById(R.id.cp_ville_societe);
            tvGenerique.setText(bon_en_cours.getClient().getCode_postal() + " " + bon_en_cours.getClient().getVille());

            tvGenerique = (TextView) findViewById(R.id.nom_commercial);
            tvGenerique.setText(bon_en_cours.getCommercial().getPrenom() + " " + bon_en_cours.getCommercial().getNom());

            tvGenerique = (TextView) findViewById(R.id.adresse_commercial);
            tvGenerique.setText(bon_en_cours.getClient().getAdresse1());

            tvGenerique = (TextView) findViewById(R.id.cp_ville_commercial);
            tvGenerique.setText(bon_en_cours.getClient().getCode_postal() + " " + bon_en_cours.getClient().getVille());

            //totaux
            calculerTotalBon();

            tvGenerique = (TextView) findViewById(R.id.quantite_total);
            tvGenerique.setText(quantiteTotale);

            tvGenerique = (TextView) findViewById(R.id.prix_total);
            tvGenerique.setText(prixTotal + " €");

            //Pour la liste des articles
            ListView lvLigneCommande;
            AffichageBonAdaptateur adaptateur;

            adaptateur = new AffichageBonAdaptateur(getApplicationContext(), bon_en_cours.getLignesBon());

            lvLigneCommande = (ListView) findViewById(R.id.liste_affichage_commande);
            lvLigneCommande.setAdapter(adaptateur);

            setListViewHeightBasedOnChildren(lvLigneCommande);

        }
    }

    public void verifierEtat(String etat, ImageView iv){

        if( etat.equals("En attente de validation")){
            iv.setBackgroundResource(R.drawable.status_commande_attvalid);
        }
        else if( etat.equals("Validée")){
            iv.setBackgroundResource(R.drawable.status_commande_valid);
        }
        else if( etat.equals("En cours de préparation")){
            iv.setBackgroundResource(R.drawable.status_commande_prep);
        }
        else if( etat.equals("Mise à disposition")){
            iv.setBackgroundResource(R.drawable.status_commande_dispo);
        }
        else if( etat.equals("En cours de livraison")){
            iv.setBackgroundResource(R.drawable.status_commande_livraison);
        }
        else if( etat.equals("Terminée")){
            iv.setBackgroundResource(R.drawable.status_commande_termine);
        }
        else if( etat.equals("Créé")){
            iv.setBackgroundResource(R.drawable.status_bon_cree);
        }
        else if( etat.equals("Fin validité")){
            iv.setBackgroundResource(R.drawable.status_bon_finvalid);
        }
        else if( etat.equals("Validé") ){
            iv.setBackgroundResource(R.drawable.status_bon_valid);
        }
        else if( etat.equals("Annulé")) {
            iv.setBackgroundResource(R.drawable.status_bon_annule);
        }
    }

    public void calculerTotalBon(){

        int quantite = 0;
        BigDecimal total = new BigDecimal(0.00);

        for(LigneCommande l : bon_en_cours.getLignesBon()){
            quantite += l.getQuantite();
            total = total.add(l.getPrixTotal());
        }

        quantiteTotale = String.valueOf(quantite);
        prixTotal = total.toString();

    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, GridLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

