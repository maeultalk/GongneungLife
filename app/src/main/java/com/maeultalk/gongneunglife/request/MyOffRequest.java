package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_FAVOR_OFF;
import static com.maeultalk.gongneunglife.key.Key.URL_MY_OFF;

public class MyOffRequest extends StringRequest {

    final static private String URL = URL_MY_OFF;
    private Map<String, String> parameters;

    public MyOffRequest(String user, String place_code, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
        parameters.put("place_code", place_code);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
