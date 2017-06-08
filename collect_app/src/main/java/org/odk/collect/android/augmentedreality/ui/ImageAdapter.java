package org.odk.collect.android.augmentedreality.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.odk.collect.android.R;
import org.odk.collect.android.augmentedreality.scan.CustomModalFotoBs;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 6/7/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> pathFotos;
    private Activity activity;
    private ArrayList<Uri> uris;

    public ImageAdapter(Context context, ArrayList<String> pathFotos, Activity activity, ArrayList<Uri> uris){
        this.context = context;
        this.pathFotos = pathFotos;
        this.activity = activity;
        this.uris = uris;
    }

    @Override
    public int getCount() {
        return pathFotos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.grid_view_halaman_utama, null);
        } else {
            grid = (View) convertView;
        }



        ImageView imageView = (ImageView)grid.findViewById(R.id.foto_bangunan_hal_utama);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeFile(pathFotos.get(position), options);
        imageView.setImageBitmap(bitmap);

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomModalFotoBs customModalScan = new CustomModalFotoBs(activity,pathFotos.get(position),uris.get(position));
                customModalScan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customModalScan.show();
            }
        });

        return grid;
    }
}
