package stucom.com.petitscamperols;

import android.content.Intent;
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

                Intent MainActivity = new Intent(act_registre2.this, MainActivity.class);
                startActivity(MainActivity);
            }
        });
    }

    private void apiPost(final EditText txt_code) {
        String URL = "...";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(act_registre2.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(act_registre2.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", act_registre.txt_eMail.getText().toString());
                params.put("verify", txt_code.getText().toString());
                return params;
            }
        };

        MyVolley.getInstance(this).add(request);
    }
}
