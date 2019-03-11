package rolu18oy.ju.se.layoutapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.Model.RestaurantAdapter;


public class Restaurants_fragment extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mAdapater;
    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Restaurant> mRestaurants;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.restaurant_fragment, null);

        mRecyclerView  = (RecyclerView) view.findViewById(rolu18oy.ju.se.layoutapp.R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mProgressCircle = view.findViewById(rolu18oy.ju.se.layoutapp.R.id.progress_circle);

        mRestaurants = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Restaurants");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurantUp = postSnapshot.getValue(Restaurant.class);
                    mRestaurants.add(restaurantUp);
                }
                mAdapater = new RestaurantAdapter(getActivity(),mRestaurants);

                mRecyclerView.setAdapter(mAdapater);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RestaurantAdapter.RecyclerItemClickListener(getContext(), mRecyclerView ,new RestaurantAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new Rest_description_fragment())
                                .commit();

                        Fragment passData  = new Fragment();
                        final Bundle bundle= new Bundle();
                        bundle.putString("RestoName:",mRestaurants.get(position).getRestaurantName());
                        bundle.putString("RestoDiscription:",mRestaurants.get(position).getDescription());
                        passData.setArguments(bundle);
                        passData.setArguments(getArguments());
                        getFragmentManager().beginTransaction().replace(R.id.Rest_description_fragment, passData).commit();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new Rest_description_fragment())
                                .commit();
                    }
                })
        );

        return view;
    }
}
