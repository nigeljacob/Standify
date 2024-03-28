package com.nnjtrading.standify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.palette.graphics.Palette;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView songName, songAuthor, isPlaying, H1, H2, M1, M2;
    private ImageView songImage, ID1, ID2;
    private ConstraintLayout Pause, musicTogle, MusicController;
    private DatabaseReference mDatabase;
    private ViewGroup viewPort;
    private WebView webView;
    private Palette.Swatch lightSwatch, darkSwatch, lightMute, darkMute, lightVibrnt, darkVibrant;
    private LinearLayout TimeController;
    private LottieAnimationView Lottie;
    private TextClock timeMin, timeHour, timeSS, timeSS2;
    private ImageView HomeBar;
    private int times = 0 , times2 = 0;
    private CardView imageCard;
    private boolean TimeVisible = true;
    private boolean darkMode = false;
    private float x1,x2,y1,y2;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1, REQUEST_BLUETOOTH_ADVERTISE_PERMISSION  = 2;

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        TimeController = findViewById(R.id.TimeController);
        MusicController = findViewById(R.id.MusicController);
        musicTogle = findViewById(R.id.musicTogle);
        Lottie = findViewById(R.id.lottie);
        timeMin = findViewById(R.id.timeMin);
        timeHour = findViewById(R.id.timeHour);
        timeSS = findViewById(R.id.timeSS);
        timeSS2 = findViewById(R.id.timeSS2);
        HomeBar = findViewById(R.id.backToTime);
        imageCard = findViewById(R.id.imageCard);
        ID1 = findViewById(R.id.ID1);
        ID2 = findViewById(R.id.ID2);

        H1 = findViewById(R.id.hour1);
        H2 = findViewById(R.id.hour2);
        M1 = findViewById(R.id.min1);
        M2 = findViewById(R.id.min2);

        viewPort = findViewById(R.id.main);
        webView = findViewById(R.id.webview);
        songName = findViewById(R.id.songTitle);
        songAuthor = findViewById(R.id.songAuthor);
        isPlaying = findViewById(R.id.playing);
        songImage = findViewById(R.id.songImage);
        Pause = findViewById(R.id.pause);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://nnjtrading.com/index.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);



        mDatabase = FirebaseDatabase.getInstance().getReference("song");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        TransitionManager.beginDelayedTransition(viewPort);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getWindow().setBackgroundBlurRadius(400);
            getWindow().getAttributes().setBlurBehindRadius(150);
            getWindow().setAttributes(getWindow().getAttributes());
        }


        ArrayList<Songs> list = new ArrayList<>();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                times += 1;
                if (times >= 5) {
                    HomeBar.animate().alpha(0.1f).setDuration(1000).setListener(null);
                }
                if (times >= 20) {
                    HomeBar.animate().alpha(0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            HomeBar.setVisibility(View.GONE);
                        }
                    });
                }

                if(times > 30) {
                    MusicController.animate().alpha(0.9f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewPort.setBackgroundColor(0xff000000);
                        }
                    });
                    imageCard.animate().alpha(0.7f).setDuration(500).setListener(null);
                    songName.animate().alpha(0.7f).setDuration(500).setListener(null);
                    songAuthor.animate().alpha(0.7f).setDuration(500).setListener(null);
                    isPlaying.animate().alpha(0.7f).setDuration(500).setListener(null);

                    timeSS.removeTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                times2 += 1;
                if(times2 > 30) {
                    TimeController.animate().alpha(0.7f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewPort.setBackgroundColor(0xff000000);
                        }
                    });
                    H1.animate().alpha(0.7f).setDuration(500).setListener(null);
                    H2.animate().alpha(0.7f).setDuration(500).setListener(null);
                    M1.animate().alpha(0.7f).setDuration(500).setListener(null);
                    M2.animate().alpha(0.7f).setDuration(500).setListener(null);
                    ID1.animate().alpha(0.7f).setDuration(500).setListener(null);
                    ID2.animate().alpha(0.7f).setDuration(500).setListener(null);
                    Lottie.animate().alpha(0.7f).setDuration(500).setListener(null);

                    timeSS2.removeTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        timeSS.addTextChangedListener(watcher);
        timeSS2.addTextChangedListener(watcher2);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager != null) {
            Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if(lightSensor != null){
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }


        viewPort.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Intent intent = new Intent(MainActivity.this, DeskActivity.class);
                startActivity(intent);
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("tapped");
                times = 0;
                times2 = 0;

                timeSS.removeTextChangedListener(watcher);
                timeSS.addTextChangedListener(watcher);
                timeSS2.removeTextChangedListener(watcher2);
                timeSS2.addTextChangedListener(watcher2);


                HomeBar.animate().alpha(0.8f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        HomeBar.setVisibility(View.VISIBLE);
                    }
                });

                onTap(); // bring everything normal after tap
                return super.onTouch(v, event);
            }
        });

        HomeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                times = 0;
                times2 = 0;

                timeSS.removeTextChangedListener(watcher);
                timeSS2.removeTextChangedListener(watcher2);
                timeSS2.addTextChangedListener(watcher2);

                TransitionManager.beginDelayedTransition(viewPort);

                MusicController.setVisibility(View.GONE);
                TimeController.setVisibility(View.VISIBLE);
                TimeVisible = true;

                if(isPlaying.getText().equals("Now Playing")) {
                    Lottie.setVisibility(View.VISIBLE);
                    Lottie.playAnimation();
                    if(TimeVisible){
                        viewPort.setBackgroundColor(0xff000000);
                    }
                } else if(isPlaying.getText().equals("Nigel has Paused")) {
                    Lottie.pauseAnimation();
                    if(TimeVisible){
                        viewPort.setBackgroundColor(0xff000000);
                    }
                } else {
                    Lottie.setVisibility(View.GONE);
                    if(TimeVisible){
                        viewPort.setBackgroundColor(0xff000000);
                    }
                }

                onTap(); // bring everything normal after tap
            }
        });

        musicTogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                times = 0;
                times2 = 0;

                timeSS.removeTextChangedListener(watcher);
                timeSS.addTextChangedListener(watcher);
                timeSS2.removeTextChangedListener(watcher2);

                System.out.println(times + " " + times2);
                TransitionManager.beginDelayedTransition(viewPort);
                TimeController.setVisibility(View.GONE);
                MusicController.setVisibility(View.VISIBLE);
                TimeVisible = false;

                onTap(); // bring everything normal after tap
            }
        });



        timeHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String timeInHours = timeHour.getText().toString();
                String hour1 = timeInHours.substring(0,1);
                String hour2 = timeInHours.substring(1);
                H1.setText(hour1);
                H2.setText(hour2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        timeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String timeinMin = timeMin.getText().toString();
                String hour1 = timeinMin.substring(0,1);
                String hour2 = timeinMin.substring(1);

                M1.setText(hour1);
                M2.setText(hour2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                list.add(snapshot.getValue(Songs.class));

                TransitionManager.beginDelayedTransition(viewPort);

                songName.setText(list.get(0).getSongName());
                songAuthor.setText(list.get(0).getSongArtist());
                if (list.get(0).isIsplaying()) {
                    isPlaying.setText("Now Playing");
                    Pause.setVisibility(View.GONE);
                    Lottie.playAnimation();
                    songImage.getLayoutParams().height = 815;
                    songImage.getLayoutParams().width = 815;
                    songImage.requestLayout();
                } else {
                    isPlaying.setText("Nigel has Paused");
                    Pause.setVisibility(View.VISIBLE);
                    Lottie.pauseAnimation();
                    songImage.getLayoutParams().height = 522;
                    songImage.getLayoutParams().width = 522;
                    songImage.requestLayout();
                }

                if(!songName.getText().equals("Not Playing")){
                    musicTogle.setVisibility(View.VISIBLE);
                } else if(songName.getText().equals("Not Playing")){
                    MusicController.setVisibility(View.GONE);
                    musicTogle.setVisibility(View.GONE);
                    TimeController.setVisibility(View.VISIBLE);
                    TimeVisible = true;

                    timeSS2.removeTextChangedListener(watcher2);
                    timeSS2.addTextChangedListener(watcher2);
                    onTap();
                }


                final int radius = 18;
                final int margin = 0;
                final Transformation transformation = new RoundedTransform(radius, margin);

                Picasso.get().load(Uri.parse(list.get(0).getImageUri())).transform(transformation).into(songImage);
                Picasso.get().load(Uri.parse(list.get(0).getImageUri())).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                lightSwatch = palette.getDarkVibrantSwatch();
                                darkSwatch = palette.getLightVibrantSwatch();

                                darkMute = palette.getDarkMutedSwatch();
                                lightMute = palette.getLightMutedSwatch();

                                darkVibrant = palette.getDarkVibrantSwatch();
                                lightVibrnt = palette.getLightVibrantSwatch();

                                GradientDrawable gd = null;


                                if(lightSwatch != null && darkSwatch != null) {

                                    gd = new GradientDrawable(
                                            GradientDrawable.Orientation.TOP_BOTTOM,
                                            new int[] {darkSwatch.getRgb(), lightSwatch.getRgb()});
                                    gd.setCornerRadius(0f);
                                } else if(lightMute != null && darkMute != null){
                                    gd = new GradientDrawable(
                                            GradientDrawable.Orientation.TOP_BOTTOM,
                                            new int[] {darkMute.getRgb(), lightMute.getRgb()});
                                    gd.setCornerRadius(0f);
                                } else if(lightVibrnt != null && darkVibrant != null) {
                                    gd = new GradientDrawable(
                                            GradientDrawable.Orientation.TOP_BOTTOM,
                                            new int[] {darkVibrant.getRgb(), lightVibrnt.getRgb()});
                                    gd.setCornerRadius(0f);
                                }

                                if(times < 30 && !TimeVisible) {
                                    if(gd != null) {
                                        viewPort.setBackgroundDrawable(gd);
                                    } else {
                                        viewPort.setBackgroundColor(0xFF000000);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void changeTextSize(float size){
        TransitionManager.beginDelayedTransition(viewPort);
        H1.setTextSize(size);
        H2.setTextSize(size);
        M1.setTextSize(size);
        M2.setTextSize(size);
    }

    private void onTap() {
        if(!TimeVisible) {
            imageCard.animate().alpha(1f).setDuration(500).setListener(null);
            songName.animate().alpha(1f).setDuration(500).setListener(null);
            songAuthor.animate().alpha(1f).setDuration(500).setListener(null);
            isPlaying.animate().alpha(1f).setDuration(500).setListener(null);
            MusicController.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(lightSwatch != null && darkSwatch != null) {

                        GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[] {darkSwatch.getRgb(), lightSwatch.getRgb()});
                        gd.setCornerRadius(0f);
                        viewPort.setBackgroundDrawable(gd);
                    } else if(lightMute != null && darkMute != null){
                        GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[] {darkMute.getRgb(), lightMute.getRgb()});
                        gd.setCornerRadius(0f);
                        viewPort.setBackgroundDrawable(gd);
                    } else if(lightVibrnt != null && darkVibrant != null) {
                        GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[] {darkVibrant.getRgb(), lightVibrnt.getRgb()});
                        gd.setCornerRadius(0f);
                        viewPort.setBackgroundDrawable(gd);
                    }
                }
            });
        } else if(TimeVisible) {
            TimeController.animate().alpha(1f).setDuration(500).setListener(null);
            H1.animate().alpha(1f).setDuration(500).setListener(null);
            H2.animate().alpha(1f).setDuration(500).setListener(null);
            M1.animate().alpha(1f).setDuration(500).setListener(null);
            M2.animate().alpha(1f).setDuration(500).setListener(null);
            ID1.animate().alpha(1f).setDuration(500).setListener(null);
            ID2.animate().alpha(1f).setDuration(500).setListener(null);
            Lottie.animate().alpha(1f).setDuration(500).setListener(null);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if(sensorEvent.values[0] < 20 && !darkMode) {
                TransitionManager.beginDelayedTransition(viewPort);
                darkMode = true;
//                ObjectAnimator colorAnim = ObjectAnimator.ofInt(H1, "textColor", 0xff87CEFA);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.setDuration(1000);
//                colorAnim.start();
//
//                colorAnim = ObjectAnimator.ofInt(M1, "textColor", 0xff87CEFA);
//                colorAnim.start();
//
//                colorAnim = ObjectAnimator.ofInt(H2, "textColor", 0xfff0f8ff);
//                colorAnim.start();
//
//                colorAnim = ObjectAnimator.ofInt(M2, "textColor", 0xfff0f8ff);
//                colorAnim.start();
//
//                ID1.setColorFilter(ContextCompat.getColor(this, R.color.lightRed), android.graphics.PorterDuff.Mode.MULTIPLY);
//                ID2.setColorFilter(ContextCompat.getColor(this, R.color.lightRed), android.graphics.PorterDuff.Mode.MULTIPLY);

                H1.animate().alpha(0.6f).setDuration(500).setListener(null);
                H2.animate().alpha(0.6f).setDuration(500).setListener(null);
                M1.animate().alpha(0.6f).setDuration(500).setListener(null);
                M2.animate().alpha(0.6f).setDuration(500).setListener(null);
                ID1.animate().alpha(0.6f).setDuration(500).setListener(null);
                ID2.animate().alpha(0.6f).setDuration(500).setListener(null);
                Lottie.animate().alpha(0.6f).setDuration(500).setListener(null);

                if(times > 30) {
                    MusicController.animate().alpha(0.9f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewPort.setBackgroundColor(0xff000000);
                        }
                    });
                    imageCard.animate().alpha(0.4f).setDuration(500).setListener(null);
                    songName.animate().alpha(0.4f).setDuration(500).setListener(null);
                    songAuthor.animate().alpha(0.4f).setDuration(500).setListener(null);
                    isPlaying.animate().alpha(0.4f).setDuration(500).setListener(null);

                }

                if(times2 > 30) {
                    TimeController.animate().alpha(0.7f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewPort.setBackgroundColor(0xff000000);
                        }
                    });
                    H1.animate().alpha(0.4f).setDuration(500).setListener(null);
                    H2.animate().alpha(0.4f).setDuration(500).setListener(null);
                    M1.animate().alpha(0.4f).setDuration(500).setListener(null);
                    M2.animate().alpha(0.4f).setDuration(500).setListener(null);
                    ID1.animate().alpha(0.4f).setDuration(500).setListener(null);
                    ID2.animate().alpha(0.4f).setDuration(500).setListener(null);
                    Lottie.animate().alpha(0.4f).setDuration(500).setListener(null);

                }

            } else if(sensorEvent.values[0] > 20 && darkMode){
                TransitionManager.beginDelayedTransition(viewPort);
                darkMode = false;
//                ObjectAnimator colorAnim = ObjectAnimator.ofInt(H1, "textColor", 0xffafdceb);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.setDuration(1000);
//                colorAnim.start();
//
//                colorAnim = ObjectAnimator.ofInt(M1, "textColor", 0xffafdceb);
//                colorAnim.start();
//
//                colorAnim = ObjectAnimator.ofInt(H2, "textColor", 0xffffffff);
//                colorAnim.start();
//
//                colorAnim = ObjectAnimator.ofInt(M2, "textColor", 0xffffffff);
//                colorAnim.start();
//                ID1.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
//                ID2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                H1.animate().alpha(0.7f).setDuration(500).setListener(null);
                H2.animate().alpha(0.7f).setDuration(500).setListener(null);
                M1.animate().alpha(0.7f).setDuration(500).setListener(null);
                M2.animate().alpha(0.7f).setDuration(500).setListener(null);
                ID1.animate().alpha(0.7f).setDuration(500).setListener(null);
                ID2.animate().alpha(0.7f).setDuration(500).setListener(null);
                Lottie.animate().alpha(0.7f).setDuration(500).setListener(null);

                if(times > 30) {
                    MusicController.animate().alpha(0.9f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewPort.setBackgroundColor(0xff000000);
                        }
                    });
                    imageCard.animate().alpha(0.7f).setDuration(500).setListener(null);
                    songName.animate().alpha(0.7f).setDuration(500).setListener(null);
                    songAuthor.animate().alpha(0.7f).setDuration(500).setListener(null);
                    isPlaying.animate().alpha(0.7f).setDuration(500).setListener(null);

                }

                if(times2 > 30) {
                    TimeController.animate().alpha(0.7f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            viewPort.setBackgroundColor(0xff000000);
                        }
                    });
                    H1.animate().alpha(0.7f).setDuration(500).setListener(null);
                    H2.animate().alpha(0.7f).setDuration(500).setListener(null);
                    M1.animate().alpha(0.7f).setDuration(500).setListener(null);
                    M2.animate().alpha(0.7f).setDuration(500).setListener(null);
                    ID1.animate().alpha(0.7f).setDuration(500).setListener(null);
                    ID2.animate().alpha(0.7f).setDuration(500).setListener(null);
                    Lottie.animate().alpha(0.7f).setDuration(500).setListener(null);

                }

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public class mywebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                System.out.println("Bluetoot");
            }
        }
        if (requestCode == REQUEST_BLUETOOTH_ADVERTISE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                System.out.println("doneeeeeee");
            } else {
                System.out.println("permission error");
            }
        }
    }
}
