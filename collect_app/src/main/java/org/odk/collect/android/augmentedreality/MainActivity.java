package org.odk.collect.android.augmentedreality;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.odk.collect.android.R;
import org.odk.collect.android.activities.ListFormForDownload;
import org.odk.collect.android.activities.MainMenuActivity;
import org.odk.collect.android.aksesdata.AksesDataOdk;
import org.odk.collect.android.aksesdata.Instances;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.augmentedreality.DatabaseHandler;
import org.odk.collect.android.augmentedreality.formisian.BangunanSensusOnMaps;
import org.odk.collect.android.augmentedreality.formisian.SetLocationActivity;
import org.odk.collect.android.augmentedreality.scan.ARPortraitActivity;
import org.odk.collect.android.dao.InstancesDao;
import org.odk.collect.android.downloadinstance.Download;
import org.odk.collect.android.downloadinstance.DownloadInstances;
import org.odk.collect.android.downloadinstance.listener.DownloadPcl;
import org.odk.collect.android.koneksi.AlamatServer;
import org.odk.collect.android.utilities.ToastUtils;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener,DownloadPcl {
//    private final static int REQUEST_CAMERA_PERMISSIONS_CODE = 11;
//    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 0;
    private RelativeLayout scanTempat,inputData,lihatPeta,editFoto;
    private Button downnload;
    private DatabaseHandler databaseHandler;
    private ListFormForDownload listFormForDownload;
    private static final Object bb= new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        scanTempat = (RelativeLayout) findViewById(R.id.rl_scan_tempat);
        inputData = (RelativeLayout) findViewById(R.id.rl_input_data);
        lihatPeta = (RelativeLayout) findViewById(R.id.rl_peta);
        downnload = (Button)findViewById(R.id.download_briefcase_bt);
        databaseHandler = new DatabaseHandler(getApplicationContext());

        listFormForDownload = new ListFormForDownload();
        scanTempat.setOnClickListener(this);
        inputData.setOnClickListener(this);
        lihatPeta.setOnClickListener(this);
        downnload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view==scanTempat){
//            if(Permission.cekCameraPermission(this)){
//                if(Permission.cekLocationPermission(this)){
                    intent = new Intent(this, ARPortraitActivity.class);
                    startActivity(intent);
//                }else{
//                    Permission.requestLocationPermission(this);
//                }
//            }else{
//                Permission.requestCameraPermission(this);
//            }

        }else if(view==inputData){
//            if(Permission.cekLocationPermission(this)){
                intent = new Intent(this, SetLocationActivity.class);
                startActivity(intent);
//            }else{
//                Permission.requestLocationPermission(this);
//            }

        }else if (view==lihatPeta){
            if(databaseHandler.getAll().isEmpty()){
                Toast.makeText(this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
            }else{
                intent = new Intent(this, BangunanSensusOnMaps.class);
                startActivity(intent);
            }
        }else if(view==downnload){
            downloadInstancesFromServer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_odk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_to_odk:
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //    public void startARLandscape(View view) {
//        Intent intent = new Intent(this, ARLandscapeActivity.class);
//        startActivity(intent);
//    }
//public void startARAutoOrienting(View view) {
//    Intent intent = new Intent(this, ARAutoOrientingActivity.class);
//    startActivity(intent);
//}


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onpostdownload(boolean mboolean, Download download) {
        if(mboolean){
            Toast.makeText(this, "File Download Completed", Toast.LENGTH_SHORT)
                    .show();
        }else{
            Toast.makeText(this,"File Download not Completed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void startDownload(Download download){
        Log.d("aji","lewat");
        synchronized (bb) {
            DownloadInstances downloadIsian = new DownloadInstances(download, MainActivity.this, this);
            downloadIsian.exscute();
        }
    }

    public void downloadInstancesFromServer(){
        StringRequest downloadFromServer = new StringRequest(Request.Method.GET, AlamatServer.ALAMAT_SERVER + AlamatServer.GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    AksesDataOdk aksesDataOdk = new AksesDataOdk();
                    if(jsonObject.getString("status").equals("true")){
                        Log.d("aji",response);
                        JSONArray jsonArray = jsonObject.getJSONArray("instances");
                        for(int i=0;i<jsonArray.length();i++){
                            Download download = new Download();
                            JSONObject uuid = jsonArray.getJSONObject(i);
                            download.setUuid(uuid.getString("uuid"));
                            download.setFormId(uuid.getString("form_id"));
                            download.setFormPath(aksesDataOdk.getKeteranganFormbyId(uuid.getString("form_id")));
                            Log.d("aji_hasil_uuid",download.getUuid().toString());
                            Log.d("aji_hasil_form",download.getFormId().toString());
                            Log.d("aji_hasil_Path",download.getFormPath().toString());
                            startDownload(download);
                        }
                    }
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Collect.getInstance2().addToRequestQueue(downloadFromServer);
    }
}
