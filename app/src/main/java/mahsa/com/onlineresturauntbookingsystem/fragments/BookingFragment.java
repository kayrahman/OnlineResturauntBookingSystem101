package mahsa.com.onlineresturauntbookingsystem.fragments;


import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;

import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.model.Restaurant;
import mahsa.com.onlineresturauntbookingsystem.ui.MainActivity;
import mahsa.com.onlineresturauntbookingsystem.ui.RestaurantDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment implements OnMapReadyCallback {

    public static final String RESTAURANT_KEY="key_res";

    private Spinner mPersonSpinner;
    private Button mDateBtn;
    private Spinner mTimeSpinner;
    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar;

    private GoogleMap mGoogleMap;

    // UI ELEMENTS
    private TextView mPhone;
    private TextView mPrice;
    private TextView mCuisine;
    private TextView mHours;
    private TextView mPaymentOptions;
    private TextView mDressCode;
    private TextView mParking;
    private TextView mDescription;
    private Button confirmBookingBtn;

    //INITIALIZING FIREBASE

    private DatabaseReference mReference;
    private DatabaseReference mBookingReference;
    private DatabaseReference mNotificationRef;
    private DatabaseReference mDatabaseUser;


    private FirebaseAuth mAuth;

    String key_res="";
    String key_manager="";



    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth=FirebaseAuth.getInstance();

         key_res=getArguments().getString(RESTAURANT_KEY);
      //  Toast.makeText(getActivity(),post_key,Toast.LENGTH_LONG).show();


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_booking, container, false);

        mPersonSpinner=(Spinner)view.findViewById(R.id.fragment_booking_person_spinner);
       mDateBtn=(Button) view.findViewById(R.id.fragment_booking_date_btn);
        mTimeSpinner=(Spinner)view.findViewById(R.id.fragment_booking_time_spinner);



        //POPULATING DATA ON SPINNERS
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.spinner_person));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPersonSpinner.setAdapter(myAdapter);

        ArrayAdapter<String> timeAdapter=new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.spinner_time));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(timeAdapter);

        // INITIALIZING CALENDER VIEW


        mCalendar=Calendar.getInstance();
        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment=new SelectDateFragment();
                dialogFragment.show(getFragmentManager(),"DatePicker");
            }
        });


        //INITIALIZING MAP VIEW

        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //INITIALIZING UI ELEMENTS
        mPhone=(TextView)view.findViewById(R.id.fragment_booking_phone);
        mPrice=(TextView)view.findViewById(R.id.fragment_booking_price);
        mCuisine=(TextView)view.findViewById(R.id.fragment_booking_cuisine);
        mHours=(TextView)view.findViewById(R.id.fragment_booking_hour);
        mPaymentOptions=(TextView)view.findViewById(R.id.fragment_booking_payment_options);
        mDressCode=(TextView)view.findViewById(R.id.fragment_booking_dress_code);
        mParking=(TextView)view.findViewById(R.id.fragment_booking_parking);
        mDescription=(TextView)view.findViewById(R.id.fragment_booking_description);
        confirmBookingBtn=(Button)view.findViewById(R.id.fragment_booking_confirm_btn);

        //INITIALIZING FIREBASE
        mReference= FirebaseDatabase.getInstance().getReference().child("restaurants");
        mBookingReference=FirebaseDatabase.getInstance().getReference().child("booking");
        mNotificationRef=FirebaseDatabase.getInstance().getReference().child("notifications");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        if(key_res!=null) {

            mReference.child(key_res).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String phone = (String) dataSnapshot.child("phone").getValue();
                    String cuisine = (String) dataSnapshot.child("cuisine").getValue();
                    String price = (String) dataSnapshot.child("price").getValue();
                    String hours = (String) dataSnapshot.child("hours").getValue();
                    String paymentOptions = (String) dataSnapshot.child("payment_options").getValue();
                    String dressCode = (String) dataSnapshot.child("dress_code").getValue();
                    String parking = (String) dataSnapshot.child("parking").getValue();
                    String desc = (String) dataSnapshot.child("description").getValue();
                    key_manager=(String)dataSnapshot.child("manager_key").getValue();


                    mPhone.setText(phone);
                    mPrice.setText(price);
                    mCuisine.setText(cuisine);
                    mHours.setText(hours);
                    mPaymentOptions.setText(paymentOptions);
                    mDressCode.setText(dressCode);
                    mParking.setText(parking);
                    mDescription.setText(desc);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        //SET LISTENERS

        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateBookingDetailInFirebase();


                HashMap<String,String> notificationData = new HashMap<String, String>();
                notificationData.put("from",mAuth.getCurrentUser().getUid());
                notificationData.put("type","booking");

                mNotificationRef.child(key_res).push().setValue(notificationData);

            }
        });


        return view;
    }

    //

    private void updateBookingDetailInFirebase() {

        if(!TextUtils.isEmpty(mPersonSpinner.getSelectedItem().toString()) && !TextUtils.isEmpty(mTimeSpinner.getSelectedItem().toString()) &&!TextUtils.isEmpty(mDateBtn.getText().toString())){



            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String username = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();


                    DatabaseReference newPost= mBookingReference.child(key_res).child(mAuth.getCurrentUser().getUid());
                    newPost.child("username").setValue(username);
                    newPost.child("time").setValue(mTimeSpinner.getSelectedItem().toString());
                    newPost.child("date").setValue(mDateBtn.getText().toString());
                    newPost.child("person").setValue(mPersonSpinner.getSelectedItem().toString());
                    newPost.child("image").setValue(image);
                    newPost.child("confirmed").setValue(false);


                    Toast.makeText(getActivity().getApplicationContext(),"You have booked table for"+mPersonSpinner.getSelectedItem().toString() +"at"+mTimeSpinner.getSelectedItem().toString()+"On"+mDateBtn.getText().toString()

                            ,Toast.LENGTH_LONG).show();



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




          /*  mBookingReference.child(key_res).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference newPost= mBookingReference.child(key_res).child(mAuth.getCurrentUser().getUid());
                    newPost.child("username").setValue();
                    newPost.child("time").setValue(mTimeSpinner.getSelectedItem().toString());
                    newPost.child("date").setValue(mDateBtn.getText().toString());
                    newPost.child("person").setValue(mPersonSpinner.getSelectedItem().toString());
                    newPost.child("confirmed").setValue(false);


                    Toast.makeText(getActivity().getApplicationContext(),"You have booked table for"+mPersonSpinner.getSelectedItem().toString() +"at"+mTimeSpinner.getSelectedItem().toString()+"On"+mDateBtn.getText().toString()

                            ,Toast.LENGTH_LONG).show();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/



        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Please Select date,time and person carefully",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap=googleMap;

        LatLng rl=new LatLng(3.048365,101.692924);
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(rl).title("Restaurant");
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rl,10));



    }
}
