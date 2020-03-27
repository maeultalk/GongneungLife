package com.maeultalk.gongneunglife.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.matisse.Glide4Engine;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.uploadImage.MyRetrofit2;
import com.maeultalk.gongneunglife.uploadImage.UpdateContentInterface;
import com.maeultalk.gongneunglife.uploadImage.UploadObject;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static com.maeultalk.gongneunglife.adapter.RecyclerViewAdapter.formatTimeString;
import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;
import static com.maeultalk.gongneunglife.fragment.mainActivity.HomeFragment.contentsInHome;
import static com.maeultalk.gongneunglife.fragment.placeActivity.TimeLineFragment.contentsInPlace;
import static com.maeultalk.gongneunglife.activity.CollectActivity.contentsInCollect;

public class EditContentActivity extends AppCompatActivity {

    String place_name;
    String place_code;

    TextView textView_time;

    EditText editText3;

    String content;

    RelativeLayout imgLayout;
    RelativeLayout imgLayout2;
    RelativeLayout imgLayout3;

    ImageView imageView_remove;
    ImageView imageView_remove2;
    ImageView imageView_remove3;

    ImageView imageView_content;
    ImageView imageView_content2;
    ImageView imageView_content3;
    ImageView imageView_camera;
    ImageView imageView_gallery;

    final String TAG = getClass().getSimpleName();
    final static int TAKE_PICTURE = 1;

    String mCurrentPhotoPath;
    String mCurrentPhotoPath2;
    static final int REQUEST_TAKE_PHOTO = 1;

    int imgCnt = 0;

    private static final int REQUEST_CODE_CHOOSE = 23;

//    String[] imagePath = new String[3];

    ArrayList<String> imagePath = new ArrayList<>();
    int uploadedCnt = 0;

    AppCompatDialog progressDialog;

    String email;
    String nick;

    TextView textView_nick;

    Content contentEdit;

