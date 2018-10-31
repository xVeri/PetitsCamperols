package stucom.com.petitscamperols;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class actAjust extends AppCompatActivity {

    EditText userName;
    EditText userEmail;
    Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGaleria = findViewById(R.id.btnGaleria);
        final ImageView imageview = findViewById(R.id.imageView);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                selectedImage = takePicture.getData();
                imageview.setImageURI(selectedImage);

            }
        });
        btnGaleria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                selectedImage = pickPhoto.getData();
                imageview.setImageURI(selectedImage);
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
        Toast.makeText(actAjust.this, "Datos guardados", Toast.LENGTH_LONG);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences mypref = getPreferences(MODE_PRIVATE);
        userName.setText(mypref.getString("userName", ""));
        userEmail.setText(mypref.getString("userEmail", ""));
        //imageview.setImageURI(selectedImage);
    }
}
