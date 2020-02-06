package com.maeultalk.gongneunglife.fragment.placeActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.Address2Activity;
import com.maeultalk.gongneunglife.activity.AddressActivity;
import com.maeultalk.gongneunglife.model.Place;
import com.maeultalk.gongneunglife.request.GetPlaceInfoRequest;
import com.maeultalk.gongneunglife.request.GetPlaceRequest;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class InfoFragment extends Fragment implements OnMapReadyCallback {

    TextView textView;

    MapView mapView;

    String placeCode;
    String placeName;

    String url;

    LinearLayout layout_tel;
    TextView textView_tel;
    LinearLayout layout_add;
    TextView textView_add;

    TextView textView_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        if(getArguments() != null) {
            placeCode = getArguments().getString("place_code");
            placeName = getArguments().getString("place_name");
        }

        url = "http://naver.me/56QjXGaC";

        /*MapFragment mapFragment = (MapFragment) view.findViewById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);*/

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String admin = pref.getString("admin", "");

        textView_edit = (TextView) view.findViewById(R.id.textView_edit);
        if(admin.equals("true")) {
            textView_edit.setVisibility(View.VISIBLE);
        } else {
            textView_edit.setVisibility(View.GONE);
        }
        textView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Address2Activity.class);
                intent.putExtra("place_code", placeCode);
                intent.putExtra("place_name", placeName);
                startActivity(intent);
            }
        });

        layout_tel = (LinearLayout) view.findViewById(R.id.layout_tel);
        textView_tel = (TextView) view.findViewById(R.id.textView_tel);

        load();

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        textView = (TextView) view.findViewById(R.id.textView_nmap);
//        textView.setLinksClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String url = "nmap://place?lat=37.62948&lng=127.07475&appname=com.maeultalk.navermapstudy";

//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                getActivity().startActivity(intent);

                /*List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list == null || list.isEmpty()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")));
                } else {
                    startActivity(intent);
                }*/

            }
        });

        return view;
    }

    void load() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    String json = jsonResponse.getString("place");
                    Gson gson = new Gson();
                    Place place = gson.fromJson(json, Place.class);

                    // 전화번호
                    String tel = place.getTel();
                    if(TextUtils.isEmpty(tel)) {
                        layout_tel.setVisibility(View.GONE);
                    } else {
                        layout_tel.setVisibility(View.VISIBLE);
                        textView_tel.setText(tel);
                    }
                } catch (JSONException e) {

                }

            }
        };
        GetPlaceInfoRequest getPlaceInfoRequest = new GetPlaceInfoRequest(placeCode, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getPlaceInfoRequest);

    }

    @UiThread
    @Override
    public void onMapReady(final @NonNull NaverMap naverMap) {

        // TODO: 16/05/2019 db에서 데이터 불러오기
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        url = jsonResponse.getString("nmap");
                        String latitude = jsonResponse.getString("latitude");
                        String longitude = jsonResponse.getString("longitude");
                        if(!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                            Marker marker = new Marker();
                            marker.setPosition(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                            marker.setMap(naverMap);
                            marker.setCaptionText(placeName);

//        naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(37.62948, 127.07475)));
                            naverMap.moveCamera(CameraUpdate.scrollAndZoomTo(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), 15));

        /*marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Toast.makeText(getActivity(), "마커 1 클릭", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
                        } else {
                            url = "http://naver.me/56QjXGaC";
                            Marker marker = new Marker();
                            marker.setPosition(new LatLng(37.624776, 127.073763));
                            marker.setMap(naverMap);
                            marker.setCaptionText("공릉동");

//        naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(37.62948, 127.07475)));
                            naverMap.moveCamera(CameraUpdate.scrollAndZoomTo(new LatLng(37.624776, 127.073763), 15));

        /*marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Toast.makeText(getActivity(), "마커 1 클릭", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
                        }

                    } else {
//                        Toast.makeText(getActivity(), "처리실패", Toast.LENGTH_SHORT).show();
                        url = "http://naver.me/56QjXGaC";
                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(37.624776, 127.073763));
                        marker.setMap(naverMap);
                        marker.setCaptionText("공릉동");

//        naverMap.moveCamera(CameraUpdate.scrollTo(new LatLng(37.62948, 127.07475)));
                        naverMap.moveCamera(CameraUpdate.scrollAndZoomTo(new LatLng(37.624776, 127.073763), 15));

        /*marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                Toast.makeText(getActivity(), "마커 1 클릭", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
                    }
                } catch (JSONException e) {

                }

            }
        };
        GetPlaceRequest getPlaceRequest = new GetPlaceRequest(placeCode, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getPlaceRequest);

    }

}
