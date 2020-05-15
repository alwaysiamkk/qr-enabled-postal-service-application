package com.example.qrps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    Button forgot_button;
    private Button mButton;
    private EditText mForgotEmail;
    private TextView mLog;
    private TextView mReg;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        initView();
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mForgotEmail = (EditText) findViewById(R.id.forgot_email);
        mLog = (TextView) findViewById(R.id.log);
        mReg = (TextView) findViewById(R.id.reg);
        mLog.setOnClickListener(this);
        mReg.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                mButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
                mProgressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(mForgotEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Forgot.this, "Reset Link Sent to Email Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Forgot.this, "Email Not Found, Go for Registration", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                break;
            case R.id.log:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.reg:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}
