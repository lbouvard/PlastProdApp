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
import java.util.Iterator;
import java.util.List;

/**
 * Created by Laurent on 10/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Nom de l'utilisateur en cours
    private String Auteur = "";

    //logcat tag
    private static final String log = "DatabaseHelper";

    //version de la base
    private static final int DATABASE_VERSION = 2;

    //nom de la base
    private static final String DATABASE_NAME = "DB_PLASTPROD";

    //nom des tables
    private static final String TABLE_BON = "Bon";
    private static final String TABLE_COM = "Commentaire";
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

    private static final String CREATE_TABLE_SOCIETE = "CREATE TABLE Societe (IdtSociete INTEGER PRIMARY_KEY, Nom TEXT NOT NULL,"
            + "Adresse1 TEXT NOT NULL, Adresse2 TEXT, CodePostal TEXT NOT NULL, Ville TEXT NOT NULL , Pays TEXT NOT NULL, Type TEXT NOT NULL, Commentaire  TEXT,"
            + "Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL)";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE Contact(IdtContact INTEGER PRIMARY KEY, Nom TEXT NOT NULL,"
            + "Prenom TEXT NOT NULL, Poste TEXT, TelFixe TEXT, Fax TEXT, TelMobile TEXT, Mail  TEXT, Adresse TEXT, CodePostal  TEXT, Ville TEXT, Pays  TEXT,"
            + "Commentaire TEXT, Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte) )";

    private static final String CREATE_TABLE_COMPTE= "CREATE TABLE Compte(IdtCompte INTEGER PRIMARY KEY, Nom TEXT NOT NULL,"
            + "MotDePasse  TEXT NOT NULL, Mail TEXT NOT NULL, Salt TEXT NOT NULL, Actif INTEGER NOT NULL, IdtContact INTEGER NOT NULL)";

    private static final String CREATE_TABLE_BON = "CREATE TABLE Bon(IdtBon INTEGER PRIMARY KEY, DateCommande TEXT NOT NULL,"
            + "EtatCommande TEXT, Type TEXT, Suivi TEXT NOT NULL, Transporteur TEXT NOT NULL, Auteur TEXT NOT NULL, DateChg TEXT, BitChg  INTEGER NOT NULL,"
            + "BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, IdtContact INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtContact) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_STOCK = "CREATE TABLE Stock(IdtEntree INTEGER PRIMARY KEY,"
            + "Quantite INTEGER NOT NULL, DelaisMoy INTEGER NOT NULL, Delais INTEGER NOT NULL)";

    private static final String CREATE_TABLE_PRODUIT = "CREATE TABLE Produit(IdtProduit INTEGER PRIMARY KEY,"
            + "Nom TEXT NOT NULL, Description TEXT NOT NULL, Categorie TEXT NOT NULL, Code TEXT NOT NULL,"
            + "Prix REAL NOT NULL, IdtEntree INTEGER NOT NULL,"
            + "FOREIGN KEY(IdtEntree) REFERENCES Stock(IdtEntree))";

    private static final String CREATE_TABLE_OBJ = "CREATE TABLE Objectif(IdtObjectif INTEGER PRIMARY KEY,"
            + "Annee TEXT NOT NULL, Type  TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, BitAjout INTEGER NOT NULL,"
            + "BitModif INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_PARAM = "CREATE TABLE Parametre(IdtParam INTEGER PRIMARY KEY,"
            + "Nom TEXT NOT NULL, Type TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, BitAjout INTEGER NOT NULL,"
            + "BitModif INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_COM = "CREATE TABLE Commentaire(IdtCommentaire INTEGER PRIMARY KEY,"
            + "Texte TEXT NOT NULL, IdtSociete INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_REPONSE = "CREATE TABLE Reponse(IdtQuestion INTEGER PRIMARY KEY,"
            + "Question TEXT NOT NULL, Reponse TEXT NOT NULL, Categorie TEXT NOT NULL, IdtSatisfaction INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSatisfaction) REFERENCES SatisfactionQ(IdtSatisfaction))";

    private static final String CREATE_TABLE_SAIISF = "CREATE TABLE SatisfactionQ(IdtSatisfaction INTEGER PRIMARY KEY,"
            + "Nom TEXT, DateEnvoi TEXT NOT NULL, DateRecu TEXT, IdtSociete INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE Evenement(IdtEvent INTEGER PRIMARY KEY, DateDeb TEXT NOT NULL,"
            + "DateFin TEXT NOT NULL, Recurrent TEXT, Frequence TEXT, Titre TEXT NOT NULL, Emplacement TEXT NOT NULL, Commentaire TEXT NOT NULL,"
            + "Disponibilite TEXT NOT NULL, EstPrive INTEGER NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtCompte  INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_LIGNE = "CREATE TABLE Ligne_commande(Idt INTEGER PRIMARY KEY, Quantite INTEGER NOT NULL,"
            + "Code TEXT, Nom TEXT NOT NULL, Description TEXT, PrixUnitaire REAL NOT NULL, Remise REAL NOT NULL, IdtProduit  INTEGER NOT NULL,"
            + "IdtBon INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtProduit) REFERENCES Produit(IdtProduit), FOREIGN KEY (IdtBon) REFERENCES Bon(IdtBon))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_BON);
        db.execSQL(CREATE_TABLE_COM);
        db.execSQL(CREATE_TABLE_COMPTE);
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_LIGNE);
        db.execSQL(CREATE_TABLE_OBJ);
        db.execSQL(CREATE_TABLE_PARAM);
        db.execSQL(CREATE_TABLE_PRODUIT);
        db.execSQL(CREATE_TABLE_REPONSE);
        db.execSQL(CREATE_TABLE_SAIISF);
        db.execSQL(CREATE_TABLE_STOCK);
        db.execSQL(CREATE_TABLE_SOCIETE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COM);
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

        // create new tables
        onCreate(db);
    }

    /***********************
     * AJOUTER
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
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 0);
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtSociete", devis.getClient().getId());
        valeurs.put("IdtContact", devis.getCommercial().getId());

        //insertion du devis
        long devis_id = db.insert(TABLE_BON, null, valeurs);

        //ajout des articles du devis
        for(LigneCommande ligne : lignes){
            ajouterLigne(ligne, devis_id);
        }

        db.close();
    }

    //Ajouter un bon de commande
    public long ajouterBonCommande(Bon bon, LigneCommande[] lignes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        valeurs.put("Type", "DE");
        valeurs.put("Suivi", "");
        valeurs.put("Transporteur", "");
        valeurs.put("Auteur", bon.getAuteur());
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 0);
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtSociete", bon.getClient().getId());
        valeurs.put("IdtContact", bon.getCommercial().getId());

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
        valeurs.put("Code", ligne.getCode() );
        valeurs.put("Nom", ligne.getNom() );
        valeurs.put("Description", ligne.getDescription() );
        valeurs.put("Remise", ligne.getRemise() );
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire() );
        valeurs.put("IdtProduit", ligne.getId_produit() );
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
        valeurs.put("BitModif", 0);

        db.insert(TABLE_SOCIETE, null, valeurs);
        db.close();
    }

    //Ajouter un contact
    public long ajouterContact(Contact contact, Societe client){
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
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtSociete", contact.getSociete().getId());

        return db.insert(TABLE_CONTACT, null, valeurs);
    }

    //Ajouter un parametre
    public long ajouterParametre(Parametre param){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", param.getNom());
        valeurs.put("Type", param.getType());
        valeurs.put("Libelle", param.getLibelle());
        valeurs.put("Valeur", param.getValeur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtCompte", param.getCompte().getId());

        return db.insert(TABLE_PARAM, null, valeurs);
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
        valeurs.put("IdtCompte", e.getCompte().getId());

        return db.insert(TABLE_EVENT, null, valeurs);
    }

    /***********************
     * LIRE
     ***********************/

    public List<Bon> getBons(String type, String clauseWhere) {

        List<Bon> bons = new ArrayList<Bon>();

        String requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, IdtContact, IdtSociete"
                        + "FROM " + TABLE_BON
                        + "WHERE " + clauseWhere;

        Log.e("LOG", requete);

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
                    ligne.setCommercial(getCommercial(c.getInt(c.getColumnIndex("IdtContact"))));
                    ligne.setClient(getClient(c.getInt(c.getColumnIndex("IdtSociete"))));
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
        String requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtProduit, IdtBon FROM Ligne_commande"
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
                    ligne.setId_produit(c.getInt(c.getColumnIndex("IdtProduit")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setPrixUnitaire(c.getDouble(c.getColumnIndex("PrixUnitaire")));
                    ligne.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                    ligne.setRemise(c.getDouble(c.getColumnIndex("Remise")));

                    ligne.calculerPrixRemise();
                    ligne.calculerPrixTotal();

                    lignes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return lignes;
    }

    public Societe getClient(int id_client){

        Societe client = new Societe();
        String requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur FROM Societe"
                        + "WHERE IdtSociete = " + id_client;

        Log.e("LOG", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
            client.setNom(c.getString(c.getColumnIndex("Nom")));
            client.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
            client.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
            client.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
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
        String requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                        + "Commentaire, Auteur FROM Contact"
                        + "WHERE IdtContact = " + id_commercial;

        Log.e("LOG", requete);

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

    public List<Societe> getSocietes(String clauseWhere){

        List<Societe> societes = new ArrayList<Societe>();

        String requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur FROM Societe " + clauseWhere;

        Log.e("LOG", requete);

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
                    ligne.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
                    ligne.setVille(c.getString(c.getColumnIndex("Ville")));
                    ligne.setPays(c.getString(c.getColumnIndex("Pays")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));

                    //On ajoute la commande
                    societes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return societes;
    }

    public List<Contact> getContacts(int id_societe, String clauseWhere){

        List<Contact> contacts = new ArrayList<Contact>();
        String requete = "";

        if( clauseWhere.isEmpty() ) {
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                    + "Commentaire, Auteur FROM Contact"
                    + "WHERE IdtSociete = " + id_societe;
        }
        else{
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                    + "Commentaire, Auteur FROM Contact " + clauseWhere;
        }

        Log.e("LOG", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les sociétés pour ajouter un objet à la liste.
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

    public List<Parametre> getParametres(int id_commercial){

        List<Parametre> params = new ArrayList<Parametre>();
        String requete = "";

        requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur FROM Parametre"
                + "WHERE IdtCompte = " + id_commercial;

        Log.e("LOG", requete);

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

        requete = "SELECT IdtObjectif, Annee, Type, Libelle, Valeur FROM Objectif"
                + "WHERE IdtCompte = " + id_commercial;

        Log.e("LOG", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les objectifs pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {
                    Objectif ligne = new Objectif();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtObjectif")));
                    ligne.setAnnee(c.getString(c.getColumnIndex("Annee")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    ligne.setValeur(c.getString(c.getColumnIndex("Valeur")));

                    //On ajoute un élément des objectifs
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

        requete = "SELECT IdtProduit, Nom, Description, Categorie, Code, Prix, IdtEntree FROM Produit";

        Log.e("LOG", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {
                    Produit ligne = new Produit();

                    ligne.setId(c.getInt(c.getColumnIndex("IdtProduit")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setCategorie(c.getString(c.getColumnIndex("Categorie")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setPrix(c.getDouble(c.getColumnIndex("Prix")));
                    ligne.setEntree(getInfoStock(c.getInt(c.getColumnIndex("IdtEntree"))));

                    //On ajoute un élément des objectifs
                    produits.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return produits;
    }

    public Stock getInfoStock(int id_entree){

        Stock info = new Stock();
        String requete = "";

        requete = "SELECT Quantite, DelaisMoy, Delais FROM Stock"
                + "WHERE IdtEntree = " + id_entree;

        Log.e("LOG", requete);

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

    /* Pour l'utilisateur de l'application. */

    public String getSalt(String login){

        String salt = "";
        String requete = "SELECT Salt FROM Contact "
                + "WHERE Nom = " + login;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            salt = c.getString(c.getColumnIndex("Salt"));

            c.close();
        }

        return salt;
    }

    public Contact verifierIdentifiantCommercial(String login, String motDePasse){

        Contact info = new Contact();
        int id_contact = -1;

        String requete = "SELECT IdtCompte FROM Contact "
                        + "WHERE Nom = " + login + " And MotDePasse = " + motDePasse;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();
            id_contact = c.getInt(c.getColumnIndex("IdtCompte"));
            c.close();

            info = getCommercial(id_contact);

            //authentification réussie, on mémorise localement l'utilisateur
            this.Auteur = info.getPrenom() + " " + info.getNom();
        }
        else
            info = null;

        return info;
    }

    /*Pour la satisfaction cliente : A faire.*/

    /***********************
     * METTRE A JOUR
     ***********************/

    public void majSociete(Societe client, boolean modif_nouveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", this.Auteur);
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);

        // updating row
        db.update(TABLE_SOCIETE, valeurs, "IdtSociete = ?",
                new String[] { String.valueOf(client.getId()) });
    }

    public void majContact(Contact contact, boolean modif_nouveau){

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
        valeurs.put("Auteur", this.Auteur);
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);
        valeurs.put("IdtSociete", contact.getSociete().getId());

        // updating row
        db.update(TABLE_CONTACT, valeurs, "IdtContact = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    public void majParametre(Parametre param, boolean modif_nouveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", param.getNom());
        valeurs.put("Type", param.getType());
        valeurs.put("Libelle", param.getLibelle());
        valeurs.put("Valeur", param.getValeur());
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);

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
        valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        valeurs.put("Type", bon.getType());
        valeurs.put("Suivi", bon.getSuivi());
        valeurs.put("Transporteur", bon.getTransporteur());
        valeurs.put("Auteur", this.Auteur);
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 1);
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);
        valeurs.put("IdtSociete", bon.getClient().getId());
        valeurs.put("IdtContact", bon.getCommercial().getId());

        // updating row
        db.update(TABLE_BON, valeurs, "IdtBon = ?",
                new String[] { String.valueOf(bon.getId()) });
    }

    public void majLigneBon(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Quantite", ligne.getQuantite());
        valeurs.put("Code", ligne.getCode() );
        valeurs.put("Nom", ligne.getNom() );
        valeurs.put("Description", ligne.getDescription() );
        valeurs.put("Remise", ligne.getRemise() );
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire() );
        valeurs.put("IdtProduit", ligne.getId_produit() );

        // updating row
        db.update(TABLE_LIGNE, valeurs, "Idt = ?",
                new String[] { String.valueOf(ligne.getId()) });
    }

    /***********************
     * SUPPRIMER
     ***********************/

    public void suppprimerSociete(Societe client, boolean supprimer_contact){

        SQLiteDatabase db = this.getWritableDatabase();

        if( supprimer_contact ){
            db.delete(TABLE_CONTACT, "IdtSociete = ?",
                    new String[] { String.valueOf(client.getId()) });
        }

        db.delete(TABLE_SOCIETE, "IdtSociete = ?",
                new String[]{String.valueOf(client.getId())});
    }

    public void supprimerContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACT, "IdtContact = ?",
                new String[]{String.valueOf(contact.getId())} );
    }

    public void supprimerEvenement(Evenement event){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EVENT, "IdtEvenement = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void supprimerBon(Bon bon){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BON, "IdtBon = ?",
                new String[]{String.valueOf(bon.getId())});
    }

    public void supprimerLigneBon(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LIGNE, "Idt = ?",
                new String[]{String.valueOf(ligne.getId())});
    }
}
