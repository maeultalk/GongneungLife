package com.maeultalk.gongneunglife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.test.ArrayTestActivity;
import com.maeultalk.gongneunglife.test.EmailConfirmActivity;
import com.maeultalk.gongneunglife.test.EmailConfirmTestActivity;
import com.maeultalk.gongneunglife.test.GetImagesActivity;
import com.maeultalk.gongneunglife.test.GetPictureOrImagesActivity;
import com.maeultalk.gongneunglife.test.GetPictureOrImagesActivity2;
import com.maeultalk.gongneunglife.test.GetPictureOrImagesActivity3;
import com.maeultalk.gongneunglife.test.ImgLoadingTestActivity;
import com.maeultalk.gongneunglife.test.MatisseActivity2;
import com.maeultalk.gongneunglife.test.YoutubeActivity;
import com.maeultalk.gongneunglife.test.YoutubeRecyclerViewActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button button_array = (Button) findViewById(R.id.button_array);
        button_array.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArrayTestActivity.class);
                startActivity(intent);
            }
        });

        Button button_load_img = (Button) findViewById(R.id.button_load_img);
        button_load_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImgLoadingTestActivity.class);
                startActivity(intent);
            }
        });

        Button button_youtube = (Button) findViewById(R.id.button_youtube);
        button_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YoutubeActivity.class);
                startActivity(intent);
            }
        });

        Button button_youtube2 = (Button) findViewById(R.id.button_youtube2);
        button_youtube2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YoutubeRecyclerViewActivity.class);
                startActivity(intent);
            }
        });


        Button button_badge = (Button) findViewById(R.id.button_badge);
        button_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AndroidNotificationCountBadge.class);
                startActivity(intent);
            }
        });

        Button button_listview = (Button) findViewById(R.id.button_listview);
        button_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListviewStudyActivity.class);
                startActivity(intent);
            }
        });

        Button button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        Button button_img = (Button) findViewById(R.id.button_img);
        button_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetImagesActivity.class);
                startActivity(intent);
            }
        });

        Button button_pic_or_imgs = (Button) findViewById(R.id.button_pic_or_imgs);
        button_pic_or_imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetPictureOrImagesActivity.class);
                startActivity(intent);
            }
        });

        Button button_pic_or_imgs2 = (Button) findViewById(R.id.button_pic_or_imgs2);
        button_pic_or_imgs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetPictureOrImagesActivity2.class);
                startActivity(intent);
            }
        });

        Button button_pic_or_imgs3 = (Button) findViewById(R.id.button_pic_or_imgs3);
        button_pic_or_imgs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetPictureOrImagesActivity3.class);
                startActivity(intent);
            }
        });

        Button button_upload_gallery_img = (Button) findViewById(R.id.button_upload_gallery_img);
        button_upload_gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), UploadGalleryImageActivity.class);
//                startActivity(intent);
            }
        });

        Button button_matisse = (Button) findViewById(R.id.button_matisse);
        button_matisse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MatisseActivity2.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailConfirmActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_email2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmailConfirmTestActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
