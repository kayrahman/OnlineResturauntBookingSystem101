package mahsa.com.onlineresturauntbookingsystem.ui;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.api.RetrofeitService;
import mahsa.com.onlineresturauntbookingsystem.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountSetUpActivity extends AppCompatActivity {

    private Uri mImageUri = null;
    private CircleImageView mDisplayImageBtn;
    private EditText mUserName;
    private EditText mUserAddress;
    private EditText mUserEmail;
    private EditText mUserPhone;
    private Button mSubmitBtn;

    private StorageReference mStorage;
    private DatabaseReference mDatabaseUsers;

    private ProgressDialog mDialog;

    private static int GALLERY_REQUEST=1;


    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private  byte[] thumb_byte ;

    public static final String PROFILE_PREFERENCES = "ProPref" ;

    private SharedPreferences sharedpreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_set_up);

        // INITIALIZING
        mDialog= new ProgressDialog(this);


        // INITIALIZING

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());


        // INITIALIZING USER FIELDS

        mDisplayImageBtn=(CircleImageView) findViewById(R.id.acc_set_up_image_btn);
        mUserName=(EditText) findViewById(R.id.acc_set_up_name_et);
        mUserEmail = (EditText)findViewById(R.id.acc_set_up_email_et);
        mUserAddress=(EditText)findViewById(R.id.acc_set_up_add_et);
        mUserPhone=(EditText)findViewById(R.id.acc_set_up_phone_et);
        mSubmitBtn=(Button)findViewById(R.id.acc_set_up_submit);

        
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPostingUserDetail();
            }
        });


        mDisplayImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });


        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("name")) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    mUserName.setText(name);

                    String email = mAuth.getCurrentUser().getEmail();
                    mUserEmail.setText(email);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sharedpreferences = getSharedPreferences(PROFILE_PREFERENCES, Context.MODE_PRIVATE);

        String user_name = sharedpreferences.getString("user_name",null);
        String email = sharedpreferences.getString("email",null);
        String address = sharedpreferences.getString("address",null);
        String phone = sharedpreferences.getString("phone",null);


        if(!TextUtils.isEmpty(user_name) &&!TextUtils.isEmpty(email) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(phone)){
            mUserName.setText(user_name);
            mUserEmail.setText(email);
            mUserAddress.setText(address);
            mUserPhone.setText(phone);


        }

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
            Toast.makeText(AccountSetUpActivity.this,"On Progress",Toast.LENGTH_LONG).show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void startPostingUserDetail() {

        mDialog.setMessage("Posting....");

        final String user_name = mUserName.getText().toString().trim();
        final String user_address= mUserAddress.getText().toString().trim();
        final String user_email =mUserEmail.getText().toString().trim();
        final String user_phone = mUserPhone.getText().toString().trim();


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user_name", user_name);
        editor.putString("email", user_email);
        editor.putString("address", user_address);
        editor.putString("phone",user_phone);

        editor.commit();

        if(!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_address) && !TextUtils.isEmpty(user_email) && !TextUtils.isEmpty(user_phone) && mImageUri != null){

            mDialog.show();

            StorageReference filepath = mStorage.child("User_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")

                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    String image = downloaduri.toString();

                    User user =new User(user_name,user_address,user_email,user_phone,image);

                   /* Call<User> setUpUserAcc = RetrofeitService.postUserData().saveUserData(mCurrentUser.getUid(),user);
                    setUpUserAcc.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(AccountSetUpActivity.this,"Successfully updated",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });*/



                   Map<String,Object> user_info = new HashMap<>();
                   user_info.put("address",user_address);
                    user_info.put("email",user_email);
                    user_info.put("phone",user_phone);
                    user_info.put("image",downloaduri.toString());

                    mDatabaseUsers.updateChildren(user_info).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mDialog.dismiss();

                            Intent intent = new Intent(AccountSetUpActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });



                 /*   DatabaseReference newPost= mDatabaseUsers;
                    newPost.child("address").setValue(user_address);
                    newPost.child("email").setValue(user_email);
                    newPost.child("phone").setValue(user_phone);
                    newPost.child("image").setValue(downloaduri.toString());*/

                }
            });

        }else{

            Toast.makeText(AccountSetUpActivity.this,"Fill in the blanks",Toast.LENGTH_LONG).show();

        }
        

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK ){

            if(data == null){
                Toast.makeText(AccountSetUpActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }else {
                mImageUri = data.getData();


                CropImage.activity(mImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropWindowSize(500,500)
                        .start(this);

            }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                mDisplayImageBtn.setImageURI(resultUri);

                File thumb_filePath = new File(resultUri.getPath());


                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .compressToBitmap(thumb_filePath);


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_byte = baos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}











































