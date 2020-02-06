/*
package com.maeultalk.gongneunglife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maeultalk.gongneunglife.R;

public class UploadGalleryImageActivity extends AppCompatActivity {

    private ​​final String IMG_FILE_PATH = "imgfilepath";

    private final String IMG_TITLE = "imgtitle"

    private ​​final String IMG_ORIENTATION = "imgorientation";

​

    private final int REQ_CODE_SELECT_IMAGE = 1001;

    private String mImgPath = null;

    private String mImgTitle = null;

    private String mImgOrient = null;​

            ...

    // 사진 선택을 위해 갤러리를 호출한다

    private void getGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_ACCOUNT);

​

        // 안드로이드 KitKat(level 19)부터는 ACTION_PICK 이용​

        if (Build.VERSION.SDK_INT >= 19) {

            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);​

        }​

    else {

            intent = new Intent(Intent.ACTION_GET_ACCOUNT);​

        }​

        intent.setType("image/*");

        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);​

    }​​

            ​

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 선택된 사진을 받아 서버에 업로드한다

        if (requestCode == REQ_CODE_SELECT_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();

                getImageNameToUri(uri);

​

                try {

                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    ImageView ​img = (ImageView)findViewById(R.id.imageview);

                    img.setImageBitmap(bm);​

                }​

        catch(Exception e) {​

                    e.printStackTrace();​

                }​

            }​

        }​

    }​

            ​

    // URI 정보를 이용하여 사진 정보 가져온다

    private void getImageNameToUri(Uri data) {

        String[] proj = {

                MediaStore.Images.Media.DATA,

                MediaStore.Images.Media.TITLE,

                MediaStore.Images.Media.ORIENTATION​

        }​​;



        Cursor cursor = this.getContentResolver().query(data, proj, null, null, null);

    ​cursor.moveToFirst();

​

    ​int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);

        int column_orientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

​

        mImgPath = cursor.getString(​column_data);

        mImgTitle = cursor.getString(​column_title);

        mImgOrient = cursor.getString(​column_orientation);​

    }​ ​

}
*/
