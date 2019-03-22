package rolu18oy.ju.se.layoutapp.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import rolu18oy.ju.se.layoutapp.R;

public class BookedTableAdapter extends RecyclerView.Adapter<BookedTableAdapter.RestaurantViewHolder> {

    private Context mContext;
    private List<Restaurant_bookings> mRestaurants;
    FirebaseDatabase database;
    DatabaseReference users;
    String Fullname;

    public BookedTableAdapter(Context context , List<Restaurant_bookings> restaurants){
        mContext = context;
        mRestaurants = restaurants;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*view v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item,viewGroup );*/
        View v = LayoutInflater.from(mContext).inflate(R.layout.table_item, viewGroup,false);

        database = FirebaseDatabase.getInstance();


        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantViewHolder restaurantViewHolder, int position) {



        final Restaurant_bookings uploadCurrent = mRestaurants.get(position);

        users = database.getReference("Users/"+uploadCurrent.getUserID());

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Fullname = user.getFirstName() +" "+ user.getLastName();

                restaurantViewHolder.restViewName.setText(Fullname+" table for: "+uploadCurrent.getNumberofpeople());
                restaurantViewHolder.restViewDescription.setText(
                        "Date: "+Integer.toString(uploadCurrent.getDay())+"/"+Integer.toString(uploadCurrent.getMonth())+"/"+Integer.toString(uploadCurrent.getYear())+" - "+Integer.toString(uploadCurrent.getHour())+":00 H");


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        /*Picasso.get().load(uploadCurrent.getImageUrl())
                //.centerInside()
                .fit()
                .centerCrop()
                .into(restaurantViewHolder.restImageView);*/
    }

    @Override
    public int getItemCount() {

        return mRestaurants.size();
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder{

        public TextView restViewName;
        //public TextView restviewDescription;
        //public ImageView restImageView;
        public TextView restViewDescription;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            restViewName = itemView.findViewById(R.id.text_view_name);
            //restImageView = itemView.findViewById(R.id.image_view_upload);
            restViewDescription = itemView.findViewById(R.id.Rest_view_Description);
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }
}
