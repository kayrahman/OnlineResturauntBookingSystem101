package mahsa.com.onlineresturauntbookingsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mahsa.com.onlineresturauntbookingsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    private DatabaseReference mDatabaseReference;
    private ListView mListView;

    String post_key;
    public static final String RESTAURANT_KEY="key_restaurant";


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
               View v= inflater.inflate(R.layout.fragment_menu, container, false);

        post_key=getArguments().getString(RESTAURANT_KEY);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Menu").child(post_key);
        mListView=(ListView)v.findViewById(R.id.list_item);


        FirebaseListAdapter<String> mAdapter=new FirebaseListAdapter<String>(getActivity(),
                String.class,
                android.R.layout.simple_list_item_1,
                mDatabaseReference
                ) {
            @Override
            protected void populateView(View v, String model, int position) {

                TextView textView=(TextView)v.findViewById(android.R.id.text1);
                textView.setText(model);

            }
        };

        mListView.setAdapter(mAdapter);


        return v;


    }

}
