package rolu18oy.ju.se.layoutapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rolu18oy.ju.se.layoutapp.Model.BookedRestaurantAdapter;
import rolu18oy.ju.se.layoutapp.Model.DeleteDialog_bookedtables;
import rolu18oy.ju.se.layoutapp.Model.DeleteDialog_bookings;
import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.Model.RestaurantAdapter;
import rolu18oy.ju.se.layoutapp.R;


public class Booked_fragment extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    private BookedRestaurantAdapter mAdapater;
    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Restaurant> mRestaurants;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.booked_fragment, null);

        mRecyclerView = (RecyclerView) view.findViewById(rolu18oy.ju.se.layoutapp.R.id.recycler_view);
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
                mAdapater = new BookedRestaurantAdapter(getActivity(), mRestaurants);

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
                new BookedRestaurantAdapter.RecyclerItemClickListener(getContext(), mRecyclerView, new BookedRestaurantAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                      /*  Bundle args = new Bundle();
                        args.putString("RestName", mRestaurants.get(position).getRestaurantName());
                        args.putString("RestDescription", mRestaurants.get(position).getDescription());
                        Book_description_fragment newFragment = new Book_description_fragment();
                        newFragment.setArguments(args);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, newFragment)
                                .commit();*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                        DialogFragment dialog = new DeleteDialog_bookings();
                        dialog.show(getActivity().getSupportFragmentManager(), "dialog_date3");
                    }
                })
        );

        return view;
    }
}
