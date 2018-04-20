package com.github.naz013.emojirateslider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.naz013.emojislider.EmojiRateSlider;

public class MainActivity extends AppCompatActivity implements EmojiRateSlider.OnMoodChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmojiRateSlider slider = findViewById(R.id.mood_slider);
        slider.setOnMoodChangeListener(this);

        EmojiRateSlider slider1 = findViewById(R.id.mood_slider2);
        slider1.setOnMoodChangeListener(this);

        EmojiRateSlider slider2 = findViewById(R.id.mood_slider3);
        slider2.setOnMoodChangeListener(this);
    }

    @Override
    public void onMoodChanged(View view, int mood) {
        Log.d("MainActivity", "onMoodChanged: " + mood);
        Toast.makeText(this, "Selected " + mood, Toast.LENGTH_SHORT).show();
    }
}
