package sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String KEY_FIXE_ = "tel_fixe";
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
    private static final String KEY_PRIX_U = "prix_unitaire";
    private static final String KEY_PRIX_T = "prix_total";

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
}
