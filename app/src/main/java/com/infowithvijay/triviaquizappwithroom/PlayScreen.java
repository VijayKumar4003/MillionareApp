package com.infowithvijay.triviaquizappwithroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.infowithvijay.triviaquizappwithroom.MusicController.StopSound;

public class PlayScreen extends AppCompatActivity {

    private static Context context;


    Animation rotation_clock_wise,rotation_anti_clock_wise;
    ImageView imageView_outerwheel,imageView_inner_wheel;

    long backPressedTime;

    Button btPlayQuiz,btSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        btPlayQuiz = findViewById(R.id.btPlayQuiz);
        btSettings = findViewById(R.id.btsettings);
        imageView_inner_wheel = findViewById(R.id.imgInnerView);
        imageView_outerwheel = findViewById(R.id.imgOuterView);

        rotation_anti_clock_wise = AnimationUtils.loadAnimation(this,R.anim.rotation_anti_clock_wise);
        rotation_clock_wise = AnimationUtils.loadAnimation(this,R.anim.rotation_clock_wise);

        imageView_inner_wheel.startAnimation(rotation_clock_wise);
        imageView_outerwheel.startAnimation(rotation_anti_clock_wise);

        context = getApplicationContext();
        MusicController.currentActivity = this;


        if (SettingPreference.getMusicEnableDisable(context)){
            try {
                MusicController.playSound();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }



        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent settingIntent = new Intent(PlayScreen.this,Settings.class);
                startActivity(settingIntent);


            }
        });



        btPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent playquizIntent = new Intent(PlayScreen.this,QuizActivity.class);
                startActivity(playquizIntent);

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


    @Override
    public void onBackPressed() {

        StopSound();

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            new AlertDialog.Builder(this)
                    .setTitle("Do you want to exit ?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            setResult(RESULT_OK, new Intent().putExtra("Exit",true));
                            finish();
                        }
                    }).create().show();
        }else {
            Toast.makeText(context, "Press Again to Exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }
}
