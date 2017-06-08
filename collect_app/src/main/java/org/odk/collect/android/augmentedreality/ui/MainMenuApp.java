package org.odk.collect.android.augmentedreality.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.odk.collect.android.R;
import org.odk.collect.android.augmentedreality.Bangunan;
import org.odk.collect.android.augmentedreality.DatabaseHandler;
import org.odk.collect.android.augmentedreality.aksesdata.AksesDataOdk;
import org.odk.collect.android.augmentedreality.aksesdata.Form;
import org.odk.collect.android.augmentedreality.aksesdata.Instances;
import org.odk.collect.android.augmentedreality.aksesdata.ParsingInstances;
import org.odk.collect.android.augmentedreality.arkit.PARController;
import org.odk.collect.android.augmentedreality.scan.ARActivity;
import org.odk.collect.android.augmentedreality.scan.ARPortraitActivity;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 6/3/2017.
 */

public class MainMenuApp extends AppCompatActivity implements View.OnClickListener {
    private BoomMenuButton bmb;
    private ArrayList<Pair> piecesAndButtons = new ArrayList<>();
    private LisfFormAdapter lisfFormAdapter;
    private ArrayList<Form> forms;
    private RecyclerView recyclerView;
    private ExpandGridView gridView;
    private ImageView sortImage;
    private AksesDataOdk aksesDataOdk;
    private int def;
    private DatabaseHandler databaseHandler;
    private ParsingInstances parsingInstances;
    private ArrayList<Uri> urisGlobal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_skripsi);

        forms = new ArrayList<>();
        aksesDataOdk = new AksesDataOdk();
        databaseHandler = new DatabaseHandler(getApplicationContext());
        parsingInstances = new ParsingInstances();

//        recyclerView = (RecyclerView)findViewById(R.id.list_form_main);
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum((PiecePlaceEnum.HAM_2));
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        sortImage = (ImageView)findViewById(R.id.sort_foto);

        gridView = (ExpandGridView) findViewById(R.id.grid_view);
        gridView.setExpanded(true);
        gridView.setFocusable(false);
        gridView.setAdapter(new ImageAdapter(getApplicationContext(),setDataAwal(),MainMenuApp.this,urisGlobal));

        HamButton.Builder builder1 = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_camera)
                .normalText("Scan Bangunan")
                .normalTextColor(Color.WHITE)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(getApplicationContext(), ARPortraitActivity.class);
                        intent.putExtra("path_form","");
                        startActivity(intent);
                    }
                });
        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_input_data)
                .normalText("Petunjuk")
                .normalTextColor(Color.WHITE)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
//                        Intent intent = new Intent(getApplicationContext(), ARPortraitActivity.class);
//                        startActivity(intent);
                        Toast.makeText(MainMenuApp.this, "About", Toast.LENGTH_SHORT).show();
                    }
                });
        bmb.addBuilder(builder1);
        bmb.addBuilder(builder2);

        sortImage.setOnClickListener(this);

    }

    public ArrayList<String> setDataAwal(){
        ArrayList<String> pathFotos = new ArrayList<>();
        ArrayList<Uri> uris = new ArrayList<>();
        for(int i=0;i<aksesDataOdk.getKeteranganForm().size();i++){
            ArrayList<Instances> getInstancesByIdForm = new ArrayList<>();
            ArrayList<String> key = databaseHandler.getAll(aksesDataOdk.getKeteranganForm().get(i).getIdForm());
            ArrayList<Bangunan> bangunanArrayList = new ArrayList<>();
            getInstancesByIdForm = aksesDataOdk.getKeteranganInstancesbyIdForm(aksesDataOdk.getKeteranganForm().get(i).getIdForm());
            key.add("foto_bangunan");
            key.add("location");
            Log.d("cinta",key.toString());
            for (Instances instances : getInstancesByIdForm){
                try{
                    bangunanArrayList.add(parsingInstances.getValueHasMap(instances.getPathInstances(),key));
                    uris.add(instances.getUri());
                    Log.d("cinta_size",""+instances.getUri().toString());
                }catch (Exception e){

                }
            }
            for (Bangunan bangunan : bangunanArrayList){
                pathFotos.add(bangunan.getPathFoto());
            }
        }

        getUri(uris);

        Log.d("wulan_07",pathFotos.toString());
        return pathFotos;
    }

    public ArrayList<Uri> getUri(ArrayList<Uri> uris){
        urisGlobal = uris;
        return urisGlobal;
    }

    public ArrayList<String> setDatabyIdForm(String formId){
        ArrayList<String> pathFotos = new ArrayList<>();
        ArrayList<Instances> getInstancesByIdForm = new ArrayList<>();
        ArrayList<String> key = databaseHandler.getAll(formId);
        ArrayList<Bangunan> bangunanArrayList = new ArrayList<>();
        getInstancesByIdForm = aksesDataOdk.getKeteranganInstancesbyIdForm(formId);
        key.add("foto_bangunan");
        key.add("location");
        Log.d("cinta",key.toString());
        for (Instances instances : getInstancesByIdForm){
            try{
                bangunanArrayList.add(parsingInstances.getValueHasMap(instances.getPathInstances(),key));
                Log.d("cinta_size",""+bangunanArrayList.size());
            }catch (Exception e){

            }
        }
        for (Bangunan bangunan : bangunanArrayList){
            pathFotos.add(bangunan.getPathFoto());
        }

        return pathFotos;
    }

    public void pilihForm(){
        String[] pilihan = new String[aksesDataOdk.getKeteranganForm().size()];
        for (int i=0;i<aksesDataOdk.getKeteranganForm().size();i++){
            pilihan[i] = aksesDataOdk.getKeteranganForm().get(i).getDisplayName();
        }

        def = 0;

        AlertDialog dialog = new AlertDialog.Builder(MainMenuApp.this)
                .setTitle("Tampilkan foto berdasarkan kuesioner")
                .setSingleChoiceItems(pilihan, 0,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        def = which;
                    }
                })
                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String formId = aksesDataOdk.getKeteranganForm().get(def).getIdForm();
                        gridView.setAdapter(new ImageAdapter(getApplicationContext(),setDatabyIdForm(formId),MainMenuApp.this,urisGlobal));
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if(v==sortImage){
            pilihForm();
        }
    }

    //    public void formLayout(){
//        lisfFormAdapter = new LisfFormAdapter(forms);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(lisfFormAdapter);
//    }
}
