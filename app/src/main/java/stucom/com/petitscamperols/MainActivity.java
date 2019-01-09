package stucom.com.petitscamperols;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import stucom.com.petitscamperols.model.Jugador;

public class MainActivity extends AppCompatActivity {

    public static boolean registred = false;
    public static Jugador player = new Jugador();

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
                if(registred == false){
                    Intent actRegistre = new Intent( MainActivity.this, act_registre.class );
                    startActivity(actRegistre);
                }else{
                    Intent actRank = new Intent(MainActivity.this, actRank.class);
                    startActivity(actRank);
                }
            }
        });

        Button btnAjustaments = findViewById(R.id.btnAjustaments);
        btnAjustaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registred == false){
                    Intent actRegistre = new Intent( MainActivity.this, act_registre.class );
                    startActivity(actRegistre);
                }else{
                    Intent actAjust = new Intent(MainActivity.this, actAjust.class);
                    startActivity(actAjust);
                }
            }
        });

        Button btnQuant = findViewById(R.id.btnQuant);
        btnQuant.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent actQuant = new Intent(MainActivity.this, actQuant.class);
                startActivity(actQuant);
            }
        });
    }
}
