package org.odk.collect.android.augmentedreality.scan;

import android.database.DatabaseErrorHandler;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.odk.collect.android.R;
import org.odk.collect.android.aksesdata.AksesDataOdk;
import org.odk.collect.android.aksesdata.Instances;
import org.odk.collect.android.aksesdata.ParsingInstances;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.augmentedreality.BangunanSensus;
import org.odk.collect.android.augmentedreality.DatabaseHandler;
import org.odk.collect.android.augmentedreality.arkit.PARController;
import org.odk.collect.android.augmentedreality.arkit.PARFragment;
import org.odk.collect.android.augmentedreality.arkit.PARPoiLabel;
import org.odk.collect.android.augmentedreality.arkit.PARPoiLabelAdvanced;
import org.odk.collect.android.augmentedreality.arkit.StikerLabel;
import org.odk.collect.android.augmentedreality.sensorkit.PSKDeviceAttitude;
import org.odk.collect.android.augmentedreality.sensorkit.enums.PSKDeviceOrientation;
import org.odk.collect.android.logic.FormController;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by doPanic on 21.02.14.
 * Fragment providing AR functionality
 * configure and add content here
 */
public class PanicARFragment extends PARFragment {

    private static ArrayList<PARPoiLabel> labelRepo = new ArrayList<PARPoiLabel>();
    private DatabaseHandler databaseHandler;
    private ArrayList<BangunanSensus> bangunanSensusArrayList;


    private AksesDataOdk aksesDataOdk;
    private ParsingInstances parsingInstances;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // add content using helper methods defined below
        // example to add costume drawable
        databaseHandler = new DatabaseHandler(getActivity());
        bangunanSensusArrayList = databaseHandler.getAll();

        aksesDataOdk = new AksesDataOdk();
        parsingInstances = new ParsingInstances();

