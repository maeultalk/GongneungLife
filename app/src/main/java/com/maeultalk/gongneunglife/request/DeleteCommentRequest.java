package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_DELETE_COMMENT;
import static com.maeultalk.gongneunglife.key.Key.URL_DELETE_CONTENT;

public class DeleteCommentRequest extends StringRequest {

    final static private String URL = URL_DELETE_COMMENT;
    private Map<String, String> parameters;

    public DeleteCommentRequest(String content, String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("content", content);
        parameters.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
