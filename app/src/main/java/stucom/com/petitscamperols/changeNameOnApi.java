package stucom.com.petitscamperols;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class changeNameOnApi extends AppCompatActivity {

    public final static String URL = "https://api.flx.cat/dam2game/user";
    private final Context context = this.getApplicationContext();

    public void apiPostName(final String name) {
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(changeNameOnApi.this, "Datos modificados con éxito", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(changeNameOnApi.this, "Error al conectar con la api", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs =
                        context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", prefs.getString("token", ""));
                params.put("name", name);
                return params;
            }
        };
        MyVolley.getInstance(this).add(request);
    }
}

