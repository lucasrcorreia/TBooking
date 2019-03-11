package rolu18oy.ju.se.layoutapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rolu18oy.ju.se.layoutapp.NavigationActivity;
import rolu18oy.ju.se.layoutapp.R;


public class Rest_description_fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.rest_description_fragment, null);


        TextView textView = (TextView) view.findViewById(R.id.textView);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);

        Bundle b = getArguments();
        String restName = b.getString("RestName");
        String restDesc = b.getString("RestDescription");

        textView.setText(restName);
        textView2.setText(restDesc);


        return view;
    }
}
