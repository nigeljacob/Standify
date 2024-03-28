package com.nnjtrading.standify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DeskActivity extends AppCompatActivity {

    private CardView app1,app2,app3,app4,app5,app6,app7,app8,app9,app10, bright_low, bright_high, vol_mute, vol_low, vol_high;
    private float x1,x2,y1,y2;
    private ConstraintLayout serverOffline;
    private DatabaseReference reference;
    private ViewGroup viewGroup, viewGroup2, viewGroup3;
    private ConstraintLayout DeskCntroller;
    private TextClock timeSS;
    private int times = 0;

    private LinearLayout row1, row2, row3;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk);

        viewGroup = findViewById(R.id.homeDesk);
        viewGroup2 = findViewById(R.id.DeskCntroller);
        viewGroup3 = findViewById(R.id.serverOffline);
        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        row3 = findViewById(R.id.row3);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        serverOffline = findViewById(R.id.serverOffline);
        DeskCntroller = findViewById(R.id.DeskCntroller);
        timeSS = findViewById(R.id.timeSS);

        bright_low = findViewById(R.id.app11);
        bright_high = findViewById(R.id.app12);
        vol_mute = findViewById(R.id.app13);
        vol_low = findViewById(R.id.app14);
        vol_high = findViewById(R.id.app15);

        app1 = findViewById(R.id.app1);
        app2 = findViewById(R.id.app2);
        app3 = findViewById(R.id.app3);
        app4 = findViewById(R.id.app4);
        app5 = findViewById(R.id.app5);
        app6 = findViewById(R.id.app6);
        app7 = findViewById(R.id.app7);
        app8 = findViewById(R.id.app8);
        app9 = findViewById(R.id.app9);
        app10 = findViewById(R.id.app10);


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                times += 1;
                System.out.println(times);

                if(times > 45) {
                    finish();
                    timeSS.removeTextChangedListener(this);
                    times = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        timeSS.addTextChangedListener(watcher);

        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(DeskActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
                finish();
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                times = 0;
                return super.onTouch(v, event);
            }
        };

        viewGroup.setOnTouchListener(onSwipeTouchListener);
        viewGroup2.setOnTouchListener(onSwipeTouchListener);
        viewGroup3.setOnTouchListener(onSwipeTouchListener);
        row1.setOnTouchListener(onSwipeTouchListener);
        row2.setOnTouchListener(onSwipeTouchListener);
        row3.setOnTouchListener(onSwipeTouchListener);

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("App").child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                TransitionManager.beginDelayedTransition(viewGroup);
                if(value.equals("Offline")) {
                    DeskCntroller.animate().alpha(0.1f).setDuration(500).setListener(null);
                    serverOffline.setVisibility(View.VISIBLE);
                } else if (value.equals("Online")) {
                    DeskCntroller.animate().alpha(1f).setDuration(500).setListener(null);
                    serverOffline.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bright_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_CHROME", bright_low);
            }
        });

        bright_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_INTELIKJ", bright_high);
            }
        });

        vol_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_BOOM", vol_mute);
            }
        });

        vol_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_MONITOR", vol_low);
            }
        });

        vol_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_FILES", vol_high);
            }
        });

        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_XCODE", app1);
            }
        });
        app2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_ANDROID_STUDIO", app2);
            }
        });
        app3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_VSCODE", app3);
            }
        });
        app4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_TEAMS", app4);
            }
        });
        app5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_SPOTIFY", app5);
            }
        });
        app6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_PHOTOSHOP", app6);
            }
        });
        app7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_PREMIER_PRO", app7);
            }
        });
        app8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_AFTER_EFFECTS", app8);
            }
        });
        app9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_WORD", app9);
            }
        });
        app10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("OPEN_POWERPOINT", app10);
            }
        });
    }

    private void sendCommand(String Message, CardView cardView) {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        cardView.startAnimation(animation);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("App").child("Command").setValue(Message);
        times = 0;
    }
}