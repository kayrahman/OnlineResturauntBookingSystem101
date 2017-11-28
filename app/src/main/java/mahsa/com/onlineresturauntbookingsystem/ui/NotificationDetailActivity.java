package mahsa.com.onlineresturauntbookingsystem.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import mahsa.com.onlineresturauntbookingsystem.R;

public class NotificationDetailActivity extends AppCompatActivity {


    private ImageView mCusImage;
    private TextView mCusName;
    private TextView mNumPersons;
    private TextView mTime;
    private TextView mDate;
    private Button mConfirmBtn;
    private Button mRejectBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mBookingRef;
    private DatabaseReference mUserRef;
    private DatabaseReference mNotificationRef;

    private FirebaseUser mFirebaseUser;

    String cus_key = "";


    public static final String FROM_USER_KEY = "user_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);


      //  cus_key = getIntent().getStringExtra(FROM_USER_KEY);
//        Log.d("CUS_KEY", cus_key);

   //     String KEY= getIntent().getExtras().get("fromUserId").toString();
  //      Log.d("KEY", KEY);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification

            cus_key = bundle.getString(FROM_USER_KEY);
//            Log.d("BUNDLE", bundle.getString("fromUserId"));
            //Log.d("CUS", cus_key);


        }



        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mBookingRef = FirebaseDatabase.getInstance().getReference().child("booking").child(mAuth.getCurrentUser().getUid()).child(cus_key);
        mNotificationRef=FirebaseDatabase.getInstance().getReference().child("notifications");


        mCusImage = (ImageView) findViewById(R.id.iv_ac_notification_user_image);
        mCusName = (TextView) findViewById(R.id.tv_ac_noti_username);
        mNumPersons = (TextView) findViewById(R.id.tv_ac_noti_persons);
        mTime = (TextView) findViewById(R.id.tv_ac_noti_time);
        mDate = (TextView) findViewById(R.id.tv_ac_noti_date);
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm_notification);
        mRejectBtn = (Button) findViewById(R.id.btn_reject_notification);


        mBookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String time = (String) dataSnapshot.child("time").getValue();
                String date = (String) dataSnapshot.child("date").getValue();
                String numOfPersons = (String) dataSnapshot.child("persons").getValue();
                String name = (String) dataSnapshot.child("username").getValue();
                String image = (String) dataSnapshot.child("image").getValue();

                mCusName.setText(name);
                mTime.setText(time);
                mDate.setText(date);
                mNumPersons.setText(numOfPersons);

                Picasso.with(NotificationDetailActivity.this)
                        .load(image)
                        .placeholder(R.drawable.male)
                        .into(mCusImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBookingRef.child("confirmed").setValue("true");

                HashMap<String,String> notificationData = new HashMap<String, String>();
                notificationData.put("from",mAuth.getCurrentUser().getUid());
                notificationData.put("type","booking");

                mNotificationRef.child(cus_key).push().setValue(notificationData);



            }
        });


        mRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();



    }
}































