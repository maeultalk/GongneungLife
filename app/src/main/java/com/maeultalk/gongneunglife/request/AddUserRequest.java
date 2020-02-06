package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_USER;
import static com.maeultalk.gongneunglife.key.Key.URL_GET_USER;

public class AddUserRequest extends StringRequest {

    final static private String URL = URL_ADD_USER;
    private Map<String, String> parameters;

    public AddUserRequest(String email, String nick, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("nick", nick);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
