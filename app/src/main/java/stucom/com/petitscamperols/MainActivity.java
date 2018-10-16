package stucom.com.petitscamperols;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnJugar = findViewById(R.id.btnJugar);
        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent actJugar = new Intent(MainActivity.this, actJugar.class);
                startActivity(actJugar);
            }
        });

        Button btnRank = findViewById(R.id.btnRanquing);
        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent actRank = new Intent(MainActivity.this, actRank.class);
                startActivity(actRank);
            }
        });
    }
}
