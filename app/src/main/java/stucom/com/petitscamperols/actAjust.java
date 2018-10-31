package stucom.com.petitscamperols;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class actAjust extends AppCompatActivity {

    EditText userName;
    EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
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
