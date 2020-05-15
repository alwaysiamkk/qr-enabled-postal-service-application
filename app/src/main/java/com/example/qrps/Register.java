package com.example.qrps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity{

    TextView signin;
    private FirebaseAuth mAuth;
    Button register;
    EditText name, email, password, contact, address;
    ProgressBar progress_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        progress_register = findViewById(R.id.prog_register);
        signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
        register = findViewById(R.id.register);
    }
    public void checkEmail(View v){
        address.onEditorAction(EditorInfo.IME_ACTION_DONE);
        final String emailid = email.getText().toString().toLowerCase().trim();
        if(emailid.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        mAuth.fetchSignInMethodsForEmail(emailid)
        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                progress_register.setVisibility(View.VISIBLE);
                String pass = password.getText().toString();
                final String cont = contact.getText().toString().trim();
                final String addr = address.getText().toString();
                final String fname = name.getText().toString();
                if(fname.isEmpty()){
                    name.setError("Name is Required");
                    name.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                if(emailid.isEmpty()){
                    email.setError("Email id is Required");
                    email.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
                    email.setError("Enter a valid email id");
                    email.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                if(pass.isEmpty()){
                    password.setError("Password can not be empty");
                    password.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                if(pass.length()<6){
                    password.setError("Password should be minimum of 6 characters");
                    password.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                if(cont.isEmpty()){
                    contact.setError("Enter Number");
                    contact.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                if(cont.length()!=10){
                    contact.setError("Enter a valid Contact Number");
                    contact.requestFocus();
                    progress_register.setVisibility(View.GONE);
                    return;
                }
                boolean check = task.getResult().getSignInMethods().isEmpty();
                if(!check){
                    progress_register.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"Email already Exists",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(emailid, pass)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("reg", "createUserWithEmail:success");
                                        User user = new User(fname,emailid,cont,addr);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progress_register.setVisibility(View.GONE);
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Register.this,MainActivitywithBNB.class));
                                                    finish();
                                                }else{
                                                    Toast.makeText(Register.this,"Registered Failed, Check details entered again",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Log.w("reg", "createUserWithEmail:failure", task.getException());
                                        progress_register.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}
