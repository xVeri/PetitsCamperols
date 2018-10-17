package stucom.com.petitscamperols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class actJugar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_jugar);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent MainActivity = new Intent(actQuant.this, MainActivity.class);
                //startActivity(MainActivity);
                /// moveTaskToBack(true);
                finish();
            }
        });
    }
}
