package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_COMMENT;
import static com.maeultalk.gongneunglife.key.Key.URL_ADD_PLACE;

public class AddCommentRequest extends StringRequest {

    final static private String URL = URL_ADD_COMMENT;
    private Map<String, String> parameters;

    public AddCommentRequest(String user, String content, String comment, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user", user);
        parameters.put("content", content);
        parameters.put("comment", comment);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
