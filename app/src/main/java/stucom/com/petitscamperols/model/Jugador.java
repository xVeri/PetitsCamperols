package stucom.com.petitscamperols.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Jugador {
    private String name;
    private String email;
    private String avatar;
    private String token;
    private String avatar64;

    public Jugador() {
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getAvatar64() { return avatar64; }
    public String getToken() { return token; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getAvatar(), R.drawable.imageView);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imagebytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imagebytes, Base64.DEFAULT);
        this.avatar64 = imageString;
    }

    public void loadPrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.name = prefs.getString("playerName", "");
        this.email = prefs.getString("playerEmail", "");
        this.avatar = prefs.getString("playerAvatar", null);
        this.token = prefs.getString("token", "");
        this.avatar64 = prefs.getString("avatar64", null);
    }

    public void savePrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("playerName", name);
        prefsEditor.putString("playerEmail", email);
        prefsEditor.putString("playerAvatar", avatar);
        prefsEditor.putString("token", token);
        prefsEditor.putString("avatar64", avatar64);
        prefsEditor.apply();
    }

    public void saveToken(Context context, String tokenapi, String emailapi) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("token", tokenapi);
        prefsEditor.putString("playerEmail", emailapi);
        prefsEditor.apply();
    }
}
