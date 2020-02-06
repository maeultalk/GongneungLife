package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_COMMENT;
import static com.maeultalk.gongneunglife.key.Key.URL_SEND_ASK;
import static com.maeultalk.gongneunglife.key.Key.URL_SEND_NEW_ASK;

public class SendAsk extends StringRequest {

    final static private String URL = URL_SEND_ASK;
    private Map<String, String> parameters;

    public SendAsk(String id, String msg, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("msg", msg);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
