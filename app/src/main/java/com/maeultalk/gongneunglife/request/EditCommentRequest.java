package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_DELETE_COMMENT;
import static com.maeultalk.gongneunglife.key.Key.URL_EDIT_COMMENT;

public class EditCommentRequest extends StringRequest {

    final static private String URL = URL_EDIT_COMMENT;
    private Map<String, String> parameters;

    public EditCommentRequest(String id, String comment, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("comment", comment);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