    String imageNameString = "";
    String imageName2String = "";
    String imageName3String = "";

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content2);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        email = pref.getString("email", "");
        nick = pref.getString("nick", "");

        textView_nick = (TextView) findViewById(R.id.textView_nick);
        textView_nick.setText(nick);

        // 액션바 타이틀 설정
        Intent intent = getIntent();
        contentEdit = (Content) intent.getSerializableExtra("content");
        place_name = contentEdit.getPlace_name();
        place_code = contentEdit.getPlace_code();
        /*place_name = intent.getStringExtra("place_name");
        place_code = intent.getStringExtra("place_code");*/
        setTitle("\'" + place_name + "\'에 글쓰기");

        from = intent.getStringExtra("from");

        String date = contentEdit.getDate();
        Date original_date = null;
        String new_date = "";
        SimpleDateFormat original_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy.M.d.\na h:mm", Locale.KOREA);
        try {
            original_date = original_format.parse(date);
            new_date = new_format.format(original_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textView_time = (TextView) findViewById(R.id.textView_time);
        textView_time.setText(formatTimeString(original_date));

        editText3 = (EditText) findViewById(R.id.editText3);
        editText3.setText(contentEdit.getContent());
//        editText3.setHint("\'" + place_name + "'엔 어떤 일이 있나요?");

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    /*case ACTION_MOVE :
                        editText3.
                        return false;*/
                    case ACTION_DOWN:
                        editText3.dispatchTouchEvent(event);
                        //Toast.makeText(getApplicationContext(), "다운", Toast.LENGTH_SHORT).show();
                        return true;
                    case ACTION_UP:
                        editText3.dispatchTouchEvent(event);
                        //Toast.makeText(getApplicationContext(), "업", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        return false;
                }


            }
        });

        imgLayout = (RelativeLayout) findViewById(R.id.imgLayout);
        imgLayout2 = (RelativeLayout) findViewById(R.id.imgLayout2);
        imgLayout3 = (RelativeLayout) findViewById(R.id.imgLayout3);

        imageView_remove = (ImageView) findViewById(R.id.imageView_remove);
        imageView_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePath.remove(0);
                imgLayout.setVisibility(View.GONE);
                imgLayout2.setVisibility(View.GONE);
                imgLayout3.setVisibility(View.GONE);

                try {

                    for(int i=0; i<imagePath.size(); i++) {
                        if(imagePath.get(i).charAt(0) == '/') {
                            File file = new File(imagePath.get(i));
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                ExifInterface ei = new ExifInterface(imagePath.get(i));
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch(orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                if(i==0) {
                                    imgLayout.setVisibility(View.VISIBLE);
                                    imageView_content.setImageBitmap(rotatedBitmap);
                                } else if(i==1) {
                                    imgLayout2.setVisibility(View.VISIBLE);
                                    imageView_content2.setImageBitmap(rotatedBitmap);
                                } else if(i==2) {
                                    imgLayout3.setVisibility(View.VISIBLE);
                                    imageView_content3.setImageBitmap(rotatedBitmap);
                                }
                            }
                        } else {
                            if(i==0) {
                                imgLayout.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content);
                            } else if(i==1) {
                                imgLayout2.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content2);
                            } else if(i==2) {
                                imgLayout3.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content3);
                            }
                        }
                    }

                } catch (Exception e) {

                }

            }
        });
        imageView_remove2 = (ImageView) findViewById(R.id.imageView_remove2);
        imageView_remove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePath.remove(1);
                imgLayout.setVisibility(View.GONE);
                imgLayout2.setVisibility(View.GONE);
                imgLayout3.setVisibility(View.GONE);

                try {

                    for(int i=0; i<imagePath.size(); i++) {
                        if(imagePath.get(i).charAt(0) == '/') {
                            File file = new File(imagePath.get(i));
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                ExifInterface ei = new ExifInterface(imagePath.get(i));
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch(orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                if(i==0) {
                                    imgLayout.setVisibility(View.VISIBLE);
                                    imageView_content.setImageBitmap(rotatedBitmap);
                                } else if(i==1) {
                                    imgLayout2.setVisibility(View.VISIBLE);
                                    imageView_content2.setImageBitmap(rotatedBitmap);
                                } else if(i==2) {
                                    imgLayout3.setVisibility(View.VISIBLE);
                                    imageView_content3.setImageBitmap(rotatedBitmap);
                                }
                            }
                        } else {
                            if(i==0) {
                                imgLayout.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content);
                            } else if(i==1) {
                                imgLayout2.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content2);
                            } else if(i==2) {
                                imgLayout3.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content3);
                            }
                        }
                    }

                } catch (Exception e) {

                }

            }
        });
        imageView_remove3 = (ImageView) findViewById(R.id.imageView_remove3);
        imageView_remove3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePath.remove(2);
                imgLayout.setVisibility(View.GONE);
                imgLayout2.setVisibility(View.GONE);
                imgLayout3.setVisibility(View.GONE);

                try {

                    for(int i=0; i<imagePath.size(); i++) {
                        if(imagePath.get(i).charAt(0) == '/') {
                            File file = new File(imagePath.get(i));
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                ExifInterface ei = new ExifInterface(imagePath.get(i));
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch(orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                if(i==0) {
                                    imgLayout.setVisibility(View.VISIBLE);
                                    imageView_content.setImageBitmap(rotatedBitmap);
                                } else if(i==1) {
                                    imgLayout2.setVisibility(View.VISIBLE);
                                    imageView_content2.setImageBitmap(rotatedBitmap);
                                } else if(i==2) {
                                    imgLayout3.setVisibility(View.VISIBLE);
                                    imageView_content3.setImageBitmap(rotatedBitmap);
                                }
                            }
                        } else {
                            if(i==0) {
                                imgLayout.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content);
                            } else if(i==1) {
                                imgLayout2.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content2);
                            } else if(i==2) {
                                imgLayout3.setVisibility(View.VISIBLE);
                                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content3);
                            }
                        }
                    }

                } catch (Exception e) {

                }

            }
        });

        imageView_content = (ImageView) findViewById(R.id.imageView_content);
        imageView_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        imageView_content2 = (ImageView) findViewById(R.id.imageView_content2);
        imageView_content2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        imageView_content3 = (ImageView) findViewById(R.id.imageView_content3);
        imageView_content3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        imageView_camera = (ImageView) findViewById(R.id.imageView_camera);
        imageView_gallery = (ImageView) findViewById(R.id.imageView_gallery);

        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagePath.size() >= 3) {
                    Toast.makeText(EditContentActivity.this, "사진은 총 3장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    RxPermissions rxPermissions = new RxPermissions(EditContentActivity.this);
                    rxPermissions.request(Manifest.permission.CAMERA)
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    if (aBoolean) {
                                        dispatchTakePictureIntent();
                                    } else {
                                        Toast.makeText(EditContentActivity.this, "권한요청이 거부되었습니다.", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                }

            }
        });

        imageView_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imagePath.size() >= 3) {
                    Toast.makeText(EditContentActivity.this, "사진은 총 3장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    RxPermissions rxPermissions = new RxPermissions(EditContentActivity.this);
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    if (aBoolean) {
                                        chooseFromAlbum();
                                    } else {
                                        Toast.makeText(EditContentActivity.this, "권한요청이 거부되었습니다.", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                }

            }
        });

        // 기존 이미지 셋팅
        if(!TextUtils.isEmpty(contentEdit.getImage())) {
            imagePath.add(contentEdit.getImage());
        }
        if(!TextUtils.isEmpty(contentEdit.getImage2())) {
            imagePath.add(contentEdit.getImage2());
        }
        if(!TextUtils.isEmpty(contentEdit.getImage3())) {
            imagePath.add(contentEdit.getImage3());
        }
        for(int i=0; i<imagePath.size(); i++) {
            if(i==0) {
                imgLayout.setVisibility(View.VISIBLE);
                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content);
            } else if(i==1) {
                imgLayout2.setVisibility(View.VISIBLE);
                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content2);
            } else if(i==2) {
                imgLayout3.setVisibility(View.VISIBLE);
                Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content3);
            }
        }

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_content_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        item.setEnabled(false);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            content = editText3.getText().toString();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(getApplicationContext(), "글 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                item.setEnabled(true);
            } else {
                /*for(int i=0; i<imagePath.size(); i++) {
                    img_upload(imagePath.get(i));

                    progressDialog = new AppCompatDialog(AddContentActivity2.this);
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.setContentView(R.layout.progress_loading);
                    progressDialog.show();
                }*/
                img_upload("");

                progressDialog = new AppCompatDialog(EditContentActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_loading);
                progressDialog.show();
                /*if(imgCnt == 0) {
                    img_upload(mCurrentPhotoPath);
                } else if(imgCnt == 1) {
                    img_upload(mCurrentPhotoPath);
                    img_upload(mCurrentPhotoPath2);
                }*/

                // 프로그레스


            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // 카메라로 촬영한 영상을 가져오는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        imagePath.add(mCurrentPhotoPath);
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            Bitmap rotatedBitmap = null;
                            switch(orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }

                            if(imagePath.size() == 1) {
                                imgLayout.setVisibility(View.VISIBLE);
                                imageView_content.setImageBitmap(rotatedBitmap);
                            } else if(imagePath.size() == 2) {
                                imgLayout2.setVisibility(View.VISIBLE);
                                imageView_content2.setImageBitmap(rotatedBitmap);
                            } else if(imagePath.size() == 3) {
                                imgLayout3.setVisibility(View.VISIBLE);
                                imageView_content3.setImageBitmap(rotatedBitmap);
                            }
                        }
                    }
                    break;
                }
                case REQUEST_CODE_CHOOSE: {
                    if (resultCode == RESULT_OK) {
                        imagePath.addAll(Matisse.obtainPathResult(intent));
                        for(int i=0; i<imagePath.size(); i++) {
                            if(imagePath.get(i).charAt(0) == '/') {
                                File file = new File(imagePath.get(i));
                                Bitmap bitmap = MediaStore.Images.Media
                                        .getBitmap(getContentResolver(), Uri.fromFile(file));
                                if (bitmap != null) {
                                    ExifInterface ei = new ExifInterface(imagePath.get(i));
                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_UNDEFINED);

                                    Bitmap rotatedBitmap = null;
                                    switch(orientation) {

                                        case ExifInterface.ORIENTATION_ROTATE_90:
                                            rotatedBitmap = rotateImage(bitmap, 90);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_180:
                                            rotatedBitmap = rotateImage(bitmap, 180);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_270:
                                            rotatedBitmap = rotateImage(bitmap, 270);
                                            break;

                                        case ExifInterface.ORIENTATION_NORMAL:
                                        default:
                                            rotatedBitmap = bitmap;
                                    }
                                    if(i==0) {
                                        imgLayout.setVisibility(View.VISIBLE);
                                        imageView_content.setImageBitmap(rotatedBitmap);
                                    } else if(i==1) {
                                        imgLayout2.setVisibility(View.VISIBLE);
                                        imageView_content2.setImageBitmap(rotatedBitmap);
                                    } else if(i==2) {
                                        imgLayout3.setVisibility(View.VISIBLE);
                                        imageView_content3.setImageBitmap(rotatedBitmap);
                                    }
                                }
                            } else {
                                if(i==0) {
                                    imgLayout.setVisibility(View.VISIBLE);
                                    Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content);
                                } else if(i==1) {
                                    imgLayout2.setVisibility(View.VISIBLE);
                                    Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content2);
                                } else if(i==2) {
                                    imgLayout3.setVisibility(View.VISIBLE);
                                    Glide.with(EditContentActivity.this).load(URL_IMAGES + imagePath.get(i)).into(imageView_content3);
                                }
                            }
                        }
                        /*for(int i=0; i<imagePath.size(); i++) {
                            File file = new File(imagePath.get(i));
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                ExifInterface ei = new ExifInterface(imagePath.get(i));
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch(orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                if(i==0) {
                                    imgLayout.setVisibility(View.VISIBLE);
                                    imageView_content.setImageBitmap(rotatedBitmap);
                                } else if(i==1) {
                                    imgLayout2.setVisibility(View.VISIBLE);
                                    imageView_content2.setImageBitmap(rotatedBitmap);
                                } else if(i==2) {
                                    imgLayout3.setVisibility(View.VISIBLE);
                                    imageView_content3.setImageBitmap(rotatedBitmap);
                                }
                            }
                        }*/
                    }
                    break;
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.maeultalk.gongneunglife.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    void chooseFromAlbum() {
        Matisse.from(EditContentActivity.this)
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)
                .countable(true)
