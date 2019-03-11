package rolu18oy.ju.se.layoutapp.TestHomePage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import rolu18oy.ju.se.layoutapp.Model.Upload;
import rolu18oy.ju.se.layoutapp.R;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private List<Upload> muploads;

    public RestaurantAdapter(Context context , List<Upload> uploads){
        mContext = context;
        muploads = uploads;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item,viewGroup );
        //View v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item, false);

        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        Upload uploadCurrent = muploads.get(position);
        restaurantViewHolder.restViewName.setText(uploadCurrent.getName());
        Picasso.get().load(uploadCurrent.getImageUrl())
                .centerInside()
                .into(restaurantViewHolder.restImageView);
    }

    @Override
    public int getItemCount() {

        return muploads.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{

        public TextView restViewName;
        //public TextView restviewDescription;
        public ImageView restImageView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            restViewName = itemView.findViewById(R.id.text_view_name);
            //restviewDescription = itemView.findViewById(R.id.Rest_view_Description);
            restImageView = itemView.findViewById(R.id.image_view_upload);

        }
    }
}
