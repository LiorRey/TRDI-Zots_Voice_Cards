package com.example.lior.rdi_soundboard_zot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            Bundle bundle = getIntent().getExtras();
            JSONArray jsonArray = null;
            if (bundle != null)
            {
                jsonArray = new JSONArray(bundle.getString("zotdata"));
            }

            ViewPager viewPager = findViewById(R.id.zotLines_viewPager);

            CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this), jsonArray);
            ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
            fragmentCardShadowTransformer.enableScaling(true);

            viewPager.setAdapter(pagerAdapter);
            viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
            viewPager.setOffscreenPageLimit(3);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Change value in dp to pixels
    public static float dpToPixels(int dp, Context context)
    {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent splashActivityIntent = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(splashActivityIntent);
        finish();
    }
}
