package org.pptik.ilham.prdoc.FragmentCollections;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pptik.ilham.prdoc.AdapterCollections.BimbinganRecyclerView;
import org.pptik.ilham.prdoc.GantiFotoProfil;
import org.pptik.ilham.prdoc.MainActivity;
import org.pptik.ilham.prdoc.ProfileEdit;
import org.pptik.ilham.prdoc.R;
import org.pptik.ilham.prdoc.RestClientApp;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai penampilan data profil pengguna
 */

public class ProfileFragment extends android.support.v4.app.Fragment {
    //CircleImageView ;
    TextView textViewFullName, textViewEmail, textViewUsername, textViewNIM, textViewUniversitas, textViewTopikYangSudahDikerjakan;
    String namaDepan, namaBelakang, email, username, nim, photo;
    Integer universitas,idPengguna;
    ProgressDialog progressDialog;
    ImageView imageViewUbahProfile, circleImageView;
    SwipeRefreshLayout swipeRefreshLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        circleImageView = (CircleImageView) v.findViewById(R.id.imageViewProfile);
        textViewFullName = (TextView) v.findViewById(R.id.textViewFullName);
        textViewEmail = (TextView) v.findViewById(R.id.textViewEmail);
        textViewUsername = (TextView) v.findViewById(R.id.textViewUsername);
        textViewNIM= (TextView) v.findViewById(R.id.textViewNomorInduk);
        textViewUniversitas= (TextView) v.findViewById(R.id.textViewUniversitas);
        textViewTopikYangSudahDikerjakan = (TextView) v.findViewById(R.id.textViewTopikYangSudahDikerjakan);
        imageViewUbahProfile = (ImageView) v.findViewById(R.id.add_friend);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

        //Ambil dari shared preferences
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SessionPengguna, MODE_PRIVATE);
        email = settings.getString("email", null);
        username = settings.getString("username", null);
        photo = settings.getString("profilePicture", null);
        namaDepan = settings.getString("namaDepan", null);
        namaBelakang = settings.getString("namaBelakang", null);
        nim = settings.getString("nim", null);
        universitas = settings.getInt("universitas", 0);
        idPengguna = settings.getInt("idPengguna", 0);

        textViewFullName.setText(namaDepan+" "+namaBelakang);
        textViewEmail.setText(email);
        textViewNIM.setText(nim);
        textViewUsername.setText("@"+username);

        imageViewUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileEdit.class);
                startActivity(intent);
            }
        });

        if(apakahTerhubungDenganInternet() == true){
            progressDialog = new ProgressDialog(getContext());
            //this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
            this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
            this.progressDialog.show();
            getNamaUniversitas(universitas);
            getTopikYangSudahDikerjakan();
            progressDialog.dismiss();
        }else{
            Toast.makeText(getContext(), "Gagal memuat data. Mohon cek kembali koneksi internet anda.", Toast.LENGTH_LONG).show();
        }

        Picasso.with(getContext()).load(photo).placeholder(R.drawable.loading).into(circleImageView);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Dialog disini
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah anda ingin mengganti foto profile?")
                        .setCancelable(true)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent intent = new Intent(getContext(), GantiFotoProfil.class);
                                startActivity(intent);

                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Log.d("Dialog","Tidak");
                    }
                });

                builder.create().show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SessionPengguna, MODE_PRIVATE);
                email = settings.getString("email", null);
                username = settings.getString("username", null);
                photo = settings.getString("profilePicture", null);
                namaDepan = settings.getString("namaDepan", null);
                namaBelakang = settings.getString("namaBelakang", null);
                nim = settings.getString("nim", null);
                universitas = settings.getInt("universitas", 0);
                idPengguna = settings.getInt("idPengguna", 0);

                textViewFullName.setText(namaDepan+" "+namaBelakang);
                textViewEmail.setText(email);
                textViewNIM.setText(nim);
                textViewUsername.setText("@"+username);

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return v;
    }

    public final boolean apakahTerhubungDenganInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getNamaUniversitas(Integer idUniversitas){
        RequestParams params = new RequestParams();
        params.put("id", idUniversitas);

        RestClientApp.post("nama_universitas", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("[Pesan]", "Sukses array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("[Pesan]", "Sukses objek");
                //dataSetJudulMateri.add("tes");
                try {
                    Log.d("[Pesan]", "Sukses TRY objek");
                    String rc = response.getString("RC");

                    if (rc.equals("00")) {//Berhasil memanggil data
                        //Menguraikan data universitas
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObjekNamaUniversitas = data.getJSONObject(i);
                            textViewUniversitas.setText(jsonObjekNamaUniversitas.getString("nama_depan")+" "+jsonObjekNamaUniversitas.getString("nama_belakang"));
                            Log.d("AAA",jsonObjekNamaUniversitas.getString("nama_depan")+" "+jsonObjekNamaUniversitas.getString("nama_belakang"));
                        }

                    } else {//Gagal mengambil data

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void getTopikYangSudahDikerjakan(){
        RequestParams params = new RequestParams();
        params.put("id", idPengguna);

        RestClientApp.post("topik_yang_sudah_dikerjakan", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("[Pesan]", "Sukses array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("[Pesan]", "Sukses objek");
                //dataSetJudulMateri.add("tes");
                try {
                    Log.d("[Pesan]", "Sukses TRY objek");
                    String rc = response.getString("RC");

                    if (rc.equals("00")) {//Berhasil memanggil data
                        //Menguraikan data universitas
                        JSONArray data = response.getJSONArray("data");
                        String koleksiTopikYangSudahDikerjakan = "";

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObjekNamaTopik = data.getJSONObject(i);
                            koleksiTopikYangSudahDikerjakan += (i+1)+". "+jsonObjekNamaTopik.getString("nama_topik")+"\n\n";
                        }

                        textViewTopikYangSudahDikerjakan.setText(koleksiTopikYangSudahDikerjakan);
                    } else {//Gagal mengambil data

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
