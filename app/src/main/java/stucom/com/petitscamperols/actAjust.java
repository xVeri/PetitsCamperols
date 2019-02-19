package stucom.com.petitscamperols;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import stucom.com.petitscamperols.model.Jugador;

public class actAjust extends AppCompatActivity implements View.OnClickListener {

    EditText edName;
    TextView edEmail;
    ImageView imAvatar;
    Uri photoURI;
    Jugador player;
    Bitmap foto;
    //changeNameOnApi changeapi = new changeNameOnApi();

    /**
     * Al crear se modifica toda la informacion con lo guardado en el Jugador
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);
        edName = findViewById(R.id.userName);
        edEmail = findViewById(R.id.edEmail);
        imAvatar = findViewById(R.id.ppAvatar);
        player = MainActivity.player;

        findViewById(R.id.btnGaleria).setOnClickListener(this);
    }

    /**
     * Al volver se hace una carga de los shared preferences y se modifica la interfaz con la
     * informacion almazenada
     */
    @Override
    public void onResume() {
        super.onResume();
        // Load player info from SharedPrefs
        player.loadPrefs(this.getApplicationContext());
        edName.setText(player.getName());
        edEmail.setText(player.getEmail());
        setAvatarImage(player.getAvatar(), false);
    }

    /**
     * Al pausar se hace un set de todo y lo guarda en los shared preferences
     */
    @Override
    public void onPause() {
        changeNameOnApi2 aux = new changeNameOnApi2();
        player.setName(edName.getText().toString());
        player.setEmail(edEmail.getText().toString());
        player.savePrefs(this.getApplicationContext());
        aux.sendRequest(this.getApplicationContext());
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnGaleria: getAvatarFromGallery(); break;
        }
    }

    private static final int AVATAR_FROM_GALLERY = 1;


    /**
     * Coge una foto de la galeria.
     */
    public void getAvatarFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_FROM_GALLERY);
    }

    /**
     * transforma una imagen a formato bitmap
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK) return;

        if (requestCode == AVATAR_FROM_GALLERY) {
            photoURI = data.getData();
            try {
                foto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException ex){

            }
        }
        String avatar = (photoURI == null) ? null : photoURI.toString();
        setAvatarImage(avatar, true);
    }

    /**
     * Modifica el avatar de un jugador.
     * En caso de que el avatar sea null se modifica este por una imagen de muestra.
     *
     * Tras configurar la imagen, esta funcion tambien se encarga de que la imagen quede guardada en
     * shared preferences.
     * @param avatar
     * @param saveToSharedPreferences
     */
    public void setAvatarImage(String avatar, boolean saveToSharedPreferences) {
        if (avatar == null) {
            imAvatar.setImageResource(R.drawable.unknown);
        }
        else {
            Uri uri = Uri.parse(avatar);
            imAvatar.setImageURI(uri);
        }
        if (!saveToSharedPreferences) return;
        player.setAvatar(avatar, getResources(), foto);
        player.savePrefs(this.getApplicationContext());
    }
}
