package stucom.com.petitscamperols;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import stucom.com.petitscamperols.BonkThings.Bonk;
import stucom.com.petitscamperols.BonkThings.Boost;
import stucom.com.petitscamperols.BonkThings.Coin;
import stucom.com.petitscamperols.BonkThings.ApiFlxService;
import stucom.com.petitscamperols.BonkThings.ApiUtils;
import stucom.com.petitscamperols.BonkThings.StatusSaver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameEngine {

    private static final int UPDATE_DELAY = 50;             // 50ms             => 20 physics/sec
    private static final int INVALIDATES_PER_UPDATE = 2;    // 2 * 50ms = 100ms => 10 redraws/sec
    private static final int SCALED_HEIGHT = 16 * 16;       // 16 rows of scene (16px each tile)

    private final StatusSaver statusSaver = new StatusSaver(this);

    private Context context;
    private GameView gameView;
    private BitmapSet bitmapSet;
    private Audio audio;
    private Handler handler;
    private Scene scene;
    private Bonk bonk;
    private Input input;

    private int delta = 0;

    private boolean boost;
    private List<Integer> scenes;
    private int sceneCounter = 0;

    private boolean hasWon;

    private boolean isPause;
    private boolean showDialog;

    private AlertDialog.Builder dialogBuilder;

    public Bonk getBonk() {
        return bonk;
    }

    public Context getContext() {
        return context;
    }

    public Bitmap getBitmap(int index) {
        return bitmapSet.getBitmap(index);
    }

    public Scene getScene() {
        return scene;
    }

    public Input getInput() {
        return input;
    }

    GameEngine(Context context, GameView gameView) {
        // Initialize everything
        this.context = context;
        bitmapSet = new BitmapSet(context);
        audio = new Audio(context);
        showDialog = true;

        // Relate to the game view
        this.gameView = gameView;
        gameView.setGameEngine(this);
        input = new Input();

        // Load Scene
        scene = new Scene(this);
        scenes = new ArrayList<>();
        scenes.add(R.raw.ncscene);
        scenes.add(R.raw.mini);
        scenes.add(R.raw.last_map);
        scene.loadFromFile(scenes.get(sceneCounter));

        // Create Bonk
        spawn();

        dialogBuilder = new AlertDialog.Builder(context);

        // Program the Handler for engine refresh (physics et al)
        handler = new Handler();
        Runnable runnable = new Runnable() {
            private long last = 0;
            private int count = 0;

            @Override
            public void run() {
                handler.postDelayed(this, UPDATE_DELAY);
                // Delta time between calls
                if (last == 0) last = System.currentTimeMillis();
                long now = System.currentTimeMillis();
                delta = (int) (now - last);
                last = now;
                physics(delta);
                if (++count % INVALIDATES_PER_UPDATE == 0) {
                    GameEngine.this.gameView.invalidate();
                    count = 0;
                }

                if (showDialog) {
                    if (bonk.isDead() && bonk.getLives() != 0) {
                        showDieDialog();
                        showDialog = false;
                    }
                    if (bonk.getLives() == 0) {
                        showGameOverDialog();
                        showDialog = false;
                    }
                    if (hasWon) {
                        showWinDialog();
                        showDialog = false;
                    }
                }

                if (seconds == 0) {
                    timerHandler.removeCallbacks(timerRunnable);
                    bonk.setVx(5);
                    boost = false;
                }
            }
        };
        handler.postDelayed(runnable, UPDATE_DELAY);
    }

    private void spawn() {
        if (scene.spawnX != 0 && scene.spawnY != 0) {
            bonk = new Bonk(this, scene.spawnX, scene.spawnY);
        } else {
            bonk = new Bonk(this, 100, 0);
        }
    }

    void start() {
        audio.startMusic();
        physics(delta);
        isPause = false;
        showDialog = true;
        spawn();
        bonk.setScore(0);
        bonk.setLives(3);
    }

    void stop() {
        audio.stopMusic();
        physics(0);
        showGameOverDialog();
//        isPause = true;
    }

    void pause() {
        audio.stopMusic();
        physics(0);
        showPauseDialog();
        isPause = true;
    }

    void resume() {
        audio.startMusic();
        physics(delta);
        isPause = false;
        showDialog = true;
    }

    public void win() {
        isPause = true;
        hasWon = true;
    }

    // Attend user input
    boolean onTouchEvent(MotionEvent motionEvent) {
        if (screenHeight * screenWidth == 0) return true;
        int act = motionEvent.getActionMasked();
        int i = motionEvent.getActionIndex();
        boolean down = (act == MotionEvent.ACTION_DOWN) ||
                (act == MotionEvent.ACTION_POINTER_DOWN);
        boolean touching = (act != MotionEvent.ACTION_UP) &&
                (act != MotionEvent.ACTION_POINTER_UP) &&
                (act != MotionEvent.ACTION_CANCEL);
        int x = (int) (motionEvent.getX(i)) * 100 / screenWidth;
        int y = (int) (motionEvent.getY(i)) * 100 / screenHeight;
        if ((y > 75) && (x < 40)) {
            if (!touching) input.stopLR();
            else if (x < 20) input.goLeft();            // LEFT
            else input.goRight();                       // RIGHT
        } else if ((y > 75) && (x > 80)) {
            if (down) input.jump();                     // JUMP
        } else if ((y < 20) && (x > 80)) {
            if (down) this.pause();                     // PAUSE
        } else {
            if (down) input.pause();                    // DEAD-ZONE
        }

        return true;
    }

    private void changeMap() {
        sceneCounter++;
        hasWon = false;
        if (!scenes.isEmpty()) {
            scene = new Scene(this);
            scene.loadFromFile(scenes.get(sceneCounter));
        } else {
            Log.d("ncs", "changeMap: No more scenes");
        }
        spawn();
    }

    private void restartMap() {
        if (!scenes.isEmpty()) {
            scene = new Scene(this);
            scene.loadFromFile(scenes.get(sceneCounter));
        } else {
            Log.d("ncs", "changeMap: No more scenes");
        }
        spawn();
    }

    // Testing with keyboard
    boolean onKeyEvent(KeyEvent keyEvent) {
        boolean down = (keyEvent.getAction() == KeyEvent.ACTION_DOWN);
        if (!down) return true;
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.KEYCODE_Z:
                input.goLeft();
                break;
            case KeyEvent.KEYCODE_X:
                input.goRight();
                break;
            case KeyEvent.KEYCODE_M:
                input.jump();
                audio.coin();
                break;
            case KeyEvent.KEYCODE_P:
                input.pause();
                break;
            default:
                return false;
        }
        input.setKeyboard(true);
        return true;
    }

    private Paint paint, paintKeys, paintScore, paintLives, paintBoost;
    private int screenWidth, screenHeight, scaledWidth;
    private float scale;

    // Perform physics on all game objects
    private void physics(int delta) {
        if (!isPause) {
            // Player physics
            bonk.physics(delta);

            // Other game objects' physics
            scene.physics(delta);
        }
        // ... and update scrolling
        updateOffsets();
    }

    // Update screen offsets to always have Bonk visible
    private int offsetX = 0, offsetY = 0;

    private void updateOffsets() {
        if (scaledWidth * SCALED_HEIGHT == 0) return;
        int x = bonk.getX();
        int y = bonk.getY();

        // OFFSET X (100 scaled-pixels margin)
        offsetX = Math.max(offsetX, x - scaledWidth + 124);     // 100 + Bonk Width (24)
        offsetX = Math.min(offsetX, scene.getWidth() - scaledWidth - 1);
        offsetX = Math.min(offsetX, x - 100);
        offsetX = Math.max(offsetX, 0);

        // OFFSET Y (50 scaled-pixels margin)
        offsetY = Math.max(offsetY, y - SCALED_HEIGHT + 82);     // 50 + Bonk Height (32)
        offsetY = Math.min(offsetY, scene.getHeight() - SCALED_HEIGHT - 1);
        offsetY = Math.min(offsetY, y - 50);
        offsetY = Math.max(offsetY, 0);
    }

    // Screen redraw
    void draw(Canvas canvas) {
        if (scene == null) return;

        // Create painters on first draw
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setTextSize(10);
            paintKeys = new Paint();
            paintKeys.setColor(Color.argb(20, 0, 0, 0));
            paintScore = new Paint();
            paintScore.setColor(Color.YELLOW);
            paintScore.setTextSize(5);
            paintLives = new Paint(paintScore);
            paintLives.setColor(Color.RED);
            paintBoost = new Paint(paintScore);
            paintBoost.setColor(Color.GREEN);
        }

        // Refresh scale factor if screen has changed sizes
        if (canvas.getWidth() * canvas.getHeight() != screenWidth * screenHeight) {
            screenWidth = canvas.getWidth();
            screenHeight = canvas.getHeight();
            if (screenWidth * screenHeight == 0) return; // 0 px on screen (not fully loaded)
            // New Scaling factor
            scale = (float) screenHeight / SCALED_HEIGHT;
            scaledWidth = (int) (screenWidth / scale);
        }

        // --- FIRST DRAW ROUND (scaled)
        canvas.save();
        canvas.scale(scale, scale);
        canvas.translate(-offsetX, -offsetY);

        // Background
        scene.draw(canvas, offsetX, offsetY, scaledWidth, SCALED_HEIGHT);

        // Player character
        bonk.draw(canvas);

        // --- SECOND DRAW ROUND (no-scaled)
        canvas.restore();

        // Debugging information on screen
        String text = "OX=" + offsetX + " OY=" + offsetY;
        canvas.drawText(text, 0, 20, paint);

        // Translucent keyboard on top
        canvas.scale(scale * scaledWidth / 100, scale * SCALED_HEIGHT / 100);
        canvas.drawRect(1, 76, 19, 99, paintKeys);
        canvas.drawText("«", 8, 92, paint);
        canvas.drawRect(21, 76, 39, 99, paintKeys);
        canvas.drawText("»", 28, 92, paint);
        canvas.drawRect(81, 76, 99, 99, paintKeys);
        canvas.drawText("^", 88, 92, paint);
        canvas.drawRect(81, 1, 99, 20, paintKeys);
        canvas.drawText("||", 87, 12, paint);

        // Score and Lives
        canvas.drawText("Score: " + this.bonk.getScore(), 1, 5, paintScore);
        canvas.drawText("Lives: " + this.bonk.getLives(), 1, 10, paintLives);

        // Boost
        if (boost)
            canvas.drawText("Boost: " + this.seconds, 1, 15, paintBoost);
    }

    public Audio getAudio() {
        return audio;
    }

    private void showPauseDialog() {
        dialogBuilder.setTitle("Pause")
                .setMessage("Click Resume to resume the game")
                .setNegativeButton("Resume", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resume();
                    }
                }).show();
    }

    private void showDieDialog() {
        dialogBuilder.setTitle("YOU DIED")
                .setMessage("Click Respawn to respawn")
                .setNegativeButton("Respawn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spawn();
                        resume();
                    }
                }).show();
    }

    private void showGameOverDialog() {
        dialogBuilder.setTitle("GAME OVER")
                .setMessage("You have no more Lives.")
                .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restartMap();
                        start();
                    }
                }).show();
    }

    private void showWinDialog() {
        dialogBuilder.setTitle("YOU WIN")
                .setMessage("Click \"Next map\" to continue. Click \"Upload score\" to upload score.")
                .setNegativeButton("Next map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeMap();
                        start();
                    }
                })
                .setPositiveButton("Upload score", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendScore();
                    }
                }).show();
    }

    public void sendScore() {
        ApiFlxService apiFlxService = ApiUtils.getApiFlxService();
        apiFlxService.scoreUpdate(MainActivity.player.getToken(), bonk.getScore()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle("Score updated")
                        .setMessage("Click to continue")
                        .setPositiveButton("Next map", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changeMap();
                                start();
                            }
                        }).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle("Error updating score")
                        .setMessage("Click to continue")
                        .setPositiveButton("Next map", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changeMap();
                                start();
                            }
                        }).show();
            }
        });
    }

    public void boost() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private long startTime = 0;
    private int seconds = 5;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            seconds = 5 - seconds % 60;
            Log.d("ncs", "Boost seconds: " + seconds);

            bonk.setVx(10);
            boost = true;

            timerHandler.postDelayed(this, 1000);
        }
    };

    void onResume() {
        SharedPreferences sharedPref = context.getSharedPreferences("statusData", Context.MODE_PRIVATE);
        String bonkStatus = sharedPref.getString("bonkStatus", "-1");
        Log.d("ncs", "onResume: Bonk Status: " + bonkStatus);

        if (bonkStatus.equals("-1")) {
            start();
        } else {
            try {
                JSONObject jsonBonkStatus = new JSONObject(bonkStatus);
                int bonkX = Integer.parseInt(jsonBonkStatus.getString("bonkX"));
                int bonkY = Integer.parseInt(jsonBonkStatus.getString("bonkY"));
                int lives = Integer.parseInt(jsonBonkStatus.getString("lives"));
                int score = Integer.parseInt(jsonBonkStatus.getString("score"));

                bonk = new Bonk(this, bonkX, bonkY);
                bonk.setLives(lives);
                bonk.setScore(score);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        String sceneStatus = sharedPref.getString("sceneStatus", "-1");
        Log.d("ncs", "onResume: Scene Status: " + sceneStatus);
        sceneStatus = sceneStatus.replace('=', ':');
        sceneStatus = sceneStatus.replaceAll("\\s+","");

        if (sceneStatus.equals("-1")) {
            start();
        } else {
            try {
                JSONObject jsonSceneStatus = new JSONObject(sceneStatus);

                JSONArray coins = jsonSceneStatus.getJSONArray("coins");
                scene.getCoins().clear();
                for (int i = 0; i < coins.length(); i++) {
                    Log.d("ncs", "onResume: coin: <" + coins.get(i) + ">");
                    scene.getCoins().add(new Coin(this, new JSONObject((String) coins.get(i)).getInt("coinX"), new JSONObject((String) coins.get(i)).getInt("coinY")));
                }

                JSONArray boosts = jsonSceneStatus.getJSONArray("boosts");
                scene.getBoosts().clear();
                for (int i = 0; i < boosts.length(); i++) {
                    scene.getBoosts().add(new Boost(this, new JSONObject((String) boosts.get(i)).getInt("boostX"), new JSONObject((String) boosts.get(i)).getInt("boostY")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void onPause() {
        this.statusSaver.saveStatus();
        this.audio.stopMusic();
    }

    void onDestroy() {
        this.statusSaver.saveStatus();
        audio.stopMusic();
        physics(0);
    }
}
