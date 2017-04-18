package org.pptik.ilham.prdoc.FragmentCollections;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.pptik.ilham.prdoc.AdapterCollections.MaterialsRecyclerView;
import org.pptik.ilham.prdoc.R;
import org.pptik.ilham.prdoc.RestClientApp;
import org.pptik.ilham.prdoc.UserRegistrationActivity;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai bagian dari penampilan fungsi materi(recycle view)
 */

public class MaterialsFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<String> dataSetJudulMateri = new ArrayList<>();
    List<String> dataSetCoverMateri = new ArrayList<>();
    List<Integer> dataSetSubJudulMateri = new ArrayList<>();

    ProgressDialog progressDialog;

    public MaterialsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_materials, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);



        //Mencek apakah terhubung dengan internet?
        if (apakahTerhubungDenganInternet() == true) {


            if (dataSetJudulMateri.size() == 0) {

                progressDialog = new ProgressDialog(getContext());
                this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
                this.progressDialog.show();
                initDataSet();

            } else {
                //Toast.makeText(getContext(), "Daftar berhasil diperbaharui", Toast.LENGTH_SHORT).show();

                dataSetJudulMateri.clear();
                dataSetCoverMateri.clear();
                dataSetSubJudulMateri.clear();

                progressDialog = new ProgressDialog(getContext());
                this.progressDialog.setMessage("Mohon tunggu, sedang mengambil data ...");
                this.progressDialog.show();
                initDataSet();//Diupdate kembali data topiknya
            }


        } else {
            Toast.makeText(getContext(), "Mohon cek kembali koneksi internet anda.", Toast.LENGTH_LONG).show();
        }

        return view;

    }

    public boolean apakahTerhubungDenganInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void initDataSet() {
        //dataSetJudulMateri.add("tes");
        final Toast toast = Toast.makeText(getContext(), "Gagal mengambil data. Cek kembali koneksi internet anda.", Toast.LENGTH_LONG);

        RestClientApp.get("dashboard_topics", null, new JsonHttpResponseHandler() {

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


                            dataSetCoverMateri.add(jsonObjekDataMateri.getString("thumbnail"));
                            dataSetJudulMateri.add(jsonObjekDataMateri.getString("nama_topik"));
                            dataSetSubJudulMateri.add(1);
                            Integer idTopik = jsonObjekDataMateri.getInt("id");

                            //ambilJumlahTopik(idTopik);

                            mAdapter = new MaterialsRecyclerView(dataSetJudulMateri, dataSetCoverMateri,dataSetSubJudulMateri);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);

                        }
                        progressDialog.dismiss();
                    } else {//Gagal mengambil data
                        progressDialog.dismiss();
                        toast.show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        });
    }

    public void ambilJumlahTopik(Integer id_topik){
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id_topik);

        RestClientApp.post("jumlah_sub_topik_dashboard", requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("[Pesan]", "Sukses array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("A","ada!");
                //dataSetJudulMateri.add("tes");
                try {
                    Log.d("[Pesan]", "Sukses TRY objek");
                    String rc = response.getString("RC");


                    Log.d("[Pesan]", "Nilai RC:" + rc);
                    if (rc.equals("00")) {//Berhasil memanggil data
                        Integer jumlahSubTopik = response.getInt("data");
                        dataSetSubJudulMateri.add(jumlahSubTopik);

                        mAdapter = new MaterialsRecyclerView(dataSetJudulMateri, dataSetCoverMateri,dataSetSubJudulMateri);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);


                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        });
    }


}
