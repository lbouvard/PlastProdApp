package com.plastprod.plastprodapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.List;

import sqlite.helper.Bon;
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

public class RestApi extends AsyncTask<Context, Void, Void> {

    //Adresse du serveur plastprod
    private final static String SVRPLASTPROD = "http://10.0.2.2/";

	//pour serialiser / déserialiser les objets
	Gson gson;
    //connexion base
    DatabaseHelper db;

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
    List<Satisfaction> liste_satifs;

    //Adresse
	private static final String API_CLIENT			= SVRPLASTPROD + "WebServices/api/societes";
	private static final String API_CLIENT_AJT		= SVRPLASTPROD + "WebServices/api/societes/ajt";
	private static final String API_CLIENT_MAJ 		= SVRPLASTPROD + "WebServices/api/societes/maj";
	
	private static final String API_CONTACT 		= SVRPLASTPROD + "WebServices/api/contacts";
	private static final String API_CONTACT_AJT 	= SVRPLASTPROD + "WebServices/api/contacts/ajt";
	private static final String API_CONTACT_MAJ		= SVRPLASTPROD + "WebServices/api/contacts/maj";
	
	private static final String API_BON 			= SVRPLASTPROD + "WebServices/api/bons";
	private static final String API_BON_AJT 		= SVRPLASTPROD + "WebServices/api/bons/ajt";
	private static final String API_BON_MAJ 		= SVRPLASTPROD + "WebServices/api/bons/maj";
	
	private static final String API_LIGNES			= SVRPLASTPROD + "WebServices/api/lignes";
	private static final String API_LIGNES_AJT		= SVRPLASTPROD + "WebServices/api/lignes/ajt";
	private static final String API_LIGNES_MAJ		= SVRPLASTPROD + "WebServices/api/lignes/maj";
	
	private static final String API_EVENEMENT 		= SVRPLASTPROD + "WebServices/api/evenements";
	private static final String API_EVENEMENT_AJT 	= SVRPLASTPROD + "WebServices/api/evenements/ajt";
	private static final String API_EVENEMENT_MAJ 	= SVRPLASTPROD + "WebServices/api/evenements/maj";
	
	private static final String API_UTILISATEUR = SVRPLASTPROD + "WebServices/api/utilisateurs";
	private static final String API_PRODUIT 	= SVRPLASTPROD + "WebServices/api/produits";
	private static final String API_PARAMETRE 	= SVRPLASTPROD + "WebServices/api/parametres";
	private static final String API_OBJECTIF	= SVRPLASTPROD + "WebServices/api/objectifs";
	private static final String API_STOCK 		= SVRPLASTPROD + "WebServices/api/stocks";
	private static final String API_REPONSE		= SVRPLASTPROD + "WebServices/api/reponses";
	private static final String API_SATISF 		= SVRPLASTPROD + "WebServices/api/satisfactions";

    @Override
    protected Void doInBackground(Context... arg){

        //initialisation
        db = new DatabaseHelper(arg[0]);
        gson = new Gson();


        /*************************************
         *  Données à envoyer sur le serveur
         *************************************/

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

        //bons à ajouter
        liste_bons = db.getSyncBon(true);
        envoyerBons(1, liste_bons);
        //bons à modifier (modif et suppression)
        liste_bons = db.getSyncBon(false);
        envoyerBons(2, liste_bons);

        //lignes des bons à ajouter
        liste_articles = db.getSyncLigneBon(true);
        envoyerLignes(1, liste_articles);
        //lignes des bons à modifier (modif et suppression)
        liste_articles = db.getSyncLigneBon(false);
        envoyerLignes(2, liste_articles);

        //événements à ajouter
        liste_evenements = db.getSyncEvent(true);
        envoyerEvents(1, liste_evenements);
        //événements à modifier (modif et suppression)
        liste_evenements = db.getSyncEvent(false);
        envoyerEvents(2, liste_evenements);


        /*************************************
         *  Tables à vider
         *************************************/
        //vide les tables à remplir
        db.viderTables();


        /*******************************************
         *  Données à récupérer depuis le serveur
         *******************************************/

        //clients
        recupererClients();
        //contacts
        recupererContacts();
        //bons
        recupererBons();
        //lignes des bons
        recupererLignes();
        //événements
        recupererEvents();
        //utilisateurs
        recupererComptes();
        //produits
        recupererProduits();
        //paramètres
        recupererParametres();
        //objectifs
        recupererObjectifs();
        //info du stock
        recupererStocks();
        //réponse au questionnaire de satisfaction
        recupererReponses();
        //info sur les questionnaires de satisfaction
        recupererQuestionnaires();

        return null;
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
                //On recupère l'objet issu de JSON
                JSONArray jsonRetour = new JSONArray(retour);
                JSONObject etat;

                for (int i = 0; i < jsonRetour.length(); i++) {
                    etat = jsonRetour.getJSONObject(i);

                    if (etat.getString("Etat").equals("OK")) {

                        //base de correspondance pour connaitre l'id créer dans la base distante
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
        //on intérroge le web service
        recupererDepuisWS(API_EVENEMENT, "Evenements");
    }

    public void recupererComptes(){
        //on intérroge le web service
        recupererDepuisWS(API_UTILISATEUR, "Comptes");
    }

    public void recupererProduits(){
        //on intérroge le web service
        recupererDepuisWS(API_PRODUIT, "Produits");
    }

    public void recupererParametres(){
        //on intérroge le web service
        recupererDepuisWS(API_PARAMETRE, "Parametres");
    }

    public void recupererObjectifs(){
        //on intérroge le web service
        recupererDepuisWS(API_OBJECTIF, "Objectifs");
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
                case "Contacts":
                    setContacts(obj);
                case "Bons":
                    setBons(obj);
                case "Articles":
                    setLignes(obj);
                case "Produits":
                    setProduits(obj);
                case "Comptes":
                    setComptes(obj);
                case "Objectifs":
                    setObjectifs(obj);
                case "Satisfactions":
                    setSatisfactions(obj);
                case "Reponses":
                    setReponses(obj);
                case "Stocks":
                    setStocks(obj);
                case "Parametres":
                    setParametres(obj);
                case "Evenements":
                    setEvents(obj);
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
            db.chargerLignes(ligne);
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
        liste_satifs = gson.fromJson(donnees.toString(), type_liste);

        for (Satisfaction sat : liste_satifs) {
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
}