package com.maeultalk.gongneunglife.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maeultalk.gongneunglife.BuildConfig;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.RecyclerItem;
import com.maeultalk.gongneunglife.fragment.mainActivity.HomeFragment;
import com.maeultalk.gongneunglife.fragment.mainActivity.InboxFragment2Copy;
import com.maeultalk.gongneunglife.fragment.mainActivity.ScrapFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /**
     * 앱 업데이트 체크
     */
    static FirebaseRemoteConfig remoteConfig;

    String currentVersion/* = "1.0.0"*/;
    int currentVersionCode;
    String latestVersion;
    String essentialCheck;

    private RecyclerView recyclerView;

    private TextView mTextMessage;

    private String[] names = {"Charlie","Andrew","Han","Liz","Thomas","Sky","Andy","Lee","Park"};
    private RecyclerView.Adapter adapter;
    private ArrayList<RecyclerItem> mItems = new ArrayList<>();

    CollapsingToolbarLayout mCollapsingToolbarLayout;

    public TabLayout tabLayout;

    private FragmentManager fragmentManager;
    private Fragment homeFragment, inboxFragment, inboxFragmentCopy, favoriteFragment, scrapFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    /*if(fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).move();
                    }*/
                    for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible()) {
                            if(fragment instanceof HomeFragment){
                                ((HomeFragment) fragment).move();
                            }
                        }
                    }
                    if(homeFragment == null) {
                        homeFragment = HomeFragment.newInstance();
                        fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment).commit();
                    }
                    if(homeFragment != null) fragmentManager.beginTransaction().show(homeFragment).commit();
//                    if(inboxFragment != null) fragmentManager.beginTransaction().hide(inboxFragment).commit();
                    if(inboxFragmentCopy != null) fragmentManager.beginTransaction().hide(inboxFragmentCopy).commit();
                    if(favoriteFragment != null) fragmentManager.beginTransaction().hide(favoriteFragment).commit();
                    if(scrapFragment != null) fragmentManager.beginTransaction().hide(scrapFragment).commit();

                    tabLayout.setVisibility(View.GONE);
                    return true;
                /*case R.id.navigation_notifications:
                    *//*if(fragment instanceof InboxFragment2) {
                        ((InboxFragment2) fragment).move();
                    }*//*
                    for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible()) {
                            if(fragment instanceof InboxFragment2){
                                ((InboxFragment2) fragment).move();
                            }
                        }
                    }
                    if(inboxFragment == null) {
                        inboxFragment = InboxFragment2.newInstance();
                        fragmentManager.beginTransaction().add(R.id.fragment_container, inboxFragment).commit();
                    }
                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                    if(inboxFragment != null) fragmentManager.beginTransaction().show(inboxFragment).commit();
                    if(inboxFragmentCopy != null) fragmentManager.beginTransaction().hide(inboxFragmentCopy).commit();
                    if(dashboardFragment != null) fragmentManager.beginTransaction().hide(dashboardFragment).commit();
                    return true;*/
                case R.id.navigation_notifications2:
                    /*if(fragment instanceof InboxFragment2) {
                        ((InboxFragment2) fragment).move();
                    }*/
                    for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible()) {
                            if(fragment instanceof InboxFragment2Copy){
                                ((InboxFragment2Copy) fragment).move();
                            }
                        }
                    }
                    if(inboxFragmentCopy == null) {
                        inboxFragmentCopy = InboxFragment2Copy.newInstance();
                        fragmentManager.beginTransaction().add(R.id.fragment_container, inboxFragmentCopy).commit();
                    }
                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
//                    if(inboxFragment != null) fragmentManager.beginTransaction().hide(inboxFragment).commit();
                    if(inboxFragmentCopy != null) fragmentManager.beginTransaction().show(inboxFragmentCopy).commit();
                    if(favoriteFragment != null) fragmentManager.beginTransaction().hide(favoriteFragment).commit();
                    if(scrapFragment != null) fragmentManager.beginTransaction().hide(scrapFragment).commit();

                    tabLayout.setVisibility(View.GONE);
                    return true;
                /*case R.id.navigation_dashboard:
                    *//*Toast.makeText(getApplicationContext(), "자주 찾는 동네정보를 저장할 수 있도록 준비중입니다.", Toast.LENGTH_SHORT).show();
                    return false;*//*
                    for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible()) {
                            if(fragment instanceof FavoriteFragment){
                                ((FavoriteFragment) fragment).move();
                            }
                        }
                    }
                    if(favoriteFragment == null) {
                        favoriteFragment = FavoriteFragment.newInstance();
                        fragmentManager.beginTransaction().add(R.id.fragment_container, favoriteFragment).commit();
                    }
                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
//                    if(inboxFragment != null) fragmentManager.beginTransaction().hide(inboxFragment).commit();
                    if(inboxFragmentCopy != null) fragmentManager.beginTransaction().hide(inboxFragmentCopy).commit();
                    if(favoriteFragment != null) fragmentManager.beginTransaction().show(favoriteFragment).commit();
                    if(scrapFragment != null) fragmentManager.beginTransaction().hide(scrapFragment).commit();

                    tabLayout.setVisibility(View.GONE);
                    return true;*/
                case R.id.navigation_scrap:
                    /*Toast.makeText(getApplicationContext(), "자주 찾는 동네정보를 저장할 수 있도록 준비중입니다.", Toast.LENGTH_SHORT).show();
                    return false;*/
                    for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible()) {
                            if(fragment instanceof ScrapFragment){
//                                ((ScrapFragment) fragment).move();
                            }
                        }
                    }
                    if(scrapFragment == null) {
                        scrapFragment = ScrapFragment.newInstance();
                        fragmentManager.beginTransaction().add(R.id.fragment_container, scrapFragment).commit();
                    }
                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
