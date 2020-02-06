package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GET_CONTENTS;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_INBOX;

public class LoadInboxRequest extends StringRequest {

    final static private String URL = URL_GET_INBOX;
    private Map<String, String> parameters;

    public LoadInboxRequest(String user, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
