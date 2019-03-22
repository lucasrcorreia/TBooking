package rolu18oy.ju.se.layoutapp.Model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import rolu18oy.ju.se.layoutapp.Fragments.Rest_description_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.table_select_fragment;
import rolu18oy.ju.se.layoutapp.R;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String restEmail;
    String RestName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle b = getArguments();
        restEmail = b.getString("RestEmail");
        RestName = b.getString("RestName");


        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());

        // Create a new instance of DatePickerDialog and return it
        return mDatePicker;

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Bundle args = new Bundle();

        args.putInt("year",year);
        args.putInt("month",month);
        args.putInt("day",day);
        args.putString("RestEmail",restEmail);
        args.putString("RestName",RestName);




        table_select_fragment newFragment = new table_select_fragment ();
        newFragment.setArguments(args);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .commit();
    }
}