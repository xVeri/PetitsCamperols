package stucom.com.petitscamperols;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
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

        final Context myCont = this;
        SharedPreferences myPref = getSharedPreferences("config", myCont.MODE_PRIVATE);

        final TextInputLayout userName = findViewById(R.id.layUserName);
        final TextInputLayout userEmail = findViewById(R.id.layEmail);
        Button btnSave = findViewById(R.id.btnSave);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences myPref = getPreferences(myCont.MODE_PRIVATE);
                SharedPreferences.Editor confData = myPref.edit();
                confData.putString("userName", userName.getEditText().toString());
                confData.putString("userEmail", userEmail.getEditText().toString());
                confData.commit();
            }
        });
    }
}
