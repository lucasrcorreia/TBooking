package rolu18oy.ju.se.layoutapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import rolu18oy.ju.se.layoutapp.LoginNavActivity;
import rolu18oy.ju.se.layoutapp.Model.DatePickerFragment;
import rolu18oy.ju.se.layoutapp.NavigationActivity;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class Rest_description_fragment extends Fragment {
    View view;
    String restEmail;
    String restName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.rest_description_fragment, null);


        TextView textView = (TextView) view.findViewById(R.id.textView);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);

        Bundle b = getArguments();
        restName = b.getString("RestName");
        String restDesc = b.getString("RestDescription");
        String restProfile = b.getString("RestProfilePic");
        restEmail = b.getString("RestEmail");
        Button datepicker = (Button) view.findViewById(R.id.buttonDatePicker);

        textView.setText(restName);
        textView2.setText(restDesc);

        ImageView profilePic = (ImageView) view.findViewById(R.id.imageView6);

        Picasso.get().load(restProfile)
                .fit()
                .centerCrop()
                .into(profilePic);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!SaveSharedPreference.getLoggedStatus(getContext())) {

                    Intent a = new Intent(getActivity(), LoginNavActivity.class);
                    startActivity(a);

                }else {

                    Bundle args = new Bundle();

                    args.putString("RestEmail", restEmail);
                    args.putString("RestName", restName);


                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "datePicker");
                }

            }
        });


        return view;
    }


}
