package rolu18oy.ju.se.layoutapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rolu18oy.ju.se.layoutapp.Model.RestaurantTable;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class create_table_fragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference RestautantsTables;
    int selectedItem;
    SharedPreferences sp;
    String Email;
    Spinner spinner1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.r_create_table_fragment, null);
        Email = SaveSharedPreference.getLoggedEmail(getContext());
        database = FirebaseDatabase.getInstance();
        RestautantsTables = database.getReference("RestaurantsTables/"+Email.replace(".",","));
        sp = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        Button btnCreateTable = (Button) view.findViewById(R.id.btnSubmit);


        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);
        list2.add(7);
        list2.add(8);
        list2.add(9);
        list2.add(10);

        ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter2);
        btnCreateTable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedItem = Integer.parseInt(spinner1.getSelectedItem().toString());
                RestautantsTables.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = database.getReference().push().getKey();
                        RestaurantTable restaurantTable = new RestaurantTable(key,selectedItem,Email);
                        RestautantsTables.child(key).setValue(restaurantTable);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.R_fragment_container, new r_freetables_fragment())
                                .commit();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }



                });
            }
        });

        return view;
    }
}
