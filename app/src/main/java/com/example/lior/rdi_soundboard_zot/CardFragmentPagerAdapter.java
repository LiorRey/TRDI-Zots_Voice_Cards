package com.example.lior.rdi_soundboard_zot;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter
{
    private List<CardFragment> fragments;
    private float baseElevation;
    private JSONArray jsonArray;

    CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, JSONArray jsonArray)
    {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        this.jsonArray = jsonArray;

        try
        {
            for(int i = 0; i < jsonArray.length(); i++)
            {
                addCardFragment(new CardFragment());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public float getBaseElevation()
    {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position)
    {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        try
        {
            return CardFragment.getInstance(position, (JSONObject) jsonArray.get(position));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    private void addCardFragment(CardFragment fragment)
    {
        fragments.add(fragment);
    }
}
