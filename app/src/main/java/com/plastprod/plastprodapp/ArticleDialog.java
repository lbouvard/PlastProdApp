package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.LigneCommande;
import sqlite.helper.Produit;


public class ArticleDialog extends DialogFragment {

    RetourListener mListener;
    DatabaseHelper db;
    int idtBon;

    int mRemise = 0;
    int mQuantite = 1;
    double mPrix = 0;
    BigDecimal mTotal = null;

    LigneCommande article_en_cours = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // constructeur du dialogue
        final AlertDialog.Builder constructeur = new AlertDialog.Builder(getActivity());

        //on recupère l'article ou null passé en argument
        article_en_cours =  (LigneCommande)getArguments().getSerializable("article");
        idtBon = getArguments().getInt("idtBon", -1);

        // on récupère la vue spécifique
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.activity_article_dialog, null);

        // on lie la vue au constructeur
        constructeur.setView(layout)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enregistrerArticle();
                        mListener.validateArticles(ArticleDialog.this);
                    }
                })
                .setNegativeButton(R.string.btn_annuler, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.annuleArticles(ArticleDialog.this);
                    }
                });

        // création du dialogue
        return constructeur.create();
    }

    @Override
    public void onAttach(Activity activite){
        super.onAttach(activite);

        try{
            mListener = (RetourListener)activite;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activite.toString() + "doit implémenter RetourListener");
        }

    }

    @Override
    public void onStart(){

        super.onStart();

        // on affiche la liste des produits
        if( article_en_cours == null ) {
            // recupérer les produits
            List<Produit> liste_produits = new ArrayList<Produit>();

            db = new DatabaseHelper(getActivity().getApplicationContext());
            liste_produits = db.getProduits();

            // on récupère la liste déroulante
            Spinner spinner = (Spinner) this.getDialog().findViewById(R.id.dropdown_article);

            ArrayAdapter<Produit> adaptateur = new ArrayAdapter<Produit>(getDialog().getContext(),
                    android.R.layout.simple_spinner_item, liste_produits);
            adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //on applique l'adaptateur à la liste déroulante
            spinner.setAdapter(adaptateur);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Produit article = (Produit) parent.getItemAtPosition(position);
                    remplirFormulaire(article);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            db.close();
        }
        //on affiche l'article
        else {
            TextView titre = (TextView) this.getDialog().findViewById(R.id.titre_article_dialog);
            titre.setText("Modification");

            Spinner spinner = (Spinner) this.getDialog().findViewById(R.id.dropdown_article);
            List<String> un_article = new ArrayList<String>();
            un_article.add(article_en_cours.getCode() + " - " + article_en_cours.getNom());

            ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(getDialog().getContext(),
                    android.R.layout.simple_spinner_item, un_article );
            adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //on applique l'adaptateur à la liste déroulante
            spinner.setAdapter(adaptateur);
            spinner.setEnabled(false);

            afficherArticle(article_en_cours);
        }

    }

    public interface RetourListener {
        public void validateArticles(DialogFragment dialog);
        public void annuleArticles(DialogFragment dialog);
    }

    private void remplirFormulaire(Produit produit){

        //variables
        TextView tvGenerique = null;
        EditText edGenerique = null;
        BigDecimal prix_affiche = null;

        // code
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.code_produit);
        tvGenerique.setText(produit.getCode() + " - ");

        // nom
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.nom_produit);
        tvGenerique.setText(produit.getNom());

        //description
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.description_produit);
        tvGenerique.setText(produit.getDescription());

        //prix unitaire
        mPrix = produit.getPrix();
        prix_affiche = new BigDecimal(mPrix);
        prix_affiche = prix_affiche.setScale(2, BigDecimal.ROUND_HALF_UP);

        tvGenerique = (TextView) this.getDialog().findViewById(R.id.prix_produit);
        tvGenerique.setText(prix_affiche.toString() + " €");

        //disponibilité
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.disponibilite);
        if( produit.getDelais() == 0 )
            tvGenerique.setText("En stock");
        else
            tvGenerique.setText("Disponible sous " + String.valueOf(produit.getDelais()) + " jours" );

        //remise
        edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_remise);
        edGenerique.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText temporaire = (EditText)v;
                    mRemise = Integer.valueOf(temporaire.getText().toString());
                    calculerPrix();
                    majPrixTotal();
                }
            }
        });
        mRemise = Integer.valueOf(edGenerique.getText().toString());

        //quantite
        edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_quantite);
        edGenerique.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText temporaire = (EditText)v;
                    mQuantite = Integer.valueOf(temporaire.getText().toString());
                    calculerPrix();
                    majPrixTotal();
                }
            }
        });
        mQuantite = Integer.valueOf(edGenerique.getText().toString());

        //prix total
        calculerPrix();
        majPrixTotal();
    }

    private void afficherArticle(LigneCommande ligne){

        // variables
        TextView tvGenerique = null;
        EditText edGenerique = null;
        BigDecimal prix_affiche = null;

        // code
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.code_produit);
        tvGenerique.setText(ligne.getCode() + " - ");

        // nom
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.nom_produit);
        tvGenerique.setText(ligne.getNom());

        //description
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.description_produit);
        tvGenerique.setText(ligne.getDescription());

        //prix unitaire
        mPrix = ligne.getPrixUnitaire();
        prix_affiche = new BigDecimal(mPrix);
        prix_affiche = prix_affiche.setScale(2, BigDecimal.ROUND_HALF_UP);

        tvGenerique = (TextView) this.getDialog().findViewById(R.id.prix_produit);
        tvGenerique.setText(prix_affiche.toString() + " €");

        //disponibilité
        tvGenerique = (TextView) this.getDialog().findViewById(R.id.disponibilite);
        tvGenerique.setText("-");

        //remise
        edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_remise);
        edGenerique.setText(String.valueOf(ligne.getRemise()));

        edGenerique.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    verifierRemise((EditText)v);
                }
            }
        });
        edGenerique.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verifierRemise((EditText)v);
                    return true;
                }
                return false;
            }
        });

        mRemise = ligne.getRemise();

        //quantite
        edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_quantite);
        edGenerique.setText(String.valueOf(ligne.getQuantite()));

        edGenerique.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    verifierQuantite((EditText) v);
                }
            }
        });
        edGenerique.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verifierQuantite((EditText) v);

                    InputMethodManager inputMethodManager = (InputMethodManager) ArticleDialog.this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        mQuantite = ligne.getQuantite();

        //prix total
        calculerPrix();
        majPrixTotal();
    }

    private void calculerPrix(){

        mTotal = new BigDecimal(mQuantite * (mPrix - (mPrix * mRemise)/100 ));
        mTotal = mTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void majPrixTotal(){

        TextView tvGenerique = (TextView) this.getDialog().findViewById(R.id.prix_total);
        tvGenerique.setText(mTotal.toString() + " €");
    }

    private void majQuantite(){

        EditText edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_quantite);
        edGenerique.setText(String.valueOf(mQuantite));
    }

    private void majRemise(){

        EditText edGenerique = (EditText) this.getDialog().findViewById(R.id.edt_remise);
        edGenerique.setText(String.valueOf(mRemise));
    }

    private void verifierQuantite(EditText edt){

        if( edt.getText().toString().length() > 0 ) {
            mQuantite = Integer.valueOf(edt.getText().toString());
        }
        else {
            mQuantite = 1;
            majQuantite();
        }

        if( mQuantite <= 0 ) {
            mQuantite = 1;
            majQuantite();
        }

        calculerPrix();
        majPrixTotal();
    }

    private void verifierRemise(EditText edt){

        if( edt.getText().toString().length() > 0 ){
            mRemise = Integer.valueOf(edt.getText().toString());
        }
        else{
            mRemise = 0;
            majRemise();
        }

        if( mRemise < 0 || mRemise > 99 ) {
            mRemise = 0;
            majRemise();
        }

        calculerPrix();
        majPrixTotal();
    }

    private void enregistrerArticle(){

        // mise à jour
        if( article_en_cours != null ){
            article_en_cours.setRemise(mRemise);
            article_en_cours.setQuantite(mQuantite);

            db = new DatabaseHelper(getActivity().getApplicationContext());

            db.majLigneBon(article_en_cours);

            db.close();
        }
        //enregistrement
        else{

            LigneCommande ligne = new LigneCommande();

            // nom
            TextView generique = (TextView) getDialog().findViewById(R.id.nom_produit);
            ligne.setNom(generique.getText().toString());

            // code
            generique = (TextView) getDialog().findViewById(R.id.code_produit);
            ligne.setCode(generique.getText().toString());

            // description
            generique = (TextView) getDialog().findViewById(R.id.description_produit);
            ligne.setDescription(generique.getText().toString());

            // prix unitaire
            generique = (TextView) getDialog().findViewById(R.id.prix_produit);
            ligne.setPrixUnitaire(Double.valueOf(generique.getText().toString()));

            // remise
            EditText ed = (EditText) getDialog().findViewById(R.id.edt_remise);
            ligne.setRemise(Integer.valueOf(ed.getText().toString()) );

            // quantite
            ed = (EditText) getDialog().findViewById(R.id.edt_quantite);
            ligne.setQuantite(Integer.valueOf(ed.getText().toString()) );

            db = new DatabaseHelper(getActivity().getApplicationContext());
            db.ajouterLigne(ligne, idtBon);
        }
    }
}
