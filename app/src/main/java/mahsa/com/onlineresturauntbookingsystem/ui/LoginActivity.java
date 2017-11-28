package mahsa.com.onlineresturauntbookingsystem.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import mahsa.com.onlineresturauntbookingsystem.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private Button mRegisterBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("users");

        mProgressDialog=new ProgressDialog(this);


        mEmailField=(EditText)findViewById(R.id.login_input_email);
        mPasswordField=(EditText)findViewById(R.id.login_input_password);

        mLoginBtn=(Button)findViewById(R.id.main_login_button);
        mRegisterBtn=(Button)findViewById(R.id.activity_login_register_button);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterAcitvity.class);
                startActivity(intent);
            }
        });


        
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });


    }

    private void checkLogin() {

        String email=mEmailField.getText().toString().trim();
        String pass=mPasswordField.getText().toString().trim();

        mProgressDialog.setMessage("Logging in");
        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    checkUserExist();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    String user_id=mAuth.getCurrentUser().getUid();

                    mDatabaseReference.child(user_id).child("device_token").setValue(deviceToken);


                    mProgressDialog.dismiss();

                }else{

                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void checkUserExist() {

        final String user_id=mAuth.getCurrentUser().getUid();

        mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("address")){




                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else {
                    Intent setupIntent = new Intent(LoginActivity.this, AccountSetUpActivity.class);
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
