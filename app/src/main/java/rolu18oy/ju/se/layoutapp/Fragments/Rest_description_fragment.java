package rolu18oy.ju.se.layoutapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import rolu18oy.ju.se.layoutapp.Model.DatePickerFragment;
import rolu18oy.ju.se.layoutapp.R;


public class Rest_description_fragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.rest_description_fragment, null);


        TextView textView = (TextView) view.findViewById(R.id.textView);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);

        Bundle b = getArguments();
        String restName = b.getString("RestName");
        String restDesc = b.getString("RestDescription");

        String restProfile = b.getString("RestProfilePic");


        textView.setText(restName);
        textView2.setText(restDesc);

        ImageView profilePic = (ImageView) view.findViewById(R.id.imageView6);

        Picasso.get().load(restProfile)
                .fit()
                .centerCrop()
                .into(profilePic);
        return view;
    }


}
