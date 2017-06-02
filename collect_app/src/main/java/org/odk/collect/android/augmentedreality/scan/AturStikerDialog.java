package org.odk.collect.android.augmentedreality.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.odk.collect.android.R;
import org.odk.collect.android.aksesdata.ParsingForm;
import org.odk.collect.android.augmentedreality.TooltipWindow.TooltipWindow;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 6/2/2017.
 */

public class AturStikerDialog extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout rl_1,rl_2,rl_3;
    private TextView tv_1,tv_2,tv_3;
    TooltipWindow tooltipWindow;
    private ParsingForm parsingForm;
    int def;
    String pathForm;
    ArrayList<String> kec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_ket_stiker);
        declaration();
        pathForm = getIntent().getStringExtra("path_form");

    }

    public void declaration(){
        rl_1 = (RelativeLayout)findViewById(R.id.rl_kolom_1);
        rl_2 = (RelativeLayout)findViewById(R.id.rl_kolom_2);
        rl_3 = (RelativeLayout)findViewById(R.id.rl_kolom_3);
        tv_1 = (TextView)findViewById(R.id.kolom_1);
        tv_2 = (TextView)findViewById(R.id.kolom_2);
        tv_3 = (TextView)findViewById(R.id.kolom_3);

        tooltipWindow = new TooltipWindow(AturStikerDialog.this);
        parsingForm = new ParsingForm();
        kec = new ArrayList<>();
        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==rl_1){
            pilihanVariabel(kec,v);

        }else if(v==rl_2){
            pilihanVariabel(kec,v);
        }else if(v==rl_3){
            pilihanVariabel(kec,v);
        }
    }

    public ArrayList<String> getKeyForm (){
        Log.d("wulan_path",pathForm);
        ArrayList<String> keyForm = new ArrayList<>();
        keyForm = parsingForm.getVariabelForm(pathForm);
        Log.d("wulan_key",keyForm.toString());
        return keyForm;
    }

    public void pilihanVariabel(ArrayList<String> kecuali,final View v){
        String[] variabel = new String[getKeyForm().size()];
//        for (int i=0;i<getKeyForm().size();i++){
//                if(kecuali != null){
//                    for (int j=0;j<kecuali.size();i++){
//                        if(!getKeyForm().get(i).equals(kecuali.get(j))){
//                            variabel[i] = getKeyForm().get(i);
//                        }
//                    }
//                }else{
//                    variabel[i] = getKeyForm().get(i);
//                }
//            }

        for (int i=0;i<getKeyForm().size();i++) {
            if(!getKeyForm().get(i).equals("foto_bangunan") || !getKeyForm().get(i).equals("location")){
                variabel[i] = getKeyForm().get(i);
            }

        }


            Log.d("wulan_kecuali",kec.toString());

            def = 0;

            AlertDialog dialog = new AlertDialog.Builder(AturStikerDialog.this)
                    .setTitle("Atur Variabel")
                    .setSingleChoiceItems(variabel, 0,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            def = which;
                        }
                    })
                    .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!tooltipWindow.isTooltipShown()) {
                                tooltipWindow.showToolTip(v);
                            }else{
                                tooltipWindow.dismissTooltip();
                            }
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            dialog.show();
    }

}
