package mahsa.com.onlineresturauntbookingsystem.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.model.Rating;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateUSFragment extends Fragment {

    public static final String RESTAURANT_KEY="restaurant_key";

    private RatingBar mRatingBar;
    private EditText mEditText;
    private Button mButton;

    private String res_key;

    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabaseReference;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;


    public RateUSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        res_key=getArguments().getString(RESTAURANT_KEY);

        View view=inflater.inflate(R.layout.fragment_rate_u, container, false);

        mProgress= new ProgressDialog(getActivity());

        //FIREBASE INITIALIZATION
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Rating").child(res_key);
        mUserDatabaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());




        mRatingBar=(RatingBar)view.findViewById(R.id.fragment_rate_us_rating_bar);
        mEditText=(EditText)view.findViewById(R.id.fragment_rate_us_edit_text);

        mButton=(Button)view.findViewById(R.id.fragment_rate_us_submit_btn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRatingOnDatabse();
            }
        });


        return view;
    }

    private void updateRatingOnDatabse(){

        final String desciption=mEditText.getText().toString();
        final String rate=Float.toString(mRatingBar.getRating());

        if(!TextUtils.isEmpty(desciption) && !TextUtils.isEmpty(rate)){

            mProgress.setMessage("Posting....");
            mProgress.show();

            mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String username= dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString() ;

                    Rating rating =new Rating(username,rate,desciption,image);


                    DatabaseReference newRate=  mDatabase.child(mAuth.getCurrentUser().getUid());
                    newRate.setValue(rating, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){
                                Toast.makeText(getActivity(),"Something went wrong!!Try rating again",Toast.LENGTH_SHORT).show();
                            }
                            else{


                                Toast.makeText(getActivity(),"You have successfully rated our restaurant",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    mProgress.dismiss();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
        else{

            Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }

    }

}
















