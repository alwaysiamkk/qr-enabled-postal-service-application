package com.example.qrps;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    TextView tvName, tvEmail, tvContact, tvAddress;
    Button btSignout,credits_but;
    DatabaseReference reference;
    //private String name, email, contact, address;
    ProgressBar prof_pbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account, container, false);


        tvName = v.findViewById(R.id.pname_text);
        tvEmail = v.findViewById(R.id.pemail_text);
        tvContact = v.findViewById(R.id.pcont_text);
        tvAddress = v.findViewById(R.id.paddress_text);
        prof_pbar = v.findViewById(R.id.profile_pbar);

        credits_but = v.findViewById(R.id.credits_but);
        credits_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Credits.class));
            }
        });

        btSignout = v.findViewById(R.id.signout);
        btSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prof_pbar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    prof_pbar.setVisibility(View.GONE);
                    getActivity().finish();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }else{
                    Toast.makeText(getContext(),"Not able to Sign Out",Toast.LENGTH_SHORT).show();
                }


            }
        });

        prof_pbar.setVisibility(View.VISIBLE);
        checkConnection();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prof_pbar.setVisibility(View.GONE);
                String name = dataSnapshot.child("name").getValue().toString();
                tvName.setText(name);
                String email = dataSnapshot.child("email").getValue().toString();
                tvEmail.setText(email);
                String contact = dataSnapshot.child("contact").getValue().toString();
                tvContact.setText(contact);
                String address = dataSnapshot.child("address").getValue().toString();
                tvAddress.setText(address);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return v;
    }
    private void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork==null){
            Toast.makeText(getContext(),"Internet Connection required",Toast.LENGTH_LONG).show();
        }
    }
}
