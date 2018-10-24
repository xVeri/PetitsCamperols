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

    EditText userName;
    EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);

        userName = findViewById(R.id.layUserName);
        userEmail = findViewById(R.id.layEmail);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences myPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor confData = myPref.edit();
        confData.putString("userName", userName.getText().toString());
        confData.putString("userEmail", userEmail.getText().toString());
        confData.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mypref = getPreferences(MODE_PRIVATE);
        userName.setText(mypref.getString("userName", ""));
        userEmail.setText(mypref.getString("userEmail", ""));

    }
}
