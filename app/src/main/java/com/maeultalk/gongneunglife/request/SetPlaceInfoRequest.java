package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GET_PLACE;
import static com.maeultalk.gongneunglife.key.Key.URL_SET_PLACE;

public class SetPlaceInfoRequest extends StringRequest {

    final static private String URL = URL_SET_PLACE;
    private Map<String, String> parameters;

    public SetPlaceInfoRequest(String place_code, String url, String latitude, String longitude, String image, String tel, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("place_code", place_code);
        parameters.put("url", url);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);
        parameters.put("image", image);
        parameters.put("tel", tel);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
