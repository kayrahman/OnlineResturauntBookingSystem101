package mahsa.com.onlineresturauntbookingsystem.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import mahsa.com.onlineresturauntbookingsystem.R;

public class RegisterAcitvity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPassField;
    private Button mSubmitBtn;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    private DatabaseReference mDatabaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitvity);

        mDialog = new ProgressDialog(this);

        mNameField = (EditText) findViewById(R.id.register_input_name);
        mEmailField = (EditText) findViewById(R.id.register_input_email);
        mPassField = (EditText) findViewById(R.id.register_input_password);
        mSubmitBtn = (Button) findViewById(R.id.register_submit_button);


        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseUser.keepSynced(true);


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistering();
            }
        });


    }

    private void startRegistering() {

        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        String pass = mPassField.getText().toString().trim();
        final String device_token = FirebaseInstanceId.getInstance().getToken();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

            mDialog.setMessage("Singing up....");
            mDialog.show();

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference ref = mDatabaseUser.child(user_id);

                        HashMap<String, String> userDetail = new HashMap<String, String>();
                        userDetail.put("name", name);
                        userDetail.put("user_type", "customer");
                        userDetail.put("device_token", device_token);
                        userDetail.put("email", mAuth.getCurrentUser().getEmail());


                        /*ref.child("name").setValue(name);
                        ref.child("image").setValue("default");
                        ref.child("user_type").setValue("customer");
*/
                        ref.setValue(userDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                mDialog.dismiss();

                                Intent intent = new Intent(RegisterAcitvity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);


                            }
                        });


                    }else{
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterAcitvity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                        }else {


                            Toast.makeText(RegisterAcitvity.this, "Something's wrong", Toast.LENGTH_LONG).show();

                        }

                        mDialog.dismiss();
                    }
                }
            });
        }
    }

/////////////////


    private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();


        mDatabaseUser.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {

                    //  DatabaseReference imageRef=mDatabaseUsers.child(user_id).child("image").getRef() ;

                    Intent mainIntent = new Intent(RegisterAcitvity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    finish();

                } else {

                    Intent setupIntent = new Intent(RegisterAcitvity.this, AccountSetUpActivity.class);
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




















