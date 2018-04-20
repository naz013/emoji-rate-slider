package com.github.naz013.emojirateslider;

import android.graphics.Color;
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
        slider2.setMoods(getMoods());
    }

    private EmojiRateSlider.Emoji[] getMoods() {
        EmojiRateSlider.Emoji[] emojis = new EmojiRateSlider.Emoji[7];
        emojis[0] = new EmojiRateSlider.Emoji(R.drawable.ic_crying, Color.parseColor("#F44336"));
        emojis[1] = new EmojiRateSlider.Emoji(R.drawable.ic_bored, Color.parseColor("#E91E63"));
        emojis[2] = new EmojiRateSlider.Emoji(R.drawable.ic_sad, Color.parseColor("#9C27B0"));
        emojis[3] = new EmojiRateSlider.Emoji(R.drawable.ic_weird, Color.parseColor("#2196F3"));
        emojis[4] = new EmojiRateSlider.Emoji(R.drawable.ic_smile, Color.parseColor("#00BCD4"));
        emojis[5] = new EmojiRateSlider.Emoji(R.drawable.ic_laught, Color.parseColor("#4CAF50"));
        emojis[6] = new EmojiRateSlider.Emoji(R.drawable.ic_in_love, Color.parseColor("#FF9800"));
        return emojis;
    }

    @Override
    public void onMoodChanged(View view, int mood) {
        Log.d("MainActivity", "onMoodChanged: " + mood);
        Toast.makeText(this, "Selected " + mood, Toast.LENGTH_SHORT).show();
    }
}
