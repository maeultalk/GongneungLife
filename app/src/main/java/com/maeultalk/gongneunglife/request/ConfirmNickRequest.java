package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_USER;
import static com.maeultalk.gongneunglife.key.Key.URL_CONFIRM_NICK;

public class ConfirmNickRequest extends StringRequest {

    final static private String URL = URL_CONFIRM_NICK;
    private Map<String, String> parameters;

    public ConfirmNickRequest(String nick, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("nick", nick);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
