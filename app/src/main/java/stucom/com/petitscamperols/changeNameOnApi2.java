package stucom.com.petitscamperols;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class changeNameOnApi2 {

    public final static String url = "https://api.flx.cat/dam2game/user";

    public void sendRequest(Context aux) {
        RequestQueue queue = Volley.newRequestQueue(aux);

        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorResponse", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", MainActivity.player.getToken());
                params.put("name", MainActivity.player.getName());
                params.put("image", MainActivity.player.getAvatar64());
                return params;
            }
        };
        queue.add(request);
    }
}
