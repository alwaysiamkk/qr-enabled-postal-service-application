package com.example.qrps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    EditText emailid, password;
    TextView forgot, register;
    Button signin;
    ProgressBar progress_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailid = findViewById(R.id.email);
        password = findViewById(R.id.password);

        forgot = findViewById(R.id.forgot);
        register = findViewById(R.id.register);

        progress_signin = findViewById(R.id.prog_signin);

        signin = findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this,MainActivitywithBNB.class));
            this.finish();
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                Intent intent = new Intent(MainActivity.this,Forgot.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                toast("Oh! You are a new User");
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });
    }

    private void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork==null){
            Toast.makeText(this,"Internet Connection required",Toast.LENGTH_LONG).show();
        }
    }

    public void signIn (View v){
        String email = emailid.getText().toString().trim();
        String pass = password.getText().toString().trim();
        password.onEditorAction(EditorInfo.IME_ACTION_DONE);
        progress_signin.setVisibility(View.VISIBLE);
        checkConnection();
        if(email.isEmpty()){
            emailid.setError("Email is Required");
            emailid.requestFocus();
            progress_signin.setVisibility(View.GONE);
            return;
        }
        if(pass.isEmpty()){
            password.setError("Password id is Required");
            password.requestFocus();
            progress_signin.setVisibility(View.GONE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailid.setError("Enter a valid Email Id");
            emailid.requestFocus();
            progress_signin.setVisibility(View.GONE);
            return;
        }
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress_signin.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            toast("SignIn Success");
                            finish();
                            startActivity(new Intent(MainActivity.this,MainActivitywithBNB.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private  void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
