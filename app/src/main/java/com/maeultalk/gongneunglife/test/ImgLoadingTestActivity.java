package com.maeultalk.gongneunglife.test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.maeultalk.gongneunglife.R;
import com.squareup.picasso.Picasso;

import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

public class ImgLoadingTestActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_loading_test);

        imageView = (ImageView) findViewById(R.id.imageView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Glide.with(ImgLoadingTestActivity.this).load(URL_IMAGES + "GongLife_20190628_144647_261224366700199966.jpg").into(imageView);

                Picasso.with(ImgLoadingTestActivity.this)
                        .load(URL_IMAGES + "GongLife_20190703_123230_506511212399917678.jpg")
                        .into(imageView);

            }
        });

    }
}
