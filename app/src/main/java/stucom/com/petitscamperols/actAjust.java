package stucom.com.petitscamperols;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import stucom.com.petitscamperols.model.Jugador;

public class actAjust extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edEmail;
    ImageView imAvatar;
    Uri photoURI;
    Jugador player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);
        edName = findViewById(R.id.userName);
        edEmail = findViewById(R.id.userEmail);
        imAvatar = findViewById(R.id.imageView);
        findViewById(R.id.btnGaleria).setOnClickListener(this);
        player = new Jugador();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load player info from SharedPrefs
        player.loadPrefs(this);
        edName.setText(player.getName());
        edEmail.setText(player.getEmail());
        setAvatarImage(player.getAvatar(), false);
    }

    @Override
    public void onPause() {
        player.setName(edName.getText().toString());
        player.setEmail(edEmail.getText().toString());
        player.savePrefs(this);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnGaleria: getAvatarFromGallery(); break;
        }
    }

    private static final int AVATAR_FROM_GALLERY = 1;


    public void getAvatarFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK) return;

        if (requestCode == AVATAR_FROM_GALLERY) {
            photoURI = data.getData();
        }
        String avatar = (photoURI == null) ? null : photoURI.toString();
        setAvatarImage(avatar, true);
    }

    public void setAvatarImage(String avatar, boolean saveToSharedPreferences) {
        if (avatar == null) {
            imAvatar.setImageResource(R.drawable.unknown);
        }
        else {
            Uri uri = Uri.parse(avatar);
            imAvatar.setImageURI(uri);
        }
        if (!saveToSharedPreferences) return;
        player.setAvatar(avatar);
        player.savePrefs(this);
    }

}
