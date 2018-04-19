# emoji-rate-slider
Simple emoji rate picker slider

[![](https://jitpack.io/v/naz013/ColorSlider.svg)](https://jitpack.io/#naz013/ColorSlider)

Screenshot

<img src="https://github.com/naz013/ColorSlider/raw/master/res/screenshot.png" width="400" alt="Screenshot">

Download
--------
Download latest version with Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.naz013:ColorSlider:1.0.0'
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