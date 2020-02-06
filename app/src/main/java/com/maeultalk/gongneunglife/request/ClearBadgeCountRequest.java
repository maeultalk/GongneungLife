package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_CLEAR_BADGE_COUNT;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_INBOX2;

public class ClearBadgeCountRequest extends StringRequest {

    final static private String URL = URL_CLEAR_BADGE_COUNT;
    private Map<String, String> parameters;

    public ClearBadgeCountRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
