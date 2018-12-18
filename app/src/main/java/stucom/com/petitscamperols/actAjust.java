package stucom.com.petitscamperols;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class actAjust extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edEmail;
    ImageView imAvatar;
    Uri photoURI;
    Jugador player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajust);
        // Capture needed layout views
        edName = findViewById(R.id.userName);
        edEmail = findViewById(R.id.userEmail);
        imAvatar = findViewById(R.id.imageView);
        // All buttons to this class (see implements in the class' declaration)
        findViewById(R.id.btnGaleria).setOnClickListener(this);
        findViewById(R.id.btnCamera).setOnClickListener(this);
        // Instantiate player object
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
        // Save player info from SharedPrefs (save changes on name and email only)
        player.setName(edName.getText().toString());
        player.setEmail(edEmail.getText().toString());
        player.savePrefs(this);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        // All buttons come here, so we decide based on their ids
        switch(view.getId()) {
            case R.id.btnCamera: getAvatarFromCamera(); break;
            case R.id.btnGaleria: getAvatarFromGallery(); break;
        }
    }

    // Needed for onActivityResult()
    private static final int AVATAR_FROM_GALLERY = 1;
    private static final int AVATAR_FROM_CAMERA = 2;


    public void getAvatarFromGallery() {
        // Call the Open Document intent searching for images
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_FROM_GALLERY);
    }

    public void getAvatarFromCamera() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photo = new File(storageDir, "photo.jpg");
        try {
            photo.createNewFile();
        } catch (IOException e) {
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoURI = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID,
                    photo);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, AVATAR_FROM_CAMERA);
        }
        catch (IllegalArgumentException e) {
        }
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
            // if null, set the default "unknown" avatar picture
            imAvatar.setImageResource(R.drawable.unknown);
        }
        else {
            // the URI must be valid, so we set it to the ImageView
            Uri uri = Uri.parse(avatar);
            imAvatar.setImageURI(uri);
        }
        if (!saveToSharedPreferences) return;
        // comply if a save to prefs was requested
        player.setAvatar(avatar);
        player.savePrefs(this);
    }

}
