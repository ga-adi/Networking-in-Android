package com.williamtygret.tripledouble4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by williamtygret on 2/15/17.
 */
public class Splash extends Activity {

    TextView mStart;
    ImageView mLightning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        mStart = (TextView)findViewById(R.id.start);
        mLightning = (ImageView)findViewById(R.id.imageLightning);

        ReplaceFont.replaceDefaultFont(this, "DEFAULT", "PixelFJVerdana12pt.ttf");
        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"PixelFJVerdana12pt.ttf");
        mStart.setTypeface(myTypeface);

        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.lightning);
        mLightning.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
