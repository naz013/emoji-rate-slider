package com.github.naz013.emojirateslider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.naz013.emojislider.EmojiRateSlider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmojiRateSlider slider = findViewById(R.id.mood_slider);
        slider.setOnMoodChangeListener(new EmojiRateSlider.OnMoodChangeListener() {
            @Override
            public void onMoodChanged(int mood) {
                Log.d("MainActivity", "onMoodChanged: " + mood);
            }
        });
    }
}
