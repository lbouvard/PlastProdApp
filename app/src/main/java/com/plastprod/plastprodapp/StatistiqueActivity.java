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
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    int nb_total_commande;
    int nb_total_devis;
    int nb_commande_prepare;
    int nb_commande_termine;

    String date_debut;
    String date_fin;

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

    // pour le dernier graphique (barres horizontale devis ayant conduit à une commande ferme
    BarData donnees_devis_donne_commande = new BarData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);

        EditText generique = (EditText) findViewById(R.id.edStatsDateDeb);
        generique.setFocusable(false);
        generique.setText(dateDebut());

        generique = (EditText) findViewById(R.id.edStatsDateFin);
        generique.setFocusable(false);
        generique.setText(dateFin());
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
        String date_affichage;
        String date_calcul;
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

        // date à afficher
        date_affichage = jour + "/" + mois + "/" + date.get(Calendar.YEAR);
        lDate.setText(date_affichage);

        // date pour le calcul
        date_calcul = date.get(Calendar.YEAR) + "-" + mois + "-" + jour;
        if( type.equals("DateDeb") )
            date_debut = date_calcul;
        else
            date_fin = date_calcul;
    }

    public void recupererDateDebut(View vue){

        DialogFragment dialog = new ChoixDate();

        // on passe le type en argument
        Bundle bundle = new Bundle();
        bundle.putString("Type", "DateDeb");
        bundle.putString("Date_en_cours", ((EditText) findViewById(R.id.edStatsDateDeb)).getText().toString());

        dialog.setArguments(bundle);

        //affiche le date picker
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    public void recupererDateFin(View vue){

        DialogFragment dialog = new ChoixDate();

        // on passe le type en argument
        Bundle bundle = new Bundle();
        bundle.putString("Type", "DateFin");
        bundle.putString("Date_en_cours", ((EditText) findViewById(R.id.edStatsDateFin)).getText().toString());

        dialog.setArguments(bundle);

        //affiche le date picker
        dialog.show(getSupportFragmentManager(), "datePicker");
    }

    private String dateFin() {

        Calendar calendar = Calendar.getInstance();

        // pour les requêtes sql
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date_fin = formatter.format(calendar.getTime());

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(calendar.getTime());
    }

    private String dateDebut(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        // pour les requêtes sql
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date_debut = formatter.format(calendar.getTime());

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(calendar.getTime());
    }

    public void recupererStat(View vue){

        //String date_debut = ((EditText) findViewById(R.id.edStatsDateDeb)).getText().toString();
        //String date_fin = ((EditText) findViewById(R.id.edStatsDateFin)).getText().toString();

        db = new DatabaseHelper(getApplicationContext());

        liste_stats_client = db.getStatsParClient(date_debut, date_fin);
        liste_stats_produit = db.getStatsProduit(date_debut, date_fin);

        afficherStatClient();
        afficherStatProduit();

        db.close();

    }

    public void afficherStatClient(){

        // on récupère les données formatées
        List<PieData> liste_donnees = new ArrayList<PieData>();
        liste_donnees = genererDonnees();

        // Remplissage du tableau
        TextView generique = (TextView) findViewById(R.id.nb_total_commande);
        generique.setText(String.valueOf(nb_total_commande));
        generique = (TextView) findViewById(R.id.nb_total_devis);
        generique.setText(String.valueOf(nb_total_devis));
        generique = (TextView) findViewById(R.id.nb_commande_termine);
        generique.setText(String.valueOf(nb_commande_termine));
        generique = (TextView) findViewById(R.id.nb_commande_prepare);
        generique.setText(String.valueOf(nb_commande_prepare));

        // Répartition des commandes par client
        PieChart chart = (PieChart) findViewById(R.id.graphique_nb_cd_client);
        mefCammenbert(chart);
        chart.setCenterText("Répartition des commandes par client");
        chart.setData(liste_donnees.get(0));
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();

        // Répartition des devis par client
        chart = (PieChart) findViewById(R.id.graphique_nb_de_client);
        mefCammenbert(chart);
        chart.setCenterText("Répartition des devis par client");
        chart.setData(liste_donnees.get(1));
        chart.highlightValues(null);
        chart.invalidate();

        // Répartition des devis par prospect
        chart = (PieChart) findViewById(R.id.graphique_nb_de_prospect);
        mefCammenbert(chart);
        chart.setCenterText("Répartition des devis par prospect");
        chart.setData(liste_donnees.get(2));
        chart.highlightValues(null);
        chart.invalidate();

        //Devis transformé en commande
        HorizontalBarChart horizontal_barres = (HorizontalBarChart) findViewById(R.id.graphique_de_vers_cd);
        mefHorizontalBarres(horizontal_barres);
        String titre = "Répartitions des devis validés";
        horizontal_barres.setDescription(titre);
        horizontal_barres.setDescriptionPosition(horizontal_barres.getWidth() / 2 + ((titre.length() / 2) * 12), 30);
        horizontal_barres.setData(donnees_devis_donne_commande);
        horizontal_barres.invalidate();

    }

    public void afficherStatProduit(){

        String titre = "";
        List<BarData> liste_donnees = new ArrayList<BarData>();
        liste_donnees = genererDonneesProduit();

        BarChart chart = (BarChart) findViewById(R.id.graphique_top_meilleure_vente);
        titre = "TOP 5 Produit le plus vendu";
        mefBarres(chart);
        chart.setDescription(titre);
        chart.setDescriptionPosition(chart.getWidth() / 2 + ((titre.length() / 2) * 12), 30);
        chart.setData(liste_donnees.get(0));
        chart.invalidate();

        chart = (BarChart) findViewById(R.id.graphique_top_pire_vente);
        titre = "TOP 5 - Produit le moins vendu";
        mefBarres(chart);
        chart.setDescription(titre);
        chart.setDescriptionPosition(chart.getWidth() / 2 + ((titre.length() / 2)  * 12), 30);
        chart.setData(liste_donnees.get(1));
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

    private void mefBarres(BarChart graph){

        graph.setDrawBarShadow(false);
        graph.setDrawValueAboveBar(true);

        graph.setDescription("");
        graph.setDescriptionTextSize(12f);

        graph.setMaxVisibleValueCount(30);
        // draw shadows for each bar that show the maximum value
        // graph.setDrawBarShadow(true);
        graph.setDrawGridBackground(false);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        //xAxis.setAvoidFirstLastClipping(true);

        ValueFormatter custom = new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return new DecimalFormat("###").format(v);
            }
        };

        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        // Pas d'axe à droite
        graph.getAxisRight().setEnabled(false);
        // Pas de légende
        graph.getLegend().setEnabled(false);

    }

    private void mefHorizontalBarres(HorizontalBarChart graph){

        graph.setDrawBarShadow(false);
        graph.setDrawValueAboveBar(true);

        graph.setDescriptionTextSize(12f);
        graph.setMaxVisibleValueCount(30);

        graph.setDrawGridBackground(false);


        XAxis xl = graph.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        //xl.setGridLineWidth(0.3f);

        YAxis yl = graph.getAxisRight();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);

        graph.getAxisLeft().setEnabled(false);

        Legend l = graph.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
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

    private List<PieData> genererDonnees(){

        // SORTIE
        List<PieData> liste_donnee = new ArrayList<PieData>();

        // DONNEES DU GRAPHIQUE
        ArrayList<Entry> yCommandeClient = new ArrayList<Entry>();
        ArrayList<Entry> yDevisClient = new ArrayList<Entry>();
        ArrayList<String> xClient = new ArrayList<String>();

        ArrayList<Entry> yDevisProspect = new ArrayList<Entry>();
        ArrayList<String> xDevisProspect = new ArrayList<String>();

        ArrayList<BarEntry> yDevisVersCommande = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yDevisClientBar = new ArrayList<BarEntry>();

        int indexCL = 0;
        int indexDEPR = 0;

        // insertion des données
        for( StatParClient i : liste_stats_client){

            if( i.getType().equals("C") ) {
                yCommandeClient.add(new Entry((float) i.getStat().getNbCommande(), indexCL));
                yDevisClient.add(new Entry((float) i.getStat().getNbDevis(), indexCL));

                yDevisClientBar.add(new BarEntry((float) i.getStat().getNbDevis(), indexCL));
                yDevisVersCommande.add(new BarEntry((float) i.getStat().getNbDevisEtCommande(), indexCL));

                xClient.add(indexCL, i.getNom());
                indexCL++;

                nb_commande_prepare += i.getStat().getNbCommandePrepare();
                nb_commande_termine += i.getStat().getNbCommandeTermine();
                nb_total_commande += i.getStat().getNbCommande();
                nb_total_devis += i.getStat().getNbDevis();
            }
            // devis prospect
            else {

                if( i.getType().equals("P") ){
                    yDevisProspect.add(new Entry((float) i.getStat().getNbDevis(), indexDEPR));
                    xDevisProspect.add(indexDEPR, i.getNom());
                    indexDEPR++;

                    nb_total_devis += i.getStat().getNbDevis();
                }
            }
        }

        //Données du graphique 1
        PieDataSet dataSet = new PieDataSet(yCommandeClient, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // on applique les couleurs
        dataSet.setColors(recupererCouleur());
        // abscisses
        PieData data = new PieData(xClient, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        // ajout
        liste_donnee.add(data);

        //Données du graphique 2
        dataSet = new PieDataSet(yDevisClient, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(recupererCouleur());
        // abscisses
        data = new PieData(xClient, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        // ajout
        liste_donnee.add(data);

        //Données du graphique 3
        dataSet = new PieDataSet(yDevisProspect, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(recupererCouleur());
        // abscisses
        data = new PieData(xDevisProspect, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        // ajout
        liste_donnee.add(data);

        /*****************************************
        * Données du graphique devis ayant conduit à une commande
        */
        ArrayList<BarDataSet> liste_barre_donnees = new ArrayList<BarDataSet>();

        //devis ayant conduit à une commande
        BarDataSet barData = new BarDataSet(yDevisVersCommande, "Commandes");
        barData.setColor(Color.rgb(237, 125, 49));
        liste_barre_donnees.add(barData);

        //nb devis
        barData = new BarDataSet(yDevisClientBar, "Devis");
        barData.setColor(Color.rgb(255, 192, 0));
        liste_barre_donnees.add(barData);

        // données en globale
        donnees_devis_donne_commande = new BarData(xClient, liste_barre_donnees);
        donnees_devis_donne_commande.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return new DecimalFormat("###").format(v);
            }
        });
        donnees_devis_donne_commande.setValueTextSize(10f);
        donnees_devis_donne_commande.setGroupSpace(80f);

        /*********************************************/

        return liste_donnee;
    }

    private List<BarData> genererDonneesProduit(){

        List<BarData> liste_donnees = new ArrayList<BarData>();

        // MEILLEURES VENTES
        ArrayList<String> xValPlus = new ArrayList<String>();
        ArrayList<BarEntry> yValPlus = new ArrayList<BarEntry>();

        StatProduit stat;
        int index = 0;
        int finListe = liste_stats_produit.size();

        for (index = 0; index < 5; index++) {
            stat = liste_stats_produit.get(index);

            xValPlus.add(stat.getCodeProduit());
            yValPlus.add(new BarEntry((float) stat.getNbProduitVendu(), index));
        }

        // MOINS DE VENTES
        ArrayList<String> xValMoins = new ArrayList<String>();
        ArrayList<BarEntry> yValMoins = new ArrayList<BarEntry>();

        for (index = finListe - 1; index > finListe - 6; index--) {
            stat = liste_stats_produit.get(index);

            xValMoins.add(stat.getCodeProduit());
            yValMoins.add(new BarEntry((float) stat.getNbProduitVendu(), finListe - index));
        }

        // Données du 1er graphique
        BarDataSet donnees = new BarDataSet(yValPlus, "");
        donnees.setBarSpacePercent(35f);
        donnees.setColors(new int[] {Color.GREEN});
        BarData data = new BarData(xValPlus, donnees);
        data.setValueFormatter( new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return new DecimalFormat("###").format(v);
            }
        });
        data.setValueTextSize(10f);
        // on ajoute les données à la liste
        liste_donnees.add(data);

        // Données du 2ème graphique
        donnees = new BarDataSet(yValMoins, "");
        donnees.setBarSpacePercent(35f);
        donnees.setColors(new int[]{Color.RED});
        data = new BarData(xValMoins, donnees);
        data.setValueTextSize(10f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return new DecimalFormat("###").format(v);
            }
        });
        liste_donnees.add(data);

        return liste_donnees;
    }
}
