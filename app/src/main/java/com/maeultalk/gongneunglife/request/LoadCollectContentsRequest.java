package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GET_COLLECT_CONTENTS;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_CONTENTS;

public class LoadCollectContentsRequest extends StringRequest {

    final static private String URL = URL_GET_COLLECT_CONTENTS;
    private Map<String, String> parameters;

    public LoadCollectContentsRequest(String user, String collect, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
        parameters.put("collect", collect);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
