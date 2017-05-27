package org.odk.collect.android.augmentedreality;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.odk.collect.android.R;
import org.odk.collect.android.activities.MainMenuActivity;
import org.odk.collect.android.augmentedreality.DatabaseHandler;
import org.odk.collect.android.augmentedreality.formisian.BangunanSensusOnMaps;
import org.odk.collect.android.augmentedreality.formisian.SetLocationActivity;
import org.odk.collect.android.augmentedreality.scan.ARPortraitActivity;
import org.odk.collect.android.dao.InstancesDao;


public class MainActivity extends Activity implements View.OnClickListener {
//    private final static int REQUEST_CAMERA_PERMISSIONS_CODE = 11;
//    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 0;
    private RelativeLayout scanTempat,inputData,lihatPeta,editFoto;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        scanTempat = (RelativeLayout) findViewById(R.id.rl_scan_tempat);
        inputData = (RelativeLayout) findViewById(R.id.rl_input_data);
        lihatPeta = (RelativeLayout) findViewById(R.id.rl_peta);
        databaseHandler = new DatabaseHandler(getApplicationContext());
        scanTempat.setOnClickListener(this);
        inputData.setOnClickListener(this);
        lihatPeta.setOnClickListener(this);
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

}
