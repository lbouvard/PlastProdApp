package com.plastprod.plastprodapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.SatisDonnee;
import sqlite.helper.StatisCommentaire;
import sqlite.helper.StatisGraphique;


public class SatisfactionActivity extends ActionBarActivity implements ChoixDate.RetourListener {

    DatabaseHelper db;
    List<StatisGraphique> liste_satisfaction = new ArrayList<StatisGraphique>();
    List<StatisCommentaire> liste_commentaire = new ArrayList<StatisCommentaire>();
    SatisDonnee donneeTableau;

    double satisfaction;

    int total_note;
    int nombre_note;

    int[] couleurs = new int[]{ Color.rgb(227,207,87), Color.rgb(245,245,220), Color.rgb(255,228,196), Color.rgb(0,0,255), Color.rgb(156,102,31),
            Color.rgb(138,51,36), Color.rgb(95,158,160), Color.rgb(237,145,33), Color.rgb(210,105,30), Color.rgb(61,89,171), Color.rgb(255,127,80),
            Color.rgb(255,248,220), Color.rgb(220,20,60), Color.rgb(0,255,255), Color.rgb(47,79,79), Color.rgb(255,125,64), Color.rgb(34,139,34),
            Color.rgb(255,215,0), Color.rgb(0,128,0), Color.rgb(173,255,47), Color.rgb(205,92,92), Color.rgb(240,230,140), Color.rgb(230,230,250),
            Color.rgb(50,205,50), Color.rgb(255,0,255), Color.rgb(3,168,158), Color.rgb(255,52,179), Color.rgb(227,168,105), Color.rgb(189,252,201),
            Color.rgb(255,228,181) };

