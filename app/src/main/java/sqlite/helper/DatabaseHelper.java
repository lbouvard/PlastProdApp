package sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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

    //nom des colonnes communes
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date_modif";
    private static final String KEY_BIT = "bit_modif";
    private static final String KEY_NOM = "nom";
    private static final String KEY_TYPE = "type";
    private static final String KEY_MAIL = "email";
    private static final String KEY_CP = "code_postal";
    private static final String KEY_VILLE = "ville";
    private static final String KEY_PAYS = "pays";
    private static final String KEY_COM = "commentaire";
    private static final String KEY_DESC = "description";
    private static final String KEY_CAT = "categorie";
    private static final String KEY_LIBELLE = "libelle";
    private static final String KEY_QUANTITE = "quantite";
    private static final String KEY_VALEUR = "valeur";
    private static final String KEY_SOCIETE_ID = "id_societe";
    private static final String KEY_CONTACT_ID = "id_contact";
    private static final String KEY_COMPTE_ID = "id_compte";
    private static final String KEY_CODE = "code";

    //table Bon
    private static final String KEY_DATE_BON = "date_commande";
    private static final String KEY_ETAT_BON = "etat_commande";
    private static final String KEY_SUIVI = "suivi";
    private static final String KEY_TRANSP = "transporteur";
    private static final String KEY_DATE_CHG = "date_changement";
    private static final String KEY_CHANGE = "change";

    //table Commentaire
    private static final String KEY_TEXT = "texte";

    //table Compte
    private static final String KEY_MDP = "mdp";
    private static final String KEY_SALT = "salt";
    private static final String KEY_ACTIF = "actif";

    //table Contact
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_POSTE = "poste";
    private static final String KEY_FIXE = "tel_fixe";
    private static final String KEY_MOBILE = "tel_mobile";
    private static final String KEY_FAX = "fax";
    private static final String KEY_ADDR = "adresse";

    //table Evenement
    private static final String KEY_DATE_DEB = "date_debut";
    private static final String KEY_DATE_FIN = "date_fin";
    private static final String KEY_RECC = "reccurent";
    private static final String KEY_FREQ = "frequence";
    private static final String KEY_TITRE = "titre";
    private static final String KEY_PLACE = "emplacement";
    private static final String KEY_DISPO = "disponibilite";
    private static final String KEY_IS_PRIVE = "est_prive";

    //table LigneCommande
    private static final String KEY_PRODUIT_ID = "id_produit";
    private static final String KEY_BON_ID = "id_bon";
    private static final String KEY_REMISE = "remise";
    private static final String KEY_PRIX_U = "prixUnitaire";

    //table Objectif
    private static final String KEY_ANNEE = "annee";

    //table Produit
    private static final String KEY_PRIX = "prix";
    private static final String KEY_ENTREE_ID = "id_entree";

    //table Reponse
    private static final String KEY_SATISF = "id_satisfaction";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_REPONSE = "reponse";

    //table Satisfaction
    private static final String KEY_DATE_ENVOI = "date_envoi";
    private static final String KEY_DATE_RECU = "date_recu";

    //table Societe
    private static final String KEY_ADDR1 = "adresse1";
    private static final String KEY_ADDR2 = "adresse2";

    //table Stock
    private static final String KEY_DATE_IN = "date_entree";
    private static final String KEY_DATE_OUT = "date_sortie";

    private static final String CREATE_TABLE_SOCIETE = "CREATE TABLE Societe (IdtSociete INTEGER PRIMARY_KEY autoincrement NOT NULL, Nom TEXT NOT NULL,"
            + "Adresse1 TEXT NOT NULL, Adresse2 TEXT, CodePostal TEXT NOT NULL, Ville TEXT NOT NULL , Pays TEXT NOT NULL, Commentaire  TEXT,"
            + "DateModif TEXT, BitModif INTEGER NOT NULL)";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE Contact(IdtContact INTEGER PRIMARY KEY autoincrement NOT NULL, NomContact TEXT NOT NULL,"
            + "PrenomContact TEXT NOT NULL, Poste TEXT, TelFixe TEXT, Fax TEXT, TelMobile TEXT, Mail  TEXT, Adresse TEXT, CP  TEXT, Ville TEXT, Pays  TEXT,"
            + "Commentaire TEXT, DateModif TEXT, BitModif INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte) )";

    private static final String CREATE_TABLE_COMPTE= "CREATE TABLE Compte(IdtCompte INTEGER PRIMARY KEY autoincrement NOT NULL, Nom TEXT NOT NULL,"
            + "MotDePasse  TEXT NOT NULL, Mail TEXT NOT NULL, Salt TEXT NOT NULL, DateModif TEXT, BitModif INTEGER NOT NULL)";

    private static final String CREATE_TABLE_BON = "CREATE TABLE Bon(IdtBon INTEGER PRIMARY KEY autoincrement NOT NULL, DateCommande TEXT NOT NULL,"
            + "EtatCommande TEXT, Type TEXT, Suivi TEXT NOT NULL, Transporteur TEXT NOT NULL, DateChg TEXT, BitChg  INTEGER NOT NULL,"
            + "IdtSociete INTEGER NOT NULL, IdtContact INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtContact) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_STOCK = "CREATE TABLE Stock(IdtEntree INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Quantite INTEGER NOT NULL, DateEntree TEXT, DateSortie TEXT)";

    private static final String CREATE_TABLE_PRODUIT = "CREATE TABLE Produit(IdtProduit INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "NomProduit TEXT NOT NULL, DescriptionProduit TEXT NOT NULL, CategorieProduit TEXT NOT NULL, CodeProduit TEXT NOT NULL,"
            + "PrixProduit REAL NOT NULL, IdtEntree INTEGER NOT NULL,"
            + "FOREIGN KEY(IdtEntree) REFERENCES Stock(IdtEntree))";

    private static final String CREATE_TABLE_OBJ = "CREATE TABLE Objectif(IdtObjectif INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Annee TEXT NOT NULL, Type  TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_PARAM = "CREATE TABLE Parametre(IdtParam INTEGER PRIMARY KEY autoincrement NOT NULL,"
            + "Nom TEXT NOT NULL, Type TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, IdtCompte  INTEGER NOT NULL,"
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
            + "Disponibilite TEXT NOT NULL, EstPrive INTEGER NOT NULL, IdtCompte  INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_LIGNE = "CREATE TABLE Ligne_commande(Idt INTEGER PRIMARY KEY autoincrement NOT NULL, Quantite INTEGER NOT NULL,"
            + "Code TEXT, Nom TEXT NOT NULL, Description TEXT, PrixUnitaire REAL NOT NULL, PrixTotal REAL NOT NULL, IdtProduit  INTEGER NOT NULL,"
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
        valeurs.put(KEY_DATE_BON, devis.getDate_commande());
        valeurs.put(KEY_ETAT_BON, devis.getEtat_commande());
        valeurs.put(KEY_TYPE, "DE");
        valeurs.put(KEY_SUIVI, "");
        valeurs.put(KEY_TRANSP, "");
        valeurs.put(KEY_DATE_CHG, "");
        valeurs.put(KEY_BIT, 0);

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
        valeurs.put(KEY_DATE_BON, bon.getDate_commande());
        valeurs.put(KEY_ETAT_BON, bon.getEtat_commande());
        valeurs.put(KEY_TYPE, "CD");
        valeurs.put(KEY_SUIVI, "");
        valeurs.put(KEY_TRANSP, "");
        valeurs.put(KEY_DATE_CHG, "");
        valeurs.put(KEY_BIT, 0);

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
        valeurs.put(KEY_QUANTITE, ligne.getQuantite());
        valeurs.put(KEY_CODE, ligne.getCode() );
        valeurs.put(KEY_NOM, ligne.getNom() );
        valeurs.put(KEY_DESC, ligne.getDescription() );
        valeurs.put(KEY_REMISE, ligne.getRemise() );
        valeurs.put(KEY_PRIX_U, ligne.getPrixUnitaire().doubleValue() );
        valeurs.put(KEY_PRODUIT_ID, ligne.getId_produit() );
        valeurs.put(KEY_BON_ID, ligne.getId_bon() );

        long ligne_id = db.insert(TABLE_LIGNE, null, valeurs);

        return ligne_id;
    }

    //Ajouter un client
    public long ajouterClient(Societe client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put(KEY_NOM, client.getNom());
        valeurs.put(KEY_ADDR1, client.getAdresse1());
        valeurs.put(KEY_ADDR2, client.getAdresse2());
        valeurs.put(KEY_CP, client.getCode_postal());
        valeurs.put(KEY_VILLE, client.getVille());
        valeurs.put(KEY_PAYS, client.getPays());
        valeurs.put(KEY_COM, client.getCommentaire());
        valeurs.put(KEY_DATE, "");
        valeurs.put(KEY_BIT, 0);

        long client_id = db.insert(TABLE_SOCIETE, null, valeurs);

        return client_id;
    }

    //Ajouter un contact
    public long ajouterContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put(KEY_NOM, contact.getNom());
        valeurs.put(KEY_PRENOM, contact.getPrenom());
        valeurs.put(KEY_POSTE, contact.getPoste());
        valeurs.put(KEY_FIXE, contact.getTel_fixe());
        valeurs.put(KEY_FAX, contact.getFax());
        valeurs.put(KEY_MOBILE, contact.getTel_mobile());
        valeurs.put(KEY_MAIL, contact.getEmail());
        valeurs.put(KEY_ADDR, contact.getAdresse());
        valeurs.put(KEY_CP, contact.getCode_postal());
        valeurs.put(KEY_VILLE, contact.getVille());
        valeurs.put(KEY_PAYS, contact.getPays());
        valeurs.put(KEY_COM, contact.getCommentaire());
        valeurs.put(KEY_DATE, "");
        valeurs.put(KEY_BIT, 0);
        valeurs.put(KEY_SOCIETE_ID, contact.getId_societe());

        long contact_id = db.insert(TABLE_CONTACT, null, valeurs);

        return contact_id;
    }

    //Ajouter un parametre
    public long ajouterParametre(Parametre param){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put(KEY_NOM, param.getNom());
        valeurs.put(KEY_TYPE, param.getType());
        valeurs.put(KEY_LIBELLE, param.getLibelle());
        valeurs.put(KEY_VALEUR, param.getValeur());
        valeurs.put(KEY_COMPTE_ID, param.getId_compte());

        long param_id = db.insert(TABLE_PARAM, null, valeurs);

        return param_id;
    }

    //Ajouter un événement
    public long ajouterEvenement(Evenement e) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put(KEY_DATE_DEB, e.getDate_debut().toString());
        valeurs.put(KEY_DATE_FIN, e.getDate_fin().toString());
        valeurs.put(KEY_RECC, e.getReccurent());
        valeurs.put(KEY_FREQ, e.getFrequence());
        valeurs.put(KEY_TITRE, e.getTitre());
        valeurs.put(KEY_PLACE, e.getEmplacement());
        valeurs.put(KEY_COM, e.getCommentaire());
        valeurs.put(KEY_DISPO, e.getDisponibilite());
        valeurs.put(KEY_IS_PRIVE, e.getEst_prive());
        valeurs.put(KEY_COMPTE_ID, e.getId_compte());

        long event_id = db.insert(TABLE_EVENT, null, valeurs);

        return event_id;
    }

    /***********************
     * LIRE
     ***********************/
    /*public List<Bon> getBons(String type, String clauseWhere) {

        List<Bon> bons = new ArrayList<Bon>();

        String requete = "SELECT id, date_commande, etat_commande, suivi, transporteur"
                        + "FROM " + TABLE_BON
                        + "WHERE " + clauseWhere;

        Log.e("LOG", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les commandes pour récupérer les articles liés.
        if (c.moveToFirst()) {
            do {
                Bon ligne = new Bon(type);
                ligne.setDate_commande(); //setId(c.getInt((c.getColumnIndex(KEY_ID))));
                ligne.setEtat_commande();                    //setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                ligne.setSuivi();
                ligne.setTransporteur(); //setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                ligne.setId_contact();
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }
    }*/

    /*public List<LigneCommande> getLignesCommande(long id_bon){

        List<LigneCommande> lignes = new ArrayList<LigneCommande>();
        String selectQuery = "SELECT  * FROM ";
    }*/

    /***********************
     * METTRE A JOUR
     ***********************/


    /***********************
     * SUPPRIMER
     ***********************/

}
