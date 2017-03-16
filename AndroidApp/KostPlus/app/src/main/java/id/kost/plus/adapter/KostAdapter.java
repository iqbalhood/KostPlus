
/**
 * Created by Mohammad Iqbal on 9/7/2016.
 * Email : iqbalhood@gmail.com
 * Ini adalah fungsi setting adapter untuk menyiapkan data yang akan ditampilkan di
 * fragment
 */


package id.kost.plus.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import id.kost.plus.setget.Kost;
import id.kost.plus.R;


public class KostAdapter extends ArrayAdapter<Kost> {
    ArrayList<Kost> kostList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public KostAdapter(Context context, int resource, ArrayList<Kost> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        kostList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview    = (ImageView) v.findViewById(R.id.img_kost);
            holder.tvNama       = (TextView) v.findViewById(R.id.tvNama);
            holder.tvAlamat     = (TextView) v.findViewById(R.id.tvAlamat);
            holder.tvHarga     = (TextView) v.findViewById(R.id.tvHarga);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.drawable.placehold);
        Glide.with(v.getContext()).load(kostList.get(position).getGambar()).into(holder.imageview);
        holder.tvNama.setText(kostList.get(position).getNama());
        holder.tvAlamat.setText(kostList.get(position).getAlamat());
        holder.tvHarga.setText(kostList.get(position).getHarga());
        return v;

    }


    static class ViewHolder {
        public TextView  id;
        public ImageView imageview;
        public TextView  tvNama;
        public TextView  tvAlamat;
        public TextView  tvHarga;

    }












}
