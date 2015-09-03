package com.plastprod.plastprodapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Laurent on 02/09/2015.
 */
public class ChoixDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    RetourListener mListener;
    String type;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        type = getArguments().getString("Type");
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Activity activite){
        super.onAttach(activite);

        try{
            mListener = (RetourListener)activite;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activite.toString() + "doit implémenter RetourListener");
        }

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        mListener.modifierDate(date, type);
    }

    public interface RetourListener {
        public void modifierDate(GregorianCalendar date, String type);
        //public void annulerDate();
    }
}