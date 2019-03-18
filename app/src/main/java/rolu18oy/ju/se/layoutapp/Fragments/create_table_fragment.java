package rolu18oy.ju.se.layoutapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import rolu18oy.ju.se.layoutapp.R;


public class create_table_fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.table_select_fragment, null);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        List<String> list2 = new ArrayList<String>();
        list2.add("1 people");
        list2.add("2 people");
        list2.add("3 people");
        list2.add("4 people");
        list2.add("5 people");
        list2.add("6 people");
        list2.add("7 people");
        list2.add("8 people");
        list2.add("9 people");
        list2.add("10 people");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter2);

        return view;
    }
}
