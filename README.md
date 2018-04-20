# Emoji Rate Picker View

<img src="https://github.com/naz013/emoji-rate-slider/raw/master/res/app_icon.png" width="100" alt="Emoji Rate Picker View">

Simple emoji rate picker slider view.

Inspired by this work - [UpLabs](https://www.uplabs.com/posts/on-off-switch-daily-ui-015-art)
--------

[![](https://jitpack.io/v/naz013/emoji-rate-slider.svg)](https://jitpack.io/#naz013/emoji-rate-slider)

Screenshot

<img src="https://github.com/naz013/emoji-rate-slider/raw/master/res/screenshot.png" width="400" alt="Screenshot">

Sample APP
--------
[Download](https://github.com/naz013/emoji-rate-slider/raw/master/res/app-release.apk)

[Google Play](https://play.google.com/store/apps/details?id=com.github.naz013.emojirateslider)


Download
--------
Download latest version with Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.naz013:emoji-rate-slider:1.0.2'
}
```

Usage
-----
```xml
<com.github.naz013.emojislider.EmojiRateSlider
        android:id="@+id/mood_slider"
        android:layout_width="wrap_content"
        android:layout_height="48dp"
        app:ers_color_bg="#fefefe"
        app:ers_color_happy="#51ED8B"
        app:ers_color_sad="#ED6651"
        app:ers_color_weird="#FFC107"
        app:ers_has_weird="true"
        app:ers_max="9"
        app:ers_progress="4" />
```


License
-------

    Copyright 2017 Nazar Suhovich

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
