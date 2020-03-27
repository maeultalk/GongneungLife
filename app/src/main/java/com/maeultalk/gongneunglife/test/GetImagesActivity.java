package com.maeultalk.gongneunglife.test;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.uploadImage.MyRetrofit2;
import com.maeultalk.gongneunglife.uploadImage.UploadImageInterface;
import com.maeultalk.gongneunglife.uploadImage.UploadObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GetImagesActivity extends AppCompatActivity {

    //UI
    ImageView image1, image2, image3;

    //constant
    final int PICTURE_REQUEST_CODE = 100;

    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_images);

        //UI
        image1 = (ImageView)findViewById(R.id.img1);
        image2 = (ImageView)findViewById(R.id.img2);
        image3 = (ImageView)findViewById(R.id.img3);

        Button btnImage = (Button)findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                //사진을 여러개 선택할수 있도록 한다
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "사진을 가져올 앱을 선택하세요."),  PICTURE_REQUEST_CODE);
            }
        });

        Button btnImage2 = (Button)findViewById(R.id.btnImage2);
        btnImage2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                img_upload(getPath(uri));
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    void img_upload(String img_url) {

        UploadImageInterface service = MyRetrofit2.getRetrofit2().create(UploadImageInterface.class);
//        UploadService service = MyRetrofit2.getRetrofit2().create(UploadService.class);

        //파일 생성
        //img_url은 이미지의 경로
        File file = new File(img_url);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), "abcd.jpg");
//        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
        String a = file.getName();
        Call<UploadObject> resultCall = service.uploadFile(body);
        resultCall.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                Toast.makeText(getApplicationContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICTURE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {

                //기존 이미지 지우기
                image1.setImageResource(0);
                image2.setImageResource(0);
                image3.setImageResource(0);

                //ClipData 또는 Uri를 가져온다
                uri = data.getData();
                ClipData clipData = data.getClipData();

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if(clipData!=null)
                {

                    for(int i = 0; i < 3; i++)
                    {
                        if(i<clipData.getItemCount()){
                            Uri urione =  clipData.getItemAt(i).getUri();
                            switch (i){
                                case 0:
                                    image1.setImageURI(urione);
                                    break;
                                case 1:
                                    image2.setImageURI(urione);
                                    break;
                                case 2:
                                    image3.setImageURI(urione);
                                    break;
                            }
                        }
                    }
                }
                else if(uri != null)
                {
                    image1.setImageURI(uri);
                }
            }
        }
    }

}
