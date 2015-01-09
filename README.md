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


Usage
===========================
Define 'app' namespace on root view in your layout

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```


Include 'com.akexorcist.roundcornerprogressbar' or 'com.akexorcist.iconroundcornerprogressbar' in your layout

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
* ```android:layout_height``` for RoundCornerProgressBar height 
* ```android:iconSize``` for IconRoundCornerProgressBar height


Public method on RoundCornerProgressBar
```java
void setProgressColor(int color)
int getProgressColor()
void setBackgroundColor(int color)
int getBackgroundColor()

void setMax(float max)
int getMax()
void setProgress(float progress)
int getProgress()
```

Public method on IconRoundCornerProgressBar
```java
void setProgressColor(int color)
int getProgressColor()
void setBackgroundColor(int color)
int getBackgroundColor()
void setHeaderColor(int color)
int getHeaderColor()

void setMax(float max)
int getMax()
void setProgress(float progress)
int getProgress()

void setIconImageResource(int resource)
void setIconImageBitmap(Bitmap bitmap)
void setIconImageDrawable(Drawable drawable)
```

Example
===========================
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_height="match_parent" 
    android:layout_width="match_parent" >

    <com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
        android:id="@+id/progress_1"
        android:layout_height="320dp" 
        android:layout_width="40dp" 
        app:backgroundPadding="5dp"
        .
        .
        />

    <com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
        android:id="@+id/progress_2"
        android:layout_height="320dp" 
        android:layout_width="wrap_content"
        app:backgroundPadding="10dp"
        app:iconSize="50dp" 
        app:iconPadding="5dp"
        .
        .
        />

</RelativeLayout>
```


Documentation
===========================
Thai Langauge : http://www.akexorcist.com/2015/01/round-corner-progress-bar-library.html
