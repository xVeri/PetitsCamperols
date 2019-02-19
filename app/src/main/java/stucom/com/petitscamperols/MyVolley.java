package stucom.com.petitscamperols;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyVolley {

    //Esta classe gestiona toda la informacion que transmite la api

    private static MyVolley instance;

    public static MyVolley getInstance(Context context){
        if(instance == null){
            instance = new MyVolley(context.getApplicationContext());
        }

        return instance;
    }

    private RequestQueue queue;

    private MyVolley(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public <T> void add(Request<T> request) {
        queue.add(request);
    }
}
