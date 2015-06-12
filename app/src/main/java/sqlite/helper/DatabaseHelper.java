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
import java.util.List;

/**
 * Created by Laurent on 10/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //logcat tag
    private static final String log = "DatabaseHelper";

    //version de la base
    private static final int DATABASE_VERSION = 1;

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

    private static final String CREATE_TABLE_SOCIETE = "CREATE TABLE Societe (IdtSociete INTEGER PRIMARY_KEY autoincrement NOT NULL, Nom TEXT NOT NULL,"
            + "Adresse1 TEXT NOT NULL, Adresse2 TEXT, CodePostal TEXT NOT NULL, Ville TEXT NOT NULL , Pays TEXT NOT NULL, Type TEXT NOT NULL, Commentaire  TEXT,"
            + "Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL)";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE Contact(IdtContact INTEGER PRIMARY KEY autoincrement NOT NULL, Nom TEXT NOT NULL,"
            + "Prenom TEXT NOT NULL, Poste TEXT, TelFixe TEXT, Fax TEXT, TelMobile TEXT, Mail  TEXT, Adresse TEXT, CodePostal  TEXT, Ville TEXT, Pays  TEXT,"
            + "Commentaire TEXT, Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte) )";

    private static final String CREATE_TABLE_COMPTE= "CREATE TABLE Compte(IdtCompte INTEGER PRIMARY KEY autoincrement NOT NULL, Nom TEXT NOT NULL,"
            + "MotDePasse  TEXT NOT NULL, Mail TEXT NOT NULL, Salt TEXT NOT NULL, Actif INTEGER NOT NULL)";

    private static final String CREATE_TABLE_BON = "CREATE TABLE Bon(IdtBon INTEGER PRIMARY KEY autoincrement NOT NULL, DateCommande TEXT NOT NULL,"
            + "EtatCommande TEXT, Type TEXT, Suivi TEXT NOT NULL, Transporteur TEXT NOT NULL, Auteur TEXT NOT NULL, DateChg TEXT, BitChg  INTEGER NOT NULL,"
            + "BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, IdtContact INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtContact) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_STOCK = "CREATE TABLE Stock(IdtEntree INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Quantite INTEGER NOT NULL, DateEntree TEXT, DateSortie TEXT)";

    private static final String CREATE_TABLE_PRODUIT = "CREATE TABLE Produit(IdtProduit INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "NomProduit TEXT NOT NULL, DescriptionProduit TEXT NOT NULL, CategorieProduit TEXT NOT NULL, CodeProduit TEXT NOT NULL,"
            + "PrixProduit REAL NOT NULL, IdtEntree INTEGER NOT NULL,"
            + "FOREIGN KEY(IdtEntree) REFERENCES Stock(IdtEntree))";

    private static final String CREATE_TABLE_OBJ = "CREATE TABLE Objectif(IdtObjectif INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Annee TEXT NOT NULL, Type  TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, BitAjout INTEGER NOT NULL,"
            + "BitModif INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_PARAM = "CREATE TABLE Parametre(IdtParam INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Nom TEXT NOT NULL, Type TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, BitAjout INTEGER NOT NULL,"
            + "BitModif INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_COM = "CREATE TABLE Commentaire(IdtCommentaire INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Texte TEXT NOT NULL, IdtSociete INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_REPONSE = "CREATE TABLE Reponse(IdtQuestion INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Question TEXT NOT NULL, Reponse TEXT NOT NULL, Categorie TEXT NOT NULL, IdtSatisfaction INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSatisfaction) REFERENCES SatisfactionQ(IdtSatisfaction))";

    private static final String CREATE_TABLE_SAIISF = "CREATE TABLE SatisfactionQ(IdtSatisfaction INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Nom TEXT, DateEnvoi TEXT NOT NULL, DateRecu TEXT, IdtSociete INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE Evenement(IdtEvent INTEGER PRIMARY KEY autoincrement NOT NULL, DateDeb TEXT NOT NULL,"
            + "DateFin TEXT NOT NULL, Recurrent TEXT, Frequence TEXT, Titre TEXT NOT NULL, Emplacement TEXT NOT NULL, Commentaire TEXT NOT NULL,"
            + "Disponibilite TEXT NOT NULL, EstPrive INTEGER NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtCompte  INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_LIGNE = "CREATE TABLE Ligne_commande(Idt INTEGER PRIMARY KEY autoincrement NOT NULL, Quantite INTEGER NOT NULL,"
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
    public long ajouterDevis(Bon devis, LigneCommande[] lignes){
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

        return devis_id;
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
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire().doubleValue() );
        valeurs.put("IdtProduit", ligne.getId_produit() );
        valeurs.put("IdtBon", bon_id );

        long ligne_id = db.insert(TABLE_LIGNE, null, valeurs);

        return ligne_id;
    }

    //Ajouter un client
    public long ajouterClient(Societe client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Type", client.getType());
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);

        long client_id = db.insert(TABLE_SOCIETE, null, valeurs);

        return client_id;
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
        valeurs.put("IdtCompte", contact.getCompte().getId());

        long contact_id = db.insert(TABLE_CONTACT, null, valeurs);

        return contact_id;
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

        long param_id = db.insert(TABLE_PARAM, null, valeurs);

        return param_id;
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

        long event_id = db.insert(TABLE_EVENT, null, valeurs);

        return event_id;
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
        if (c.moveToFirst()) {
            do {
                Bon ligne = new Bon(type);
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

        return bons;
    }

    public List<LigneCommande> getLignesCommande(long id_bon){

        List<LigneCommande> lignes = new ArrayList<LigneCommande>();
        String requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtProduit, IdtBon FROM Ligne_commande"
                            + "WHERE IdtBon = " + id_bon ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les lignes d'articles.
        if (c.moveToFirst()){
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

        return lignes;
    }

    public Societe getClient(int id_client){

        Societe client = new Societe();
        String requete = "SELECT Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur FROM Societe"
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
        }
        else
            client = null;

        return client;
    }

    public Contact getCommercial(int id_commercial){

        Contact commercial = new Contact();
        String requete = "SELECT Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                        + "Commentaire, Auteur FROM Contact"
                        + "WHERE IdtContact = " + id_commercial;

        Log.e("LOG", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            commercial.setId(c.getInt(c.getColumnIndex("IdtSociete")));
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
        }
        else
            commercial = null;

        return commercial;
    }

    public List<Societe> getSocietes(String type, String clauseWhere){

        List<Societe> societes = new ArrayList<Societe>();

        String requete = "SELECT Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur FROM Societe " + clauseWhere;

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
        }

        return societes;
    }

    
    /***********************
     * METTRE A JOUR
     ***********************/


    /***********************
     * SUPPRIMER
     ***********************/

}
