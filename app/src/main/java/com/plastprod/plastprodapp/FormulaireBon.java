package com.plastprod.plastprodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.LigneCommande;

public class FormulaireBon extends ActionBarActivity implements AdapterView.OnItemClickListener, ArticleDialog.RetourListener  {

    Context context;
    DatabaseHelper db;

    //Pour la liste des articles
    ListView lvLigneCommande;
    AffichageBonAdaptateur adaptateur;

    Bon bon_en_cours;
    String type;
    int idtClient;
    int idtBon;

    TextView tvGenerique;

    String quantiteTotale;
    String prixTotal;

    List<LigneCommande> liste_article = new ArrayList<>();
    boolean nouveauBon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_bon);

        context = getApplicationContext();

        //on recupère les données passé en argument
        Intent intent = getIntent();
        bon_en_cours = (Bon) intent.getSerializableExtra("Bon");
        type = intent.getStringExtra("Type");
        idtClient = intent.getIntExtra("IdtClient", -1);

        // Seulement si on est en modification/consultation
        if( bon_en_cours != null ) {

            nouveauBon = false;
            idtBon = bon_en_cours.getId();

            setTitle("Modifier " + bon_en_cours.getNumero_commande());

            tvGenerique = (TextView) findViewById(R.id.nom_societe);
            tvGenerique.setText(bon_en_cours.getClient().getNom());

            tvGenerique = (TextView) findViewById(R.id.adresse_societe);
            tvGenerique.setText(bon_en_cours.getClient().getAdresse1());

            tvGenerique = (TextView) findViewById(R.id.cp_ville_societe);
            tvGenerique.setText(bon_en_cours.getClient().getCode_postal() + " " + bon_en_cours.getClient().getVille());

            //totaux
            calculerTotalBon();

            tvGenerique = (TextView) findViewById(R.id.quantite_total);
            tvGenerique.setText(quantiteTotale);

            tvGenerique = (TextView) findViewById(R.id.prix_total_bon);
            tvGenerique.setText(prixTotal + " €");

            liste_article = bon_en_cours.getLignesBon();
        }
        else{

            nouveauBon = true;

            final Global jeton = (Global) getApplicationContext();

            if( type.equals("DE"))
                setTitle("Ajout d'un devis");
            else
                setTitle("Ajout d'une commande");

            // Mise en base d'un bon vide
            bon_en_cours = new Bon();
            bon_en_cours.setNumero_commande(type);
            bon_en_cours.setType(type);
            bon_en_cours.setCommercial_id(jeton.getUtilisateur().getId());
            bon_en_cours.setClient_id(idtClient);
            bon_en_cours.setAuteur(jeton.getUtilisateur().toString());
            bon_en_cours.setDate_commande(Outils.dateNow());
            bon_en_cours.setSuivi("");
            bon_en_cours.setTransporteur("");
            bon_en_cours.setCommentaire("");

            db = new DatabaseHelper(context);

            if( type.equals("DE")) {
                bon_en_cours.setEtat_commande("Créé");
                idtBon = (int) db.ajouterDevis(bon_en_cours, null);
            }
            else {
                bon_en_cours.setEtat_commande("En attente de validation");
                idtBon = (int) db.ajouterCommande(bon_en_cours, null, -1);
            }

            db.close();

            bon_en_cours.setId(idtBon);
        }

        adaptateur = new AffichageBonAdaptateur(getApplicationContext(), liste_article);

        lvLigneCommande = (ListView) findViewById(R.id.liste_affichage_commande);
        lvLigneCommande.setOnItemClickListener(this);
        lvLigneCommande.setAdapter(adaptateur);

        registerForContextMenu(lvLigneCommande);

        Outils.setListViewHeightBasedOnChildren(lvLigneCommande);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if ( v.getId() == R.id.liste_affichage_commande ) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            menu.setHeaderTitle(liste_article.get(info.position).getCode());
            menu.add(Menu.NONE, 0, 0, "Supprimer" );
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if( item.getItemId() == 0) {

            LigneCommande article = liste_article.get(info.position);
            supprimerArticle(article);
            majListe();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulaire_bon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_ajout_article) {

            // On ouvre le dialogue
            Bundle bundle = new Bundle();
            bundle.putSerializable("Article", null);
            bundle.putInt("IdtBon", idtBon);
            bundle.putString("ArticleDansLePanier", articleDejaDansLePanier());

            DialogFragment ajout_article = new ArticleDialog();
            ajout_article.setArguments(bundle);
            ajout_article.show(getSupportFragmentManager(), "article_dialog");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //on récupère la classe qui contient les données du client
        LigneCommande article = (LigneCommande)((ListView) arg0).getAdapter().getItem(arg2);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Article", article);
        bundle.putInt("IdtBon", article.getId_bon());

        DialogFragment modif_article = new ArticleDialog();
        modif_article.setArguments(bundle);
        modif_article.show(getSupportFragmentManager(), "article_dialog");

    }

    public void validateArticles(DialogFragment diag){
        majListe();
    }

    public void annuleArticles(DialogFragment diag){
        //Outils.afficherToast(getApplicationContext(), "Annulé");
    }

    public void supprimerArticle(LigneCommande article){

        db = new DatabaseHelper(context);

        if( nouveauBon ){
            // dans le cas d'une création de bon, on supprime totalement l'article (pas d'existence sur le serveur)
            db.supprimerTotalementLigneBon(article.getId());
        }
        else{
            // suppression logique
            db.supprimerLigneBon(article);
        }
    }

    public void majListe(){

        db = new DatabaseHelper(context);
        liste_article = db.getLignesCommande(bon_en_cours.getId());
        db.close();

        adaptateur.majListe(liste_article);

        //mise à jour des totaux
        bon_en_cours.setLignesBon(liste_article);
        majTotaux();

        // pour afficher toute la liste
        lvLigneCommande = (ListView) findViewById(R.id.liste_affichage_commande);
        Outils.setListViewHeightBasedOnChildren(lvLigneCommande);

    }

    private void majTotaux(){

        calculerTotalBon();

        tvGenerique = (TextView) findViewById(R.id.quantite_total);
        tvGenerique.setText(quantiteTotale);

        tvGenerique = (TextView) findViewById(R.id.prix_total_bon);
        tvGenerique.setText(prixTotal + " €");
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

    //pour annuler les modification
    public void annulerModification(View vue){

        if( nouveauBon ){

            db = new DatabaseHelper(context);

            // on supprime les articles du bon
            for(LigneCommande ligne : liste_article) {
                db.supprimerTotalementLigneBon(ligne.getId());
            }
            // on supprime le bon
            db.supprimerTotalementBon(bon_en_cours.getId());

            db.close();

        }
        finish();
    }

    //pour enregistrer les modification
    public void validerModification(View vue){

        String indice;

        if( nouveauBon ){
            db = new DatabaseHelper(context);

            //génération du numéro de commande
            Global jeton = (Global) getApplicationContext();

            indice = bon_en_cours.getNumero_commande() + String.valueOf(jeton.getUtilisateur().getId()) + String.valueOf(jeton.getIndice_bon());
            //incrémente l'indice
            jeton.setIndice_bon(jeton.getIndice_bon() + 1 );

            bon_en_cours.setNumero_commande(indice);
            bon_en_cours.setDate_commande(Outils.dateNow());

            // mise à jour du bon
            db.majBon(bon_en_cours, true);
        }

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    private String articleDejaDansLePanier(){

        String sortie = "";

        if( liste_article.size() > 0 ){

            for(LigneCommande ligne : liste_article){
                sortie +=  "'" + ligne.getCode() + "',";
            }

            sortie = sortie.substring(0, sortie.length() - 1);
        }

        return sortie;
    }
}
