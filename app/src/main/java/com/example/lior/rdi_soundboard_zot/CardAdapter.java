package com.example.lior.rdi_soundboard_zot;

import android.support.v7.widget.CardView;

public interface CardAdapter
{
    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
