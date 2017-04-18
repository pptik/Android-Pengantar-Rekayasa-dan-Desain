package org.pptik.ilham.prdoc.AdapterCollections;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.pptik.ilham.prdoc.FragmentCollections.MaterialsFragment;
import org.pptik.ilham.prdoc.R;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan untuk menangani daftar materi yang akan disajikan kepada pengguna
 */

public class MaterialsRecyclerView extends RecyclerView.Adapter<MaterialsRecyclerView.ViewHolder> {
    private List<String> dataJudulMateri;
    private List<Integer> dataSubMateri;
    private List<String> dataCoverMateri;
    private String[] judul, cover;
    private Integer[] subMateri;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView, textView2;
        public ImageView imageView;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.namaMateri);
            textView2 = (TextView)itemView.findViewById(R.id.textViewSubMateri);
            imageView = (ImageView) itemView.findViewById(R.id.coverMateri);
            view = (View) itemView.findViewById(R.id.separatorLine);
        }
    }

    public MaterialsRecyclerView(List<String> dataBerupaJudul, List<String> dataBerupaCover, List<Integer> dataBerupaSubMateri) {
        this.dataJudulMateri = dataBerupaJudul;
        this.dataCoverMateri = dataBerupaCover;
        this.dataSubMateri = dataBerupaSubMateri;

        judul =  new String[dataJudulMateri.size()];
        cover =  new String[dataCoverMateri.size()];
        subMateri =  new Integer[dataSubMateri.size()];

        judul = dataBerupaJudul.toArray(judul);
        cover = dataBerupaCover.toArray(cover);
        subMateri = dataBerupaSubMateri.toArray(subMateri);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_materials, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.textView.setText(judul[position]);
        holder.textView2.setText(subMateri[position]+" Sub Materi");
        holder.imageView.setImageDrawable(null);

        Glide
                .with(holder.imageView.getContext())
                .load(cover[position])
                .crossFade()
                .placeholder(R.drawable.loading)
                .into(holder.imageView);

        if(judul.length == (position+1)){
            holder.view.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return judul.length;
    }
}
