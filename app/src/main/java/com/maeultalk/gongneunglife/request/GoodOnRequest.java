package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_DELETE_CONTENT;
import static com.maeultalk.gongneunglife.key.Key.URL_GOOD_ON;

public class GoodOnRequest extends StringRequest {

    final static private String URL = URL_GOOD_ON;
    private Map<String, String> parameters;

    public GoodOnRequest(String user, String content, Response.Listener<String> listener) {
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
