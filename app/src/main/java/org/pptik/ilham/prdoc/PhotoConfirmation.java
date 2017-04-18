package org.pptik.ilham.prdoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan untuk mengatasi konfirmasi unggah foto KTM / KTP(Pengajar)
 */

public class PhotoConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_confirmation);

        //Memberikan judul pada action bar
        setTitle("Dashboard");


    }
}
