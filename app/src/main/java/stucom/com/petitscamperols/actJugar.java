package stucom.com.petitscamperols;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class actJugar extends AppCompatActivity implements wormyView.WormyListener, SensorEventListener {

    private wormyView wormyView;
    private TextView tvScore;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_jugar);

        final Button btnNewGame = findViewById(R.id.btnNewGame);
        final ImageView pauseBg = findViewById(R.id.pauseBg);
        wormyView = findViewById(R.id.wormyView);
        tvScore = findViewById(R.id.tvScore);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvScore.setText("0");
                btnNewGame.setVisibility(View.GONE);
                pauseBg.setVisibility(View.GONE);
                wormyView.newGame();
            }
        });
        wormyView.setWormyListener(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Connect the sensor's listener to the view
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        // Nicely disconnect the sensor's listener from the view
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float ax = sensorEvent.values[0];
        float ay = sensorEvent.values[1];
        wormyView.update(-ax/2, ay/2);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void scoreUpdated(View view, int score) {
        tvScore.setText(String.valueOf(score));
    }

    @Override
    public void gameLost(View view) {
        Toast.makeText(this, getString(R.string.you_lost), Toast.LENGTH_LONG).show();
        final Button btnNewGame = findViewById(R.id.btnNewGame);
        final ImageView pauseBg = findViewById(R.id.pauseBg);
        pauseBg.setVisibility(View.VISIBLE);
        btnNewGame.setVisibility(View.VISIBLE);

        String url = "https://api.flx.cat/dam2game/user/score";
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url,
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
                params.put("level", "0");
                params.put("score", tvScore.getText().toString());
                return params;
            }
        };
        queue.add(request);
    }
}