//                    if(inboxFragment != null) fragmentManager.beginTransaction().hide(inboxFragment).commit();
                    if(inboxFragmentCopy != null) fragmentManager.beginTransaction().hide(inboxFragmentCopy).commit();
                    if(favoriteFragment != null) fragmentManager.beginTransaction().hide(favoriteFragment).commit();
                    if(scrapFragment != null) fragmentManager.beginTransaction().show(scrapFragment).commit();

                    tabLayout.setVisibility(View.VISIBLE);
                    return true;
                    /*if(dashboardFragment == null) {
                        dashboardFragment = FavoriteFragment.newInstance();
                        fragmentManager.beginTransaction().add(R.id.fragment_container, dashboardFragment).commit();
                    }
                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                    if(inboxFragment != null) fragmentManager.beginTransaction().hide(inboxFragment).commit();
                    if(dashboardFragment != null) fragmentManager.beginTransaction().show(dashboardFragment).commit();
                    return true;*/
                /*case R.id.navigation_home:
                    replaceFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(InboxFragment.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(HomeFragment.newInstance());
                    return true;*/
            }
            return false;
        }
    };

    // Fragment 변환을 해주기 위한 부분, Fragment의 Instance를 받아서 변경
    /*private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_logo);
//        getSupportActionBar().setLogo(R.mipmap.simbol_icon2);
//        getSupportActionBar().setLogo(R.drawable.app_icon);

        tabLayout = (TabLayout) findViewById(R.id.tabs);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        navigation.setSelectedItemId(R.id.navigation_home);
        /*homeFragment = HomeFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,homeFragment).commit();*/
        /*inboxFragment = InboxFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, inboxFragment).commit();*/

        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, HomeFragment.newInstance()).commit();*/

        /*recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setRecyclerView();*/

        /**
         * 유저 앱 실행 기록
         */


        /**
         * 앱 업데이트 체크
         */
        // 저장되어 있는 앱버전 불러오기
//        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            currentVersion = i.versionName;
            currentVersionCode = i.versionCode;
        } catch(PackageManager.NameNotFoundException e) { }
//        currentVersion = pref.getString("version", "1.0.0");

        checkGooglePlayServices();
        initialize();
//        checkVersion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==500) {
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 앱 업데이트 체크
     */
    private void checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());

        if (status != ConnectionResult.SUCCESS) {
            Dialog dialog = googleApiAvailability.getErrorDialog(MainActivity.this, status, -1);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.show();

            googleApiAvailability.showErrorNotification(getApplicationContext(), status);
        }
    }

    public void initialize() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                // Debug일 때 Developer Mode를 enable 하여 캐쉬 설정을 변경한다.
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        remoteConfig.setConfigSettings(configSettings);
        // 로컬 기본값을 저장한 xml을 설정한다.
        // set in-app defaults
        HashMap remoteConfigDefaults = new HashMap<String, Object>();
        remoteConfigDefaults.put("latest_version", "1.0.0");
//        remoteConfig.setDefaults(R.xml.remote_config_defaults);
        remoteConfig.setDefaults(remoteConfigDefaults);

        // 기본 캐쉬 만료시간은 12시간이다. Developer Mode 여부에 따라 fetch()에 적설한 캐시 만료시간을 넘긴다.
        remoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    remoteConfig.activateFetched();
                    checkVersion();
                }
            }
        });
    }

    private void checkVersion() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        latestVersion = remoteConfig.getString("latest_version");
        int latestVersionCode = Integer.valueOf(latestVersion);
        essentialCheck = remoteConfig.getString("essential");

//        if (!TextUtils.equals(currentVersion, latestVersion)) {
        if (currentVersionCode<latestVersionCode) {
            if (TextUtils.equals(essentialCheck, "essential")) {
                showUpdateDialog(true);
            } else {
                showUpdateDialog(false);
            }
        } else {
//            Toast.makeText(getApplicationContext(), "최신 버전입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateDialog(final boolean essential) {

        String message;
        String negativeBtn;
        if (!essential) {
            message = "새로운 버전이 있습니다. 업데이트 하시겠습니까?";
            negativeBtn = "나중에";
        } else {
            message = "새로운 버전으로 업데이트해야 사용이 가능합니다. 업데이트 하시겠습니까?";
            negativeBtn = "앱 종료";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("업데이트 알림");
        dialog.setMessage(message);
        dialog.setCancelable(!essential);
        dialog.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent marketLaunch = new Intent(
                        Intent.ACTION_VIEW);
                marketLaunch.setData(Uri
                        .parse("https://play.google.com/store/apps/details?id=com.maeultalk.gongneunglife"));
//                        .parse("https://play.google.com/store/apps/details?id=com.aligames.sgzhg.google"));
                startActivity(marketLaunch);
                finish();

                // 앱 버전 저장
                /*SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("version", latestVersion);
                editor.commit();

                currentVersion = pref.getString("version", "1.0.0");*/

//                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!essential) {
                    dialog.dismiss();
                } else {
                    finish();
                }
            }
        });
        dialog.show();
    }

    /*private void setRecyclerView(){
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new RecyclerAdapter(mItems);
        recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setData();
    }

    private void setData(){
        mItems.clear();
        // RecyclerView 에 들어갈 데이터를 추가합니다.
        for(String name : names){
            mItems.add(new RecyclerItem(name));
            mItems.add(new RecyclerItem(name));
        }
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_add_place) {
            Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
