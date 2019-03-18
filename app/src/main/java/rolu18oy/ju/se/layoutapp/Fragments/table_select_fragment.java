package rolu18oy.ju.se.layoutapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rolu18oy.ju.se.layoutapp.R;


public class table_select_fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.table_select_fragment, null);

        Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("9am");
        list.add("10am");
        list.add("11am");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        List<String> list2 = new ArrayList<String>();
        list2.add("table 1 - 2 people");
        list2.add("table 2 - 2 people");
        list2.add("table 3 - 5 people");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter2);

        return view;
    }
}
