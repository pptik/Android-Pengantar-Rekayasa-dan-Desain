package org.pptik.ilham.prdoc.FragmentCollections;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pptik.ilham.prdoc.AdapterCollections.BimbinganRecyclerView;
import org.pptik.ilham.prdoc.AdapterCollections.MaterialsRecyclerView;
import org.pptik.ilham.prdoc.MainActivity;
import org.pptik.ilham.prdoc.R;
import org.pptik.ilham.prdoc.RestClientApp;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai bagian dari penampilan fungsi daftar bimbingan(recycle view)
 */

public class BimbinganFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<String> dataSetJudulBimbingan = new ArrayList<>();
    List<String> dataSetJudulMateri = new ArrayList<>();
    List<String> dataSetTanggalPembuatan = new ArrayList<>();
    ProgressDialog progressDialog;
    Integer idUser;

    public BimbinganFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bimbingan, container, false);


        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SessionPengguna, MODE_PRIVATE);
        idUser = settings.getInt("idPengguna", 0);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Mencek apakah terhubung dengan internet?
        if (apakahTerhubungDenganInternet() == true) {


            if (dataSetJudulBimbingan.size() == 0) {

                progressDialog = new ProgressDialog(getContext());
                this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
                this.progressDialog.show();
                initDataSet(idUser);
                this.progressDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Daftar berhasil diperbaharui", Toast.LENGTH_SHORT).show();

                dataSetJudulBimbingan.clear();
                progressDialog = new ProgressDialog(getContext());
                this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
                this.progressDialog.show();
                initDataSet(idUser);//Diupdate kembali data topiknya
                this.progressDialog.dismiss();
            }


        }
        return view;
    }

    public final boolean apakahTerhubungDenganInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void initDataSet(Integer idUser) {

        final Toast toast = Toast.makeText(getContext(), "Gagal mengambil data. Cek kembali koneksi internet anda.", Toast.LENGTH_LONG);



        RequestParams params = new RequestParams();
        params.put("id", idUser);

        RestClientApp.post("judul_bimbingan", params, new JsonHttpResponseHandler() {



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


                    Log.d("[Pesan]", "Nilai RC:" + rc);
                    if (rc.equals("00")) {//Berhasil memanggil data
                        //Menguraikan data universitas
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObjekDataMateri = data.getJSONObject(i);

                            dataSetJudulBimbingan.add(jsonObjekDataMateri.getString("judul"));
                            dataSetJudulMateri.add(jsonObjekDataMateri.getString("nama_topik"));
                            dataSetTanggalPembuatan.add(jsonObjekDataMateri.getString("created_at"));


                            mAdapter = new BimbinganRecyclerView(dataSetJudulBimbingan,dataSetJudulMateri,dataSetTanggalPembuatan);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);

                        }
                        //progressDialog.dismiss();
                    } else {//Gagal mengambil data
                        //progressDialog.dismiss();
                        toast.show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

                //mRecyclerView.noti
            }
        });
    }

}
