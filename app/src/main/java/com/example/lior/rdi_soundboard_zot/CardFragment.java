package com.example.lior.rdi_soundboard_zot;

import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import org.json.JSONObject;
import java.util.ArrayList;

public class CardFragment extends Fragment
{
    private CardView cardView;
    private ArrayList<String> mp3FilesNames;
    boolean isPlayerPlaying = false;

    public static Fragment getInstance(int position, JSONObject jsonObject)
    {
        CardFragment cardFragment = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        try
        {
            args.putString("text", jsonObject.getString("text"));
            args.putString("thumbnail", jsonObject.getString("thumbnail"));
            args.putInt("numOfVersions", jsonObject.getInt("numOfVersions"));
            args.putString("file", jsonObject.getString("file"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        cardFragment.setArguments(args);

        return cardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView cardLine = view.findViewById(R.id.card_textView);
        final ImageView cardImage = view.findViewById(R.id.card_imageView);
        LinearLayout linearLayout1 = view.findViewById(R.id.beers_container1);
        LinearLayout linearLayout2 = view.findViewById(R.id.beers_container2);

        if (getArguments() != null)
        {
            cardLine.setText(getArguments().getString("text"));
            cardLine.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Boisterb.ttf"));

            String cardImage_resourceStr = getArguments().getString("thumbnail");
            int cardImage_resourceID = getResources().getIdentifier(cardImage_resourceStr, "drawable", view.getContext().getPackageName());
            cardImage.setImageResource(cardImage_resourceID);

            int numOfVersions = getArguments().getInt("numOfVersions");
            String mp3BasicFileName = getArguments().getString("file");
            mp3FilesNames = new ArrayList<>();

            for (int i = 1; i <= numOfVersions; i++)
            {
                mp3FilesNames.add(mp3BasicFileName + i + ".mp3");

                int beerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                int beerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());

                // Handling the layout for the beer mug icons
                LinearLayout.LayoutParams beerMugLayoutParams = new LinearLayout.LayoutParams(beerWidth, beerHeight);
                beerMugLayoutParams.weight = 18;
                final RelativeLayout beerMug_RelativeLayout = new RelativeLayout(view.getContext());
                beerMug_RelativeLayout.setLayoutParams(beerMugLayoutParams);

                // Container for the the containers of each beer mug icon and progress bar
                RelativeLayout.LayoutParams mainContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mainContainerParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                mainContainerParams.topMargin = 15;
                final RelativeLayout mainContainer = new RelativeLayout(view.getContext());
                //mainContainer.setBackgroundResource(R.drawable.border);
                mainContainer.setLayoutParams(mainContainerParams);
                beerMug_RelativeLayout.addView(mainContainer);

                // Container for each beer mug icon
                RelativeLayout.LayoutParams beerMugContainerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                beerMugContainerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                final RelativeLayout beerMug_Container = new RelativeLayout(view.getContext());
                //beerMug_Container.setBackgroundResource(R.drawable.border);
                beerMug_Container.setLayoutParams(beerMugContainerParams);
                mainContainer.addView(beerMug_Container);

                // Displaying the beer mug icons appearance
                final int beerMugIcon_resourceID = getResources().getIdentifier("beer_mug_icon" + i, "drawable", view.getContext().getPackageName());
                RelativeLayout.LayoutParams beerMugImageParams = new RelativeLayout.LayoutParams(beerWidth, beerWidth);
                final ImageView beerMug_Image = new ImageView(view.getContext());
                beerMug_Image.setImageResource(beerMugIcon_resourceID);
                beerMug_Image.setLayoutParams(beerMugImageParams);
                beerMug_Container.addView(beerMug_Image);

                // Container for each progress bar
                RelativeLayout.LayoutParams progressBarContainerParams = new RelativeLayout.LayoutParams(beerWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                progressBarContainerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                final RelativeLayout progressBar_Container = new RelativeLayout(view.getContext());
                progressBar_Container.setLayoutParams(progressBarContainerParams);
                mainContainer.addView(progressBar_Container);

                // Setting the mp3 file for each beer mug icon
                final int iCurrent = i-1;
                beerMug_Image.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View view)
                    {
                        if (!isPlayerPlaying)
                        {
                            isPlayerPlaying = true;

                            try
                            {
                                AssetFileDescriptor afd = view.getContext().getAssets().openFd("mp3_lines/" + mp3FilesNames.get(iCurrent));
                                final MediaPlayer player = new MediaPlayer();
                                player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                                player.prepare();
                                player.start();

                                final ProgressBar progressBar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleHorizontal);
                                progressBar.setProgress(0);
                                progressBar.setMax(player.getDuration());
                                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.cardBrown), android.graphics.PorterDuff.Mode.SRC_IN);
                                progressBar_Container.addView(progressBar);

                                YoYo.with(Techniques.Tada)
                                        .duration(player.getDuration())
                                        .playOn(mainContainer);

                                final Handler handler = new Handler();

                                new Thread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        while (player.isPlaying())
                                        {
                                            final int progressPosition = player.getCurrentPosition();

                                            handler.post(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    progressBar.setProgress(progressPosition);
                                                }
                                            });
                                        }

                                        handler.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                progressBar_Container.removeView(progressBar);
                                                isPlayerPlaying = false;
                                            }
                                        });
                                    }
                                }).start();

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                // Adding each beer mug icon layout to its proper row
                if (i <= 4)
                    linearLayout1.addView(beerMug_RelativeLayout);

                else
                    linearLayout2.addView(beerMug_RelativeLayout);
            }
        }

        return view;
    }

    public CardView getCardView()
    {
        return cardView;
    }
}