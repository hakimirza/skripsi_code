package org.odk.collect.android.landingpage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import org.odk.collect.android.augmentedreality.MainActivity;
import org.odk.collect.android.landingpage.SliderPage;

import org.odk.collect.android.R;

/**
 * Created by Septiawan Aji Pradan on 5/31/2017.
 */

public class LandingPageActivity extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1= new SliderPage();


        addSlide(AppIntroFragment.newInstance("Penomoran Bangunan","Menggunakan Augmented Reality",R.drawable.img_ar,Color.TRANSPARENT));


        addSlide(AppIntroFragment.newInstance("Download Frame","Menggunakan data hasil cacah",R.drawable.img_ar,Color.TRANSPARENT));


        addSlide(AppIntroFragment.newInstance("Stiker Digital","Penomoran bangunan tanpa menggunakan stiker tempel",R.drawable.img_ar,Color.TRANSPARENT));

        // Declare a new image view
        ImageView imageView = new ImageView(this);

        // Bind a drawable to the imageview
        imageView.setImageResource(R.drawable.landing_background);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Set background color
        // Set layout params
        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Bind the background to the intro
        setBackgroundView(imageView);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
