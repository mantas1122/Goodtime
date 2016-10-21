package com.apps.adrcotfas.goodtime;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class ProductTourActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen(true);

        /**
         * Standard fade (like Google's intros)
         */
        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_avoid_distractions)
                .description(R.string.intro_avoid_distractions_description)
                .image(R.drawable.intro01)
                .background(R.color.intro01)
                .backgroundDark(R.color.intro01)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_clear_mind)
                .description(R.string.intro_clear_mind_description)
                .image(R.drawable.intro02)
                .background(R.color.intro02)
                .backgroundDark(R.color.intro02)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.intro_get_started)
                .description(R.string.intro_get_started_description)
                .image(R.drawable.intro03)
                .background(R.color.intro03)
                .backgroundDark(R.color.intro03)
                .build());
    }
}
