package rolu18oy.ju.se.layoutapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.Model.RestaurantTable;
import rolu18oy.ju.se.layoutapp.Model.Restaurant_bookings;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class table_select_fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
    FirebaseDatabase database;
    DatabaseReference restaurantsTable;
    DatabaseReference restaurantBooking;
    List<RestaurantTable> mRestaurants;
    List<Integer> list;
    List<String> idTables;
    Button btnSubmit;

    int i=0;

    int year_choosen;
    int month_choosen;
    int day_choosen;
    List<Integer> hours_booked;
    List<Integer> numberOfPeople;
    Spinner spinner2;
    Spinner spinner1;

    Restaurant_bookings restaurant_bookings;
    String Email;
    String restEmail;
    String RestName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.table_select_fragment, null);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        Bundle b = getArguments();
        year_choosen = b.getInt("year");
        month_choosen = b.getInt("month")+1;
        day_choosen = b.getInt("day");
        restEmail = b.getString("RestEmail");
        RestName = b.getString("RestName");


        idTables = new ArrayList<>();
        hours_booked = new ArrayList<>();

        numberOfPeople = new ArrayList<>();

        mRestaurants = new ArrayList<>();

        Email = SaveSharedPreference.getLoggedEmail(getContext());


        for (int i = 10; i < 22; i = i + 2) {
            hours_booked.add(i);
        }

        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        restaurantsTable = database.getReference("RestaurantsTables/"+restEmail.replace(".",","));

        spinner2 = (Spinner) view.findViewById(R.id.spinner1);

        restaurantsTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RestaurantTable restaurantUp = postSnapshot.getValue(RestaurantTable.class);
                    //mRestaurants.add(restaurantUp);
                    list.add(restaurantUp.getNumberOfPeople());
                    idTables.add(restaurantUp.getTableId());
                    numberOfPeople.add(restaurantUp.getNumberOfPeople());
                    count++;

                }


                ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner2.setOnItemSelectedListener(this);


        spinner1 = (Spinner) view.findViewById(R.id.spinner2);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restaurantBooking = database.getReference("RestaurantsBooking/"+restEmail.replace(".",",")+"/"+idTables.get(spinner2.getSelectedItemPosition()));

                restaurantBooking.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //restaurants.child(restaurant.getRestaurantEmail()).setValue(restaurant);
                        //String key = restaurantBooking.push().getKey();
                        String key = database.getReference().push().getKey();
                        restaurant_bookings = new Restaurant_bookings(RestName,key,numberOfPeople.get(spinner2.getSelectedItemPosition()),Email.replace(".",","),year_choosen,month_choosen,day_choosen,Integer.parseInt(spinner1.getSelectedItem().toString()));
                        restaurantBooking.child(key).setValue(restaurant_bookings);
                        Restaurants_fragment newFragment = new Restaurants_fragment ();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, newFragment)
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




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        hours_booked.clear();
        for(int i= 10;i<22;i=i+2){
            hours_booked.add(i);
        }
        restaurantBooking = database.getReference("RestaurantsBooking/"+restEmail.replace(".",",")+"/"+idTables.get(position));

        restaurantBooking.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        restaurant_bookings = postSnapshot.getValue(Restaurant_bookings.class);
                        int day = restaurant_bookings.getDay();
                        int month = restaurant_bookings.getMonth();
                        int year = restaurant_bookings.getYear();
                        if (day == day_choosen && month==month_choosen && year==year_choosen ){
                            hours_booked.remove(new Integer(restaurant_bookings.getHour()));
                        }

                    }
                }
                ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>(getActivity(),
                        android.R.layout.simple_spinner_item, hours_booked);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(dataAdapter2);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
