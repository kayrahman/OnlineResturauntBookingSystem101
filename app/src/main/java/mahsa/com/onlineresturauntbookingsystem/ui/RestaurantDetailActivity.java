package mahsa.com.onlineresturauntbookingsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.fragments.RateUSFragment;
import mahsa.com.onlineresturauntbookingsystem.fragments.ViewPagerFragment;
import mahsa.com.onlineresturauntbookingsystem.model.Photos;

public class RestaurantDetailActivity extends AppCompatActivity {

    public static final String RESTAURANT_KEY="post_key";



    private DatabaseReference mDatabaseReference;

    private DatabaseReference mMenuDatabaseReference;
    private DatabaseReference mPhotosDatabase;

    private Button mBookTblBtn;

    private FirebaseAuth mAuth;


    private String res_key;
    private ViewPager mViewPager;

    private ImageView mImageView;
    private RecyclerView mImageRecyclerview;

    private  CollapsingToolbarLayout mCollapsingToolbarLayout;


    private String title="";

    public static final String VIEWPAGER_FRAGMENT="viewpager_fragment";
    public static final String RATEUS_FRAGMENT="rateus_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.restaurant_detail_toolbar);
        setSupportActionBar(toolbar);



        mAuth=FirebaseAuth.getInstance();

        final Intent intent=getIntent();
        res_key= intent.getStringExtra(RESTAURANT_KEY);

       // mImageView = (ImageView)findViewById(R.id.iv_ac_res_detail);

       mCollapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar);
       // mCollapsingToolbarLayout.setTitle("Title");




        mImageRecyclerview = (RecyclerView)findViewById(R.id.rv_av_res_detail_image_rv);
        mImageRecyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        mImageRecyclerview.setLayoutManager(linearLayoutManager);


        mPhotosDatabase = FirebaseDatabase.getInstance().getReference().child("photos").child(res_key);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants");
        mPhotosDatabase.keepSynced(true);
        mDatabaseReference.keepSynced(true);

        mDatabaseReference.child(res_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("name")) {
                    title = (String) dataSnapshot.child("name").getValue();
                    mCollapsingToolbarLayout.setTitle(title);
                    getSupportActionBar().setTitle(title);
                }else{
                    mCollapsingToolbarLayout.setTitle("Title");
                }


              /*  String image =(String) dataSnapshot.child("image").getValue();
                Picasso.with(getApplicationContext())
                        .load(image)
                        .placeholder(R.drawable.restaurant_view)
                        .into(mImageView);*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //VIEW PAGER FRAGMENT INITIALIZATION
        mViewPager=(ViewPager)findViewById(R.id.viewPager);

        ViewPagerFragment savedFragment=(ViewPagerFragment)getSupportFragmentManager().findFragmentByTag(VIEWPAGER_FRAGMENT);

        if(savedFragment==null) {
            ViewPagerFragment fragment = new ViewPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ViewPagerFragment.POST_KEY, res_key);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.place_holder, fragment,VIEWPAGER_FRAGMENT);
            fragmentTransaction.commit();

        }

        //TEST FIREBASE

//        mMenuDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Menu");
//
//        DatabaseReference newPost=mMenuDatabaseReference.child(post_key);
//
//        newPost.child("item").setValue("Nasi goreng ayam kunyit");
       // newPost.push().child("menus").setValue("Chiken Fried Rice");



/*

        mBookTblBtn=(Button)findViewById(R.id.book_tbl_btn);
        mBookTblBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference newPost= mDatabaseReference.child(post_key).child(mAuth.getCurrentUser().getUid());
                        newPost.child("time").setValue("07:30pm-09-30pm");
                        newPost.child("person").setValue("2");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailactivity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_logout){
            mAuth.signOut();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Photos,ImageHolder> imageFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Photos, ImageHolder>(
                Photos.class,
                R.layout.cardview_res_photo_horizontal,
                ImageHolder.class,
                mPhotosDatabase

        ) {
            @Override
            protected void populateViewHolder(ImageHolder viewHolder, Photos model, int position) {

                viewHolder.setImage(model.getImage());
            }
        };


        mImageRecyclerview.setAdapter(imageFirebaseRecyclerAdapter);

        }

    public static class ImageHolder extends RecyclerView.ViewHolder{


        public ImageHolder(View itemView) {
            super(itemView);

        }

        public void setImage(String imageUrl){
            ImageView imageView = (ImageView)itemView.findViewById(R.id.iv_cardview_res_photo_horizontal);

            Picasso.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.restaurant_view)
                    .into(imageView);

        }

    }




}





















