package stucom.com.petitscamperols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class actJugar extends AppCompatActivity implements wormyView.WormyListener {

    private wormyView wormyView;
    private TextView tvScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_jugar);

        wormyView = findViewById(R.id.wormyView);           //TODO
        Button btnNewGame = findViewById(R.id.btnNewGame);
        tvScore = findViewById(R.id.tvScore);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvScore.setText("0");
                wormyView.newGame();
            }
        });
        wormyView.setWormyListener(this);
    }


    //Key setter, TODO Change for accelerometer
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_A:
                wormyView.update(0, +10);
                break; //Down
            case KeyEvent.KEYCODE_Q:
                wormyView.update(0, -10);
                break; //Up
            case KeyEvent.KEYCODE_O:
                wormyView.update(-10, 0);
                break; //Left
            case KeyEvent.KEYCODE_P:
                wormyView.update(+10, 0);
                break; //Right
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void scoreUpdated(View view, int score) {
        tvScore.setText(String.valueOf(score));
    }

    @Override
    public void gameLost(View view) {
        Toast.makeText(this, getString(R.string.you_lost), Toast.LENGTH_LONG).show();
    }
}