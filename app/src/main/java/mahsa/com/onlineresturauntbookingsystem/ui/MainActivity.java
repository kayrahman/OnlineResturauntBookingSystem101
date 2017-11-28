package mahsa.com.onlineresturauntbookingsystem.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.model.ConnectivityReceiever;
import mahsa.com.onlineresturauntbookingsystem.model.MyApplication;
import mahsa.com.onlineresturauntbookingsystem.model.OnRestaurantListSelected;
import mahsa.com.onlineresturauntbookingsystem.model.Restaurant;
import static mahsa.com.onlineresturauntbookingsystem.ui.RestaurantDetailActivity.RESTAURANT_KEY;

public class MainActivity extends AppCompatActivity implements OnRestaurantListSelected,ConnectivityReceiever.ConnectivityRecieverListener {



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseUser;

    private RecyclerView mRecyclerView;

    private OnRestaurantListSelected mListSelected;

    private CircleImageView mCusCircleimage;
    private TextView mUserAddress;

    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListSelected=MainActivity.this;
        mCusCircleimage = (CircleImageView)findViewById(R.id.civ_app_bar_thumb_image);
        mUserAddress = (TextView)findViewById(R.id.current_city_address);


        mAuth = FirebaseAuth.getInstance();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabaseReference.keepSynced(true);
        mDatabaseUser.keepSynced(true);



        setAddress();

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_logout){
            mAuth.signOut();
        }else if(item.getItemId()==R.id.action_add){
           Intent intent = new Intent(MainActivity.this,AccountSetUpActivity.class);
           startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();

        checkUserExist();

        mAuth.addAuthStateListener(mListener);

        FirebaseRecyclerAdapter<Restaurant,RestaurantViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(
                Restaurant.class,
                R.layout.cardview_res_list,
                RestaurantViewHolder.class,
                mDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, Restaurant model, int position) {

                String key=getRef(position).getKey();

                viewHolder.key=key;
                viewHolder.setName(model.getTitle());
                viewHolder.setImage(model.getImage());
                viewHolder.mOnRestaurantListSelected=mListSelected;

            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onRestaurantItemSelected(String key) {

        Intent intent=new Intent(MainActivity.this, RestaurantDetailActivity.class);
        intent.putExtra(RESTAURANT_KEY,key);
        startActivity(intent);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnackBar(isConnected);

    }

    private void showSnackBar(boolean isConnected) {

        String message;

        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.activity_main_relative_layout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityListener(this);
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnRestaurantListSelected mOnRestaurantListSelected;

        String key;
        View mView;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mView.setOnClickListener(this);

        }

        public void setName(String name) {
            TextView nameTxt = (TextView) mView.findViewById(R.id.cardview_res_title);
            nameTxt.setText(name);
        }

        public void setImage(String url) {
            ImageView imageView = (ImageView) mView.findViewById(R.id.cardview_image);

            Picasso.with(mView.getContext())
                    .load(url)
                    .placeholder(R.drawable.restaurant)
                    .into(imageView);

        }
        @Override
        public void onClick(View view) {
           mOnRestaurantListSelected.onRestaurantItemSelected(key);
        }
    }



    private void checkUserExist() {

        if(mAuth.getCurrentUser() != null) {

            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUser.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("image")) {

                       final String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.with(MainActivity.this)
                                .load(image)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.male)
                                .into(mCusCircleimage, new Callback() {
                                    @Override
                                    public void onSuccess() {


                                    }

                                    @Override
                                    public void onError() {

                                        Picasso.with(MainActivity.this)
                                                .load(image)
                                                .placeholder(R.drawable.male)
                                                .into(mCusCircleimage);

                                    }
                                });


                    }else{
                        Intent setupIntent = new Intent(MainActivity.this, AccountSetUpActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setupIntent);
                        finish();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


    // get closest city name


    private String getCityName(double lat, double lon){

        String currentCity = " ";
        String countryName = "";


        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat,lon,1);
            if(addressList.size() > 0 ){
                currentCity = addressList.get(0).getLocality();
                countryName = addressList.get(0).getCountryName();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentCity+","+countryName;

    }

    private void setAddress(){

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                    )){

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION
                        );
            }else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION
                );
            }

        }else {

            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                try {

                    mUserAddress.setText(getCityName(location.getLatitude(), location.getLongitude()));
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"Location Not Found",Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(MainActivity.this,"Location Not Found",Toast.LENGTH_SHORT).show();
            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case MY_PERMISSION_REQUEST_LOCATION:{

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            ){


                    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location location = null;
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        try {

                            mUserAddress.setText(getCityName(location.getLatitude(), location.getLongitude()));

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"Location not found",Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        Toast.makeText(MainActivity.this,"Location Not Found",Toast.LENGTH_SHORT).show();
                    }

                }

            }else{

                    Toast.makeText(MainActivity.this,"No permission granted",Toast.LENGTH_SHORT).show();
                }
            }

        }


    }
}


















