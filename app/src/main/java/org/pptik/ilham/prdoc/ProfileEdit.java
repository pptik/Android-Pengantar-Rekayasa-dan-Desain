package org.pptik.ilham.prdoc;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan untuk mengubah data pengguna
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static org.pptik.ilham.prdoc.MainActivity.SessionPengguna;

public class ProfileEdit extends AppCompatActivity {
    Toolbar toolbar;
    EditText editTextNamaDepan, editTextNamaBelakang, ediTextNIM;
    Button buttonKirim;
    ProgressDialog progressDialog;
    Integer idPengguna;
    String namaDepan, namaBelakang, nim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        toolbar = (Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitle("Ubah Profile");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences settings = getSharedPreferences(SessionPengguna, MODE_PRIVATE);
        idPengguna = settings.getInt("idPengguna", 0);
        namaDepan = settings.getString("namaDepan", null);
        namaBelakang = settings.getString("namaBelakang", null);
        nim = settings.getString("nim", null);

        editTextNamaDepan = (EditText)findViewById(R.id.editTextNamaDepan);
        editTextNamaBelakang = (EditText)findViewById(R.id.editTextNamaBelakang);
        ediTextNIM = (EditText)findViewById(R.id.editTextNIM);



        ediTextNIM.setText(nim);
        editTextNamaDepan.setText(namaDepan);
        editTextNamaBelakang.setText(namaBelakang);

        buttonKirim = (Button)findViewById(R.id.buttonKirim);

        buttonKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(apakahTerhubungDenganInternet() == true){
                    progressDialog = new ProgressDialog(ProfileEdit.this);
                    progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
                    progressDialog.show();
                    ubahProfile(idPengguna, editTextNamaDepan.getText().toString(), editTextNamaBelakang.getText().toString(), ediTextNIM.getText().toString());

                    SharedPreferences settings = getSharedPreferences(SessionPengguna, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("namaDepan", editTextNamaDepan.getText().toString());
                    editor.putString("namaBelakang", editTextNamaBelakang.getText().toString());
                    editor.putString("nim", ediTextNIM.getText().toString());
                    editor.commit();

                    String namaDepanUpdate = editTextNamaDepan.getText().toString();
                    String namaBelakangUpdate = editTextNamaBelakang.getText().toString();
                    String nimUpdate = ediTextNIM.getText().toString();

                    editTextNamaDepan.setText(namaDepanUpdate);
                    editTextNamaBelakang.setText(namaBelakangUpdate);
                    ediTextNIM.setText(nimUpdate);

                    progressDialog.dismiss();
                }else{
                    Toast.makeText(ProfileEdit.this, "Mohon cek kembali koneksi internet anda.", Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ubahProfile(Integer id, String nama_depan, String nama_belakang, String nim){
        final Toast toastBerhasil = Toast.makeText(ProfileEdit.this, "Profil telah diubah silahkan swipe untuk melihat perubahan.", Toast.LENGTH_LONG);
        final Toast toastGagal = Toast.makeText(ProfileEdit.this, "Gagal mengambil data.", Toast.LENGTH_LONG);

        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("nama_depan", nama_depan);
        params.put("nama_belakang", nama_belakang);
        params.put("nim", nim);

        RestClientApp.post("ubah_profile",params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("[Pesan]","Sukses array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("[Pesan]","Sukses objek");

                try {
                    Log.d("[Pesan]","Sukses TRY objek");
                    String rc = response.getString("RC");

                    if(rc.equals("00")){//Berhasil memanggil data
                        toastBerhasil.show();
                        finish();
                    }else{//Gagal mengambil data
                        finish();
                        toastGagal.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean apakahTerhubungDenganInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
