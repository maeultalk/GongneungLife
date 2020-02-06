package com.maeultalk.gongneunglife.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.maeultalk.gongneunglife.R;

public class YoutubeActivity extends YouTubeBaseActivity {

    YouTubePlayerView youtubeView;

    Button button;

    YouTubePlayer.OnInitializedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        button = (Button) findViewById((R.id.youtubeButton));

        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);

        listener = new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                // 비디오 아이디

//                youTubePlayer.loadVideo("FTK-mZRjaIs");
                youTubePlayer.cueVideo("FTK-mZRjaIs");
//                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.setFullscreen(false);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        };

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                // api 키 값
                youtubeView.initialize("AIzaSyCVuF6-gx1ih3T6sxC_EpCwrI3XARn4FGI", listener);

            }

        });

    }
}
