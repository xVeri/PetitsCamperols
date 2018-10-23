package stucom.com.petitscamperols;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class actQuant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_quant);

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

        Button btnOnSom = findViewById(R.id.btnOnSom);
        btnOnSom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent btnOnSom = new Intent(actQuant.this, actMap.class);
                startActivity(btnOnSom);
            }
        });
    }
}
