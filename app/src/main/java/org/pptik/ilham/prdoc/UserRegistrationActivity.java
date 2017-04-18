package org.pptik.ilham.prdoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.loopj.android.http.*;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan untuk mendaftarkan pengguna ke DB
 */

public class UserRegistrationActivity extends AppCompatActivity {
    //Inisialisasi widget
    Spinner spinnerPeran, spinnerNamaUniversitas;
    Button buttonKirim;
    ProgressDialog progressDialog;
    EditText editTextUsername, editTextEmail, editTextSandi, editTextTulisUlangSandi;
    Toolbar toolbar;

    //String untuk Array Adapter
    String[] isianSpinnerPeran = {"Dosen","Mahasiswa"};
    List<String> isianSpinnerNamaUniversitas = new ArrayList<>();
    List<Integer> isianSpinnerIdUniversitas = new ArrayList<>();
    ArrayAdapter<String> spinnerUniversitasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //Pengaturan toolbar
        setTitle("Form Pendaftaran");


        //Deklarasi widget
        spinnerPeran = (Spinner)findViewById(R.id.spinnerPeran);
        spinnerNamaUniversitas = (Spinner)findViewById(R.id.spinnerUniversitas);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextSandi = (EditText)findViewById(R.id.editTextSandi);
        editTextTulisUlangSandi = (EditText)findViewById(R.id.editTextUlangSandi);
        buttonKirim = (Button)findViewById(R.id.buttonKirim);
        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Deklarasi array adapter untuk spinner
        //peran
        ArrayAdapter<String> spinnerPeranAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, isianSpinnerPeran);
        spinnerPeranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeran.setAdapter(spinnerPeranAdapter);

        //universitas
        spinnerUniversitasAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, isianSpinnerNamaUniversitas);
        spinnerUniversitasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNamaUniversitas.setAdapter(spinnerUniversitasAdapter);

        progressDialog = new ProgressDialog(UserRegistrationActivity.this);
        this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
        this.progressDialog.show();

        //Mencek apakah terhubung dengan internet?
        if(apakahTerhubungDenganInternet() == true){
            getUniversities();
        }else{
            finish();
            Toast.makeText(UserRegistrationActivity.this, "Mohon cek kembali koneksi internet anda sebelum melanjutkan.", Toast.LENGTH_LONG).show();
        }


        //Memberikan judul pada action bar
        setTitle("Form Pendaftaran");

        //Aksi button kirim ketika di klik
        buttonKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Menentukan Id peran
                int indexSpinnerPeran = spinnerPeran.getSelectedItemPosition();
                int peran = -1;//Inisial untuk keperluan deklarasi awal

                switch (indexSpinnerPeran){
                    case 0:peran = 2;break;
                    case 1:peran = 4;break;
                }

                //Id Universitas yang dipilih pada spinner nama universitas
                int idUniversitas = isianSpinnerIdUniversitas.get(spinnerNamaUniversitas.getSelectedItemPosition());

                progressDialog.setMessage("Mohon tunggu sedang diproses ...");
                progressDialog.show();

                postUsers(peran,editTextEmail.getText().toString(),idUniversitas,editTextUsername.getText().toString(),editTextSandi.getText().toString(),editTextTulisUlangSandi.getText().toString());


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUniversities(){
        Log.d("[Pesan]","Masuk fungsi");
        final Toast toast = Toast.makeText(UserRegistrationActivity.this, "Gagal mengambil data.", Toast.LENGTH_LONG);

        RestClientApp.get("universities",null,new JsonHttpResponseHandler(){

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
                    progressDialog.dismiss(); //Menghilangkan progress dialog

                    Log.d("[Pesan]","Nilai RC:"+rc);
                    if(rc.equals("00")){//Berhasil memanggil data
                        //Menguraikan nama Universitas
                        JSONArray data = response.getJSONArray("data");
                        for(int i = 0; i < data.length(); i++){
                            JSONObject jsonObjekDataUniversitas = data.getJSONObject(i);

                            String namaDepanUniversitas = jsonObjekDataUniversitas.getString("nama_depan");
                            String namaBelakangUniversitas = jsonObjekDataUniversitas.getString("nama_belakang");

                            isianSpinnerIdUniversitas.add(jsonObjekDataUniversitas.getInt("id"));
                            isianSpinnerNamaUniversitas.add(namaDepanUniversitas+" "+namaBelakangUniversitas);
                        }

                    }else{//Gagal mengambil data

                        finish();
                        toast.show();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
                spinnerUniversitasAdapter.notifyDataSetChanged();
            }
        });
    }

    public boolean apakahTerhubungDenganInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void postUsers(Integer peran, String email, Integer universitas, String username, String password, String password_ulang){

        final Toast toastBerhasilMendaftarkanDiri = Toast.makeText(UserRegistrationActivity.this, "Silahkan unggah foto Kartu Pelajar Anda.", Toast.LENGTH_LONG);

        RequestParams params = new RequestParams();
        params.put("peran", peran);
        params.put("email", email);
        params.put("universitas", universitas);
        params.put("username", username);
        params.put("password", password);
        params.put("password_ulang", password_ulang);

        RestClientApp.post("register",params,new JsonHttpResponseHandler(){

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

                    Log.d("[Pesan]","Nilai RC:"+rc);
                    if(rc.equals("00")){//Berhasil mendaftarkan diri
                        progressDialog.dismiss();

                        //Menguraikan hasil request data
                        JSONArray data = response.getJSONArray("data");
                        for(int i = 0; i <= data.length(); i++){
                            JSONObject jsonObjekDataPengguna = data.getJSONObject(i);

                            //Data untuk user preferences
                            //email, idPengguna, universitas, peran, username
                            String email = jsonObjekDataPengguna.getString("email");
                            Integer idPengguna = jsonObjekDataPengguna.getInt("id");
                            Integer universitas = jsonObjekDataPengguna.getInt("universitas");
                            Integer peran = jsonObjekDataPengguna.getInt("peran");
                            String username = jsonObjekDataPengguna.getString("username");

                            // Restore preferences
                            //SharedPreferences settings = getSharedPreferences("userPreferences", 0);
                            //SharedPreferences.Editor editor = settings.edit();

                            //editor.putInt("idPengguna", idPengguna);
                            //editor.commit();


                            toastBerhasilMendaftarkanDiri.show();

                            //Integer userId = settings.getInt("idPengguna", idPengguna);

                            //final Toast toastGagalMendaftarkanDiri = Toast.makeText(UserRegistrationActivity.this, "Id usernya:"+userId, Toast.LENGTH_LONG);
                            //toastGagalMendaftarkanDiri.show();

                            //Pindah ke intent konfirmasi photo
                            Intent intent = new Intent(getApplication(), PhotoConfirmation.class);
                            startActivity(intent);
                        }

                    }else{//Gagal mendaftarkan diri
                        progressDialog.dismiss();
                        String pesanGagal = response.getString("message");
                        final Toast toastGagalMendaftarkanDiri = Toast.makeText(UserRegistrationActivity.this, pesanGagal, Toast.LENGTH_LONG);
                        toastGagalMendaftarkanDiri.show();
                        /*toastGagalMendaftarkanDiri.makeText(getApplication(), pesanGagal, Toast.LENGTH_LONG);
                        toastGagalMendaftarkanDiri.show();*/
                        Log.d("[POST]","Pesan: "+pesanGagal);
                    }

                } catch (JSONException e) {
                    progressDialog.show();
                    e.printStackTrace();
                }
                //spinnerUniversitasAdapter.notifyDataSetChanged();
            }
        });
    }
}
