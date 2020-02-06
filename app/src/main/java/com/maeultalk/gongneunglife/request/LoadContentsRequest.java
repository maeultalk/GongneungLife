package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GET_CONTENTS;

public class LoadContentsRequest extends StringRequest {

    final static private String URL = URL_GET_CONTENTS;
    private Map<String, String> parameters;

    public LoadContentsRequest(String user, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
