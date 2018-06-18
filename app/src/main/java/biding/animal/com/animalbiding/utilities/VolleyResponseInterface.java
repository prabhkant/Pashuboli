package biding.animal.com.animalbiding.utilities;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Prabhakant on 4-12-2017.
 */

public interface VolleyResponseInterface {

    public void onJsonResponse(JSONObject object, int tag);
    public void onJsonArrayResponse(JSONArray array, int tag);
    public void onStringResponse(String string, int tag);
    public void onStatusCodeResponse(int status, int tag);
    public void onError(VolleyError error, int tag);
}
