//MainActiviy.java
//Copyright by Muhammad Ilham Fadillah
//This class has intention as a first Controller
package org.pptik.ilham.prdoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pptik.ilham.prdoc.JSONModels.RequestStatus;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai halaman awal ketika pengguna mengakses aplikasi(apabila kondisinya sebelum masuk)
 */

public class MainActivity extends AppCompatActivity {
    public static final String SessionPengguna = "DataPengguna";

    TextView lableSignUp;
    EditText ediTextEmail, editTextSandi;
    FloatingActionButton floatingActionButtonKirim;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LeakCanary.install(getApplication());

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
        //Mencek apakah pengguna sudah masuk sebelumnya
        if(apakahUserSudahMasuk() == true){
            finish();
            Intent intent = new Intent(getApplication(), Dashboard.class);
            startActivity(intent);

        }

        //Deklarasi widget
        ediTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextSandi = (EditText)findViewById(R.id.editTextSandi);
        floatingActionButtonKirim = (FloatingActionButton)findViewById(R.id.floatingActionButton);


        //Aksi ketika tombol kirim ditekan
        floatingActionButtonKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mencek apakah terhubung dengan internet?
                if(apakahTerhubungDenganInternet() == true){
                    loginUser(ediTextEmail.getText().toString(),editTextSandi.getText().toString());
                }else{
                    finish();
                    Toast.makeText(MainActivity.this, "Mohon cek kembali koneksi internet anda sebelum melanjutkan.", Toast.LENGTH_LONG).show();
                }
            }
        });





        //Pindah ke halaman daftar akun baru
        lableSignUp = (TextView)findViewById(R.id.lableSignUp);
        lableSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), UserRegistrationActivity.class);
                startActivity(intent);

            }
        });
    }

    public boolean apakahTerhubungDenganInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loginUser(String email, String password){
        progressDialog = new ProgressDialog(MainActivity.this);
        this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
        this.progressDialog.show();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        RestClientApp.post("login",params,new JsonHttpResponseHandler(){

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
                    //Untuk memanggil RC nya
                    RequestStatus requestStatus = new Gson().fromJson(response.toString(), RequestStatus.class);
                    if(requestStatus.getRC().equals("00")){

                    }

                    String rc = response.getString("RC");
                    Log.d("[Pesan]","Nilai RC:"+rc);
                    if(rc.equals("00")){//Berhasil mendaftarkan diri
                        progressDialog.dismiss();

                        //Menguraikan hasil request data
                        JSONArray data = response.getJSONArray("data");
                        for(int i = 0; i < data.length(); i++){
                            JSONObject jsonObjekDataPengguna = data.getJSONObject(i);

                            //Data untuk shared preferences
                            String email = jsonObjekDataPengguna.getString("email");
                            Integer idPengguna = jsonObjekDataPengguna.getInt("id");
                            Integer universitas = jsonObjekDataPengguna.getInt("universitas");
                            Integer peran = jsonObjekDataPengguna.getInt("peran");
                            String username = jsonObjekDataPengguna.getString("username");
                            String profilePicture = jsonObjekDataPengguna.getString("url_foto");
                            String namaDepan = jsonObjekDataPengguna.getString("nama_depan");
                            String namaBelakang = jsonObjekDataPengguna.getString("nama_belakang");
                            String nim = jsonObjekDataPengguna.getString("nim");

                            SharedPreferences settings = getSharedPreferences(SessionPengguna, MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("email", email);
                            editor.putInt("idPengguna", idPengguna);
                            editor.putInt("universitas", universitas);
                            editor.putInt("peran", peran);
                            editor.putString("username", username);
                            editor.putString("namaDepan", namaDepan);
                            editor.putString("namaBelakang", namaBelakang);
                            editor.putString("nim", nim);
                            editor.putString("profilePicture", profilePicture);
                            editor.putBoolean("sudahMasuk", true);
                            editor.commit();

                            //Mengambil status konfirmasi
                            JSONArray dataResponse = response.getJSONArray("data");
                            JSONObject jsonObjekDataPenggunas = dataResponse.getJSONObject(0);

                            Integer statusKonfirmasi = jsonObjekDataPenggunas.getInt("status_konfirmasi");

                            if(statusKonfirmasi == 1){
                                finish();
                                //Memanggil activity dashboard apabila sudah dikonfirmasi statusnya
                                Intent intent = new Intent(getApplication(), Dashboard.class);
                                startActivity(intent);
                            }else if(statusKonfirmasi == 0){
                                finish();
                                //Pengaturan shared preferences

                                //Memanggil activity konfirmasi foto apabila belum dikonfirmasi statusnya
                                Intent intent = new Intent(getApplication(), PhotoConfirmation.class);
                                startActivity(intent);
                            }


                        }

                    }else{//Gagal mendaftarkan diri
                        progressDialog.dismiss();
                        String pesanGagal = response.getString("message");
                        final Toast toastGagalMendaftarkanDiri = Toast.makeText(MainActivity.this, pesanGagal, Toast.LENGTH_LONG);
                        toastGagalMendaftarkanDiri.show();
                        Log.d("[POST]","Pesan: "+pesanGagal);
                    }

                } catch (JSONException e) {
                    progressDialog.show();
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean apakahUserSudahMasuk(){
        SharedPreferences settings = getSharedPreferences(SessionPengguna, MODE_PRIVATE);
        Boolean sudahMasuk = settings.getBoolean("sudahMasuk",false);
        if(sudahMasuk == false){
            return false;
        }else{
            return true;
        }

    }
}
