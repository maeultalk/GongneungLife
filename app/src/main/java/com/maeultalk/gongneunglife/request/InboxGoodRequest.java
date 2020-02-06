package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GOOD_ON;
import static com.maeultalk.gongneunglife.key.Key.URL_INBOX_GOOD;

public class InboxGoodRequest extends StringRequest {

    final static private String URL = URL_INBOX_GOOD;
    private Map<String, String> parameters;

    public InboxGoodRequest(String id, String good, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("good", good);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
