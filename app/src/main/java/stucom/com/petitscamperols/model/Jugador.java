package stucom.com.petitscamperols.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Jugador {
    private String name;
    private String email;
    private String avatar;
    private String token;

    public Jugador() {
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getToken() { return token; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public void loadPrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.name = prefs.getString("playerName", "");
        this.email = prefs.getString("playerEmail", "");
        this.avatar = prefs.getString("playerAvatar", null);
        this.token = prefs.getString("token", "");
    }

    public void savePrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("playerName", name);
        prefsEditor.putString("playerEmail", email);
        prefsEditor.putString("playerAvatar", avatar);
        prefsEditor.putString("token", token);
        prefsEditor.apply();
    }

    public void saveToken(Context context, String tokenapi) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("token", tokenapi);
        prefsEditor.apply();
    }
}
