package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
    //commande ou devis (CD ou DE)
    String type;
    int idtClient;

    DatabaseHelper db;

    //Pour la liste des articles
    ListView lvLigneCommande;
    AffichageBonAdaptateur adaptateur;

    Context context;

    //ligne des totaux
    String quantiteTotale;
    String prixTotal;

    static final int RETOUR_MAJ = 4000;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_bon);
        context = getApplicationContext();

        //on recupère les données passé en argument
        Intent intent = getIntent();
        bon_en_cours = (Bon) intent.getSerializableExtra("Bon");
        idtClient = intent.getIntExtra("IdtClient", -1);
        type = intent.getStringExtra("Type");

        setTitle(type + bon_en_cours.getId());

        // on rempli les champs
        remplirAffichage();

        adaptateur = new AffichageBonAdaptateur(getApplicationContext(), bon_en_cours.getLignesBon());

        lvLigneCommande = (ListView) findViewById(R.id.liste_affichage_commande);
        lvLigneCommande.setAdapter(adaptateur);

        Outils.setListViewHeightBasedOnChildren(lvLigneCommande);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_affichage_bon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_valide_devis:

                validerDevis();
                return true;

            case R.id.action_supprime_bon :

                supprimerBon();
                finish();
                return true;

            case R.id.action_modifie_bon :

                afficherFormulaireBon();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    private void validerDevis(){

        db = new DatabaseHelper(context);
        db.ajouterCommande(bon_en_cours, bon_en_cours.getLignesBon(), bon_en_cours.getId());
        db.close();
    }

    private void supprimerBon(){

        db = new DatabaseHelper(context);
        db.supprimerBon(bon_en_cours);
        db.close();
    }

    private void afficherFormulaireBon(){

        Intent activite = new Intent(this, FormulaireBon.class);

        activite.putExtra("Bon", bon_en_cours);
        activite.putExtra("IdtClient", idtClient);
        activite.putExtra("Type", bon_en_cours.getType());

        startActivityForResult(activite, RETOUR_MAJ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RETOUR_MAJ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                majDonnees();
            }
        }
    }

    private void majDonnees(){

        db = new DatabaseHelper(context);
        bon_en_cours = db.getBon(bon_en_cours.getId());
        db.close();

        adaptateur.majListe(bon_en_cours.getLignesBon());

        remplirAffichage();

        Outils.setListViewHeightBasedOnChildren(lvLigneCommande);
    }

    private void remplirAffichage(){

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

        tvGenerique = (TextView) findViewById(R.id.prix_total_bon);
        tvGenerique.setText(prixTotal + " €");
    }
}

