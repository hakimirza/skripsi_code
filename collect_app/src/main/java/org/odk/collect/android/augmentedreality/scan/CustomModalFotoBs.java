package org.odk.collect.android.augmentedreality.scan;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.odk.collect.android.R;
import org.odk.collect.android.augmentedreality.koneksi.VolleySingletonImage;
import org.odk.collect.android.provider.InstanceProviderAPI;
import org.odk.collect.android.utilities.ApplicationConstants;

/**
 * Created by Septiawan Aji Pradan on 4/1/2017.
 */

public class CustomModalFotoBs extends Dialog{
    private Activity activity;
    private TextView lihatDetail;
    private ImageView fotoBangunan;
    private ImageLoader imageLoader;
    private String pathFoto;
    private Uri uri;

    public CustomModalFotoBs(Activity activity, String pathFoto, Uri uri){
        super(activity);
        this.activity = activity;
        this.pathFoto = pathFoto;
        this.uri = uri;
        Log.d("aji_custom",pathFoto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_foto_bs_modal);
        imageLoader = VolleySingletonImage.getInstance(activity).getImageLoader();
        fotoBangunan = (ImageView)findViewById(R.id.gambar_bangunan_sensus);
        lihatDetail = (TextView)findViewById(R.id.lihat_detail);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Log.d("aji_custom_22",pathFoto);
        options.inSampleSize = 8;
        if(!pathFoto.equals("")){
            final Bitmap bitmap = BitmapFactory.decodeFile(pathFoto, options);
            fotoBangunan.setImageBitmap(bitmap);
        }
        lihatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = activity.getIntent().getAction();
                activity.setResult(-1, new Intent().setData(uri));
                if (Intent.ACTION_PICK.equals(action)) {
                    // caller is waiting on a picked form
                    activity.setResult(-1, new Intent().setData(uri));
                } else {
                    // the form can be edited if it is incomplete or if, when it was
                    // marked as complete, it was determined that it could be edited
                    // later.

                    // caller wants to view/edit a form, so launch formentryactivity
                    Intent parentIntent = activity.getIntent();
                    Intent intent = new Intent(Intent.ACTION_EDIT, uri);
                    String formMode = parentIntent.getStringExtra(ApplicationConstants.BundleKeys.FORM_MODE);
                    if (formMode == null || ApplicationConstants.FormModes.EDIT_SAVED.equalsIgnoreCase(formMode)) {
                        intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.EDIT_SAVED);
                    } else {
                        intent.putExtra(ApplicationConstants.BundleKeys.FORM_MODE, ApplicationConstants.FormModes.VIEW_SENT);
                    }
                    activity.startActivity(intent);
                }
            }
        });

    }
}
