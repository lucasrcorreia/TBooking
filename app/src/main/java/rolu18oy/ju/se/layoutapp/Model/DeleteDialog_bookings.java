package rolu18oy.ju.se.layoutapp.Model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteDialog_bookings extends DialogFragment {
    String BookingID;
    FirebaseDatabase database;
    DatabaseReference Boo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle a = new Bundle();
        a = getArguments();
        BookingID =    a.getString("BookingID");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to cancel your booking")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        database = FirebaseDatabase.getInstance();
                        Boo = database.getReference("RestaurantsBooking");

                        Boo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()){
                                        for (DataSnapshot postSnapshot3 : postSnapshot2.getChildren()){
                                            Restaurant_bookings restaurant_bookings = postSnapshot3.getValue(Restaurant_bookings.class);
                                            if (restaurant_bookings.getId().equals(BookingID)){
                                                postSnapshot3.getRef().removeValue();
                                            }
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Fragment frg = null;
                        frg = getFragmentManager().findFragmentByTag("booked_fragment");
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}