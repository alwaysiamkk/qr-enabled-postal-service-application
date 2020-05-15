package com.example.qrps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class Generateqrcode extends AppCompatActivity {

    ImageView qrcode_image;
    ProgressBar progressBar;
    DatabaseReference reference;
    StorageReference storageReference;
    String tname, temail, tcontact, taddress;
    //public Uri imageuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateqrcode);

        qrcode_image = findViewById(R.id.qrcode);
        progressBar = findViewById(R.id.qr_progress);
        progressBar.setVisibility(View.VISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference("qrcodes");

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tname = dataSnapshot.child("name").getValue().toString();
                temail = dataSnapshot.child("email").getValue().toString();
                tcontact = dataSnapshot.child("contact").getValue().toString();
                taddress = dataSnapshot.child("address").getValue().toString();
                Intent intent = getIntent();
                String name = intent.getStringExtra("name");
                String contact = intent.getStringExtra("cont");
                String address = intent.getStringExtra("addr");
                String final_text = "To:\n"+name+"\n"+contact+"\n"+address+"\n\nFrom:"+tname+"\n"+tcontact+"\n"+temail+"\n"+taddress;

                try{
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    BitMatrix bitMatrix = multiFormatWriter.encode(final_text, BarcodeFormat.QR_CODE,500,500);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    progressBar.setVisibility(View.GONE);
                    qrcode_image.setImageBitmap(bitmap);
                    //uploadQR(bitmap);

                }catch (Exception e){
                    Log.i("exception","Excp: "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*public void uploadQR(Bitmap bitmap){
        StorageReference qr_reference = storageReference.child(System.currentTimeMillis()+ ".png");
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        bitmap.recycle();
        byte[] byteArray = bao.toByteArray();
        String imageB64 = Base64.encodeToString(byteArray, Base64.URL_SAFE);
        imageuri = Uri.parse(imageB64);
        qr_reference.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //String uploadId = reference.push().getKey();
                        String url_txt = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        url.setText(url_txt);
                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(Generateqrcode.this,"QR Code is uploaded to DataBase\nNote the code to retrive the QR code",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Generateqrcode.this,"QR Code is not uploaded",Toast.LENGTH_LONG).show();
                    }
                });
    }*/
}
