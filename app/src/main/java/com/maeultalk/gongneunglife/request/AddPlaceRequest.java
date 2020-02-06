package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_PLACE;
//import static com.maeultalk.gongneunglife.key.Key.URL_ADD_PLACE_OVERLAP;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_CONTENTS;

public class AddPlaceRequest extends StringRequest {

    final static private String URL = URL_ADD_PLACE;
    private Map<String, String> parameters;

    public AddPlaceRequest(String code, String name, String nmap, String latitude, String longitude, String tel, String image, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("name", name);
        parameters.put("nmap", nmap);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);
        parameters.put("tel", tel);
        parameters.put("image", image);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
