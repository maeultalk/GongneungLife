package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_COMMENT;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_PLACE;

public class GetPlaceRequest extends StringRequest {

    final static private String URL = URL_GET_PLACE;
    private Map<String, String> parameters;

    public GetPlaceRequest(String place_code, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("place_code", place_code);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