        if(aksesDataOdk != null || aksesDataOdk.getKeteranganInstances().size()!=0){

            for (Instances instances : aksesDataOdk.getKeteranganInstances()){
                try{

                    BangunanSensus bangunanSensus = parsingInstances.parseXml(instances.getPathInstances());

                    String imgPath= aksesDataOdk.getParentDir(instances.getPathInstances())+File.separator+bangunanSensus.getPathFoto();
                    Log.d("aji_path_foto",imgPath);
                    Log.d("aji_bangunan",bangunanSensus.getSls());
                    Log.d("aji_location",""+bangunanSensus.getLat());

                    ArrayList<String> parameter = new ArrayList<>();
                    Location location = new Location("location");
                    location.setLatitude(bangunanSensus.getLat());
                    location.setLongitude(bangunanSensus.getLon());

                    parameter.add(bangunanSensus.getSls());
                    parameter.add(bangunanSensus.getNoFisik());
                    parameter.add(bangunanSensus.getNoSensus());
                    parameter.add(imgPath);
                    parameter.add("");

                    Log.d("aji_path_foto",imgPath);
                    Log.d("aji_location",""+location.getLatitude());

                    PARController.getInstance().addPoi( createStiker(parameter,location));
                }catch (Exception e){
                    Log.d("aji_eror",e.toString());
                }

            }
        }else{

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // FIRST: setup default resource IDs
        // IMPORTANT: call before super.onCreate()
        this.viewLayoutId = R.layout.panicar_view;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getRadarView().setRadarRange(500);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_random_poi:
                int random = (new Random().nextInt(labelRepo.size()-1)+0);
                PARController.getInstance().addPoi(labelRepo.get(random));
                Toast.makeText(this.getActivity(),"Added: " + labelRepo.get(random).getTitle(), Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            case  R.id.action_add_cardinal_pois:
                createCDPOIs();
                return super.onOptionsItemSelected(item);
            case R.id.action_delete_last_poi:
                if (PARController.getInstance().numberOfObjects() > 0){
                    int lastObject = PARController.getInstance().numberOfObjects()-1;
                    Toast.makeText(this.getActivity(),"Removing: " + ((PARPoiLabel)PARController.getInstance().getObject(lastObject)).getTitle(), Toast.LENGTH_SHORT).show();
                    PARController.getInstance().removeObject(lastObject);
                }
                return super.onOptionsItemSelected(item);
            case R.id.action_delete_all_pois:
                PARController.getInstance().clearObjects();
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    //==============================================================================================
    // Callback
    //==============================================================================================
    @Override
    public void onDeviceOrientationChanged(PSKDeviceOrientation newOrientation) {
        super.onDeviceOrientationChanged(newOrientation);
        Toast.makeText(getActivity(), "onDeviceOrientationChanged: " + PSKDeviceAttitude.rotationToString(newOrientation), Toast.LENGTH_LONG).show();
    }

    //==============================================================================================
    // Create Label
    //==============================================================================================
    /**
     * Create a poi with title, description and position
     * @return PARPoiLabel which is a subclass of PARPoi (extended for title, description and so on)
     */
//    public PARPoiLabel createPoi(String title, String description, double lat, double lon, final String pathFoto) {
//        Location poiLocation = new Location(title);
//        poiLocation.setLatitude(lat);
//        poiLocation.setLongitude(lon);
//
//        final PARPoiLabel parPoiLabel = new PARPoiLabel(poiLocation, title, description, R.layout.sticker_label, R.drawable.ic_dot_blue,pathFoto);
//
//        parPoiLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getActivity(), parPoiLabel.getTitle() + " - " + parPoiLabel.getDescription(), Toast.LENGTH_LONG).show();
//                CustomModalFotoBs customModalScan = new CustomModalFotoBs(getActivity(),parPoiLabel.getTitle(),pathFoto);
//                customModalScan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                customModalScan.show();
//            }
//        });
//
//        return parPoiLabel;
//    }

    public StikerLabel createStiker(final ArrayList<String> bangunansensus, Location location){
        Log.d("aji_bangunan_sensus",bangunansensus.get(3));
        final StikerLabel stiker = new StikerLabel(location,bangunansensus,R.layout.stiker_label_2,R.drawable.ic_dot_blue);

        stiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("aji_bangunan_sensus_67",bangunansensus.get(3));
                CustomModalFotoBs customModalScan = new CustomModalFotoBs(getActivity(),bangunansensus.get(0),bangunansensus.get(3));
                customModalScan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customModalScan.show();
            }
        });
        return stiker;
    }

    /**
     * Create a poi with title, description and position
     *
     * @param title       Title of poi
     * @param description Description of poi (if you want none, set this to "")
     * @param lat         Latitude of poi
     * @param lon         Longitude of poi
     * @return PARPoiLabelAdvanced which is a subclass of PARPoiLabel (extended for altitude support)
     */
    public PARPoiLabelAdvanced createPoi(String title, String description, double lat, double lon, double alt) {
        Location poiLocation = new Location(title);
        poiLocation.setLatitude(lat);
        poiLocation.setLongitude(lon);
        poiLocation.setAltitude(alt);

        final PARPoiLabelAdvanced parPoiLabel = new PARPoiLabelAdvanced(poiLocation, title, description, R.layout.sticker_label, R.drawable.radar_dot);
        parPoiLabel.setIsAltitudeEnabled(true);
        parPoiLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), parPoiLabel.getTitle() + " - " + parPoiLabel.getDescription(), Toast.LENGTH_LONG).show();
            }
        });

        return parPoiLabel;
    }

    //==============================================================================================
    // Helper methods
    //==============================================================================================
    private PARPoiLabelAdvanced createRepoPoi(
            String title,
            String description,
            double latitude,
            double longitude,
            double altitude) {
        return createPoi(title, description, latitude, longitude,altitude);
    }



    /**
     * adds 4 points in cardinal direction at current location
     */
    private void createCDPOIs(){

        double degreeCorrection = 0.1;    // approx 7700 meter
        //double degreeCorrection = 0.01; // approx 1000 meter
        //double degreeCorrection = 0.001; // approx 121 meter
        //double degreeCorrection = 0.0001; // approx 12 meter
        Location currentLocation = PSKDeviceAttitude.sharedDeviceAttitude().getLocation();

//        PARController.getInstance().addPoi(createPoi("North", "", currentLocation.getLatitude()+degreeCorrection, currentLocation.getLongitude(),""));
//        PARController.getInstance().addPoi(createPoi("South", "", currentLocation.getLatitude()-degreeCorrection, currentLocation.getLongitude(),""));
//        PARController.getInstance().addPoi(createPoi("West", "",  currentLocation.getLatitude(),                  currentLocation.getLongitude()-degreeCorrection,""));
//        PARController.getInstance().addPoi(createPoi("East", "",  currentLocation.getLatitude(),                  currentLocation.getLongitude()+degreeCorrection,""));
    }
}
