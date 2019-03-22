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

import java.util.List;

import rolu18oy.ju.se.layoutapp.R;

public class BookedRestaurantAdapter extends RecyclerView.Adapter<BookedRestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private List<Restaurant_bookings> mRestaurants;

    public BookedRestaurantAdapter(Context context , List<Restaurant_bookings> restaurants){
        mContext = context;
        mRestaurants = restaurants;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*view v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item,viewGroup );*/
        View v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item, viewGroup,false);

        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        Restaurant_bookings uploadCurrent = mRestaurants.get(position);
        restaurantViewHolder.restViewName.setText(uploadCurrent.getRestname()+": booked for "+uploadCurrent.getNumberofpeople()+" person(s)");
        restaurantViewHolder.restViewDescription.setText(
                "Date: "+Integer.toString(uploadCurrent.getDay())+"/"+Integer.toString(uploadCurrent.getMonth())+"/"+Integer.toString(uploadCurrent.getYear())+" - "+Integer.toString(uploadCurrent.getHour())+":00 H");
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
