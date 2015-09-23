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
 * Auteur       : Michael Goncalo
 * Date         : 29/07/2015
 * Description  : Permet d'afficher un bon (devis ou commande)
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

        setTitle(bon_en_cours.getNumero_commande());

        // on rempli les champs
        remplirAffichage();

        adaptateur = new AffichageBonAdaptateur(getApplicationContext(), bon_en_cours.getLignesBon());

        lvLigneCommande = (ListView) findViewById(R.id.liste_affichage_commande);
        lvLigneCommande.setAdapter(adaptateur);

        Outils.setListViewHeightBasedOnChildren(lvLigneCommande);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_affichage_bon, menu);

        // si le bon n'est pas un devis à valider (état créé), on cache l'option de validation
        if( !(type.equals("DE") && bon_en_cours.getEtat_commande().equals("Créé")) ){
            menu.getItem(0).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_affichage_bon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_valide_devis:

                validerDevis();
                return true;

            case R.id.action_supprime_bon :

                if( verifierModifPossible(bon_en_cours.getEtat_commande()) ) {
                    supprimerBon();
                    finish();
                }
                else{
                    Outils.afficherToast(context, "L'état du bon ne permet pas sa suppression.");
                }
                return true;

            case R.id.action_modifie_bon :

                if( verifierModifPossible(bon_en_cours.getEtat_commande()) ) {
                    afficherFormulaireBon();
                    return true;
                }
                else{
                    Outils.afficherToast(context, "L'état du bon ne permet pas sa modification.");
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void verifierEtat(String etat, ImageView iv) {

        switch (etat) {

            case "En attente de validation":
                iv.setBackgroundResource(R.drawable.status_commande_attvalid);
                break;
            case "Validée":
                iv.setBackgroundResource(R.drawable.status_commande_valid);
                break;
            case "En cours de préparation":
                iv.setBackgroundResource(R.drawable.status_commande_prep);
                break;
            case "Mise à disposition":
                iv.setBackgroundResource(R.drawable.status_commande_dispo);
                break;
            case "En cours de livraison":
                iv.setBackgroundResource(R.drawable.status_commande_livraison);
                break;
            case "Terminée":
                iv.setBackgroundResource(R.drawable.status_commande_termine);
                break;
            case "Créé":
                iv.setBackgroundResource(R.drawable.status_bon_cree);
                break;
            case "Fin validité":
                iv.setBackgroundResource(R.drawable.status_bon_finvalid);
                break;
            case "Validé":
                iv.setBackgroundResource(R.drawable.status_bon_valid);
                break;
            case "Annulé":
                iv.setBackgroundResource(R.drawable.status_bon_annule);
                break;
            default:
                break;
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

        // génération d'un numéro de commande unique
        Global jeton = (Global) getApplicationContext();
        String indice = "CD" + String.valueOf(jeton.getUtilisateur().getId()) + String.valueOf(jeton.getIndice_bon());
        bon_en_cours.setNumero_commande(indice);
        bon_en_cours.setCommercial_id(jeton.getUtilisateur().getId());
        bon_en_cours.setClient_id(idtClient);
        //incrémente l'indice
        jeton.setIndice_bon(jeton.getIndice_bon() + 1 );

        // ajout de la commande
        db.ajouterCommande(bon_en_cours, bon_en_cours.getLignesBon(), bon_en_cours.getId());
        //mise à jour du devis
        db.majEtatDevis(bon_en_cours);

        db.close();

        Outils.afficherToast(context, "Le devis a été validé. La commande " + indice + " a été généré.");
        // on retourne à la liste des devis
        finish();
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

    private boolean verifierModifPossible(String etat){

        boolean actionPossible = false;

        switch( etat ){

            case "En attente de validation":
                actionPossible = true;
                break;
            case "Validée":
                actionPossible = true;
                break;
            case "En cours de préparation":
                actionPossible = true;
                break;
            case "Mise à disposition":
                actionPossible = false;
                break;
            case "En cours de livraison":
                actionPossible = false;
                break;
            case "Terminée":
                actionPossible = false;
                break;
            case "Créé":
                actionPossible = true;
                break;
            case "Fin validité":
                actionPossible = false;
                break;
            case "Validé":
                actionPossible = false;
                break;
            case "Annulé":
                actionPossible = false;
                break;
            default:
                break;
        }

        return actionPossible;
    }
}

