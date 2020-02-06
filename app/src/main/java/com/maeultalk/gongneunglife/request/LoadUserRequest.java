package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoadUserRequest extends StringRequest {

    final static private String URL = "http://maeultalk.vps.phps.kr/app/apis/admin/get_users.php";
    private Map<String, String> parameters;

    public LoadUserRequest(String user, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
