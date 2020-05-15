package com.example.qrps;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GeneratorFragment extends Fragment {

    EditText gen_name, gen_cont, gen_addr;
    Button generate;
    ProgressBar generate_pbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qrgenerator, container, false);

        gen_name = v.findViewById(R.id.qrgen_name);
        gen_cont = v.findViewById(R.id.qrgen_cont);
        gen_addr = v.findViewById(R.id.qrgen_addr);

        generate_pbar = v.findViewById(R.id.generate_pbar);
        generate = v.findViewById(R.id.generate);
        generate.setOnClickListener(v1 -> {
            String sname = gen_name.getText().toString();
            String scont = gen_cont.getText().toString();
            String saddr = gen_addr.getText().toString();
            checkAndSend(sname,scont,saddr);
        });

        return v;
    }

    private void checkAndSend(String sname, String scont, String saddr) {
            if(sname.isEmpty()){
                gen_name.setError("Name is Required");
                gen_name.requestFocus();
                return;
            }
            else if(scont.isEmpty()){
                gen_cont.setError("Contact Number is Required");
                gen_cont.requestFocus();
                return;
            }
            else if(scont.length()!=10){
                gen_cont.setError("Enter a valid Contact Number");
                gen_cont.requestFocus();
                return;
            }
            else if(saddr.isEmpty()){
                gen_addr.setError("Address is Required");
                gen_addr.requestFocus();
                return;
            }
            else{
                generate_pbar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getContext(),Generateqrcode.class);
                intent.putExtra("name",sname);
                intent.putExtra("cont",scont);
                intent.putExtra("addr",saddr);
                startActivity(intent);
                generate_pbar.setVisibility(View.GONE);
            }
    }
}
