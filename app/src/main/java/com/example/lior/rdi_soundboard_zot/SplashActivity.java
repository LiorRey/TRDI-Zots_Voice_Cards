package com.example.lior.rdi_soundboard_zot;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splashImage = findViewById(R.id.splash_backgroundImage);
        Animation splashImageAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_image_animation);
        splashImage.startAnimation(splashImageAnimation);

        TextView splashTitle = findViewById(R.id.splash_title);
        splashTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Boisterb.ttf"));

        //asdfasd

        ImageView ribbonBanner = findViewById(R.id.ribbon_banner_image);
//        Animator circularReveal = ViewAnimationUtils.createCircularReveal(ribbonBanner, 50, 50, 100, 100)


        YoYo.with(Techniques.SlideInDown)
                .duration(2000)
                .playOn(findViewById(R.id.ribbon_banner_image));

        YoYo.with(Techniques.FadeInDown)
                .duration(2000)
                .playOn(findViewById(R.id.splash_title));

        TextView liorreyTitle = findViewById(R.id.liorrey_title);
        liorreyTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/eacologica-round-slab-ffp.ttf"));

        YoYo.with(Techniques.FadeInUp)
                .duration(2000)
                .playOn(findViewById(R.id.liorrey_container));

//        Animation animation = new TranslateAnimation(0, 500,0, 0);
//        animation.setDuration(1000);
//        animation.setFillAfter(true);
//        splashImage.startAnimation(animation);

        try
        {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("zotdata");
            final Bundle bundle = new Bundle();
            bundle.putString("zotdata", jsonArray.toString());

            splashImage.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                    mainActivityIntent.putExtras(bundle);
                    startActivity(mainActivityIntent);
                    finish();
                }
            }, splashImageAnimation.getDuration());

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run()
//                {
//                    Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
//                    mainActivityIntent.putExtras(bundle);
//                    startActivity(mainActivityIntent);
//                    finish();
//                }
//            }, 3000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset()
    {
        String json;

        try
        {
            InputStream is = getAssets().open("zotdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
