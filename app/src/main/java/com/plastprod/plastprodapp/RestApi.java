package com.plastprod.plastprodapp;

//import android.app.NotificationManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.Choix;
import sqlite.helper.Compte;
import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Evenement;
import sqlite.helper.LigneCommande;
import sqlite.helper.Objectif;
import sqlite.helper.Parametre;
import sqlite.helper.Produit;
import sqlite.helper.Reponse;
import sqlite.helper.Satisfaction;
import sqlite.helper.Societe;
import sqlite.helper.Stock;
import sqlite.helper.Synchro;

public class RestApi extends AsyncTask<Context, Integer, Integer> {

    //Adresse du serveur plastprod
    private static String SVRPLASTPROD = "http://192.168.0.26/";

	//pour serialiser / déserialiser les objets
	Gson gson;
    //connexion base
    DatabaseHelper db;
    //Contexte
    Context context;

    //les données à envoyer/récupérer
    List<Societe> liste_clients;
    List<Contact> liste_contacts;
    List<Bon> liste_bons;
    List<LigneCommande> liste_articles;
    List<Evenement> liste_evenements;
    List<Compte> liste_utilisateurs;
    List<Produit> liste_produits;
    List<Parametre> liste_parametres;
    List<Objectif> liste_objectifs;
    List<Stock> liste_stocks;
    List<Reponse> liste_reponses;
    List<Satisfaction> liste_satis;
    List<Choix> liste_choix;

    //Pour la correspondance des identité
    Hashtable<Integer, Integer> corresp_client = null;
    Hashtable<Integer, Integer> corresp_bon = null;

    //Adresse web services
	private static String API_CLIENT;
	private static String API_CLIENT_AJT;
	private static String API_CLIENT_MAJ;
	private static String API_CONTACT;
	private static String API_CONTACT_AJT;
	private static String API_CONTACT_MAJ;
	private static String API_BON;
	private static String API_BON_AJT;
	private static String API_BON_MAJ;
	private static String API_LIGNES;
	private static String API_LIGNES_AJT;
	private static String API_LIGNES_MAJ;
	private static String API_EVENEMENT;
	private static String API_EVENEMENT_AJT;
	private static String API_EVENEMENT_MAJ;
	private static String API_UTILISATEUR;
    private static String API_COMMERCIAL;
	private static String API_PRODUIT;
	private static String API_PARAMETRE;
    private static String API_PARAMETRE_MAJ;
    private static String API_PARAMADMIN_MAJ;
	private static String API_OBJECTIF;
	private static String API_STOCK;
	private static String API_REPONSE;
	private static String API_SATISF;
    private static String API_SATISF_AJT;
    private static String API_CHOIX;

