Android-RoundCornerProgressBar
==============================

![Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/header.jpg)

Round Corner Progress Bar Library for Android

Colorful progress bar with round corner on progress which you can customized a color and corner radius. A library has two type of progress bar.


Round Corner Progress Bar
===============================

A simple round corner progress bar that can change a color of progress and background and a corner radius in dp unit (Recommended)

![Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/screenshot_02.png)


Icon Round Corner Progress Bar
===============================

A round corner progress bar with a changable icon on the left of progress bar

![Icon Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/screenshot_01.png)


Dependencies
===============================
Maven Central package for your dependencies

    dependencies {
        compile 'com.akexorcist:RoundCornerProgressBar:1.0.0'
    }


Feature
===========================
* Round value configurable (recommend dp) for a corner of progress bar 
* Color changable for a progress
* Adjust padding range between progress bar and progress or between image icon and progress
* Easy easy and easy to use


Simple Usage
===========================
Thai language usage article http://www.akexorcist.com/2015/01/round-corner-progress-bar-library.html

Include 'com.akexorcist.roundcornerprogressbar' in your layout

```xml
<com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
        android:layout_width="dimension"
        android:layout_height="dimension"
        app:backgroundPadding="dimension"
        app:backgroundRadius="dimension"
        app:backgroundColor="color"
        app:progressColor="color"
        app:progress="integer"
        app:max="integer" />
```

![Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_002.jpg)


```xml
<com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
        android:layout_width="dimension"
        android:layout_height="dimension"
        app:iconPadding="dimension"
        app:iconSize="dimension"
        app:iconSrc="drawable"
        app:backgroundPadding="dimension"
        app:backgroundRadius="dimension"
        app:backgroundColor="color"
        app:headerColor="color"
        app:progressColor="color"
        app:progress="integer"
        app:max="integer" />
```
![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_001.jpg)

We recommend to define a progress bar height 
* 'android:layout_height' for RoundCornerProgressBar height 
* 'android:iconSize' for IconRoundCornerProgressBar height



