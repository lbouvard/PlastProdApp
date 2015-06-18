package com.plastprod.plastprodapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Laurent on 14/06/2015.
 */
public class MenuAdaptateur extends ArrayAdapter<String> {

    private final Context context;
    private final String[] valeurs;

    public MenuAdaptateur(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.valeurs = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View vueLigne = inflater.inflate(R.layout.liste_menu, parent, false);
        TextView nomModule = (TextView) vueLigne.findViewById(R.id.nom_module);

        nomModule.setText(valeurs[position]);

        return vueLigne;
    }
}
