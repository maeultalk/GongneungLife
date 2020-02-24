package com.maeultalk.gongneunglife.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.matisse.Glide4Engine;
import com.maeultalk.gongneunglife.request.AddContentRequest;
import com.maeultalk.gongneunglife.uploadImage.MyRetrofit2;
import com.maeultalk.gongneunglife.uploadImage.UploadContentInterface;
import com.maeultalk.gongneunglife.uploadImage.UploadImageInterface;
import com.maeultalk.gongneunglife.uploadImage.UploadObject;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class AddContentActivity2 extends AppCompatActivity {

    String place_name;
    String place_code;

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

    CheckBox checkBox;
    EditText editText_priority;

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
    String admin;

    TextView textView_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content2);

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        email = pref.getString("email", "");
        nick = pref.getString("nick", "");
        admin = pref.getString("admin", "");

        textView_nick = (TextView) findViewById(R.id.textView_nick);
        textView_nick.setText(nick);

        // 액션바 타이틀 설정
        Intent intent = getIntent();
        place_name = intent.getStringExtra("place_name");
        place_code = intent.getStringExtra("place_code");
        setTitle("\'" + place_name + "\'에 글쓰기");

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

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        if(admin.equals("true")) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        editText_priority = (EditText) findViewById(R.id.editText_priority);
        if(admin.equals("true")) {
            editText_priority.setVisibility(View.VISIBLE);
        } else {
            editText_priority.setVisibility(View.GONE);
        }

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

        editText3 = (EditText) findViewById(R.id.editText3);
        editText3.setHint("\'" + place_name + "'엔 어떤 일이 있나요?");

        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagePath.size() >= 3) {
                    Toast.makeText(AddContentActivity2.this, "사진은 총 3장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    RxPermissions rxPermissions = new RxPermissions(AddContentActivity2.this);
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
                                        Toast.makeText(AddContentActivity2.this, "권한요청이 거부되었습니다.", Toast.LENGTH_SHORT)
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
                    Toast.makeText(AddContentActivity2.this, "사진은 총 3장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    RxPermissions rxPermissions = new RxPermissions(AddContentActivity2.this);
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
                                        Toast.makeText(AddContentActivity2.this, "권한요청이 거부되었습니다.", Toast.LENGTH_SHORT)
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
        getMenuInflater().inflate(R.menu.menu_add_content_activity, menu);
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

                progressDialog = new AppCompatDialog(AddContentActivity2.this);
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
                        }
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

    void  chooseFromAlbum() {
        Matisse.from(AddContentActivity2.this)
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

        UploadContentInterface service = MyRetrofit2.getRetrofit2().create(UploadContentInterface.class);

        //파일 생성
        //img_url은 이미지의 경로

        MultipartBody.Part body = null;
        MultipartBody.Part body2 = null;
        MultipartBody.Part body3 = null;
        RequestBody imageName = null;
        RequestBody imageName2 = null;
        RequestBody imageName3 = null;

        for(int i=0; i<imagePath.size(); i++) {
            File file = new File(imagePath.get(i));
            Log.d("file", file.getAbsolutePath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            int pos = file.getName().lastIndexOf( "." );
            String suffix = file.getName().substring( pos );
            String imageFileName = "GongLife_" + timeStamp + "_" + numberGen(18, 1) + suffix;
            if(i == 0) {
                body = MultipartBody.Part.createFormData("image", imageFileName, requestFile);
                imageName = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
            } else if(i == 1) {
                body2 = MultipartBody.Part.createFormData("image2", imageFileName, requestFile);
                imageName2 = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
            } else if(i == 2) {
                body3 = MultipartBody.Part.createFormData("image3", imageFileName, requestFile);
                imageName3 = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
            }
        }

        RequestBody codeBody = RequestBody.create(MediaType.parse("text/plain"), place_code);
        RequestBody userBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);
        RequestBody checkBody;
        if(checkBox.isChecked()) {
            checkBody = RequestBody.create(MediaType.parse("text/plain"), "true");
        } else {
            checkBody = RequestBody.create(MediaType.parse("text/plain"), "false");
        }
        String priority = editText_priority.getText().toString();
        if(TextUtils.isEmpty(priority)) {
            priority = "0";
        }
        RequestBody priorityBody = RequestBody.create(MediaType.parse("text/plain"), priority);

        Call<UploadObject> resultCall = null;
        if(imagePath.size() == 0) {
            resultCall = service.uploadContent(codeBody, userBody, contentBody, checkBody, priorityBody);
        } else if(imagePath.size() == 1) {
            resultCall = service.uploadContent(codeBody, userBody, contentBody, checkBody, priorityBody, imageName, body);
        } else if(imagePath.size() == 2) {
            resultCall = service.uploadContent(codeBody, userBody, contentBody, checkBody, priorityBody, imageName, imageName2, body, body2);
        } else if(imagePath.size() == 3) {
            resultCall = service.uploadContent(codeBody, userBody, contentBody, checkBody, priorityBody, imageName, imageName2, imageName3, body, body2, body3);
        }
        resultCall.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
