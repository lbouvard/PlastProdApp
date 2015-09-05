package com.plastprod.plastprodapp;

import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sqlite.helper.DatabaseHelper;
import sqlite.helper.StatParClient;
import sqlite.helper.StatProduit;


public class StatistiqueActivity extends ActionBarActivity implements ChoixDate.RetourListener {

    DatabaseHelper db;
    List<StatParClient> liste_stats_client = new ArrayList<StatParClient>();
    List<StatProduit> liste_stats_produit = new ArrayList<StatProduit>();
    int[] couleurs = new int[]{ Color.rgb(211,88,247), Color.rgb(0,64,255), Color.rgb(1,223,215),
                                Color.rgb(58,223,0), Color.rgb(128,255,0), Color.rgb(134,180,4),
                                Color.rgb(255,255,0), Color.rgb(250,204,46), Color.rgb(255,128,0),
                                Color.rgb(245,218,129), Color.rgb(180,4,4), Color.rgb(255,0,0),
                                Color.rgb(247,129,243), Color.rgb(255,0,64), Color.rgb(8,138,8)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_statistique, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
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

        liste_stats_client = db.getStatsParClient();
        liste_stats_produit = db.getStatsProduit();

        afficherStat();

        db.close();

    }

    public void afficherStat(){

        int index = 0;

        // Répartition des commandes par client
        PieChart chart = (PieChart) findViewById(R.id.graphique_nb_cd_client);
        mefCammenbert(chart);
        chart.setCenterText("Répartition des commandes par client");

        chart.setData(genererDonneesCommandesClient());
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();

        // Répartition des devis par client
        chart = (PieChart) findViewById(R.id.graphique_nb_de_client);
        mefCammenbert(chart);
        chart.setCenterText("Répartition des devis par client");

        chart.setData(genererDonneesDevis("C"));
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();

        // Répartition des devis par prospect
        chart = (PieChart) findViewById(R.id.graphique_nb_de_prospect);
        mefCammenbert(chart);
        chart.setCenterText("Répartition des devis par prospect");

        chart.setData(genererDonneesDevis("P"));
        // undo all highlights
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

    private void mefBarres(BarChart graph){

    }

    private ArrayList<Integer> recupererCouleur(){

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // les couleurs du graphique
        for(int c : couleurs)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        return colors;
    }

    private PieData genererDonneesCommandesClient(){

        // DONNEES DU GRAPHIQUE
        ArrayList<Entry> yValeur = new ArrayList<Entry>();
        ArrayList<String> xValeur = new ArrayList<String>();
        int index = 0;

        // insertion des données
        for( StatParClient i : liste_stats_client){

            //on prend seulement les sociétés clientes
            if( i.getType().equals("C") ) {
                yValeur.add(new Entry((float) i.getStat().getNbCommande(), index));
                xValeur.add(index, i.getNom());
                index++;
            }
        }

        //Données du graphique
        PieDataSet dataSet = new PieDataSet(yValeur, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // on applique les couleurs
        dataSet.setColors(recupererCouleur());

        //abscisses
        PieData data = new PieData(xValeur, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        return data;
    }

    private PieData genererDonneesDevis(String type){

        // DONNEES DU GRAPHIQUE
        ArrayList<Entry> yValeur = new ArrayList<Entry>();
        ArrayList<String> xValeur = new ArrayList<String>();
        int index = 0;

        // insertion des données
        for( StatParClient i : liste_stats_client){

            //on différencie le type (client ou prospect)
            if( i.getType().equals(type) ) {
                yValeur.add(new Entry((float) i.getStat().getNbDevis(), index));
                xValeur.add(index, i.getNom());
                index++;
            }
        }

        //Données du graphique
        PieDataSet dataSet = new PieDataSet(yValeur, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // on applique les couleurs
        dataSet.setColors(recupererCouleur());

        //abscisses
        PieData data = new PieData(xValeur, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        return data;
    }

}
