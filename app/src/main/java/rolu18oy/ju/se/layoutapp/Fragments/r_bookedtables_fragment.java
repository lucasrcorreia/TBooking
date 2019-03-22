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

import rolu18oy.ju.se.layoutapp.Model.BookedTableAdapter;
import rolu18oy.ju.se.layoutapp.Model.DeleteDialog_bookedtables;
import rolu18oy.ju.se.layoutapp.Model.DeleteDialog_freetables;
import rolu18oy.ju.se.layoutapp.Model.FreeTableAdapter;
import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.Model.Restaurant_bookings;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class r_bookedtables_fragment extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    private BookedTableAdapter mAdapater;
    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Restaurant_bookings> mRestaurants;
    List<String> booking_id;

    String Email;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.r_bookedtables_fragment, null);

        Email = SaveSharedPreference.getLoggedEmail(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(rolu18oy.ju.se.layoutapp.R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        booking_id = new ArrayList<>();


        mProgressCircle = view.findViewById(rolu18oy.ju.se.layoutapp.R.id.progress_circle);

        mRestaurants = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RestaurantsBooking/"+Email.replace(".",","));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRestaurants.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

                        Restaurant_bookings restaurantUp = postSnapshot2.getValue(Restaurant_bookings.class);
                        mRestaurants.add(restaurantUp);
                        booking_id.add(restaurantUp.getId());
                    }

                }

                mAdapater = new BookedTableAdapter(getActivity(), mRestaurants);

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
                new BookedTableAdapter.RecyclerItemClickListener(getContext(), mRecyclerView, new BookedTableAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        Bundle a = new Bundle();

                        a.putString("bookingID",booking_id.get(position));

                        // do whatever
                        DialogFragment dialog = new DeleteDialog_bookedtables();
                        dialog.setArguments(a);
                        dialog.show(getActivity().getSupportFragmentManager(), "dialog_date2");
                    }
                })
        );

        return view;
    }
}
