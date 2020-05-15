package com.example.qrps;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ScannerFragment extends Fragment {

    Button copy_button;
    EditText recdet_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qrscanner, container, false);

        recdet_tv = v.findViewById(R.id.recdet_tv);
        copy_button = v.findViewById(R.id.copy_button);

        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        .setCameraId(0).setOrientationLocked(false).setPrompt("Scanning").setBeepEnabled(true).setBarcodeImageEnabled(true)
        .forSupportFragment(ScannerFragment.this).initiateScan();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult!=null && intentResult.getContents()!=null){

            recdet_tv.setText(intentResult.getContents());

            copy_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager manager = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("result",intentResult.getContents());
                    manager.setPrimaryClip(data);
                    Toast.makeText(getContext(),"Copied to Clipboard",Toast.LENGTH_LONG).show();
                }
            });

            /*new AlertDialog.Builder(getContext())
            .setTitle("Details of the Post")
            .setMessage(intentResult.getContents())
            .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ClipboardManager manager = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("result",intentResult.getContents());
                    manager.setPrimaryClip(data);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();*/
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
