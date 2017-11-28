package mahsa.com.onlineresturauntbookingsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.model.Rating;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    public static final String KEY_POST_REVIEW_FRAGMENT="key_post";


    private DatabaseReference mDatabaseReference;

    private DatabaseReference ratingDatabaseReference;

    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;

    String post_key;

    private Button btn;




    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        post_key=getArguments().getString(KEY_POST_REVIEW_FRAGMENT);

        mAuth=FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Rating");
        ratingDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Rating").child(post_key);

        View v= inflater.inflate(R.layout.fragment_review, container, false);

//


        mRecyclerView=(RecyclerView)v.findViewById(R.id.fragment_review_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return v;

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Rating,RatingViewHolder> ratingRecyclerAdapter=new FirebaseRecyclerAdapter<Rating, RatingViewHolder>(
                Rating.class,
                R.layout.list_review,
                RatingViewHolder.class,
                ratingDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(RatingViewHolder viewHolder, Rating model, int position) {

                viewHolder.setRating(model.getRating());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setUsername(model.getUsername());

            }
        };

        mRecyclerView.setAdapter(ratingRecyclerAdapter);

    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        public RatingViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

        }

        public void setRating(String rating){
            RatingBar ratingBar=(RatingBar)mView.findViewById(R.id.fragment_review_rating_bar);
            ratingBar.setRating(Float.parseFloat(rating));

        }

        public void setDesc(String desc){
            TextView descText=(TextView)mView.findViewById(R.id.review_description);
            descText.setText(desc);
        }

        public void setUsername(String username){
            TextView usernameText =(TextView)mView.findViewById(R.id.fragment_review_rater);
            usernameText.setText("By "+username);

        }


    }




}
