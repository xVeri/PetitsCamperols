package stucom.com.petitscamperols.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;

import stucom.com.petitscamperols.R;

public class Jugador {

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    private String avatar;
    private String token;
    @SerializedName("image")
    private String image;

    public Jugador() {
    }

    //Datos del jugador
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getImage() { return image; }
    public String getToken() { return token; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    //Guarda el vatar de un jugador
    public void setAvatar(String avatar, Resources res, Bitmap foto) {
        this.avatar = avatar;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageString = Base64.encodeToString(b, Base64.DEFAULT);
        this.image = imageString;
    }

    //Carga la información de un jugador
    public void loadPrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.name = prefs.getString("playerName", "");
        this.email = prefs.getString("playerEmail", "");
        this.avatar = prefs.getString("playerAvatar", null);
        this.token = prefs.getString("token", "");
        this.image = prefs.getString("avatar64", null);
    }

    //Guarda la información de un jugador a sharedPreferences
    public void savePrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("playerName", name);
        prefsEditor.putString("playerEmail", email);
        prefsEditor.putString("playerAvatar", avatar);
        prefsEditor.putString("token", token);
        prefsEditor.putString("avatar64", image);
        prefsEditor.apply();
    }

    //Guarda el token de un jugador a share4d preferences
    public void saveToken(Context context, String tokenapi, String emailapi) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("token", tokenapi);
        prefsEditor.putString("playerEmail", emailapi);
        prefsEditor.apply();
    }
}
