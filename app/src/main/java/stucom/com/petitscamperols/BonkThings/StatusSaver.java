package stucom.com.petitscamperols.BonkThings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import stucom.com.petitscamperols.GameEngine;
import stucom.com.petitscamperols.Scene;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusSaver {

    private GameEngine gameEngine;
    private Map<String, Object> bonkStatusData = new HashMap<>();
    private JSONObject sceneStatusData = new JSONObject();

    public StatusSaver(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void saveStatus() {
        Log.d("ncs", "saveStatus: Saving bonk status");
        setBonkStatusData();

        String bonkStatus = new JSONObject(bonkStatusData).toString();
        Log.d("ncs", "saveStatus: " + bonkStatus);

        Log.d("ncs", "saveStatus: Saving bonk status into Shared Preferences");
        SharedPreferences sharedPref = gameEngine.getContext().getSharedPreferences("statusData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bonkStatus", bonkStatus);
        editor.apply();


        Log.d("ncs", "saveStatus: Saving scene status");
        setSceneStatusData();

        String sceneStatus = sceneStatusData.toString();
        Log.d("ncs", "saveStatus: " + sceneStatus);

        Log.d("ncs", "saveStatus: Saving scene status into Shared Preferences");
        editor.putString("sceneStatus", sceneStatus);
        editor.apply();
    }

    private void setBonkStatusData() {
        Bonk bonk = this.gameEngine.getBonk();

        bonkStatusData.put("bonkX", bonk.getX());
        bonkStatusData.put("bonkY", bonk.getY());
        bonkStatusData.put("lives", bonk.getLives());
        bonkStatusData.put("score", bonk.getScore());
    }

    private void setSceneStatusData() {
        Scene scene = this.gameEngine.getScene();
        List<Coin> coins = scene.getCoins();
        List<Boost> boosts = scene.getBoosts();

        JSONArray jsonCoins = new JSONArray();
        JSONArray jsonBoosts = new JSONArray();

        for (int i = 0; i < coins.size() - 1; i++) {
            Map<String, Integer> coinDataMap = new HashMap<>();
            coinDataMap.put("\"coinX\"", coins.get(i).getX());
            coinDataMap.put("\"coinY\"", coins.get(i).getY());

            jsonCoins.put(coinDataMap);
        }

        for (int i = 0; i < boosts.size(); i++) {
            Map<String, Integer> boostDataMap = new HashMap<>();
            boostDataMap.put("\"boostX\"", boosts.get(i).getX());
            boostDataMap.put("\"boostY\"", boosts.get(i).getY());

            jsonBoosts.put(boostDataMap);
        }

        try {
            sceneStatusData.put("coins", jsonCoins);
            sceneStatusData.put("boosts", jsonBoosts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
