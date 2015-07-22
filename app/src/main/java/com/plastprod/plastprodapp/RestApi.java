package com.plastprod.plastprodapp;

import android.content.Context;
import android.util.Log;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Evenement;
import sqlite.helper.LigneCommande;
import sqlite.helper.Societe;
import sqlite.helper.Synchro;

public class RestApi {

	//pour les requetes HTTP
	AsyncHttpClient client;
	//pour serialiser / déserialiser les objets
	Gson gson;
	Context context;
    DatabaseHelper db;

    //les données à récupérer
    List<Societe> liste_clients;
    List<Contact> liste_contacts;
    List<Bon> liste_bons;
    List<LigneCommande> liste_articles;
    List<Evenement> liste_evenements;

	private static final String API_CLIENT			= "http://10.0.2.2/WebServices/api/societes";
	private static final String API_CLIENT_AJT		= "http://10.0.2.2/WebServices/api/societes/ajt";
	private static final String API_CLIENT_MAJ 		= "http://10.0.2.2/WebServices/api/societes/maj";
	
	private static final String API_CONTACT 		= "http://10.0.2.2/WebServices/api/contacts";
	private static final String API_CONTACT_AJT 	= "http://10.0.2.2/WebServices/api/contacts/ajt";
	private static final String API_CONTACT_MAJ		= "http://10.0.2.2/WebServices/api/contacts/maj";
	
	private static final String API_BON 			= "http://10.0.2.2/WebServices/api/bons";
	private static final String API_BON_AJT 		= "http://10.0.2.2/WebServices/api/bons/ajt";
	private static final String API_BON_MAJ 		= "http://10.0.2.2/WebServices/api/bons/maj";
	
	private static final String API_LIGNES			= "http://10.0.2.2/WebServices/api/lignes";
	private static final String API_LIGNES_AJT		= "http://10.0.2.2/WebServices/api/lignes/ajt";
	private static final String API_LIGNES_MAJ		= "http://10.0.2.2/WebServices/api/lignes/maj";
	
	private static final String API_EVENEMENT 		= "http://10.0.2.2/WebServices/api/evenements";
	private static final String API_EVENEMENT_AJT 	= "http://10.0.2.2/WebServices/api/evenements/ajt";
	private static final String API_EVENEMENT_MAJ 	= "http://10.0.2.2/WebServices/api/evenements/maj";
	
	private static final String API_UTILISATEUR = "http://10.0.2.2/WebServices/api/utilisateurs";
	private static final String API_PRODUIT 	= "http://10.0.2.2/WebServices/api/produits";
	private static final String API_PARAMETRE 	= "http://10.0.2.2/WebServices/api/parametres";
	private static final String API_OBJECTIF	= "http://10.0.2.2/WebServices/api/objectifs";
	private static final String API_STOCK 		= "http://10.0.2.2/WebServices/api/stocks";
	private static final String API_REPONSE		= "http://10.0.2.2/WebServices/api/reponses";
	private static final String API_SATISF 		= "http://10.0.2.2/WebServices/api/satisfactions";

	//constructeur
	public RestApi(Context context) {
		client = new AsyncHttpClient();
		gson = new Gson();
		this.context = context;
	}

	public void recupererDepuisWS(String url, RequestParams parametres, final String type){

        client.get(url, parametres, new AsyncHttpResponseHandler() {

            //Réponse OK
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    //on récupère le corps de la réponse
                    String reponse = new String(responseBody, StandardCharsets.UTF_8);
                    //données JSON (tableau)
                    JSONArray obj = new JSONArray(reponse);

                    //Mise en place des données
                    switch (type) {

                        case "Societes":
                            setClients(obj);
                        case "Contacts":
                        case "Bons":
                        case "Articles":
                        case "Produits":
                        case "Utilisateurs":
                        case "Objectifs":
                        case "Satisfaction":
                        case "Reponses":
                        case "Stock":
                        case "Parametres":
                        case "Evenements":
                            
                        default:

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Erreur
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Erreur
            }

        });
	}
	
