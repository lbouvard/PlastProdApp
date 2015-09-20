package com.plastprod.plastprodapp;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import sqlite.helper.DatabaseHelper;
import sqlite.helper.Parametre;

/**
 * Created by Laurent on 14/09/2015.
 */
public class OptQuestionnaireAdaptateur extends BaseAdapter{

    List<Parametre> liste_parm_questionnaire = new ArrayList<Parametre>();
    Context context;
    DatabaseHelper db;

    public OptQuestionnaireAdaptateur(Context context, List<Parametre> liste_param) {
        //super();
        this.context = context;
        this.liste_parm_questionnaire = liste_param;
    }

    @Override
    public int getViewTypeCount(){
        return 4;
    }

    @Override
    public int getCount() {
        return liste_parm_questionnaire.size();
    }

    @Override
    public Parametre getItem(int position) {
        return liste_parm_questionnaire.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if( convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            if( type == 1 ){
                // switch
                convertView = inflater.inflate(R.layout.liste_parm_booleen, parent, false);
            }
            else if( type == 2 ){
                // nom + valeur en cours
                convertView = inflater.inflate(R.layout.liste_parm_choix, parent, false);
            }
        }

        final Parametre param = liste_parm_questionnaire.get(position);

        TextView nom = (TextView) convertView.findViewById(R.id.id_nom_param);
        nom.setText(param.getLibelle());

        if( type == 1 ){
            // toggle button (switch)
            Switch onoff = (Switch) convertView.findViewById(R.id.id_valeur_param);
            onoff.setTag(param);

            if( param.getValeur().equals("0") )
                onoff.setChecked(false);
            else
                onoff.setChecked(true);

            onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Parametre parm = (Parametre) buttonView.getTag();

                    if( isChecked ) {
                        parm.setValeur("1");
                    }
                    else {
                        parm.setValeur("0");
                    }

                    db = new DatabaseHelper(context);
                    db.majParametre(parm);
                    db.close();
                }
            });
        }
        else{
            TextView valeur = (TextView) convertView.findViewById(R.id.id_valeur_param);
            valeur.setText(param.getValeur());
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position){
        return getType(liste_parm_questionnaire.get(position).getType());
    }

    public void majResultat(){

        //liste_parm_questionnaire = liste;
        notifyDataSetChanged();
    }

    private int getType(String type){

        int retour = 0;

        switch (type){
            case "BOOL" :
                retour = 1;
                break;

            case "CHOIX" :
                retour = 2;
                break;

            default:
                retour = 0;
        }

        return retour;
    }
}
