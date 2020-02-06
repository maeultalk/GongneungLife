package com.maeultalk.gongneunglife.test;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.matisse.Glide4Engine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MatisseActivity2 extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxPermissions rxPermissions = new RxPermissions(MatisseActivity2.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    Matisse.from(MatisseActivity2.this)
                                            .choose(MimeType.ofImage())
                                            .theme(R.style.Matisse_Dracula)
                                            .countable(true)
//                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                            .maxSelectable(3)
//                                            .originalEnable(true)
//                                            .maxOriginalSize(10)
//                                            .imageEngine(new PicassoEngine())
                                            .imageEngine(new Glide4Engine())
                                            .forResult(REQUEST_CODE_CHOOSE);
                                } else {
                                    Toast.makeText(MatisseActivity2.this, "권한요청이 거부되었습니다.", Toast.LENGTH_LONG)
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
        });
    }
}
