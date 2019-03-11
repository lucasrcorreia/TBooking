package rolu18oy.ju.se.layoutapp.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rolu18oy.ju.se.layoutapp.R;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private List<Restaurant> mRestaurants;

    public RestaurantAdapter(Context context , List<Restaurant> restaurants){
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
        Restaurant uploadCurrent = mRestaurants.get(position);
        restaurantViewHolder.restViewName.setText(uploadCurrent.getRestaurantName());
        restaurantViewHolder.restViewDescription.setText(uploadCurrent.getDescription());
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
}