    int[] couleurs2 = new int[]{ Color.rgb(0,0,128), Color.rgb(128,128,0), Color.rgb(255,128,0), Color.rgb(218,112,214), Color.rgb(51,161,201), Color.rgb(255,192,203),
            Color.rgb(221,160,221), Color.rgb(128,0,128), Color.rgb(135,38,87), Color.rgb(188,143,143), Color.rgb(250,128,114), Color.rgb(244,164,96), Color.rgb(84,255,159),
            Color.rgb(94,38,18), Color.rgb(160,82,45), Color.rgb(216,191,216), Color.rgb(64,224,208), Color.rgb(238,130,238), Color.rgb(208,32,144), Color.rgb(245,222,179),
            Color.rgb(255,255,0), Color.rgb(255,0,0), Color.rgb(128,0,0), Color.rgb(142,56,142), Color.rgb(113,113,198), Color.rgb(125,158,192), Color.rgb(56,142,142),
            Color.rgb(113,198,113), Color.rgb(142,142,56), Color.rgb(197,193,170), Color.rgb(198,113,113) };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction);

        EditText generique = (EditText) findViewById(R.id.edStatsDateDeb);
        //generique.setEnabled(false);
        generique.setFocusable(false);
        generique = (EditText) findViewById(R.id.edStatsDateFin);
        //generique.setEnabled(false);
        generique.setFocusable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_satisfaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_stats :

                // en cours !
                return true;

            case R.id.action_commentaire :

                Intent activite_com = new Intent(this, CommentaireActivity.class);
                startActivity(activite_com);

                return true;

            case R.id.action_config :

                Intent activite_cfg = new Intent(this, MenuQuestionnaireActivity.class);
                startActivity(activite_cfg);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void modifierDate(GregorianCalendar date, String type){

        EditText lDate = null;
        String jour = "";
        String mois = "";
        int iMois = 0;

        if( type.equals("DateDeb") )
            lDate = (EditText) findViewById(R.id.edStatsDateDeb);
        else
            lDate = (EditText) findViewById(R.id.edStatsDateFin);

        if( date.get(Calendar.DAY_OF_MONTH) < 10)
            jour = "0" + String.valueOf(date.get(Calendar.DAY_OF_MONTH));
        else
            jour = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

        //le premier mois commence à 0
        iMois = date.get(Calendar.MONTH);
        iMois++;

        if( iMois < 10 )
            mois = "0" + String.valueOf(iMois);
        else
            mois = String.valueOf(iMois);

        lDate.setText(jour + "/" + mois + "/" + date.get(Calendar.YEAR));
    }

    public void recupererDateDebut(View vue){

        DialogFragment dialog = new ChoixDate();

        // on passe le type en argument
        Bundle bundle = new Bundle();
        bundle.putString("Type", "DateDeb");

        dialog.setArguments(bundle);

        //affiche le date picker
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void recupererDateFin(View vue){

        DialogFragment dialog = new ChoixDate();

        // on passe le type en argument
        Bundle bundle = new Bundle();
        bundle.putString("Type", "DateFin");

        dialog.setArguments(bundle);

        //affiche le date picker
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void recupererStat(View vue){

        db = new DatabaseHelper(getApplicationContext());

        liste_satisfaction = db.getSatisGraphique();
        donneeTableau = db.getSatisTableau();

        afficherSatisfaction();

        db.close();
    }

    public void afficherSatisfaction(){

        // on récupère les données formatées
        List<PieData> liste_donnees = new ArrayList<PieData>();
        liste_donnees = genererDonnees();

        // Remplissage du tableau
        TextView generique = (TextView) findViewById(R.id.nb_questionnaire_envoye);
        generique.setText(String.valueOf(donneeTableau.getNbQuestionnaireEnvoye()));
        generique = (TextView) findViewById(R.id.nb_reponse_recu);
        generique.setText(String.valueOf(donneeTableau.getNbReponse()));

        generique = (TextView) findViewById(R.id.satisfaction_generale);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        String satis = nf.format(satisfaction);
        generique.setText(String.valueOf(satis) + "%");

        // Satisfaction des livraisons
        PieChart chart = (PieChart) findViewById(R.id.satis_livraison);
        mefCammenbert(chart);
        chart.setCenterText("Satisfaction livraison");
        chart.setData(liste_donnees.get(0));
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();

        // Satisfaction de la qualité
        chart = (PieChart) findViewById(R.id.satis_qualite);
        mefCammenbert(chart);
        chart.setCenterText("Satisfaction qualité");
        chart.setData(liste_donnees.get(1));
        chart.highlightValues(null);
        chart.invalidate();

        // Répartition des devis par prospect
        chart = (PieChart) findViewById(R.id.satis_communication);
        mefCammenbert(chart);
        chart.setCenterText("Satisfaction communication");
        chart.setData(liste_donnees.get(2));
        chart.highlightValues(null);
        chart.invalidate();

        // Satisfaction de la qualité
        chart = (PieChart) findViewById(R.id.satis_fidelite);
        mefCammenbert(chart);
        chart.setCenterText("Satisfaction fidélité");
        chart.setData(liste_donnees.get(3));
        chart.highlightValues(null);
        chart.invalidate();

        // Répartition des devis par prospect
        chart = (PieChart) findViewById(R.id.satis_image);
        mefCammenbert(chart);
        chart.setCenterText("Satisfaction image");
        chart.setData(liste_donnees.get(4));
        chart.highlightValues(null);
        chart.invalidate();
    }

    private void mefCammenbert(PieChart graph){

        // PARAMETRE DU GRAPHIQUE
        graph.setUsePercentValues(true);
        graph.setDragDecelerationFrictionCoef(0.95f);
        graph.setDrawHoleEnabled(true);
        graph.setHoleColorTransparent(true);
        graph.setTransparentCircleColor(Color.WHITE);
        graph.setTransparentCircleAlpha(110);
        graph.setHoleRadius(50f);
        graph.setTransparentCircleRadius(55f);
        graph.setDrawCenterText(true);
        graph.setCenterTextSize(12f);
        graph.setDescription("");
        graph.setRotationAngle(0);
        // enable rotation of the chart by touch
        graph.setRotationEnabled(false);
        graph.setCenterTextWordWrapEnabled(true);

        graph.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        //Légende du graphique
        Legend l = graph.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setWordWrapEnabled(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private ArrayList<Integer> recupererCouleur(int val){

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // les couleurs du graphique
        if( val == 1) {
            for (int c : couleurs)
                colors.add(c);
        }
        else{
            for (int c : couleurs2)
                colors.add(c);
        }

        return colors;
    }

    private List<PieData> genererDonnees(){

        // SORTIE
        List<PieData> liste_donnee = new ArrayList<PieData>();
        ArrayList<ArrayList<Entry>> liste_ordonnees  = new ArrayList<ArrayList<Entry>>(5);

        // DONNEES DU GRAPHIQUE
        ArrayList<Entry> ySatisLivraison = new ArrayList<Entry>();
        ArrayList<Entry> ySatisQualite = new ArrayList<Entry>();
        ArrayList<Entry> ySatisCommunication = new ArrayList<Entry>();
        ArrayList<Entry> ySatisFidelite = new ArrayList<Entry>();
        ArrayList<Entry> ySatisImage = new ArrayList<Entry>();

        ArrayList<String> xNiveau = new ArrayList<String>();
        int index = 0;

        // insertion des données satisfaction
        for( StatisGraphique i : liste_satisfaction){

            if( i.getCategorie().equals("LIVRAISON") ){
                ySatisLivraison.add(new Entry((float) i.getNbNivUn(), index));
                ySatisLivraison.add(new Entry((float) i.getNbNivDeux(), index));
                ySatisLivraison.add(new Entry((float) i.getNbNivTrois(), index));
                ySatisLivraison.add(new Entry((float) i.getNbNivQuatre(), index));
                ySatisLivraison.add(new Entry((float) i.getNbNivCinq(), index));
                liste_ordonnees.add(ySatisLivraison);

                total_note += i.getNbNivUn() + i.getNbNivDeux() * 2 + i.getNbNivTrois() * 3 + i.getNbNivQuatre() * 4 + i.getNbNivCinq() * 5;
                nombre_note += i.getNbNivUn() + i.getNbNivDeux() + i.getNbNivTrois() + i.getNbNivQuatre() + i.getNbNivCinq();
            }
            else if( i.getCategorie().equals("QUALITE") ){
                ySatisQualite.add(new Entry((float) i.getNbNivUn(), index));
                ySatisQualite.add(new Entry((float) i.getNbNivDeux(), index));
                ySatisQualite.add(new Entry((float) i.getNbNivTrois(), index));
                ySatisQualite.add(new Entry((float) i.getNbNivQuatre(), index));
                ySatisQualite.add(new Entry((float) i.getNbNivCinq(), index));
                liste_ordonnees.add(ySatisQualite);

                total_note += i.getNbNivUn() + i.getNbNivDeux() * 2 + i.getNbNivTrois() * 3 + i.getNbNivQuatre() * 4 + i.getNbNivCinq() * 5;
                nombre_note += i.getNbNivUn() + i.getNbNivDeux() + i.getNbNivTrois() + i.getNbNivQuatre() + i.getNbNivCinq();
            }
            else if( i.getCategorie().equals("COMMUNICATION") ){
                ySatisCommunication.add(new Entry((float) i.getNbNivUn(), index));
                ySatisCommunication.add(new Entry((float) i.getNbNivDeux(), index));
                ySatisCommunication.add(new Entry((float) i.getNbNivTrois(), index));
                ySatisCommunication.add(new Entry((float) i.getNbNivQuatre(), index));
                ySatisCommunication.add(new Entry((float) i.getNbNivCinq(), index));
                liste_ordonnees.add(ySatisCommunication);

                total_note += i.getNbNivUn() + i.getNbNivDeux() * 2 + i.getNbNivTrois() * 3 + i.getNbNivQuatre() * 4 + i.getNbNivCinq() * 5;
                nombre_note += i.getNbNivUn() + i.getNbNivDeux() + i.getNbNivTrois() + i.getNbNivQuatre() + i.getNbNivCinq();
            }
            else if( i.getCategorie().equals("FIDELITE") ){
                ySatisFidelite.add(new Entry((float) i.getNbNivUn(), index));
                ySatisFidelite.add(new Entry((float) i.getNbNivDeux(), index));
                ySatisFidelite.add(new Entry((float) i.getNbNivTrois(), index));
                ySatisFidelite.add(new Entry((float) i.getNbNivQuatre(), index));
                ySatisFidelite.add(new Entry((float) i.getNbNivCinq(), index));
                liste_ordonnees.add(ySatisFidelite);

                total_note += i.getNbNivUn() + i.getNbNivDeux() * 2 + i.getNbNivTrois() * 3 + i.getNbNivQuatre() * 4 + i.getNbNivCinq() * 5;
                nombre_note += i.getNbNivUn() + i.getNbNivDeux() + i.getNbNivTrois() + i.getNbNivQuatre() + i.getNbNivCinq();
            }
            else if( i.getCategorie().equals("IMAGE") ){
                ySatisImage.add(new Entry((float) i.getNbNivUn(), index));
                ySatisImage.add(new Entry((float) i.getNbNivDeux(), index));
                ySatisImage.add(new Entry((float) i.getNbNivTrois(), index));
                ySatisImage.add(new Entry((float) i.getNbNivQuatre(), index));
                ySatisImage.add(new Entry((float) i.getNbNivCinq(), index));
                liste_ordonnees.add(ySatisImage);

                total_note += i.getNbNivUn() + i.getNbNivDeux() * 2 + i.getNbNivTrois() * 3 + i.getNbNivQuatre() * 4 + i.getNbNivCinq() * 5;
                nombre_note += i.getNbNivUn() + i.getNbNivDeux() + i.getNbNivTrois() + i.getNbNivQuatre() + i.getNbNivCinq();
            }

            index++;
        }

        //calcul de la satisfaction générale
        satisfaction = (double) total_note / nombre_note;
        satisfaction = (double) (satisfaction * 100) / 5;

        xNiveau.add("Niveau 1");
        xNiveau.add("Niveau 2");
        xNiveau.add("Niveau 3");
        xNiveau.add("Niveau 4");
        xNiveau.add("Niveau 5");

        int couleur = 0;

        for( ArrayList<Entry> ord : liste_ordonnees) {

            //Données du graphique 1
            PieDataSet dataSet = new PieDataSet(ord, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            // on applique les couleurs
            dataSet.setColors(recupererCouleur(couleur));
            // abscisses
            PieData data = new PieData(xNiveau, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            // ajout
            liste_donnee.add(data);

            if( couleur == 0 )
                couleur = 1;
            else
                couleur = 0;
        }

        return liste_donnee;
    }

}