	public void envoyerVersWS(String url, String donnees, final String type ){

		StringEntity entite = null;

		try {
			//entite = new StringEntity(obj.toString(), "UTF-8");
            entite = new StringEntity(donnees);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}


		client.post(context, url, entite, "text/html", new AsyncHttpResponseHandler() {

            //Réponse 200 OK
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                //Traiter
				try{
                    //on récupère le retour
                    String test = new String(responseBody, StandardCharsets.UTF_8);
                    //on trace
                    Log.d("test", test);

                    //On recupère l'objet issu de JSON
                    JSONArray donnees = new JSONArray(test);
					JSONObject etat = null;

					for(int i = 0; i < donnees.length() ; i++)
					{
						etat = donnees.getJSONObject(i);

						if( etat.get("Etat").toString() == "OK" ){
							//base de correspondance
							Synchro correspondance = new Synchro();

							correspondance.setType(type);
							correspondance.setNewId(etat.getInt("NewId"));
							correspondance.setOldId(etat.getInt("OldId"));

							db.ajouterCorrespondance(correspondance);
						}
					}

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }


            }

            //Erreur
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Erreur
            }
        });
	}

	public int envoyerClients(int methode, List<Societe> liste_clients, DatabaseHelper base){

        if( liste_clients.size() > 0 ) {

			db = base;

            try {

                Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
                String clients = gson.toJson(liste_clients, type_liste);

                //Ajout
                if (methode == 1) {
                    envoyerVersWS(API_CLIENT_AJT, clients, "Clients");
                }
                //Modification
                else {
                    envoyerVersWS(API_CLIENT_MAJ, clients, "Clients");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

		return 1;
	}

	public int envoyerContacts(int methode, List<Contact> liste_contacts){
		try{
			Type type_liste = new TypeToken<ArrayList<Contact>>() {}.getType();
			String contacts = gson.toJson(liste_contacts, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_CONTACT_AJT, contacts, "Contacts");
			}
			//Modification
			else{
				envoyerVersWS(API_CONTACT_MAJ, contacts, "Contacts");
			}

		}
		catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
		return 1;		
	}

	public int envoyerBons(int methode, List<Bon> liste_bons){
		try{
			Type type_liste = new TypeToken<ArrayList<Bon>>() {}.getType();
			String bons = gson.toJson(liste_bons, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_BON_AJT, bons, "Bons");
			}
			//Modification
			else{
				envoyerVersWS(API_BON_MAJ, bons, "Bons");
			}

		}
		catch(Exception e){
            e.printStackTrace();
            return -1;
		}
		
		return 1;		
	}

	public int envoyerLignes(int methode, List<LigneCommande> liste_lignes){
		try{
			Type type_liste = new TypeToken<ArrayList<LigneCommande>>() {}.getType();
			String lignes = gson.toJson(liste_lignes, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_LIGNES_AJT, lignes, "Articles");
			}
			//Modification
			else{
				envoyerVersWS(API_LIGNES_MAJ, lignes, "Articles");
			}

		}
		catch(Exception e){
            e.printStackTrace();
            return -1;
		}
		
		return 1;		
	}

	public int envoyerEvenements(int methode, List<Evenement> liste_events){
		try{
			Type type_liste = new TypeToken<ArrayList<Evenement>>() {}.getType();
			String events = gson.toJson(liste_events, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_EVENEMENT_AJT, events, "Evenements");
			}
			//Modification
			else{
				envoyerVersWS(API_EVENEMENT_MAJ, events, "Evenements");
			}

		}
		catch(Exception e){
            e.printStackTrace();
            return -1;
		}
		
		return 1;		
	}

    public void setClients(JSONArray donnees){

        Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
        liste_clients = gson.fromJson(donnees.toString(), type_liste);

        for (Societe soc : liste_clients) {
            db.chargerClient(soc);
        }
    }

	public void recupererClients(DatabaseHelper base){

        db = base;

		RequestParams parametres = new RequestParams();
		//on intérroge le web service
		recupererDepuisWS(API_CLIENT, parametres, "Societes");

	}
}