package org.pptik.ilham.prdoc.AdapterCollections;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import org.pptik.ilham.prdoc.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan untuk menangani daftar bimbingan yang dilakukan oleh mahasiswa
 */

public class BimbinganRecyclerView extends RecyclerView.Adapter<BimbinganRecyclerView.ViewHolder> {
    public List<String> dataKoleksiJudulBimbingan;
    private String[] judul, materi, tanggalPembuatan;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewBimbingan, textViewMateri, textViewTanggalPembuatan;
        public RelativeTimeTextView relativeTimeTextView;
        public View viewSeparatorLine;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewBimbingan = (TextView)itemView.findViewById(R.id.textViewBimbingan);
            textViewMateri = (TextView)itemView.findViewById(R.id.textViewMateri);
            textViewTanggalPembuatan = (TextView)itemView.findViewById(R.id.textViewTanggal);
            relativeTimeTextView = (RelativeTimeTextView) itemView.findViewById(R.id.timestamp);
            viewSeparatorLine = (View) itemView.findViewById(R.id.separatorLine);
        }
    }

    public BimbinganRecyclerView(List<String> dataJudulBimbingan, List<String> dataNamaTopik, List<String> dataPembuatanBimbingan) {
        this.dataKoleksiJudulBimbingan = dataJudulBimbingan;

        judul =  new String[dataJudulBimbingan.size()];
        materi =  new String[dataNamaTopik.size()];
        tanggalPembuatan =  new String[dataPembuatanBimbingan.size()];

        judul = dataJudulBimbingan.toArray(judul);
        materi = dataNamaTopik.toArray(materi);
        tanggalPembuatan = dataPembuatanBimbingan.toArray(tanggalPembuatan);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_bimbingan, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textViewBimbingan.setText(judul[position]);
        holder.textViewMateri.setText(materi[position]);
        holder.textViewTanggalPembuatan.setText(tanggalPembuatan[position]);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            //parsedDate = dateFormat.parse(tanggalPembuatan[position]);
            String[] separated = tanggalPembuatan[position].split(" ");

            parsedDate = dateFormat.parse(separated[0]);
        } catch (ParseException e) {
            Log.e("Errornya",e.toString());
            e.printStackTrace();
        }

        holder.relativeTimeTextView.setReferenceTime(parsedDate.getTime());

        if(judul.length == (position+1)){
            holder.viewSeparatorLine.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return judul.length;
    }
}