//                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(3 - imagePath.size())
//                                            .originalEnable(true)
//                                            .maxOriginalSize(10)
//                                            .thumbnailScale(0.25f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    void img_upload(String img_url) {

        /*UploadImageInterface service = MyRetrofit2.getRetrofit2().create(UploadImageInterface.class);

        //파일 생성
        //img_url은 이미지의 경로
        File file = new File(img_url);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        int pos = file.getName().lastIndexOf( "." );
        String suffix = file.getName().substring( pos );
        String imageFileName = "GongLife_" + timeStamp + "_" + numberGen(18, 1) + suffix;
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFileName, requestFile);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
        String a = file.getName();
        Call<UploadObject> resultCall = service.uploadFile(body);
        resultCall.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {

                uploadedCnt++;
                if(imagePath.size() == uploadedCnt) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });*/

        UpdateContentInterface service = MyRetrofit2.getRetrofit2().create(UpdateContentInterface.class);

        //파일 생성
        //img_url은 이미지의 경로

        MultipartBody.Part body = null;
        MultipartBody.Part body2 = null;
        MultipartBody.Part body3 = null;
        RequestBody imageName = null;
        RequestBody imageName2 = null;
        RequestBody imageName3 = null;

        /*for(int i=0; i<imagePath.size(); i++) {
            if(imagePath.get(i).charAt(0) == '/') {
                File file = new File(imagePath.get(i));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                int pos = file.getName().lastIndexOf( "." );
                String suffix = file.getName().substring( pos );
                String imageFileName = "GongLife_" + timeStamp + "_" + numberGen(18, 1) + suffix;
                if(i == 0) {
                    body = MultipartBody.Part.createFormData("image", imageFileName, requestFile);
                    imageName = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    imageNameString = imageFileName;
                } else if(i == 1) {
                    body2 = MultipartBody.Part.createFormData("image2", imageFileName, requestFile);
                    imageName2 = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    imageName2String = imageFileName;
                } else if(i == 2) {
                    body3 = MultipartBody.Part.createFormData("image3", imageFileName, requestFile);
                    imageName3 = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    imageName3String = imageFileName;
                }
            } else {
                if(i == 0) {
                    imageName = RequestBody.create(MediaType.parse("text/plain"), imagePath.get(i));
                    imageNameString = imagePath.get(i);
                } else if(i == 1) {
                    imageName2 = RequestBody.create(MediaType.parse("text/plain"), imagePath.get(i));
                    imageName2String = imagePath.get(i);
                } else if(i == 2) {
                    imageName3 = RequestBody.create(MediaType.parse("text/plain"), imagePath.get(i));
                    imageName3String = imagePath.get(i);
                }
            }
        }*/

        for(int i=0; i<imagePath.size(); i++) {
            if(imagePath.get(i).charAt(0) == '/') {
                File file = new File(imagePath.get(i));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                int pos = file.getName().lastIndexOf( "." );
                String suffix = file.getName().substring( pos );
                String imageFileName = "GongLife_" + timeStamp + "_" + numberGen(18, 1) + suffix;
                if(i == 0) {
                    body = MultipartBody.Part.createFormData("image", imageFileName, requestFile);
                    imageName = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    imageNameString = imageFileName;
                } else if(i == 1) {
                    body2 = MultipartBody.Part.createFormData("image2", imageFileName, requestFile);
                    imageName2 = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    imageName2String = imageFileName;
                } else if(i == 2) {
                    body3 = MultipartBody.Part.createFormData("image3", imageFileName, requestFile);
                    imageName3 = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    imageName3String = imageFileName;
                }
            } else {
                if(i == 0) {
                    imageName = RequestBody.create(MediaType.parse("text/plain"), imagePath.get(i));
                    imageNameString = imagePath.get(i);
                } else if(i == 1) {
                    imageName2 = RequestBody.create(MediaType.parse("text/plain"), imagePath.get(i));
                    imageName2String = imagePath.get(i);
                } else if(i == 2) {
                    imageName3 = RequestBody.create(MediaType.parse("text/plain"), imagePath.get(i));
                    imageName3String = imagePath.get(i);
                }
            }
        }

        if(imagePath.size() == 0) {
            imageName = RequestBody.create(MediaType.parse("text/plain"), "");
            imageNameString = "";
            imageName2 = RequestBody.create(MediaType.parse("text/plain"), "");
            imageName2String = "";
            imageName3 = RequestBody.create(MediaType.parse("text/plain"), "");
            imageName3String = "";
        } else if(imagePath.size() == 1) {
            imageName2 = RequestBody.create(MediaType.parse("text/plain"), "");
            imageName2String = "";
            imageName3 = RequestBody.create(MediaType.parse("text/plain"), "");
            imageName3String = "";
        } else if(imagePath.size() == 2) {
            imageName3 = RequestBody.create(MediaType.parse("text/plain"), "");
            imageName3String = "";
        }

        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), contentEdit.getId());
        RequestBody codeBody = RequestBody.create(MediaType.parse("text/plain"), place_code);
        RequestBody userBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);

        Call<UploadObject> resultCall = null;
        if(imagePath.size() == 0) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, imageName2, imageName3);
        } else if(imagePath.size() == 1) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, imageName2, imageName3, body);
        } else if(imagePath.size() == 2) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, imageName2, imageName3, body, body2);
        } else if(imagePath.size() == 3) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, imageName2, imageName3, body, body2, body3);
        }
        /*if(imagePath.size() == 0) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody);
        } else if(imagePath.size() == 1) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, body);
        } else if(imagePath.size() == 2) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, imageName2, body, body2);
        } else if(imagePath.size() == 3) {
            resultCall = service.uploadContent(idBody, codeBody, userBody, contentBody, imageName, imageName2, imageName3, body, body2, body3);
        }*/
        resultCall.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {

                // TODO: 18/07/2019 arrayList 수정
                if(from.equals("home")) {
                    for (int i = 0; i < contentsInHome.size(); i++) {
                        Content c = contentsInHome.get(i);
                        if (c.getId().equals(contentEdit.getId())) {
                            contentsInHome.get(i).setContent(content);
                            contentsInHome.get(i).setImage(imageNameString);
                            contentsInHome.get(i).setImage2(imageName2String);
                            contentsInHome.get(i).setImage3(imageName3String);
                        /*if(imagePath.size() >= 1) {
                            contentsInHome.get(i).setImage(imageNameString);
                        }
                        if(imagePath.size() >= 2) {
                            contentsInHome.get(i).setImage2(imageName2String);
                        }
                        if(imagePath.size() == 3) {
                            contentsInHome.get(i).setImage3(imageName3String);
                        }*/
                            break;
                        }
                    }
                } else if(from.equals("place")) {
                    for (int i = 0; i < contentsInPlace.size(); i++) {
                        Content c = contentsInPlace.get(i);
                        if (c.getId().equals(contentEdit.getId())) {
                            contentsInPlace.get(i).setContent(content);
                            contentsInPlace.get(i).setImage(imageNameString);
                            contentsInPlace.get(i).setImage2(imageName2String);
                            contentsInPlace.get(i).setImage3(imageName3String);
                        /*if(imagePath.size() >= 1) {
                            contentsInHome.get(i).setImage(imageNameString);
                        }
                        if(imagePath.size() >= 2) {
                            contentsInHome.get(i).setImage2(imageName2String);
                        }
                        if(imagePath.size() == 3) {
                            contentsInHome.get(i).setImage3(imageName3String);
                        }*/
                            break;
                        }
                    }
                } else if(from.equals("collect")) {
                    for (int i = 0; i < contentsInCollect.size(); i++) {
                        Content c = contentsInCollect.get(i);
                        if (c.getId().equals(contentEdit.getId())) {
                            contentsInCollect.get(i).setContent(content);
                            contentsInCollect.get(i).setImage(imageNameString);
                            contentsInCollect.get(i).setImage2(imageName2String);
                            contentsInCollect.get(i).setImage3(imageName3String);
                        /*if(imagePath.size() >= 1) {
                            contentsInHome.get(i).setImage(imageNameString);
                        }
                        if(imagePath.size() >= 2) {
                            contentsInHome.get(i).setImage2(imageName2String);
                        }
                        if(imagePath.size() == 3) {
                            contentsInHome.get(i).setImage3(imageName3String);
                        }*/
                            break;
                        }
                    }
                }

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "게시글을 수정하였습니다.", Toast.LENGTH_SHORT).show();
                finish();

            }
            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "처리 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 전달된 파라미터에 맞게 난수를 생성한다
     * @param len : 생성할 난수의 길이
     * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
     *
     * Created by 닢향
     * http://niphyang.tistory.com
     */
    public static String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<len;i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }

}
