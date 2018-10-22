package stucom.com.petitscamperols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class actAjust extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Layout layUserName = findViewById(R.id.layUserName);
                //setContentView(R.layout.layUserName);
                //LayoutInflater factory = getLayoutInflater();
                //View layUserName = factory.inflate(R.layout.layUserName, null);
               // EditText user = (EditText) layUserName.findViewById(R.id.layUserName);
                //String User = user.getText().toString();
            }
        });


    }
}