    @Override
    protected Integer doInBackground(Context... arg){

        //initialisation
        db = new DatabaseHelper(arg[0]);
        context = arg[0];
        gson = new Gson();
        int UID = 1254;

        //recupération adresse ip du serveur pour synchroniser
        SVRPLASTPROD = "http://" + db.getAdresseIpServeur() + "/";
        alimenterConstantes();

        // Configuration admin post installation
        if( arg.length > 1 ){

            //on enregistre les paramètre d'accès au web services modifié
            liste_parametres = db.getParametres(0, true);

            if( liste_parametres.size() > 0 ) {
                envoyerParametresAdmin(liste_parametres);
                db.finAjoutParametre(liste_parametres);
            }

            db.viderTables();
            db.viderTableCompte();

            //pour les comptes des commerciaux
            recupererComptes();
            recupererCommerciaux();

            db.close();

            return 0;
        }

        NotificationManager mManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Synchronisation PlastProdApp")
                .setContentText("Synchronisation en cours...")
                .setSmallIcon(R.drawable.ic_refresh_white_48dp);

        /*************************************
         *  Données à envoyer sur le serveur
         *************************************/

        //pour l'avancement
        mManager.notify(UID, mBuilder.build());

        //clients à ajouter
        liste_clients = db.getSyncClient(true);
        envoyerClients(1, liste_clients);
        //clients à modifier (modif et suppression)
        liste_clients = db.getSyncClient(false);
        envoyerClients(2, liste_clients);

        //contacts à ajouter
        liste_contacts = db.getSyncContact(true);
        envoyerContacts(1, liste_contacts);
        //contacts à modifier (modif et suppression)
        liste_contacts = db.getSyncContact(false);
        envoyerContacts(2, liste_contacts);

        mBuilder.setProgress(100, 15, false);
        mManager.notify(UID, mBuilder.build());

        //bons à ajouter
        liste_bons = db.getSyncBon(true);
        envoyerBons(1, liste_bons);
        //bons à modifier (annuulation ou suppression)
        liste_bons = db.getSyncBon(false);
        envoyerBons(2, liste_bons);

        //lignes des bons à ajouter
        liste_articles = db.getSyncLigneBon(true);
        envoyerLignes(1, liste_articles);
        //lignes des bons à modifier (modif et suppression)
        liste_articles = db.getSyncLigneBon(false);
        envoyerLignes(2, liste_articles);

        mBuilder.setProgress(100, 30, false);
        mManager.notify(UID, mBuilder.build());

        //événements à ajouter
        liste_evenements = db.getSyncEvent(true);
        envoyerEvents(1, liste_evenements);
        //événements à modifier (modif et suppression)
        liste_evenements = db.getSyncEvent(false);
        envoyerEvents(2, liste_evenements);

        mBuilder.setProgress(100, 40, false);
        mManager.notify(UID, mBuilder.build());

        //paramètres à modifier
        liste_parametres = db.getSyncParam();
        envoyerParametres(liste_parametres);

        //questionnaire de satisfaction envoyé à ajouter
        liste_satis = db.getSyncSatisfaction();
        envoyerSatisfaction(liste_satis);

        mBuilder.setProgress(100, 45, false);
        mManager.notify(UID, mBuilder.build());

        /*************************************
         *  Tables à vider
         *************************************/
        db.viderTables();

        mBuilder.setProgress(100, 50, false);
        mManager.notify(UID, mBuilder.build());

        /*******************************************
         *  Données à récupérer depuis le serveur
         *******************************************/

        //clients
        recupererClients();
        //contacts
        recupererContacts();
        //bons
        recupererBons();

        mBuilder.setProgress(100, 60, false);
        mManager.notify(UID, mBuilder.build());

        //lignes des bons
        recupererLignes();
        //événements
        recupererEvents();

        mBuilder.setProgress(100, 70, false);
        mManager.notify(UID, mBuilder.build());

        //produits
        recupererProduits();
        //paramètres
        recupererParametres();
        //choix
        recupererChoix();
        //info du stock
        recupererStocks();

        mBuilder.setProgress(100, 80, false);
        mManager.notify(UID, mBuilder.build());

        //objectifs
        recupererObjectifs();

        mBuilder.setProgress(100, 85, false);
        mManager.notify(UID, mBuilder.build());

        //réponse au questionnaire de satisfaction
        recupererReponses();

        mBuilder.setProgress(100, 90, false);
        mManager.notify(UID, mBuilder.build());

        //info sur les questionnaires de satisfaction
        recupererQuestionnaires();

        mBuilder.setContentText("Synchronisation terminé")
                .setProgress(0, 0, false);
        mManager.notify(UID, mBuilder.build());

        db.close();

        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        //delegue.etatSynchro(result);
        Toast.makeText(context, "Synchronisation terminé avec succès.", Toast.LENGTH_LONG).show();

        db = new DatabaseHelper(context);
        //recupération du dernier indice des bons
        Global jeton = (Global) context;

        jeton.setIndice_bon(db.getDernierIndiceBon());

        db.close();
    }

