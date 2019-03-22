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

import rolu18oy.ju.se.layoutapp.Model.DeleteDialog_freetables;
import rolu18oy.ju.se.layoutapp.Model.FreeTableAdapter;
import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.Model.RestaurantTable;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class r_freetables_fragment extends Fragment {
    View view;
    private RecyclerView mRecyclerView;
    private FreeTableAdapter mAdapater;
    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<RestaurantTable> mRestaurants;
    String Email;
    List<String> tableID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.r_freetables_fragment, null);

        mRecyclerView = (RecyclerView) view.findViewById(rolu18oy.ju.se.layoutapp.R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Email = SaveSharedPreference.getLoggedEmail(getContext());

        mProgressCircle = view.findViewById(rolu18oy.ju.se.layoutapp.R.id.progress_circle);

        tableID = new ArrayList<>();

        mRestaurants = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RestaurantsTables/"+Email.replace(".",","));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRestaurants.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RestaurantTable restaurantUp = postSnapshot.getValue(RestaurantTable.class);
                    mRestaurants.add(restaurantUp);
                    tableID.add(restaurantUp.getTableId());
                }
                mAdapater = new FreeTableAdapter(getActivity(), mRestaurants);

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
                new FreeTableAdapter.RecyclerItemClickListener(getContext(), mRecyclerView, new FreeTableAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever

                        Bundle a = new Bundle();
                        a.putString("tableID",tableID.get(position));
                        DialogFragment dialog = new DeleteDialog_freetables();
                        dialog.setArguments(a);
                        dialog.show(getActivity().getSupportFragmentManager(), "dialog_date");
                    }
                })
        );

        return view;
    }
}
