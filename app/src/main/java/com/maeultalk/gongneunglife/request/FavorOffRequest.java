package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_FAVOR_OFF;
import static com.maeultalk.gongneunglife.key.Key.URL_GOOD_OFF;

public class FavorOffRequest extends StringRequest {

    final static private String URL = URL_FAVOR_OFF;
    private Map<String, String> parameters;

    public FavorOffRequest(String user, String content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
        parameters.put("id", content);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
