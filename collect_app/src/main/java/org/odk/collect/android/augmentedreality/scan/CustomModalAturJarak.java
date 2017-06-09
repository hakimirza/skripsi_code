package org.odk.collect.android.augmentedreality.scan;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;

import org.odk.collect.android.R;

/**
 * Created by Septiawan Aji Pradan on 6/9/2017.
 */

public class CustomModalAturJarak extends Dialog {
    private Activity activity;
    private int jarak;
    private SeekBar seekBar;
    private TextView ok,hasilJarak;

    OnMyDialogAturJarakResult onMyDialogAturJarakResult;

    public CustomModalAturJarak(Activity activity){
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_modal_atur_jarak);
        seekBar = (SeekBar)findViewById(R.id.seek_bar);
        ok = (TextView)findViewById(R.id.ok_jarak_tv);
        hasilJarak = (TextView)findViewById(R.id.hasil_jarak);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                jarak = progressChangedValue;
                hasilJarak.setVisibility(View.VISIBLE);
                hasilJarak.setText("Jarak : "+Integer.toString(jarak)+" meter");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMyDialogAturJarakResult != null){
                    onMyDialogAturJarakResult.finish(jarak);
                    CustomModalAturJarak.this.dismiss();
                }
            }
        });

    }



    public interface OnMyDialogAturJarakResult{
        void finish(int jarak);
    }

    public void setDialog(CustomModalAturJarak.OnMyDialogAturJarakResult dialogResult){
        onMyDialogAturJarakResult = dialogResult;
    }
}
