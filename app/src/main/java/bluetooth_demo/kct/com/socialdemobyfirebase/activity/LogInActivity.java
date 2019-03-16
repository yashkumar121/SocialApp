package bluetooth_demo.kct.com.socialdemobyfirebase.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bluetooth_demo.kct.com.socialdemobyfirebase.R;
import bluetooth_demo.kct.com.socialdemobyfirebase.bean.User;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AVLoadingIndicatorView;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AppPrefernce;

public class LogInActivity extends AppCompatActivity {


    TextView login;
    EditText email,password;
    AVLoadingIndicatorView progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.buttonlogin);



        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toCheckUserValidity();
            }
        });
    }

    private void toCheckUserValidity()
    {


        final String emailText=email.getText().toString();
        String passwordText=password.getText().toString();

        if(emailText.equalsIgnoreCase(""))
            Toast.makeText(this,"Please enter email id",Toast.LENGTH_LONG).show();
        else if(passwordText.equalsIgnoreCase(""))
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();

        else
        {
           progressBar=findViewById(R.id.loader);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.smoothToShow();
            final FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {


                            if (!task.isSuccessful())
                            {

                                progressBar.smoothToHide();
                                //progressBar.setVisibility(View.GONE);

                                Toast.makeText(LogInActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();


                            }
                            else
                                storeCredential(auth,emailText);


                        }
                    });


        }
    }

    private void storeCredential(FirebaseAuth auth,String email)
    {
        FirebaseUser user=auth.getCurrentUser();
                    final String tokenId=user.getUid();//"EcZGeEgVEWWMJWq9JVJjwQ3ooX42";//task.getResult().getToken();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference(tokenId).child("UserDetail");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {

                                User userValue = dataSnapshot.getValue(User.class);
                                AppPrefernce prefernce = new AppPrefernce(LogInActivity.this);
                                prefernce.setString(AppPrefernce.EMAILID, userValue.getEmailId());
                                prefernce.setString(AppPrefernce.USERNAME, userValue.getUserName());
                                prefernce.setString(AppPrefernce.PROFILEDP, userValue.getProfileDp());
                                prefernce.setString(AppPrefernce.TOKENKEY, tokenId);

                            progressBar.smoothToHide();
                            //progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                finish();


                        }

                        @Override
                        public void onCancelled(DatabaseError error)
                        {
                            progressBar.smoothToHide();
                            //progressBar.setVisibility(View.GONE);
                        }
                    });






    }
}
