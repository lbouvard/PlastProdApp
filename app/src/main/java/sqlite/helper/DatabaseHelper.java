package sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Laurent on 10/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //logcat tag
    //private static final String log = "DatabaseHelper";

    //version de la base
    private static final int DATABASE_VERSION = 65;

    //nom de la base
    private static final String DATABASE_NAME = "DB_PLASTPROD";

    //nom des tables
    private static final String TABLE_BON = "Bon";
    private static final String TABLE_COMPTE = "Compte";
    private static final String TABLE_CONTACT = "Contact";
    private static final String TABLE_EVENT = "Evenement";
    private static final String TABLE_LIGNE = "LigneCommande";
    private static final String TABLE_OBJ = "Objectif";
    private static final String TABLE_PARAM = "Parametre";
    private static final String TABLE_PRODUIT = "Produit";
    private static final String TABLE_REPONSE = "Reponse";
    private static final String TABLE_SATISF = "Satisfaction";
    private static final String TABLE_SOCIETE = "Societe";
    private static final String TABLE_STOCK = "Stock";
    private static final String TABLE_CORRESP_COULEUR = "CorrespCouleur";
    private static final String TABLE_CORRESP_ID = "CorrespId";
    private static final String TABLE_CALENDRIER = "MajCalendrier";
    private static final String TABLE_CHOIX = "Choix";


    private static final String CREATE_TABLE_SOCIETE = "CREATE TABLE Societe (IdtSociete INTEGER PRIMARY KEY, Nom TEXT NOT NULL, "
            + "Adresse1 TEXT NOT NULL, Adresse2 TEXT, CodePostal TEXT NOT NULL, Ville TEXT NOT NULL , Pays TEXT NOT NULL, Type TEXT NOT NULL, Commentaire  TEXT, "
            + "Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitSup INTEGER NOT NULL, ATraiter INTEGER NOT NULL)";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE Contact (IdtContact INTEGER PRIMARY KEY, Nom TEXT NOT NULL, "
            + "Prenom TEXT NOT NULL, Poste TEXT NOT NULL, TelFixe TEXT NOT NULL, Fax TEXT NOT NULL, TelMobile TEXT NOT NULL, Mail TEXT NOT NULL, Adresse TEXT NOT NULL, CodePostal TEXT NOT NULL, Ville TEXT NOT NULL, Pays TEXT NOT NULL, "
            + "Commentaire TEXT NOT NULL, Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitSup INTEGER NOT NULL, ATraiter INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_COMPTE= "CREATE TABLE Compte (IdtCompte INTEGER PRIMARY KEY, Nom TEXT NOT NULL, "
            + "MotDePasse  TEXT NOT NULL, Mail TEXT NOT NULL, Salt TEXT NOT NULL, Actif INTEGER NOT NULL, IdtContact INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtContact) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_BON = "CREATE TABLE Bon (IdtBon INTEGER PRIMARY KEY, DateCommande TEXT NOT NULL, "
            + "EtatCommande TEXT NOT NULL, Commentaire TEXT, Type TEXT NOT NULL, Suivi TEXT NOT NULL, Transporteur TEXT NOT NULL, Auteur TEXT NOT NULL, DateChg TEXT, BitChg  INTEGER NOT NULL, "
            + "BitAjout INTEGER NOT NULL, BitSup INTEGER NOT NULL, ATraiter INTEGER NOT NULL, Devis_id INTEGER, Societe_id INTEGER NOT NULL, Contact_id INTEGER NOT NULL, "
            + "FOREIGN KEY (Societe_id) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (Contact_id) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_STOCK = "CREATE TABLE Stock (IdtEntree INTEGER PRIMARY KEY, "
            + "Quantite INTEGER NOT NULL, DelaisMoy INTEGER NOT NULL, Delais INTEGER NOT NULL)";

    private static final String CREATE_TABLE_PRODUIT = "CREATE TABLE Produit (IdtProduit INTEGER PRIMARY KEY, "
            + "Nom TEXT NOT NULL, Description TEXT NOT NULL, Categorie TEXT NOT NULL, Code TEXT NOT NULL, "
            + "Prix REAL NOT NULL)";

    private static final String CREATE_TABLE_OBJ = "CREATE TABLE Objectif (IdtObjectif INTEGER PRIMARY KEY, "
            + "Annee TEXT NOT NULL, Type  TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, IdtCompte INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_PARAM = "CREATE TABLE Parametre (IdtParam INTEGER PRIMARY KEY, "
            + "Nom TEXT NOT NULL, Type TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, ATraiter INTEGER NOT NULL, "
            + "IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_REPONSE = "CREATE TABLE Reponse (IdtQuestion INTEGER PRIMARY KEY, "
            + "Question TEXT NOT NULL, Reponse TEXT NOT NULL, Categorie TEXT NOT NULL, Type TEXT NOT NULL, Niveau INTEGER NOT NULL, IdtSatisfaction INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtSatisfaction) REFERENCES Satisfaction(IdtSatisfaction))";

    private static final String CREATE_TABLE_SATISF = "CREATE TABLE Satisfaction (IdtSatisfaction INTEGER PRIMARY KEY, "
            + "Nom TEXT, DateEnvoi TEXT, DateRecu TEXT, Corps TEXT, Lien TEXT, Contact TEXT, BitAjout INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE Evenement (IdtEvent INTEGER PRIMARY KEY, DateDeb TEXT NOT NULL, "
            + "DateFin TEXT NOT NULL, Recurrent TEXT NOT NULL, Frequence TEXT NOT NULL, Titre TEXT NOT NULL, Emplacement TEXT NOT NULL, Commentaire TEXT NOT NULL, "
            + "Disponibilite TEXT NOT NULL, EstPrive INTEGER NOT NULL, BitAjout INTEGER NOT NULL, BitSup INTEGER NOT NULL, ATraiter INTEGER NOT NULL, IdtCompte  INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_LIGNE = "CREATE TABLE LigneCommande (Idt INTEGER PRIMARY KEY, Quantite INTEGER NOT NULL, "
            + "Code TEXT, Nom TEXT NOT NULL, Description TEXT, PrixUnitaire REAL NOT NULL, Remise INTEGER NOT NULL, "
            + "BitAjout INTEGER NOT NULL, BitSup INTEGER NOT NULL, ATraiter INTEGER NOT NULL, IdtBon INTEGER NOT NULL, "
            + "FOREIGN KEY (IdtBon) REFERENCES Bon(IdtBon))";

    private static final String CREATE_TABLE_CORRESP_COULEUR = "CREATE TABLE CorrespCouleur (IdtLigne INTEGER PRIMARY KEY, Nom TEXT NOT NULL, "
            + "Couleur TEXT NOT NULL)";

    private static final String CREATE_TABLE_CORRESP_ID = "CREATE TABLE CorrespId (Idt INTEGER PRIMARY KEY, Type TEXT NOT NULL, "
            + "IdtAndroid INTEGER NOT NULL, IdtServeur INTEGER NOT NULL)";

    private static final String CREATE_TABLE_CALENDRIER = "CREATE TABLE MajCalendrier (Idt INTEGER PRIMARY KEY, DateDerniereMaj TEXT, IdtParamCompte INTEGER NOT NULL )";

    private static final String CREATE_TABLE_CHOIX = "CREATE TABLE Choix (Idt INTEGER PRIMARY KEY, Type TEXT NOT NULL, Valeur TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_BON);
        db.execSQL(CREATE_TABLE_REPONSE);
        db.execSQL(CREATE_TABLE_COMPTE);
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_LIGNE);
        db.execSQL(CREATE_TABLE_OBJ);
        db.execSQL(CREATE_TABLE_PARAM);
        db.execSQL(CREATE_TABLE_PRODUIT);
        db.execSQL(CREATE_TABLE_SATISF);
        db.execSQL(CREATE_TABLE_STOCK);
        db.execSQL(CREATE_TABLE_SOCIETE);
        db.execSQL(CREATE_TABLE_CORRESP_COULEUR);
        db.execSQL(CREATE_TABLE_CORRESP_ID);
        db.execSQL(CREATE_TABLE_CALENDRIER);
        db.execSQL(CREATE_TABLE_CHOIX);

        chargerTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGNE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPONSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SATISF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOCIETE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRESP_COULEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRESP_ID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDRIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHOIX);

        // create new tables
        onCreate(db);

        //populate tables
        chargerTables(db);
    }

    /*******************************
     * AUTHENTIFICATION COMMERCIAL
     ******************************/

    // permet de récupérer le salt pour le cryptage du mot de passe
    public String getSalt(String login){

        String salt = "";
        String requete = "SELECT Salt FROM Compte "
                + "WHERE Nom = '" + login + "' OR Mail = '" + login + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c.getCount() > 0 ) {
            c.moveToFirst();

            salt = c.getString(c.getColumnIndex("Salt"));

            c.close();
        }

        return salt;
    }

    // permet de contrôler les identifiants
    public Contact verifierIdentifiantCommercial(String login, String motDePasse){

        Contact info = new Contact();
        int id_contact = -1;

        String requete = "SELECT IdtContact FROM Compte "
                + "WHERE Nom = '" + login + "' And MotDePasse = '" + motDePasse + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c.getCount() > 0) {
            c.moveToFirst();
            id_contact = c.getInt(c.getColumnIndex("IdtContact"));
            c.close();

            //Authentification réussie
            if( !login.equals("full_god") ){
                info = getCommercial(id_contact);
            }
            else{
                info.setNom("ADMSYS");
            }
        }
        else
            info = null;

        return info;
    }

    // récupère les paramètres du calendrier du commercial (compte google, hotmail...)
    public List<Parametre> getParamCalendrier(int id_contact){

        List<Parametre> paramCalendrier = new ArrayList<Parametre>();
        String requete = "";

        requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur FROM Parametre "
                + "WHERE IdtCompte = " + id_contact + " AND ( Nom = 'TYPE_COMPTE' OR Nom = 'ADRESSE_COMPTE' ) "
                + "ORDER BY Nom";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les paramètres
            if (c.moveToFirst()) {

                do {
                    Parametre ligne = new Parametre();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtParam")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    ligne.setValeur(c.getString(c.getColumnIndex("Valeur")));

                    //On ajoute la commande
                    paramCalendrier.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return paramCalendrier;
    }

    // renvoi la date + 1 jour de la dernière synchro avec le calendrier
    public String getDerniereRecuperationEvenements(int idt_param){

        String datePlusUn = "";
        String requete;

        requete = "SELECT datetime( DateDerniereMaj, '+1 days') Date "
                + "FROM MajCalendrier "
                + "WHERE IdtParamCompte = " + String.valueOf(idt_param);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null){
            if( c.moveToFirst()){
                datePlusUn = c.getString(c.getColumnIndex("Date"));
            }

            c.close();
        }

        return datePlusUn;
    }

    /***********************
     * AJOUT
     ***********************/

    //Ajouter un devis
    public void ajouterDevis(Bon devis, LigneCommande[] lignes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateCommande", devis.getDate_commande());
        valeurs.put("EtatCommande", devis.getEtat_commande());
        valeurs.put("Type", "DE");
        valeurs.put("Suivi", "");
        valeurs.put("Transporteur", "");
        valeurs.put("Auteur", devis.getAuteur());
        //valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 0);
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("Societe_id", devis.getClient().getId());
        valeurs.put("Contact_id", devis.getCommercial().getId());

        //insertion du devis
        long devis_id = db.insert(TABLE_BON, null, valeurs);

        //ajout des articles du devis
        for(LigneCommande ligne : lignes){
            ajouterLigne(ligne, devis_id);
        }

        db.close();
    }

    //Ajouter un bon de commande
    public long ajouterBonCommande(Bon bon, LigneCommande[] lignes, int devis_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        valeurs.put("Type", "CD");
        valeurs.put("Suivi", "");
        valeurs.put("Transporteur", "");
        valeurs.put("Auteur", bon.getAuteur());
        //valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 0);
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);

        if( devis_id != -1 )
            valeurs.put("Devis_id", devis_id);

        valeurs.put("Societe_id", bon.getClient().getId());
        valeurs.put("Contact_id", bon.getCommercial().getId());

        //insertion du bon de commande
        long bon_id = db.insert(TABLE_BON, null, valeurs);

        //ajout des articles du bon de commande
        for(LigneCommande ligne : lignes){
            ajouterLigne(ligne, bon_id);
        }

        return bon_id;
    }

    //Ajouter une ligne d'article
    public long ajouterLigne(LigneCommande ligne, long bon_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Quantite", ligne.getQuantite());
        valeurs.put("Code", ligne.getCode());
        valeurs.put("Nom", ligne.getNom());
        valeurs.put("Description", ligne.getDescription());
        valeurs.put("Remise", ligne.getRemise());
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire());
        valeurs.put("IdtBon", bon_id);

        return db.insert(TABLE_LIGNE, null, valeurs);
    }

    //Ajouter un client
    public void ajouterClient(Societe client, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Type", type);
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 1);

        db.insert(TABLE_SOCIETE, null, valeurs);
        db.close();
    }

    //Ajouter un contact
    public void ajouterContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());
        valeurs.put("Fax", contact.getFax());
        valeurs.put("TelMobile", contact.getTel_mobile());
        valeurs.put("Mail", contact.getEmail());
        valeurs.put("Adresse", contact.getAdresse());
        valeurs.put("CodePostal", contact.getCode_postal());
        valeurs.put("Ville", contact.getVille());
        valeurs.put("Pays", contact.getPays());
        valeurs.put("Commentaire", contact.getCommentaire());
        valeurs.put("Auteur", contact.getAuteur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 1);
        valeurs.put("IdtSociete", contact.getId_societe());

        db.insert(TABLE_CONTACT, null, valeurs);
        db.close();
    }

    //Ajouter un événement
    public long ajouterEvenement(Evenement e) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateDeb", e.getDate_debut());
        valeurs.put("DateFin", e.getDate_fin());
        valeurs.put("Recurrent", e.getReccurent());
        valeurs.put("Frequence", e.getFrequence());
        valeurs.put("Titre", e.getTitre());
        valeurs.put("Emplacement", e.getEmplacement());
        valeurs.put("Commentaire", e.getCommentaire());
        valeurs.put("Disponibilite", e.getDisponibilite());
        valeurs.put("EstPrive", e.getEst_prive());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtCompte", e.getCompte_id());

        return db.insert(TABLE_EVENT, null, valeurs);
    }

    //Ajouter un compte (seulement IT)
    public long ajouterCompte(Compte cp){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", cp.getNom());
        valeurs.put("MotDePasse", cp.getMdp());
        valeurs.put("Mail", cp.getEmail());
        valeurs.put("Salt", cp.getSalt());
        valeurs.put("Actif", cp.getActif());
        valeurs.put("IdtContact", cp.getContact_id());

        return db.insert(TABLE_COMPTE, null, valeurs);
    }

    //Ajouter un questionnaire de satisfaction envoyé
    public long ajouterSatisfaction(Satisfaction sat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", sat.getNom());
        valeurs.put("DateEnvoi", sat.getDate_envoi());
        valeurs.put("Corps", sat.getCorps());
        valeurs.put("Lien", sat.getLien());
        valeurs.put("Contact", sat.getContact());
        valeurs.put("IdtSociete", sat.getId_societe());
        valeurs.put("BitAjout", 1);

        return db.insert(TABLE_SATISF, null, valeurs);
    }

    /**************************
     * RECUPERATION UNIQUE
     **************************/

    public Societe getClient(int id_client){

        Societe client = new Societe();
        String requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur "
                       + "FROM Societe "
                       + "WHERE IdtSociete = " + id_client;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
            client.setNom(c.getString(c.getColumnIndex("Nom")));
            client.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
            client.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
            client.setCodePostal(c.getString(c.getColumnIndex("CodePostal")));
            client.setVille(c.getString(c.getColumnIndex("Ville")));
            client.setPays(c.getString(c.getColumnIndex("Pays")));
            client.setType(c.getString(c.getColumnIndex("Type")));
            client.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
            client.setAuteur(c.getString(c.getColumnIndex("Auteur")));

            c.close();
        }
        else
            client = null;

        return client;
    }

    public Contact getCommercial(int id_commercial){

        Contact commercial = new Contact();
        String requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, "
                       + "CodePostal, Ville, Pays, Commentaire, Auteur "
                       + "FROM Contact "
                       + "WHERE IdtContact = " + id_commercial;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            commercial.setId(id_commercial);
            commercial.setNom(c.getString(c.getColumnIndex("Nom")));
            commercial.setPrenom(c.getString(c.getColumnIndex("Prenom")));
            commercial.setPoste(c.getString(c.getColumnIndex("Poste")));
            commercial.setTel_fixe(c.getString(c.getColumnIndex("TelFixe")));
            commercial.setFax(c.getString(c.getColumnIndex("Fax")));
            commercial.setTel_mobile(c.getString(c.getColumnIndex("TelMobile")));
            commercial.setEmail(c.getString(c.getColumnIndex("Mail")));
            commercial.setAdresse(c.getString(c.getColumnIndex("Adresse")));
            commercial.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
            commercial.setVille(c.getString(c.getColumnIndex("Ville")));
            commercial.setPays(c.getString(c.getColumnIndex("Pays")));
            commercial.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
            commercial.setAuteur(c.getString(c.getColumnIndex("Auteur")));

            c.close();
        }
        else
            commercial = null;

        return commercial;
    }

    public LigneCommande getArticle(int id_article){

        LigneCommande ligne = new LigneCommande();

        String requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtBon "
                + "FROM LigneCommande "
                + "WHERE Idt = " + id_article ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les lignes d'articles.
        if( c != null ) {

            if (c.moveToFirst()) {

                    ligne.setId(c.getInt(c.getColumnIndex("Idt")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setId_bon(c.getInt(c.getColumnIndex("IdtBon")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setPrixUnitaire(c.getDouble(c.getColumnIndex("PrixUnitaire")));
                    ligne.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                    ligne.setRemise(c.getInt(c.getColumnIndex("Remise")));

                    ligne.calculerPrixRemise();
                    ligne.calculerPrixTotal();
            }

            c.close();
        }

        return ligne;
    }

    public String getAdresseIpServeur(){

        String retour;
        String requete = "SELECT Valeur FROM Parametre WHERE Nom = 'ADRESSEIP_SRV'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            c.moveToFirst();
            retour = c.getString(c.getColumnIndex("Valeur"));
            c.close();
        }
        else
            retour = null;

        return retour;
    }

    public String getSsidWifi(){

        String retour;
        String requete = "SELECT Valeur FROM Parametre WHERE Nom = 'SSID_SOCIETE'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            c.moveToFirst();
            retour = c.getString(c.getColumnIndex("Valeur"));
            c.close();
        }
        else
            retour = null;

        return retour;
    }

    public Satisfaction recupererQuestionnaire(int id_commercial){

        Satisfaction sat = new Satisfaction();

        String requete = "SELECT sat.Nom, sat.Corps, sat.Lien \n" +
                "FROM Satisfaction sat\n" +
                "INNER JOIN Parametre parm ON sat.Nom = parm.Valeur \n" +
                "WHERE sat.IdtSociete = -1 AND parm.Nom = 'QVERSION' AND parm.IdtCompte = " + String.valueOf(id_commercial);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            c.moveToFirst();

            sat.setNom(c.getString(c.getColumnIndex("Nom")));
            sat.setCorps(c.getString(c.getColumnIndex("Corps")));
            sat.setLien(c.getString(c.getColumnIndex("Lien")));

            c.close();
        }
        else
            sat = null;

        return sat;
    }

    /**************************
     * RECUPERATION MULTIPLE
     **************************/

    public List<Bon> getBons(String type, int id_societe, int id_commercial, boolean tout) {

        List<Bon> bons = new ArrayList<Bon>();
        String requete = "";

        if( id_societe == -1 && id_commercial == -1 && !tout){
            //tous les devis ou les commandes
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, Auteur, Contact_id, Societe_id "
                           + "FROM Bon "
                           + "WHERE Type = '" + type + "' AND BitChg = 0 AND BitSup = 0";
        }
        else if( id_societe == -1 && id_commercial == -1 && tout){
            //tous les types de bon
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, Auteur, Contact_id, Societe_id "
                    + "FROM Bon "
                    + "WHERE BitChg = 0 AND BitSup = 0";
        }
        else if( id_commercial == -1 && !tout ){
            //les devis ou les commandes d'un client
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, Auteur, Contact_id, Societe_id "
                    + "FROM Bon "
                    + "WHERE Type = '" + type + "' AND Societe_id = " + id_societe + " AND BitChg = 0 AND BitSup = 0";
        }
        else if( id_commercial == -1 && tout ){
            //les devis et les commandes d'un client
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, Auteur, Contact_id, Societe_id "
                    + "FROM Bon "
                    + "WHERE Societe_id = " + id_societe + " AND BitChg = 0 AND BitSup = 0";
        }
        else if( !tout ){
            //les bons ou les commandes d'un commercial
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, Auteur, Contact_id, Societe_id "
                    + "FROM Bon "
                    + "WHERE Type = '" + type + "' AND Contact_id = " + id_commercial + " AND BitChg = 0 AND BitSup = 0";
        }
        else {
            //tout les types de bon d'un commercial
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, Auteur, Contact_id, Societe_id "
                    + "FROM Bon "
                    + "WHERE Contact_id = " + id_commercial + " AND BitChg = 0 AND BitSup = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les commandes pour récupérer les articles.
        if( c != null ) {
            if (c.moveToFirst()) {
                do {
                    Bon ligne = new Bon(type);
                    ligne.setId(c.getInt(c.getColumnIndex("IdtBon")));
                    ligne.setDate_commande(c.getString(c.getColumnIndex("DateCommande")));
                    ligne.setEtat_commande(c.getString(c.getColumnIndex("EtatCommande")));
                    ligne.setSuivi(c.getString(c.getColumnIndex("Suivi")));
                    ligne.setTransporteur(c.getString(c.getColumnIndex("Transporteur")));
                    ligne.setCommercial(getCommercial(c.getInt(c.getColumnIndex("Contact_id"))));
                    ligne.setClient(getClient(c.getInt(c.getColumnIndex("Societe_id"))));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));
                    ligne.setLignesBon(getLignesCommande(c.getInt(c.getColumnIndex("IdtBon"))));

                    //On ajoute la commande
                    bons.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return bons;
    }

    public List<LigneCommande> getLignesCommande(long id_bon){

        List<LigneCommande> lignes = new ArrayList<LigneCommande>();
        String requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtBon "
                + "FROM LigneCommande "
                + "WHERE IdtBon = " + id_bon ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les lignes d'articles.
        if( c != null ) {
            if (c.moveToFirst()) {
                do {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setId(c.getInt(c.getColumnIndex("Idt")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setId_bon(c.getInt(c.getColumnIndex("IdtBon")));
                    //ligne.setId_produit(c.getInt(c.getColumnIndex("IdtProduit")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setPrixUnitaire(c.getDouble(c.getColumnIndex("PrixUnitaire")));
                    ligne.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                    ligne.setRemise(c.getInt(c.getColumnIndex("Remise")));

                    ligne.calculerPrixRemise();
                    ligne.calculerPrixTotal();

                    lignes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return lignes;
    }

    public List<Societe> getSocietes(String type){

        List<Societe> societes = new ArrayList<Societe>();
        String requete = "";

        requete = "SELECT soc.IdtSociete, soc.Nom, soc.Adresse1, soc.Adresse2, soc.CodePostal, soc.Ville, soc.Pays, soc.Type, soc.Commentaire, " +
                "soc.Auteur, cc.Couleur, ifnull(COUNT(con.IdtContact),0) AS Nb " +
                "FROM Societe soc LEFT JOIN Contact con ON soc.IdtSociete = con.IdtSociete AND con.BitSup = 0 " +
                "INNER JOIN CorrespCouleur cc ON soc.Auteur = cc.Nom " +
                "WHERE soc.Type = '" + type + "' AND soc.BitSup = 0 GROUP BY soc.IdtSociete ORDER BY soc.Nom";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les sociétés pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {

                    Societe ligne = new Societe();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtSociete")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
                    ligne.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
                    ligne.setCodePostal(c.getString(c.getColumnIndex("CodePostal")));
                    ligne.setVille(c.getString(c.getColumnIndex("Ville")));
                    ligne.setPays(c.getString(c.getColumnIndex("Pays")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));
                    ligne.setNb_contact(c.getInt(c.getColumnIndex("Nb")));
                    ligne.setCouleur(c.getString(c.getColumnIndex("Couleur")));

                    //On ajoute la société
                    societes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return societes;
    }

    public Cursor getCurseurClient(){

        String requete = "";

        requete = "SELECT soc.IdtSociete as _id, soc.Nom, soc.Adresse1, soc.Adresse2, soc.CodePostal, soc.Ville, soc.Pays, soc.Type, soc.Commentaire, " +
                "soc.Auteur, cc.Couleur, ifnull(COUNT(con.IdtContact),0) AS Nb " +
                "FROM Societe soc LEFT JOIN Contact con ON soc.IdtSociete = con.IdtSociete AND con.BitSup = 0 " +
                "INNER JOIN CorrespCouleur cc ON soc.Auteur = cc.Nom " +
                "WHERE soc.BitSup = 0 GROUP BY soc.IdtSociete ORDER BY soc.Nom";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        return c;
    }

    public List<Contact> getContacts(int id_societe, String clauseWhere){

        List<Contact> contacts = new ArrayList<Contact>();
        String requete = "";

        if( clauseWhere == null ) {
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                    + "Commentaire, Auteur FROM Contact "
                    + "WHERE IdtSociete = " + id_societe + " AND BitSup = 0";
        }
        else{
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                    + "Commentaire, Auteur FROM Contact " + clauseWhere;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les soci굩s pour ajouter un objet ࡬a liste.
            if (c.moveToFirst()) {
                do {

                    Contact ligne = new Contact();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtContact")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setPrenom(c.getString(c.getColumnIndex("Prenom")));
                    ligne.setPoste(c.getString(c.getColumnIndex("Poste")));
                    ligne.setTel_fixe(c.getString(c.getColumnIndex("TelFixe")));
                    ligne.setFax(c.getString(c.getColumnIndex("Fax")));
                    ligne.setTel_mobile(c.getString(c.getColumnIndex("TelMobile")));
                    ligne.setEmail(c.getString(c.getColumnIndex("Mail")));
                    ligne.setAdresse(c.getString(c.getColumnIndex("Adresse")));
                    ligne.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
                    ligne.setVille(c.getString(c.getColumnIndex("Ville")));
                    ligne.setPays(c.getString(c.getColumnIndex("Pays")));
                    ligne.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));

                    //On ajoute la commande
                    contacts.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return contacts;
    }

    public List<Parametre> getParametres(int id_commercial, boolean ajout){

        List<Parametre> params = new ArrayList<Parametre>();
        String requete = "";

        if( ajout ){
            requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur FROM Parametre "
                    + "WHERE ATraiter = 1 AND IdtCompte = " + id_commercial;
        }
        else {
            requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur FROM Parametre "
                    + "WHERE IdtCompte = " + id_commercial;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les paramètres pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {
                    Parametre ligne = new Parametre();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtParam")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    ligne.setValeur(c.getString(c.getColumnIndex("Valeur")));

                    //On ajoute la commande
                    params.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return params;
    }

    public List<Objectif> getObjectifs(int id_commercial){

        List<Objectif> objectifs = new ArrayList<Objectif>();
        String requete = "";

        requete = "SELECT IdtObjectif, Annee, Type, Libelle, Valeur FROM Objectif "
                + "WHERE IdtCompte = " + id_commercial;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les objectifs pour ajouter un objet ࡬a liste.
            if (c.moveToFirst()) {
                do {
                    Objectif ligne = new Objectif();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtObjectif")));
                    ligne.setAnnee(c.getString(c.getColumnIndex("Annee")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    ligne.setValeur(c.getString(c.getColumnIndex("Valeur")));

                    //On ajoute un ꭩment des objectifs
                    objectifs.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return objectifs;
    }

    public List<Produit> getProduits(){

        List<Produit> produits = new ArrayList<Produit>();
        String requete = "";

        requete = "SELECT prod.IdtProduit _id, prod.Nom, prod.Description, prod.Categorie, prod.Code, prod.Prix, st.Delais "
                + "FROM Produit prod "
                + "INNER JOIN Stock st ON prod.IdtProduit = st.IdtEntree";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet ࡬a liste.
            if (c.moveToFirst()) {
                do {
                    Produit ligne = new Produit();

                    ligne.setId(c.getInt(c.getColumnIndex("_id")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setCategorie(c.getString(c.getColumnIndex("Categorie")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setPrix(c.getDouble(c.getColumnIndex("Prix")));
                    ligne.setDelais(c.getInt(c.getColumnIndex("Delais")));

                    //On ajoute un produit à la liste
                    produits.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return produits;
    }

    public Stock getStocks(int id_entree){

        Stock info = new Stock();
        String requete = "";

        requete = "SELECT Quantite, DelaisMoy, Delais FROM Stock "
                + "WHERE IdtEntree = " + id_entree;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                info.setId(id_entree);
                info.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                info.setDelaisMoy(c.getInt(c.getColumnIndex("DelaisMoy")));
                info.setDelais(c.getInt(c.getColumnIndex("Delais")));
            }

            c.close();
        }
        else
            info = null;

        return info;
    }

    public List<StatParClient> getStatsParClient(String debut, String fin){

        List<StatParClient> stats_client = new ArrayList<StatParClient>();
        String requete = "";

        requete = "SELECT Nom, Type, COALESCE(cmd1.nb,0) NbCommande, COALESCE(cmd2.nb, 0) NbDevis, COALESCE(cmd3.nb, 0) NbCommandeTermine, COALESCE(cmd4.nb, 0) NbCommandePrepare, COALESCE(cmd5.nb,0) NbDevisTransformeCommande\n" +
                "FROM Societe\n" +
                "LEFT JOIN (\n" +
                "    SELECT Societe_id, COUNT(*) as nb\n" +
                "    FROM Bon\n" +
                "    WHERE BitSup = 0 AND BitChg = 0 AND Type = 'CD' AND DateCommande > '" + debut + "' AND DateCommande < '" + fin + "'\n" +
                "    GROUP BY (Societe_id)\n" +
                "    ) AS cmd1 ON cmd1.Societe_id = IdtSociete\n" +
                "LEFT JOIN (\n" +
                "    SELECT Societe_id, COUNT(*) as nb\n" +
                "    FROM Bon\n" +
                "    WHERE BitSup = 0 AND BitChg = 0 AND Type = 'DE' AND DateCommande > '" + debut + "' AND DateCommande < '" + fin + "'\n" +
                "    GROUP BY (Societe_id)\n" +
                "    ) AS cmd2 ON cmd2.Societe_id = IdtSociete\n" +
                "LEFT JOIN (\n" +
                "    SELECT Societe_id, COUNT(*) as nb\n" +
                "    FROM Bon\n" +
                "    WHERE BitSup = 0 AND BitChg = 0 AND Type = 'CD' AND EtatCommande = 'Terminée' AND DateCommande > '" + debut + "' AND DateCommande < '" + fin + "'\n" +
                "    GROUP BY (Societe_id)\n" +
                "    ) AS cmd3 ON cmd3.Societe_id = IdtSociete\n" +
                "LEFT JOIN (\n" +
                "    SELECT Societe_id, COUNT(*) as nb\n" +
                "    FROM Bon\n" +
                "    WHERE BitSup = 0 AND BitChg = 0 AND Type = 'CD' AND EtatCommande = 'Préparation'  AND DateCommande > '" + debut + "' AND DateCommande < '" + fin + "'\n" +
                "    GROUP BY (Societe_id)\n" +
                "    ) AS cmd4 ON cmd4.Societe_id = IdtSociete\n" +
                "LEFT JOIN (\n" +
                "    SELECT Societe_id, COUNT(*) as nb\n" +
                "    FROM Bon\n" +
                "    WHERE BitSup = 0 AND BitChg = 0 AND Type = 'CD' AND NOT Devis_id is NULL  AND DateCommande > '" + debut + "' AND DateCommande < '" + fin + "'\n" +
                "    GROUP BY (Societe_id)\n" +
                "    ) AS cmd5 ON cmd5.Societe_id = IdtSociete\n" +
                "WHERE BitSup = 0 AND Type IN ('C', 'P')\n" +
                "ORDER BY Nom";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            if (c.moveToFirst()) {

                do {
                    StatParClient client = new StatParClient();
                    client.setNom(c.getString(c.getColumnIndex("Nom")));
                    client.setType(c.getString(c.getColumnIndex("Type")));

                    Statistique stat = new Statistique();
                    stat.setNbCommande(c.getInt(c.getColumnIndex("NbCommande")));
                    stat.setNbDevis(c.getInt(c.getColumnIndex("NbDevis")));
                    stat.setNbCommandeTermine(c.getInt(c.getColumnIndex("NbCommandeTermine")));
                    stat.setNbCommandePrepare(c.getInt(c.getColumnIndex("NbCommandePrepare")));
                    stat.setNbDevisEtCommande(c.getInt(c.getColumnIndex("NbDevisTransformeCommande")));

                    client.setStat(stat);

                    //On ajoute la commande
                    stats_client.add(client);

                } while (c.moveToNext());
            }

            c.close();
        }

        return stats_client;
    }

    public List<StatProduit> getStatsProduit(String debut, String fin){

        List<StatProduit> stats_produit = new ArrayList<StatProduit>();
        String requete;

        requete = "SELECT prd.Code, prd.Nom, COALESCE(articles.nb, 0) NbVente \n" +
                "FROM Produit prd\n" +
                "LEFT JOIN (\n" +
                "    SELECT Code, SUM(Quantite) nb\n" +
                "    FROM LigneCommande\n" +
                "    WHERE IdtBon IN (\n" +
                "        SELECT IdtBon\n" +
                "        FROM Bon\n" +
                "        WHERE Type = 'CD' AND DateCommande > '" + debut + "' AND DateCommande < '" + fin + "')\n" +
                "    GROUP BY Code\n" +
                "    ) AS articles ON articles.Code = prd.Code\n" +
                "ORDER BY NbVente DESC ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            if (c.moveToFirst()) {

                do {
                    StatProduit produit = new StatProduit();

                    produit.setNomProduit(c.getString(c.getColumnIndex("Nom")));
                    produit.setCodeProduit(c.getString(c.getColumnIndex("Code")));
                    produit.setNbProduitVendu(c.getInt(c.getColumnIndex("NbVente")));

                    //On ajoute le produit
                    stats_produit.add(produit);

                } while (c.moveToNext());
            }

            c.close();
        }

        return stats_produit;
    }

    public List<Livraison> getSuiviLivraison(){

        List<Livraison> suivi = new ArrayList<Livraison>();
        String requete;

        requete = "SELECT cmd.IdtBon, soc.Nom, cmd.Transporteur, cmd.Suivi, hdispo.DateChg Depart, hsuivi.DateChg ArriveTransporteur, datetime(hsuivi.DateChg, '+2 days', '+6 hours') ArrivePrevue\n" +
                "FROM Bon cmd\n" +
                "INNER JOIN Societe soc ON cmd.Societe_id = soc.IdtSociete\n" +
                "JOIN Bon hdispo ON cmd.DateCommande = hdispo.DateCommande AND hdispo.BitChg = 1 AND hdispo.Suivi = \"\" AND hdispo.EtatCommande = \"En cours de préparation\"\n" +
                "JOIN Bon hsuivi ON cmd.DateCommande = hsuivi.DateCommande AND hsuivi.BitChg = 1 AND hsuivi.Suivi = \"\" AND hsuivi.EtatCommande = \"Mise à disposition\"\n" +
                "WHERE cmd.EtatCommande = \"En cours de livraison\" \n" +
                "AND cmd.BitSup = 0 AND cmd.BitChg = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            if (c.moveToFirst()) {

                do {
                    Livraison info = new Livraison();

                    info.setIdt(c.getInt(c.getColumnIndex("IdtBon")));
                    info.setNomSociete(c.getString(c.getColumnIndex("Nom")));
                    info.setTransporteur(c.getString(c.getColumnIndex("Transporteur")));
                    info.setTrack(c.getString(c.getColumnIndex("Suivi")));
                    info.setDateDispo(c.getString(c.getColumnIndex("Depart")));
                    info.setDateEnvoi(c.getString(c.getColumnIndex("ArriveTransporteur")));
                    info.setDateRecu(c.getString(c.getColumnIndex("ArrivePrevue")));

                    //On ajoute le suivi de livraison
                    suivi.add(info);

                } while (c.moveToNext());
            }

            c.close();
        }

        return suivi;
    }

    public List<StatisGraphique> getSatisGraphique(){

        List<StatisGraphique> liste = new ArrayList<StatisGraphique>();
        String requete = "";

        requete = "select rep.Categorie, COALESCE(Nb1.nb, 0) Niveau1, COALESCE(Nb2.nb, 0) Niveau2, COALESCE(Nb3.nb, 0) Niveau3, COALESCE(Nb4.nb, 0) Niveau4, COALESCE(Nb5.nb, 0) Niveau5\n" +
                "from Reponse rep\n" +
                "left join (\n" +
                "    select Categorie, COUNT(Niveau) nb from Reponse\n" +
                "    where Niveau = 1\n" +
                "    group by Categorie\n" +
                ") as Nb1 ON Nb1.Categorie = rep.Categorie\n" +
                "left join (\n" +
                "    select Categorie, COUNT(Niveau) nb from Reponse\n" +
                "    where Niveau = 2\n" +
                "    group by Categorie\n" +
                ") as Nb2 ON Nb2.Categorie = rep.Categorie\n" +
                "left join (\n" +
                "    select Categorie, COUNT(Niveau) nb from Reponse\n" +
                "    where Niveau = 3\n" +
                "    group by Categorie\n" +
                ") as Nb3 ON Nb3.Categorie = rep.Categorie\n" +
                "left join (\n" +
                "    select Categorie, COUNT(Niveau) nb from Reponse\n" +
                "    where Niveau = 4\n" +
                "    group by Categorie\n" +
                ") as Nb4 ON Nb4.Categorie = rep.Categorie\n" +
                "left join (\n" +
                "    select Categorie, COUNT(Niveau) nb from Reponse\n" +
                "    where Niveau = 5\n" +
                "    group by Categorie\n" +
                ") as Nb5 ON Nb5.Categorie = rep.Categorie\n" +
                "group by rep.Categorie\n" +
                "order by rep.Categorie";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les paramètres
            if (c.moveToFirst()) {

                do {
                    StatisGraphique ligne = new StatisGraphique();
                    ligne.setCategorie(c.getString(c.getColumnIndex("Categorie")));
                    ligne.setNbNivUn(c.getInt(c.getColumnIndex("Niveau1")));
                    ligne.setNbNivDeux(c.getInt(c.getColumnIndex("Niveau2")));
                    ligne.setNbNivTrois(c.getInt(c.getColumnIndex("Niveau3")));
                    ligne.setNbNivQuatre(c.getInt(c.getColumnIndex("Niveau4")));
                    ligne.setNbNivCinq(c.getInt(c.getColumnIndex("Niveau5")));

                    //On ajoute la commande
                    liste.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return liste;
    }

    public SatisDonnee getSatisTableau(){

        SatisDonnee info = new SatisDonnee();
        String requete = "";

        requete = "SELECT COUNT(sat.IdtSatisfaction) Envoye, COALESCE(sat2.Nb, 0) Recu \n" +
                "FROM Satisfaction sat\n" +
                "LEFT JOIN (\n" +
                "    SELECT IdtSatisfaction, COUNT(IdtSatisfaction) Nb\n" +
                "    FROM Satisfaction\n" +
                "    WHERE DateRecu IS NULL\n" +
                ") AS sat2 ON sat2.IdtSatisfaction = sat.IdtSatisfaction";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                info.setNbQuestionnaireEnvoye(c.getInt(c.getColumnIndex("Envoye")));
                info.setNbReponse(c.getInt(c.getColumnIndex("Envoye")) - c.getInt(c.getColumnIndex("Recu")));
            }

            c.close();
        }
        else
            info = null;

        return info;
    }

    public List<StatisCommentaire> getSatisCommentaire(){

        List<StatisCommentaire> info = new ArrayList<StatisCommentaire>();
        String requete = "";

        requete = "SELECT soc.Nom, satis.Contact, rep.Question, rep.Reponse\n" +
                "FROM Satisfaction satis\n" +
                "INNER JOIN Societe soc ON satis.IdtSociete = soc.IdtSociete\n" +
                "INNER JOIN Reponse rep ON satis.IdtSatisfaction = rep.IdtSatisfaction AND rep.Type = 'OUVERT'\n" +
                "WHERE satis.DateRecu IS NOT NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les commentaires
            if (c.moveToFirst()) {

                do {
                    StatisCommentaire com = new StatisCommentaire();
                    com.setSociete(c.getString(c.getColumnIndex("Nom")));
                    com.setClient(c.getString(c.getColumnIndex("Contact")));
                    com.setQuestion(c.getString(c.getColumnIndex("Question")));
                    com.setCommentaire(c.getString(c.getColumnIndex("Reponse")));

                    info.add(com);
                }
                while(c.moveToNext());
            }

            c.close();
        }
        else
            info = null;

        return info;
    }

    public List<Parametre> getParamQuestionnaire(int id_commercial){

        List<Parametre> liste = new ArrayList<Parametre>();
        String requete;

        // IdtParam, Nom, Type, Libelle, Valeur, IdtCompte
        requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur\n" +
                "FROM Parametre\n" +
                "WHERE Nom IN ('QAUTO', 'QETAPE', 'QDELAIS', 'QVERSION') AND IdtCompte = " + String.valueOf(id_commercial);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            if (c.moveToFirst()) {

                do {
                    Parametre info = new Parametre();

                    info.setId(c.getInt(c.getColumnIndex("IdtParam")));
                    info.setNom(c.getString(c.getColumnIndex("Nom")));
                    info.setType(c.getString(c.getColumnIndex("Type")));
                    info.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    info.setValeur(c.getString(c.getColumnIndex("Valeur")));
                    info.setId_Compte(id_commercial);

                    //On ajoute le paramètre
                    liste.add(info);

                } while (c.moveToNext());
            }

            c.close();
        }

        return liste;
    }

    public Cursor getCurseurChoix(String type){

        String requete;

        // Idt, Type, Valeur
        requete = "SELECT Idt _id, Valeur\n" +
                    "FROM Choix\n" +
                    "WHERE Type = '" + type + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        return c;
    }

    /**********************************
     * FIN AJOUT
     *********************************/
    public void finAjoutParametre(List<Parametre> params){

        SQLiteDatabase db = this.getWritableDatabase();

        for(Parametre parm : params){
            ContentValues valeurs = new ContentValues();
            valeurs.put("ATraiter", 0);

            // updating row
            db.update(TABLE_PARAM, valeurs, "IdtParam = ?",
                    new String[] { String.valueOf(parm.getId()) });
        }

        db.close();
    }

    /**********************************
     * RECHERCHES
     *********************************/

    public List<Societe> rechercherSociete(String type, String recherche){
        List<Societe> societes = new ArrayList<Societe>();
        String requete = "";

        requete = "SELECT soc.IdtSociete, soc.Nom, soc.Adresse1, soc.Adresse2, soc.CodePostal, soc.Ville, soc.Pays, soc.Type, soc.Commentaire, " +
                "soc.Auteur, cc.Couleur, ifnull(COUNT(con.IdtContact),0) AS Nb " +
                "FROM Societe soc LEFT JOIN Contact con ON soc.IdtSociete = con.IdtSociete AND con.BitSup = 0 " +
                "INNER JOIN CorrespCouleur cc ON soc.Auteur = cc.Nom " +
                "WHERE soc.Type = '" + type + "' AND soc.Nom LIKE '%" + recherche + "%' AND soc.BitSup = 0 GROUP BY soc.IdtSociete ORDER BY soc.Nom";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les sociétés pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {

                    Societe ligne = new Societe();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtSociete")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
                    ligne.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
                    ligne.setCodePostal(c.getString(c.getColumnIndex("CodePostal")));
                    ligne.setVille(c.getString(c.getColumnIndex("Ville")));
                    ligne.setPays(c.getString(c.getColumnIndex("Pays")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));
                    ligne.setNb_contact(c.getInt(c.getColumnIndex("Nb")));
                    ligne.setCouleur(c.getString(c.getColumnIndex("Couleur")));

                    //On ajoute la société
                    societes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return societes;
    }

    /***************************
     * VERIFICATION EXISTANCE
     ***************************/

    public Boolean clientExiste(String nom, int id_modif){

        Boolean existe = true;
        int id_trouve = -1;

        String requete = "SELECT IdtSociete FROM Societe "
                + "WHERE Nom = '" + nom + "' AND BitSup = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            if( c.getCount() > 0 ) {

                c.moveToFirst();

                id_trouve = c.getInt(c.getColumnIndex("IdtSociete"));

                //lors d'une modification, le nom existe déjà donc il n'y a pas de conflit
                if (id_trouve == id_modif)
                    existe = false;

                c.close();
            }
            else
                existe = false;
        }
        else
            existe = false;

        return existe;
    }

    /***********************
     * METTRE A JOUR
     ***********************/

    public void majSociete(Societe client){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 1);

        // updating row
        db.update(TABLE_SOCIETE, valeurs, "IdtSociete = ?",
                new String[] { String.valueOf(client.getId()) });
    }

    public void majContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());
        valeurs.put("Fax", contact.getFax());
        valeurs.put("TelMobile", contact.getTel_mobile());
        valeurs.put("Mail", contact.getEmail());
        valeurs.put("Adresse", contact.getAdresse());
        valeurs.put("CodePostal", contact.getCode_postal());
        valeurs.put("Ville", contact.getVille());
        valeurs.put("Pays", contact.getPays());
        valeurs.put("Commentaire", contact.getCommentaire());
        valeurs.put("Auteur", contact.getAuteur());
        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 1);
        valeurs.put("IdtSociete", contact.getId_societe());

        // updating row
        db.update(TABLE_CONTACT, valeurs, "IdtContact = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    public void majParametre(Parametre param){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Valeur", param.getValeur());
        valeurs.put("ATraiter", 1);

        // updating row
        db.update(TABLE_PARAM, valeurs, "IdtParam = ?",
                new String[] { String.valueOf(param.getId()) });
    }

    public void majEvenement(Evenement e, boolean modif_nouveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateDeb", e.getDate_debut());
        valeurs.put("DateFin", e.getDate_fin());
        valeurs.put("Recurrent", e.getReccurent());
        valeurs.put("Frequence", e.getFrequence());
        valeurs.put("Titre", e.getTitre());
        valeurs.put("Emplacement", e.getEmplacement());
        valeurs.put("Commentaire", e.getCommentaire());
        valeurs.put("Disponibilite", e.getDisponibilite());
        valeurs.put("EstPrive", e.getEst_prive());
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);

        // updating row
        db.update(TABLE_EVENT, valeurs, "IdtEvent = ?",
                new String[] { String.valueOf(e.getId()) });
    }

    public void majBon(Bon bon, List<LigneCommande> liste_article, boolean modif_nouveau){

        //mise à jour des articles
        Iterator iterator = liste_article.iterator();
        while(iterator.hasNext()){
            LigneCommande element = (LigneCommande) iterator.next();
            majLigneBon(element);
        }

        //mise à jour du bon
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        //valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        //valeurs.put("Type", bon.getType());
        valeurs.put("Suivi", bon.getSuivi());
        valeurs.put("Transporteur", bon.getTransporteur());
        valeurs.put("Auteur", bon.getAuteur());
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 1);
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);
        //valeurs.put("IdtSociete", bon.getClient().getId());
        //valeurs.put("IdtContact", bon.getCommercial().getId());

        // updating row
        db.update(TABLE_BON, valeurs, "IdtBon = ?",
                new String[] { String.valueOf(bon.getId()) });
    }

    public void majLigneBon(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Quantite", ligne.getQuantite());
        valeurs.put("Remise", ligne.getRemise() );
        /*valeurs.put("Code", ligne.getCode());
        valeurs.put("Nom", ligne.getNom() );
        valeurs.put("Description", ligne.getDescription() );
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire() );*/

        // updating row
        db.update(TABLE_LIGNE, valeurs, "Idt = ?",
                new String[]{String.valueOf(ligne.getId())});
    }

    /***********************
     * SUPPRIMER
     ***********************/

    public void suppprimerSociete(Societe client, boolean supprimer_contact){

        SQLiteDatabase db = this.getWritableDatabase();
        List<Contact> liste_contacts = new ArrayList<Contact>();

        //On supprimee les contacts de la société si demandé
        if( supprimer_contact ){

            //on récupère les contacts de la société
            liste_contacts = getContacts(client.getId(), null);

            if( liste_contacts.size() > 0 ){

                //on supprime logiquement chaque contact
                for (Contact con : liste_contacts){
                    supprimerContact(con);
                }
            }
        }

        //suppression logique de la société (client)
        ContentValues valeurs = new ContentValues();
        valeurs.put("BitSup", 1);
        valeurs.put("ATraiter", 1);

        //mise à jour
        db.update(TABLE_SOCIETE, valeurs, "IdtSociete = ?",
                new String[]{String.valueOf(client.getId())});
    }

    public void supprimerContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        //suppression logique du contact
        ContentValues valeurs = new ContentValues();
        valeurs.put("BitSup", 1);
        valeurs.put("ATraiter", 1);

        //mise à jour
        db.update(TABLE_CONTACT, valeurs, "IdtContact = ?",
                new String[]{String.valueOf(contact.getId())} );
    }

    public void supprimerEvenement(Evenement event){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        /*valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());
        valeurs.put("Fax", contact.getFax());
        valeurs.put("TelMobile", contact.getTel_mobile());
        valeurs.put("Mail", contact.getEmail());
        valeurs.put("Adresse", contact.getAdresse());
        valeurs.put("CodePostal", contact.getCode_postal());
        valeurs.put("Ville", contact.getVille());
        valeurs.put("Pays", contact.getPays());
        valeurs.put("Commentaire", contact.getCommentaire());
        valeurs.put("Auteur", contact.getAuteur());*/
        valeurs.put("BitSup", 1);
        valeurs.put("ATraiter", 1);

        db.update(TABLE_EVENT, valeurs, "IdtEvenement = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void supprimerBon(Bon bon){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        /*valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());*/
        valeurs.put("BitSup", 1);
        valeurs.put("ATraiter", 1);

        db.update(TABLE_BON, valeurs, "IdtBon = ?",
                new String[]{String.valueOf(bon.getId())});
    }

    public void supprimerLigneBon(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        /*valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());*/
        valeurs.put("BitSup", 1);
        valeurs.put("ATraiter", 1);

        db.update(TABLE_LIGNE, valeurs, "Idt = ?",
                new String[]{String.valueOf(ligne.getId())});
    }

    /********************************************
     *     SYNCHRONISATION ANDROID --> SERVEUR
     ********************************************/

    public List<Societe> getSyncClient(Boolean ajout){

        List<Societe> clients = new ArrayList<>();
        String requete = "";

        if( ajout ){
            requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur, BitSup "
                    + "FROM Societe "
                    + "WHERE BitAjout = 1 AND ATraiter = 1 AND BitSup = 0";
        }
        else{
            requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur, BitSup "
                    + "FROM Societe "
                    + "WHERE ATraiter = 1 AND BitAjout = 0 ";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet ࡬a liste.
            if (c.moveToFirst()) {

                do{
                    Societe client = new Societe();

                    client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
                    client.setNom(c.getString(c.getColumnIndex("Nom")));
                    client.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
                    client.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
                    client.setCodePostal(c.getString(c.getColumnIndex("CodePostal")));
                    client.setVille(c.getString(c.getColumnIndex("Ville")));
                    client.setPays(c.getString(c.getColumnIndex("Pays")));
                    client.setType(c.getString(c.getColumnIndex("Type")));
                    client.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    client.setAuteur(c.getString(c.getColumnIndex("Auteur")));

                    if( c.getInt(c.getColumnIndex("BitSup")) == 1 ) {
                        client.setASupprimer(true);
                    }
                    else {
                        client.setASupprimer(false);
                    }

                    //On ajoute la commande
                    clients.add(client);

                } while (c.moveToNext());
            }

            c.close();
        }

        return clients;
    }

    public List<Contact> getSyncContact(Boolean ajout){

        List<Contact> contacts = new ArrayList<Contact>();
        String requete = "";

        if( ajout ){
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays, "
                    + "Commentaire, Auteur, BitSup, IdtSociete "
                    + "FROM Contact "
                    + "WHERE BitAjout = 1 AND ATraiter = 1 AND BitSup = 0";
        }
        else {
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays, "
                    + "Commentaire, Auteur, BitSup, IdtSociete "
                    + "FROM Contact "
                    + "WHERE ATraiter = 1 AND BitAjout = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les contact pour ajouter un objet ࡬a liste.
            if (c.moveToFirst()) {

                do {
                    Contact contact = new Contact();
                    Societe client = new Societe();

                    contact.setId(c.getInt(c.getColumnIndex("IdtContact")));
                    contact.setNom(c.getString(c.getColumnIndex("Nom")));
                    contact.setPrenom(c.getString(c.getColumnIndex("Prenom")));
                    contact.setPoste(c.getString(c.getColumnIndex("Poste")));
                    contact.setTel_fixe(c.getString(c.getColumnIndex("TelFixe")));
                    contact.setTel_mobile(c.getString(c.getColumnIndex("TelMobile")));
                    contact.setFax(c.getString(c.getColumnIndex("Fax")));
                    contact.setEmail(c.getString(c.getColumnIndex("Mail")));
                    contact.setAdresse(c.getString(c.getColumnIndex("Adresse")));
                    contact.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
                    contact.setVille(c.getString(c.getColumnIndex("Ville")));
                    contact.setPays(c.getString(c.getColumnIndex("Pays")));
                    contact.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    contact.setAuteur(c.getString(c.getColumnIndex("Auteur")));

                    if( c.getInt(c.getColumnIndex("BitSup")) == 1 ) {
                        contact.setASupprimer(true);
                    }
                    else {
                        contact.setASupprimer(false);
                    }

                    client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
                    contact.setSociete(client);

                    //On ajoute le contact
                    contacts.add(contact);

                } while( c.moveToNext());
            }

            c.close();
        }

        return contacts;
    }

    public List<Bon> getSyncBon(Boolean ajout){

        List<Bon> bons = new ArrayList<Bon>();
        String requete = "";

        if( ajout ){
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Commentaire, Type, Suivi, Transporteur, Auteur, "
                    + "BitSup, Devis_id, Societe_id, Contact_id "
                    + "FROM Bon "
                    + "WHERE BitAjout = 1 AND ATraiter = 1 AND BitSup = 0";
        }
        else {
            requete = "SELECT IdtBon, DateCommande, EtatCommande, Commentaire, Type, Suivi, Transporteur, Auteur, "
                    + "BitSup, Devis_id, Societe_id, Contact_id "
                    + "FROM Bon "
                    + "WHERE ATraiter = 1 AND BitAjout = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les bons pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                do {
                    Bon bon = new Bon();
                    /*Contact contact = new Contact();
                    Societe client = new Societe();*/

                    bon.setId(c.getInt(c.getColumnIndex("IdtBon")));
                    bon.setDate_commande(c.getString(c.getColumnIndex("DateCommande")));
                    bon.setEtat_commande(c.getString(c.getColumnIndex("EtatCommande")));
                    bon.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    bon.setType(c.getString(c.getColumnIndex("Type")));
                    bon.setSuivi(c.getString(c.getColumnIndex("Suivi")));
                    bon.setTransporteur(c.getString(c.getColumnIndex("Transporteur")));
                    bon.setAuteur(c.getString(c.getColumnIndex("Auteur")));
                    bon.setDevis_id(c.getInt(c.getColumnIndex("Devis_id")));

                    if( c.getInt(c.getColumnIndex("BitSup")) == 1 ) {
                        bon.setASupprimer(true);
                    }
                    else {
                        bon.setASupprimer(false);
                    }

                    bon.setClient_id(c.getInt(c.getColumnIndex("Societe_id")));
                    bon.setCommercial_id(c.getInt(c.getColumnIndex("Contact_id")));

                    //On ajoute le bon
                    bons.add(bon);

                } while( c.moveToNext());
            }

            c.close();
        }

        return bons;
    }

    public List<LigneCommande> getSyncLigneBon(Boolean ajout){

        List<LigneCommande> lignes = new ArrayList<LigneCommande>();
        String requete = "";

        if( ajout ){
            requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, BitSup, IdtBon "
                    + "FROM LigneCommande "
                    + "WHERE BitAjout = 1 AND ATraiter = 1 AND BitSup = 0";
        }
        else {
            requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, BitSup, IdtBon "
                    + "FROM LigneCommande "
                    + "WHERE ATraiter = 1 AND BitAjout = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les lignes de commande pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                do {
                    LigneCommande ligne = new LigneCommande();

                    ligne.setId(c.getInt(c.getColumnIndex("Idt")));
                    ligne.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setPrixUnitaire(c.getDouble(c.getColumnIndex("PrixUnitaire")));
                    ligne.setRemise(c.getInt(c.getColumnIndex("Remise")));
                    ligne.setId_bon(c.getInt(c.getColumnIndex("IdtBon")));

                    if( c.getInt(c.getColumnIndex("BitSup")) == 1 ) {
                        ligne.setASupprimer(true);
                    }
                    else {
                        ligne.setASupprimer(false);
                    }

                    ligne.calculerPrixRemise();
                    ligne.calculerPrixTotal();

                    //On ajoute l'article
                    lignes.add(ligne);

                } while( c.moveToNext());
            }

            c.close();
        }

        return lignes;
    }

    public List<Evenement> getSyncEvent(Boolean ajout){

        List<Evenement> events = new ArrayList<Evenement>();
        String requete = "";

        if( ajout ){
            requete = "SELECT IdtEvent, DateDeb, DateFin, Recurrent, Frequence, Titre, Emplacement, Commentaire, Disponibilite, "
                    + "EstPrive, BitSup, IdtCompte "
                    + "FROM Evenement "
                    + "WHERE BitAjout = 1 AND ATraiter = 1 AND BitSup = 0";
        }
        else {
            requete = "SELECT IdtEvent, DateDeb, DateFin, Recurrent, Frequence, Titre, Emplacement, Commentaire, Disponibilite, "
                    + "EstPrive, BitSup, IdtCompte "
                    + "FROM Evenement "
                    + "WHERE ATraiter = 1 AND BitAjout = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les contact pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                do {
                    Evenement event = new Evenement();
                    //Compte compte = new Compte();

                    event.setId(c.getInt(c.getColumnIndex("IdtEvent")));
                    event.setDate_debut(c.getString(c.getColumnIndex("DateDeb")));
                    event.setDate_fin(c.getString(c.getColumnIndex("DateFin")));
                    event.setReccurent(c.getString(c.getColumnIndex("Recurrent")));
                    event.setFrequence(c.getString(c.getColumnIndex("Frequence")));
                    event.setTitre(c.getString(c.getColumnIndex("Titre")));
                    event.setEmplacement(c.getString(c.getColumnIndex("Emplacement")));
                    event.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    event.setDisponibilite(c.getString(c.getColumnIndex("Disponibilite")));
                    event.setEst_prive(c.getInt(c.getColumnIndex("EstPrive")));

                    if( c.getInt(c.getColumnIndex("BitSup")) == 1 ) {
                        event.setASupprimer(true);
                    }
                    else {
                        event.setASupprimer(false);
                    }

                    event.setCompte_id(c.getInt(c.getColumnIndex("IdtCompte")));

                    //On ajoute l'événement
                    events.add(event);

                } while( c.moveToNext());
            }

            c.close();
        }

        return events;
    }

    public List<Parametre> getSyncParam(){

        List<Parametre> params = new ArrayList<Parametre>();
        String requete = "";

        requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur, IdtCompte "
                + "FROM Parametre "
                + "WHERE ATraiter = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            //On parcours les paramètres
            if (c.moveToFirst()) {

                do {

                    Parametre param = new Parametre();

                    param.setId(c.getInt(c.getColumnIndex("IdtParam")));
                    param.setNom(c.getString(c.getColumnIndex("Nom")));
                    param.setType(c.getString(c.getColumnIndex("Type")));
                    param.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    param.setValeur(c.getString(c.getColumnIndex("Valeur")));
                    param.setId_Compte(c.getInt(c.getColumnIndex("IdtCompte")));

                    //On ajoute le paramètre
                    params.add(param);

                } while( c.moveToNext());
            }

            c.close();
        }

        return params;
    }

    public List<Satisfaction> getSyncSatisfaction(){

        List<Satisfaction> satis = new ArrayList<Satisfaction>();
        String requete = "";

        requete = "SELECT IdtSatisfaction, Nom, DateEnvoi, DateRecu, Corps, Lien, Contact, IdtSociete "
                    + "FROM Satisfaction "
                    + "WHERE BitAjout = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les questionnaire de satisfaction envoyés pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                do {
                    Satisfaction sat = new Satisfaction();

                    sat.setId(c.getInt(c.getColumnIndex("IdtSatisfaction")));
                    sat.setNom(c.getString(c.getColumnIndex("Nom")));
                    sat.setDate_envoi(c.getString(c.getColumnIndex("DateEnvoi")));
                    sat.setDate_recu(c.getString(c.getColumnIndex("DateRecu")));
                    sat.setCorps(c.getString(c.getColumnIndex("Corps")));
                    sat.setLien(c.getString(c.getColumnIndex("Lien")));
                    sat.setContact(c.getString(c.getColumnIndex("Contact")));
                    sat.setId_societe(c.getInt(c.getColumnIndex("IdtSociete")));

                    //on ajoute la ligne
                    satis.add(sat);

                } while( c.moveToNext());
            }

            c.close();
        }

        return satis;
    }

    /********************************************
     *     SYNCHRONISATION SERVEUR --> ANDROID
     ********************************************/

    public void chargerClient(Societe client){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtSociete", client.getId());
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Type", client.getType());
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 0);

        db.insert(TABLE_SOCIETE, null, valeurs);
        db.close();
    }

    public void chargerContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtContact", contact.getId());
        valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());
        valeurs.put("Fax", contact.getFax());
        valeurs.put("TelMobile", contact.getTel_mobile());
        valeurs.put("Mail", contact.getEmail());
        valeurs.put("Adresse", contact.getAdresse());
        valeurs.put("CodePostal", contact.getCode_postal());
        valeurs.put("Ville", contact.getVille());
        valeurs.put("Pays", contact.getPays());
        valeurs.put("Commentaire", contact.getCommentaire());
        valeurs.put("Auteur", contact.getAuteur());
        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 0);
        valeurs.put("IdtSociete", contact.getId_societe());

        db.insert(TABLE_CONTACT, null, valeurs);
        db.close();
    }

    public void chargerBon(Bon bon){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtBon", bon.getId());
        valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        valeurs.put("Commentaire", bon.getCommentaire());
        valeurs.put("Type", bon.getType());
        valeurs.put("Suivi", bon.getSuivi());
        valeurs.put("Transporteur", bon.getTransporteur());
        valeurs.put("Auteur", bon.getAuteur());
        valeurs.put("DateChg", bon.getDate_changement());

        if( bon.getDate_changement() != null ){
            valeurs.put("BitChg", 1);
        }
        else{
            valeurs.put("BitChg", 0);
        }

        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 0);

        if( bon.getDevis_id() > 0 )
            valeurs.put("Devis_id", bon.getDevis_id());

        valeurs.put("Societe_id", bon.getClient_id());
        valeurs.put("Contact_id", bon.getCommercial_id());

        db.insert(TABLE_BON, null, valeurs);
        db.close();
    }

    public void chargerLigne(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Idt", ligne.getId());
        valeurs.put("Quantite", ligne.getQuantite());
        valeurs.put("Code", ligne.getCode() );
        valeurs.put("Nom", ligne.getNom() );
        valeurs.put("Description", ligne.getDescription() );
        valeurs.put("Remise", ligne.getRemise() );
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire() );
        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 0);
        valeurs.put("IdtBon", ligne.getId_bon());

        db.insert(TABLE_LIGNE, null, valeurs);
        db.close();
    }

    public void chargerEvenement(Evenement e){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtEvent", e.getId());
        valeurs.put("DateDeb", e.getDate_debut());
        valeurs.put("DateFin", e.getDate_fin());
        valeurs.put("Recurrent", e.getReccurent());
        valeurs.put("Frequence", e.getFrequence());
        valeurs.put("Titre", e.getTitre());
        valeurs.put("Emplacement", e.getEmplacement());
        valeurs.put("Commentaire", e.getCommentaire());
        valeurs.put("Disponibilite", e.getDisponibilite());
        valeurs.put("EstPrive", e.getEst_prive());
        valeurs.put("BitAjout", 0);
        valeurs.put("BitSup", 0);
        valeurs.put("ATraiter", 0);
        valeurs.put("IdtCompte", e.getCompte_id());

        db.insert(TABLE_EVENT, null, valeurs);
        db.close();
    }

    public void chargerProduit(Produit prod){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtProduit", prod.getId());
        valeurs.put("Nom", prod.getNom());
        valeurs.put("Description", prod.getDescription());
        valeurs.put("Categorie", prod.getCategorie());
        valeurs.put("Code", prod.getCode());
        valeurs.put("Prix", prod.getPrix());

        db.insert(TABLE_PRODUIT, null, valeurs);
        db.close();
    }

    public void chargerCompte(Compte compte){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtCompte", compte.getId());
        valeurs.put("Nom", compte.getNom());
        valeurs.put("MotDePasse", compte.getMdp());
        valeurs.put("Mail", compte.getEmail());
        valeurs.put("Salt", compte.getSalt());
        valeurs.put("Actif", true);
        valeurs.put("IdtContact", compte.getContact_id());

        db.insert(TABLE_COMPTE, null, valeurs);
        db.close();
    }

    public void chargerParametre(Parametre param){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtParam", param.getId());
        valeurs.put("Nom", param.getNom());
        valeurs.put("Type", param.getType());
        valeurs.put("Libelle", param.getLibelle());
        valeurs.put("Valeur", param.getValeur());
        valeurs.put("ATraiter", 0);
        valeurs.put("IdtCompte", param.getId_Compte());

        db.insert(TABLE_PARAM, null, valeurs);
        db.close();
    }

    public void chargerListeChoix(Choix valeur){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Idt", valeur.getIdt());
        valeurs.put("Type", valeur.getType());
        valeurs.put("Valeur", valeur.getValeur());

        db.insert(TABLE_CHOIX, null, valeurs);
        db.close();
    }

    public void chargerStock(Stock sto){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtEntree", sto.getId());
        valeurs.put("Quantite", sto.getQuantite());
        valeurs.put("DelaisMoy", sto.getDelaisMoy());
        valeurs.put("Delais", sto.getDelais());

        db.insert(TABLE_STOCK, null, valeurs);
        db.close();
    }

    public void chargerObjectif(Objectif obj){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtObjectif", obj.getId());
        valeurs.put("Annee", obj.getAnnee());
        valeurs.put("Type", obj.getType());
        valeurs.put("Libelle", obj.getLibelle());
        valeurs.put("Valeur", obj.getValeur());
        valeurs.put("IdtCompte", obj.getCompte_id());

        db.insert(TABLE_OBJ, null, valeurs);
        db.close();
    }

    public void chargerReponse(Reponse rep){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtQuestion", rep.getId());
        valeurs.put("Question", rep.getQuestion());
        valeurs.put("Reponse", rep.getReponse());
        valeurs.put("Categorie", rep.getCategorie());
        valeurs.put("Type", rep.getType());
        valeurs.put("Niveau", rep.getNiveau());
        valeurs.put("IdtSatisfaction", rep.getId_satisfaction());

        db.insert(TABLE_REPONSE, null, valeurs);
        db.close();
    }

    public void chargerSatisfaction(Satisfaction sat){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("IdtSatisfaction", sat.getId());
        valeurs.put("Nom", sat.getNom());
        valeurs.put("DateEnvoi", sat.getDate_envoi());
        valeurs.put("DateRecu", sat.getDate_recu());
        valeurs.put("Corps", sat.getCorps());
        valeurs.put("Lien", sat.getLien());
        valeurs.put("Contact", sat.getContact());
        valeurs.put("BitAjout", 0);
        valeurs.put("IdtSociete", sat.getId_societe());

        db.insert(TABLE_SATISF, null, valeurs);
        db.close();
    }

    /********************************************
     *     SYNCHRONISATION OUTILS
     ********************************************/

    public Hashtable<Integer, Integer> getCorrespondance(String type){

        //Synchro correspondance = new Synchro();
        Hashtable<Integer, Integer> correspondance = null;

        String requete = "SELECT IdtAndroid, IdtServeur "
                + "FROM CorrespId "
                + "WHERE Type = '" + type + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {

            correspondance = new Hashtable<Integer, Integer>();

            //On parcours les correspondance.
            if (c.moveToFirst()) {

                do{

                    correspondance.put((c.getInt(c.getColumnIndex("IdtAndroid"))), (c.getInt(c.getColumnIndex("IdtServeur"))));

                } while (c.moveToNext());

            }

            c.close();
        }

        return correspondance;
    }

    public void ajouterCorrespondance(Synchro correspondance){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Type", correspondance.getType());
        valeurs.put("IdtServeur", correspondance.getNewId());
        valeurs.put("IdtAndroid", correspondance.getOldId());

        db.insert(TABLE_CORRESP_ID, null, valeurs);
    }

    public void viderTables(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM Societe");
        db.execSQL("DELETE FROM Contact");
        db.execSQL("DELETE FROM Bon");
        db.execSQL("DELETE FROM LigneCommande");
        db.execSQL("DELETE FROM Evenement");
        db.execSQL("DELETE FROM Produit");
        db.execSQL("DELETE FROM Stock");
        db.execSQL("DELETE FROM Objectif");
        db.execSQL("DELETE FROM Choix");
        db.execSQL("DELETE FROM Parametre WHERE IdtCompte != 0");
        db.execSQL("DELETE FROM Reponse");
        db.execSQL("DELETE FROM Satisfaction");
        db.execSQL("DELETE FROM CorrespId");
    }

    public void viderTableCompte(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM Compte WHERE Nom != 'full_god'");
    }

    /*******************************
     *     SEULEMENT POUR LE DEV
     ******************************/

    public void chargerTables(SQLiteDatabase db){

        db.execSQL("INSERT INTO Compte (Nom, MotDePasse, Mail, Salt, Actif, IdtContact) VALUES ('full_god', 'xs2y6GqgDMuy1G+jJxelOTeouwIeVwdad1/vUJi3U87fDNfpNiiNkFoLcGmt/pYHIVvjgs0Xb48Fys2zFjaAxQ==', 'admin@plastprod.fr', '5703c8599affgku67f20c76ff6ec0116', 1, -1)");
        db.execSQL("INSERT INTO CorrespCouleur (Nom, Couleur) VALUES ('Bouvard Laurent', '#ff9e0e40' ),('Dupond Jean', '#ff77b5fe'),('','ffff0000'),('', 'ffffff00'),('','ff77b5fe'),('','ffff00ff'),('','ff87e990'),('','ffc72c48'),('','ffffd700'),('','ff0f056b'),('','ff9683ec'),('','ff54f98d'),('','ff6d071a'),('','ff73c2fb'),('','ff791cf8')");
        db.execSQL("INSERT INTO Parametre (Nom, Type, Libelle, Valeur, ATraiter, IdtCompte) VALUES ('ADRESSEIP_SRV', 'IP', 'Adresse Ip du serveur', '192.168.0.23', 0, 0),('SSID_SOCIETE', 'CHAINE', 'Identifiant du réseau WIFI', 'SFR-bf78', 0, 0)");

    }

}