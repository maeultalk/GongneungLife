package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GET_COMMENTS;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_CONTENTS;

public class LoadCommentsRequest extends StringRequest {

    final static private String URL = URL_GET_COMMENTS;
    private Map<String, String> parameters;

    public LoadCommentsRequest(String content, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("content", content);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
