package biding.animal.com.animalbiding.utilities;

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prabhakant on 4-12-2017.
 */
public class VolleyInvokeWebService {

    public static final int JSON_TYPE_REQUEST = 1;
    public static final int STRING_TYPE_REQUEST = 2;
    public static final int JSON_ARRAY_REQUEST = 3;
    private int mRequestType;
    private VolleyResponseInterface mListener;
    private int mRequestMethod;
    private Context mContext;

    public VolleyInvokeWebService(Context context, int requestType, VolleyResponseInterface listener, int method) {
        mRequestType = requestType;
        mListener = listener;
        mRequestMethod = method;
        this.mContext = context;
    }

    //...
    public void hitWithOutTokenService(String URL, JSONObject payload, int requestTag) {
        switch (mRequestType) {
            case JSON_TYPE_REQUEST: {
                fetchJSONDataFromWebService(URL, payload, requestTag);
                break;
            }
            case STRING_TYPE_REQUEST: {
                fetchStringDataFromWebService(URL, payload, requestTag);
                break;
            }
            case JSON_ARRAY_REQUEST: {
                fetchJSONArrayFromWebService(URL, null, requestTag);
                break;
            }
        }
    }

    //...
    private void fetchJSONDataFromWebService(String URL, JSONObject jsonPayload, final int requestTag) {
        Log.d("VolleyInvokeWebService", "Request : URL JSON hit is-->" + URL);
        Log.d("VolleyInvokeWebService", "Payload : hit is-->" + jsonPayload);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(mRequestMethod, URL, jsonPayload,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VolleyInvokeWebService", "Response - >" + response.toString());
                        mListener.onJsonResponse(response, requestTag);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListener.onError(error, requestTag);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getCustomHeaders();
            }
        };
        jsonObjReq.setTag(requestTag);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationClass.getInstance().addToRequestQueue(jsonObjReq);
    }

    //...
    private void fetchJSONArrayFromWebService(String URL, JSONArray jsonPayload, final int requestTag) {
        Log.d("VolleyInvokeWebService", "URL JSON array hit is-->" + URL);
        Log.d("VolleyInvokeWebService", "Payload :hit is-->" + jsonPayload);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(mRequestMethod, URL, jsonPayload,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        mListener.onJsonArrayResponse(array, requestTag);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListener.onError(error, requestTag);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getCustomHeaders();
            }
        };
        jsonObjReq.setTag(requestTag);
        ApplicationClass.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void fetchStringDataFromWebService(String URL, JSONObject jsonPayload, final int requestTag) {
        Log.d("VolleyInvokeWebService", "URL String hit is-->" + URL);
        Log.d("VolleyInvokeWebService", "Payload : hit is-->" + jsonPayload);

        StringRequest stringRequest = new StringRequest(mRequestMethod, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onStringResponse(response, requestTag);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListener.onError(error, requestTag);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return getCustomHeaders();
            }
        };
        stringRequest.setTag(requestTag);
        ApplicationClass.getInstance().addToRequestQueue(stringRequest);
    }


    private HashMap<String, String> getCustomHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("TOKEN", SharedPrefernceManger.getToken());
        return headers;
    }

}
