package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_FAVOR_ON;
import static com.maeultalk.gongneunglife.key.Key.URL_GOOD_ON;

public class FavorOnRequest extends StringRequest {

    final static private String URL = URL_FAVOR_ON;
    private Map<String, String> parameters;

    public FavorOnRequest(String user, String place_code, String content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
        parameters.put("place_code", place_code);
        parameters.put("id", content);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
