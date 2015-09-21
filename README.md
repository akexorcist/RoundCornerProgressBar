 [![Build Status](https://travis-ci.org/akexorcist/Android-RoundCornerProgressBar.svg?branch=master)](https://travis-ci.org/akexorcist/Android-RoundCornerProgressBar) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.akexorcist/RoundCornerProgressBar/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.akexorcist/RoundCornerProgressBar)
 
 
Instruction for version 2.0.0 or later isn't yet updated now. And I change attribute name, follow this

RoundCornerProgressBar Attribute
```
rcProgress
rcSecondaryProgress
rcMax
rcRadius
rcBackgroundPadding
rcReverse
rcProgressColor
rcSecondaryProgressColor
rcBackgroundColor
```

Additional attribute for IconRoundCornerProgressBar
```
rcIconSrc
rcIconSize
rcIconWidth
rcIconHeight
rcIconPadding
rcIconPaddingLeft
rcIconPaddingRight
rcIconPaddingTop
rcIconPaddingBottom
rcIconBackgroundColor
```

Additional attribute for TextRoundCornerProgressBar
```
rcTextProgressColor
rcTextProgressSize
rcTextProgressMargin
rcTextProgress
```
 
 
 
Android-RoundCornerProgressBar
==============================

![Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/header.jpg)

Round Corner Progress Bar Library for Android

Colorful progress bar with round corner on progress which you can customized a color and corner radius. A library has two type of progress bar.


What's new?
===========================
* Fix bug on older android version
* Add secondary progress (Thanks! first087)
* Refactor code (Thank! first087)
* Rename attribute


Round Corner Progress Bar
===============================

A simple round corner progress bar that can change a color of progress and background and a corner radius in dp unit (Recommended)

![Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/screenshot_02.png)


Icon Round Corner Progress Bar
===============================

A round corner progress bar with a changeable icon on the left of progress bar

![Icon Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/screenshot_01.png)



Download
===============================

Maven
```
<dependency>
  <groupId>com.akexorcist</groupId>
  <artifactId>RoundCornerProgressBar</artifactId>
  <version>1.1.0</version>
</dependency>
```

Gradle
```
compile 'com.akexorcist:RoundCornerProgressBar:1.2.2'
```

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
        app:rcBackgroundPadding="dimension"
        app:rcBackgroundRadius="dimension"
        app:rcBackgroundColor="color"
        app:rcProgressColor="color"
        app:rcProgress="integer"
        app:rcMax="integer" />
```

![Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_002.jpg)


```xml
<com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
        android:layout_width="dimension"
        android:layout_height="dimension"
        app:rcIconPadding="dimension"
        app:rcIconSize="dimension"
        app:rcIconSrc="drawable"
        app:rcBackgroundPadding="dimension"
        app:rcBackgroundRadius="dimension"
        app:rcBackgroundColor="color"
        app:rcHeaderColor="color"
        app:rcProgressColor="color"
        app:rcProgress="integer"
        app:rcMax="integer" />
```
![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_001.jpg)


Progress bar height 
* ```android:layout_height``` for RoundCornerProgressBar height 
* ```android:iconSize``` for IconRoundCornerProgressBar height




**Public method on RoundCornerProgressBar**
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



**Public method on IconRoundCornerProgressBar**
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
        app:backgroundPadding="5dp" />

    <com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
        android:id="@+id/progress_2"
        android:layout_height="320dp" 
        android:layout_width="wrap_content"
        app:backgroundPadding="10dp"
        app:iconSize="50dp" 
        app:iconPadding="5dp" />

</RelativeLayout>
```
![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/example_01.jpg)

![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/example_02.jpg)

```java
RoundCornerProgressBar progress1 = (RoundCornerProgressBar) findViewById(R.id.progress_1);
progress1.setProgressColor(Color.parseColor("#ed3b27"));
progress1.setBackgroundColor(Color.parseColor("#808080"));
progress1.setMax(70);
progress1.setProgress(15);

int progressColor1 = progress1.getProgressColor();
int backgroundColor1 = progress1.getBackgroundColor();
int max1 = progress1.etMax();
int progress1 = progress1.getProgress();


RoundCornerProgressBar progress2 = (RoundCornerProgressBar) findViewById(R.id.progress_1);
progress2.setProgressColor(Color.parseColor("#56d2c2"));
progress2.setBackgroundColor(Color.parseColor("#757575"));
progress2.setHeaderColor(Color.parseColor("#38c0ae"));
progress2.setMax(550);
progress2.setProgress(147);
progress2.setIconImageResource(anyResource);
progress2.setIconImageBitmap(anyBitmap);
progress2.setIconImageDrawable(anyDrawable);

int progressColor2 = progress2.getProgressColor();
int backgroundColor2 = progress2.getBackgroundColor();
int headerColor2 = progress2.getHeaderColor();
int max2 = progress2.getMax();
int progress2 = progress2.getProgress();
```


What's next?
===========================
* Explain new feature from first087


Documentation
===========================
Thai Language : http://www.akexorcist.com/2015/01/round-corner-progress-bar-library.html


Special Thanks
===========================
Artit Kuiwilai @first087


Licence
===========================
Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