    public void alimenterConstantes(){

        API_CLIENT   		= SVRPLASTPROD + "WebServices/api/societes";
        API_CLIENT_AJT		= SVRPLASTPROD + "WebServices/api/societes/ajt";
        API_CLIENT_MAJ 		= SVRPLASTPROD + "WebServices/api/societes/maj";

        API_CONTACT 		= SVRPLASTPROD + "WebServices/api/contacts";
        API_CONTACT_AJT 	= SVRPLASTPROD + "WebServices/api/contacts/ajt";
        API_CONTACT_MAJ		= SVRPLASTPROD + "WebServices/api/contacts/maj";

        API_BON 			= SVRPLASTPROD + "WebServices/api/bons";
        API_BON_AJT 		= SVRPLASTPROD + "WebServices/api/bons/ajt";
        API_BON_MAJ 		= SVRPLASTPROD + "WebServices/api/bons/maj";

        API_LIGNES			= SVRPLASTPROD + "WebServices/api/articles";
        API_LIGNES_AJT		= SVRPLASTPROD + "WebServices/api/articles/ajt";
        API_LIGNES_MAJ		= SVRPLASTPROD + "WebServices/api/articles/maj";

        API_EVENEMENT 		= SVRPLASTPROD + "WebServices/api/evenements/id";
        API_EVENEMENT_AJT 	= SVRPLASTPROD + "WebServices/api/evenements/ajt";
        API_EVENEMENT_MAJ 	= SVRPLASTPROD + "WebServices/api/evenements/maj";

        API_UTILISATEUR     = SVRPLASTPROD + "WebServices/api/utilisateurs";
        API_COMMERCIAL      = SVRPLASTPROD + "WebServices/api/commerciaux";
        API_PRODUIT 	    = SVRPLASTPROD + "WebServices/api/produits";

        API_PARAMETRE 	    = SVRPLASTPROD + "WebServices/api/parametres/id";
        API_PARAMETRE_MAJ   = SVRPLASTPROD + "WebServices/api/parametres/maj";
        API_PARAMADMIN_MAJ  = SVRPLASTPROD + "WebServices/api/paramadmin/maj";

        API_OBJECTIF	    = SVRPLASTPROD + "WebServices/api/objectifs/id";
        API_STOCK 		    = SVRPLASTPROD + "WebServices/api/stocks";
        API_REPONSE		    = SVRPLASTPROD + "WebServices/api/reponses";

        API_SATISF 		    = SVRPLASTPROD + "WebServices/api/satisfactions";
        API_SATISF_AJT      = SVRPLASTPROD + "WebServices/api/satisfactions/ajt";

        API_CHOIX           = SVRPLASTPROD + "WebServices/api/choix";
    }

