package stucom.com.petitscamperols;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class act_registre2 extends AppCompatActivity {

    public static EditText txt_code;
    Button btn_send2;
    public final static String URL = "https://api.flx.cat/dam2game/register";
    boolean responseOk = false;
    String responseaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_registre2);


        btn_send2 = findViewById(R.id.btn_send2);

        btn_send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_code = findViewById(R.id.txt_code);
                apiPost(txt_code);


                Intent tomenu = new Intent(act_registre2.this, MainActivity.class);
                startActivity(tomenu);
            }
        });



    }

    public void saveToken(Context context, String data) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("token", data);
        prefsEditor.apply();
    }

    private void apiPost(final EditText txt_code){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Response", response);
                        MainActivity.registred = true;
                        Toast.makeText(act_registre2.this, response, Toast.LENGTH_LONG).show();
                        responseOk = true;
                        responseaux = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error.Response", error.toString());
                Toast.makeText(act_registre2.this, "Error de registro", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", act_registre.txt_eMail.getText().toString());
                params.put("verify", txt_code.getText().toString());
                //Toast.makeText(act_registre.this, txt_eMail.getText().toString(), Toast.LENGTH_LONG).show();
                return params;
            }
        };
        if (responseOk) {
            saveToken(this, responseaux);
        }
        MyVolley.getInstance(this).add(request);
    }
}
