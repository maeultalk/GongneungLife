package com.maeultalk.gongneunglife.test;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_ADD_COMMENT;

public class NetworkRequest extends StringRequest {

    final static private String URL = "http://maeultalk.vps.phps.kr/app/apis/test/upload_content.php";
    private Map<String, String> parameters;

    public NetworkRequest(String subject,/* String content,*/ Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("subject", subject);
//        parameters.put("content", content);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