    public int envoyerClients(int methode, List<Societe> liste_clients) {

        if( liste_clients.size() > 0 ) {

            try {

                Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
                String clients = gson.toJson(liste_clients, type_liste);

                //Ajout
                if (methode == 1) {
                    envoyerVersWS(API_CLIENT_AJT, clients, "Client");
                }
                //Modification
                else {
                    envoyerVersWS(API_CLIENT_MAJ, clients, "Client");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        return 1;
    }

    public int envoyerContacts(int methode, List<Contact> liste_contacts) {

        if( liste_contacts.size() > 0 ) {

            try {

                //seulement en cas d'ajout
                if( methode == 1 ) {
                    corresp_client = db.getCorrespondance("Client");

                    if( corresp_client != null && corresp_client.size() > 0 ) {
                        //mise en place des nouveaux indice pour l'ajout
                        for (Contact con : liste_contacts) {
                            con.setId_societe(corresp_client.get((con.getId_societe())));
                        }
                    }
                }

                Type type_liste = new TypeToken<ArrayList<Contact>>() {}.getType();
                String contacts = gson.toJson(liste_contacts, type_liste);

                //Ajout
                if (methode == 1) {
                    envoyerVersWS(API_CONTACT_AJT, contacts, "Contact");
                }
                //Modification
                else {
                    envoyerVersWS(API_CONTACT_MAJ, contacts, "Contact");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        return 1;
    }

    public int envoyerBons(int methode, List<Bon> liste_bons) {

        if( liste_bons.size() > 0 ) {

            try {

                Type type_liste = new TypeToken<ArrayList<Bon>>() {}.getType();
                String bons = gson.toJson(liste_bons, type_liste);

                //Ajout
                if (methode == 1) {
                    envoyerVersWS(API_BON_AJT, bons, "Bon");
                }
                //Modification
                else {
                    envoyerVersWS(API_BON_MAJ, bons, "Bon");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        return 1;
    }

    public int envoyerLignes(int methode, List<LigneCommande> liste_lignes) {

        if( liste_lignes.size() > 0 ) {

            try {

                //seulement en cas d'ajout
                if( methode == 1 ) {
                    corresp_bon = db.getCorrespondance("Bon");

                    if( corresp_bon != null && corresp_bon.size() > 0 ) {
                        //mise en place des nouveaux indice pour l'ajout
                        for (LigneCommande ligne : liste_lignes) {
                            ligne.setId_bon(corresp_bon.get((ligne.getId_bon())));
                        }
                    }
                }

                Type type_liste = new TypeToken<ArrayList<LigneCommande>>() {}.getType();
                String lignes = gson.toJson(liste_lignes, type_liste);

                //Ajout
                if (methode == 1) {
                    envoyerVersWS(API_LIGNES_AJT, lignes, "Ligne");
                }
                //Modification
                else {
                    envoyerVersWS(API_LIGNES_MAJ, lignes, "Ligne");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        return 1;
    }

    public int envoyerEvents(int methode, List<Evenement> liste_events) {

        if( liste_events.size() > 0 ) {

            try {

                Type type_liste = new TypeToken<ArrayList<Evenement>>() {}.getType();
                String events = gson.toJson(liste_events, type_liste);

                //Ajout
                if (methode == 1) {
                    envoyerVersWS(API_EVENEMENT_AJT, events, "Evenement");
                }
                //Modification
                else {
                    envoyerVersWS(API_EVENEMENT_MAJ, events, "Evenement");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        return 1;
    }

    public int envoyerParametres(List<Parametre> liste_params){

        if( liste_params.size() > 0 ){

            try {

                Type type_liste = new TypeToken<ArrayList<Parametre>>() {}.getType();
                String params = gson.toJson(liste_params, type_liste);

                envoyerVersWS(API_PARAMETRE_MAJ, params, "Parametre");

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 1;
    }

    public int envoyerParametresAdmin(List<Parametre> liste_params){

        if( liste_params.size() > 0 ){

            try {

                Type type_liste = new TypeToken<ArrayList<Parametre>>() {}.getType();
                String params = gson.toJson(liste_params, type_liste);

                envoyerVersWS(API_PARAMADMIN_MAJ, params, "Parametre");

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 1;
    }

    public int envoyerSatisfaction(List<Satisfaction> liste_satis){

        if( liste_satis.size() > 0 ){

            try {

                Type type_liste = new TypeToken<ArrayList<Satisfaction>>() {}.getType();
                String params = gson.toJson(liste_satis, type_liste);

                envoyerVersWS(API_SATISF_AJT, params, "Satisfaction");

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 1;
    }

    public void envoyerVersWS(String url, String donnees, final String type ){

        URL adresse;
        OutputStream donneesAEnvoyer = null;
        InputStream resultat = null;
        HttpURLConnection connexion = null;

        try {
            //appel du web service
            adresse = new URL(url);

            //pour un POST
            connexion = (HttpURLConnection) adresse.openConnection();
            connexion.setRequestMethod("POST");
            connexion.setDoInput(true);
            connexion.setDoOutput(true);
            connexion.setFixedLengthStreamingMode(donnees.getBytes().length);

            //header
            connexion.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connexion.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //connexion
            connexion.connect();

            //envoi des données
            donneesAEnvoyer = new BufferedOutputStream(connexion.getOutputStream());
            donneesAEnvoyer.write(donnees.getBytes());
            donneesAEnvoyer.flush();

            //recupération des résultat
            resultat = connexion.getInputStream();
            String retour = RecupererChaineDepuisStream(resultat);

            //pour debug
            Log.d("Retour", retour);

            //si retour non vide alors c'est un ajout
            if( retour.length() > 1 )
            {
                // On récupère l'ojbjet issu de JSON
                JSONArray jsonRetour = new JSONArray(retour);
                JSONObject etat;

                for (int i = 0; i < jsonRetour.length(); i++){
                    etat = jsonRetour.getJSONObject(i);

                    if( etat.getString("Etat").equals("OK")) {

                        // base de corresondance
                        Synchro correspondance = new Synchro();

                        correspondance.setType(type);
                        correspondance.setNewId(etat.getInt("NewId"));
                        correspondance.setOldId(etat.getInt("OldId"));

                        db.ajouterCorrespondance(correspondance);
                    }
                }
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {

            try {
                donneesAEnvoyer.close();
                resultat.close();
                connexion.disconnect();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }

    public void recupererClients(){
        //on intérroge le web service
        recupererDepuisWS(API_CLIENT, "Societes");
    }

    public void recupererContacts(){
        //on intérroge le web service
        recupererDepuisWS(API_CONTACT, "Contacts");
    }

    public void recupererBons(){
        //on intérroge le web service
        recupererDepuisWS(API_BON, "Bons");
    }

    public void recupererLignes(){
        //on intérroge le web service
        recupererDepuisWS(API_LIGNES, "Articles");
    }

    public void recupererEvents(){

        final Global jeton = (Global)context;

        //on remplace l'id du commercial
        String url = API_EVENEMENT;
        url = url.replace("id", String.valueOf(jeton.getUtilisateur().getId()) );

        //on intérroge le web service
        recupererDepuisWS(url, "Evenements");
    }

    public void recupererComptes(){
        //on intérroge le web service
        recupererDepuisWS(API_UTILISATEUR, "Comptes");
    }

    public void recupererCommerciaux(){
        recupererDepuisWS(API_COMMERCIAL, "Contacts");
    }

    public void recupererProduits(){
        //on intérroge le web service
        recupererDepuisWS(API_PRODUIT, "Produits");
    }

    public void recupererParametres(){

        final Global jeton = (Global)context;

        //on remplace l'id du commercial
        String url = API_PARAMETRE;
        url = url.replace("id", String.valueOf(jeton.getUtilisateur().getId()) );

        recupererDepuisWS(url, "Parametres");
    }

    public void recupererAdminParametres(){

        String url = API_PARAMETRE;
        url = url.replace("id", String.valueOf(0));

        recupererDepuisWS(url, "Parametres");
    }

    public void recupererChoix(){

        recupererDepuisWS(API_CHOIX, "Choix");
    }

    public void recupererObjectifs(){

        final Global jeton = (Global)context;

        //on remplace l'id du commercial
        String url = API_OBJECTIF;
        url = url.replace("id", String.valueOf(jeton.getUtilisateur().getId()) );

        recupererDepuisWS(url, "Objectifs");
    }

    public void recupererStocks(){
        //on intérroge le web service
        recupererDepuisWS(API_STOCK, "Stocks");
    }

    public void recupererReponses(){
        //on intérroge le web service
        recupererDepuisWS(API_REPONSE, "Reponses");
    }

    public void recupererQuestionnaires(){
        //on intérroge le web service
        recupererDepuisWS(API_SATISF, "Satisfactions");
    }

    public void recupererDepuisWS(String url, final String type){

        URL adresse;
        InputStream retour = null;
        HttpURLConnection connexion = null;

        try{
            adresse = new URL(url);

            connexion = (HttpURLConnection)adresse.openConnection();
            connexion.setRequestMethod("GET");

            //connexion
            connexion.connect();

            retour = connexion.getInputStream();
            String jsonString = RecupererChaineDepuisStream(retour);

            //pour debug
            Log.d("Retour", jsonString);

            //transformation des résultats en objet json
            JSONArray obj = new JSONArray(jsonString);

            //Mise en place des données
            switch (type) {

                case "Societes":
                    setClients(obj);
                    break;
                case "Contacts":
                    setContacts(obj);
                    break;
                case "Bons":
                    setBons(obj);
                    break;
                case "Articles":
                    setLignes(obj);
                    break;
                case "Produits":
                    setProduits(obj);
                    break;
                case "Comptes":
                    setComptes(obj);
                    break;
                case "Objectifs":
                    setObjectifs(obj);
                    break;
                case "Satisfactions":
                    setSatisfactions(obj);
                    break;
                case "Reponses":
                    setReponses(obj);
                    break;
                case "Stocks":
                    setStocks(obj);
                    break;
                case "Parametres":
                    setParametres(obj);
                    break;
                case "Evenements":
                    setEvents(obj);
                    break;
                case "Choix":
                    setChoix(obj);
                    break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {

            try {
                retour.close();
                connexion.disconnect();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void setClients(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
        liste_clients = gson.fromJson(donnees.toString(), type_liste);

        for (Societe soc : liste_clients) {
            db.chargerClient(soc);
        }
    }

    public void setContacts(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Contact>>() {}.getType();
        liste_contacts = gson.fromJson(donnees.toString(), type_liste);

        for (Contact con : liste_contacts) {
            db.chargerContact(con);
        }
    }

    public void setBons(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Bon>>() {}.getType();
        liste_bons = gson.fromJson(donnees.toString(), type_liste);

        for (Bon bon : liste_bons) {
            db.chargerBon(bon);
        }
    }

    public void setLignes(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<LigneCommande>>() {}.getType();
        liste_articles = gson.fromJson(donnees.toString(), type_liste);

        for (LigneCommande ligne : liste_articles) {
            db.chargerLigne(ligne);
        }
    }

    public void setEvents(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Evenement>>() {}.getType();
        liste_evenements = gson.fromJson(donnees.toString(), type_liste);

        for (Evenement event : liste_evenements) {
            db.chargerEvenement(event);
        }
    }

    public void setComptes(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Compte>>() {}.getType();
        liste_utilisateurs = gson.fromJson(donnees.toString(), type_liste);

        for (Compte com : liste_utilisateurs) {
            db.chargerCompte(com);
        }
    }

    public void setParametres(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Parametre>>() {}.getType();
        liste_parametres = gson.fromJson(donnees.toString(), type_liste);

        for (Parametre par : liste_parametres) {
            db.chargerParametre(par);
        }
    }

    public void setChoix(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Choix>>() {}.getType();
        liste_choix = gson.fromJson(donnees.toString(), type_liste);

        for (Choix c : liste_choix) {
            db.chargerListeChoix(c);
        }
    }

    public void setProduits(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Produit>>() {}.getType();
        liste_produits = gson.fromJson(donnees.toString(), type_liste);

        for (Produit prod : liste_produits) {
            db.chargerProduit(prod);
        }
    }

    public void setStocks(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Stock>>() {}.getType();
        liste_stocks = gson.fromJson(donnees.toString(), type_liste);

        for (Stock st : liste_stocks) {
            db.chargerStock(st);
        }
    }

    public void setObjectifs(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Objectif>>() {}.getType();
        liste_objectifs = gson.fromJson(donnees.toString(), type_liste);

        for (Objectif obj : liste_objectifs) {
            db.chargerObjectif(obj);
        }
    }

    public void setReponses(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Reponse>>() {}.getType();
        liste_reponses = gson.fromJson(donnees.toString(), type_liste);

        for (Reponse rep : liste_reponses) {
            db.chargerReponse(rep);
        }
    }

    public void setSatisfactions(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Satisfaction>>() {}.getType();
        liste_satis = gson.fromJson(donnees.toString(), type_liste);

        for (Satisfaction sat : liste_satis) {
            db.chargerSatisfaction(sat);
        }
    }

    public static String RecupererChaineDepuisStream(InputStream flux) throws IOException{

        int n;
        char[] buffer = new char[1024*4];

        InputStreamReader lecteur = new InputStreamReader(flux, "UTF8");

        StringWriter ecrivain = new StringWriter();

        while( -1 != (n = lecteur.read(buffer)))
            ecrivain.write(buffer, 0, n);

        return ecrivain.toString();
    }

    public interface FinSynchro {
        void etatSynchro(Integer resultat);
    }
}