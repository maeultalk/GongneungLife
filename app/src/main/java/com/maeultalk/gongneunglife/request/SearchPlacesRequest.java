package com.maeultalk.gongneunglife.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.maeultalk.gongneunglife.key.Key.URL_GET_PLACES;

public class SearchPlacesRequest extends StringRequest {

    final static private String URL = URL_GET_PLACES;
    private Map<String, String> parameters;

    public SearchPlacesRequest(String name, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("name", name);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
