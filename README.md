[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Round%20Corner%20Progress%20Bar-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1375) [ ![Download](https://api.bintray.com/packages/akexorcist/maven/round-corner-progress-bar/images/download.svg) ](https://bintray.com/akexorcist/maven/round-corner-progress-bar) ![Minimum SDK Version](https://img.shields.io/badge/minSdkVersion-17-brightgreen) [![Build Status](https://travis-ci.org/akexorcist/Android-RoundCornerProgressBar.svg?branch=master)](https://travis-ci.org/akexorcist/Android-RoundCornerProgressBar)

RoundCornerProgressBar
===
Round corner is cool. Let's make your progress bar to round corner

![Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/header.jpg)

Colorful progress bar with round corner on progress which you can customized a color and corner radius


Release Notes
===
### 2.1.0
* `CenteredRoundCornerProgressBar` added ([#42](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/42))
* `IndeterminateRoundCornerProgressBar` and `IndeterminateCenteredRoundCornerProgressBar` added
* `IconRoundCornerProgressBar` now support for `Bitmap` and `Drawable` for icon
* Animation for progress update (disable by default) added. This feature applied to all progress bars
* Gradient progress color support (both primary and secondary progress) added. This feature applied to all progress bars (([#39](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/39)))
* Text gravity when inside/outside and text position pririty attribute in `TextRoundCornerProgressBar` added
* Integer value support for progress setter (convert to float inside) added
* Update to Gradle Plugin 3.6.3 and Gradle 5.6.4
* Migrate from Android Support to AndroidX
* Still in Java! (will be Kotlin in next version)
* Fix bug in ([#43](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/43)) ([#20](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/20)) ([#74](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/74))
* Moved from MavenCentral to JCenter. Please see "Installation" section for new artifact ID
* All new sample code. You should try it!

### 2.0.X
* New code structure, Easy for further development


Demo
===
[![Round Corner Progress Bar Demo (Google Play)](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/google_play.jpg)](https://play.google.com/store/apps/details?id=com.akexorcist.roundcornerprogressbar)


Overview
===
### Round Corner Progress Bar

Simple round corner progress bar

![Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/screenshot_01.jpg)

### CenteredRoundCornerProgressBar

Round corner progress bar with progress expands from the center  

### Icon Round Corner Progress Bar

Round corner progress bar with heading icon

![Icon Round Corner Progress Bar Sample](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/screenshot_02.jpg)

### TextRoundCornerProgressBar
Round corner progress bar with text inside the progress

### IndeterminateRoundCornerProgressBar

Simple round corner progress bar with indeterminate animation

### IndeterminateCenteredRoundCornerProgressBar

Centered Round corner progress bar with indeterminate animation


Installation
===============================

```groovy
implementation 'com.akexorcist:round-corner-progress-bar:2.1.0'
```

Migration from 2.0.x to 2.1.0
===
### Moving from MavenCentral to JCenter with new artifact ID
From (MavenCentral)

```groovy
// Old (MavenCentral)
implementation 'com.akexorcist:RoundCornerProgressBar:2.0.3'

// New (JCenter)
implementation 'com.akexorcist:round-corner-progress-bar:2.1.0'
```


Feature
===========================

### Standard Features

* Primary progress and secondary progress supported
* Primary progress, secondary progress and progress background color are customizable
* Customize your own progress background padding
* Customize your own progress's corner radius
* Reversing progress bar supported
* Progress bar with gradient color? Yes!
* Progress change animation? Absolutely yes!

### Component Features
* Progress expanding from center with `CenteredRoundCornerProgressBar` 
* Heading icon supported with `IconRoundCornerProgressBar`
* Text inside progress supported with `TextRoundCornerProgressBar`
* Indeterminate animation supported with `IndeterminateRoundCornerProgressBar` or `IndeterminateCenteredRoundCornerProgressBar`


Usage
===========================
For using custom attribute of progress bar, define 'app' namespace as root view attribute in your layout 

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

RoundCornerProgressBar
-------------------------------

![Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_01.jpg)


### Layout XML
```xml
<com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
        app:rcProgress="float"
        app:rcSecondaryProgress="float"
        app:rcMax="float"
        app:rcRadius="dimension"
        app:rcBackgroundPadding="dimension"
        app:rcReverse="boolean"
        app:rcProgressColor="color"
        app:rcSecondaryProgressColor="color"
        app:rcBackgroundColor="color"
        app:rcAnimationEnable="boolean"
        app:rcAnimationSpeedScale="float" />
```

### Public Methods
```kotlin
// Progress
fun getMax(): Float
fun setMax(max: Float)
fun getProgress(): Float
fun setProgress(progress: Int)
fun setProgress(progress: Float)
fun getSecondaryProgress(): Float
fun setSecondaryProgress(secondaryProgress: Int)
fun setSecondaryProgress(secondaryProgress: Float)

// Dimension
fun getRadius(): Int
fun setRadius(radius: Int)
fun getPadding(): Int
fun setPadding(padding: Int)
fun getLayoutWidth(): Float

// Animation
fun enableAnimation()
fun disableAnimation()
fun getAnimationSpeedScale(): Float
fun setAnimationSpeedScale(scale: Float)
fun isProgressAnimating(): Boolean
fun isSecondaryProgressAnimating(): Boolean

// Reversing Progress
fun isReverse(): Boolean
fun setReverse(isReverse: Boolean)

// Color
fun getProgressBackgroundColor(): Int
fun setProgressBackgroundColor(color: Int)
fun getProgressColor(): Int
fun setProgressColor(color: Int)
fun getProgressColors(): IntArray
fun setProgressColors(colors: IntArray)
fun getSecondaryProgressColor(): Int
fun setSecondaryProgressColor(color: Int)
fun getSecondaryProgressColors(): IntArray
fun setSecondaryProgressColors(colors: IntArray)

// Listener
fun setOnProgressChangedListener(listener: OnProgressChangedListener)
```

### Example
```xml
<com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
    android:layout_width="320dp" 
    android:layout_height="40dp" 
    app:rcBackgroundPadding="5dp" />

<com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
    android:layout_width="320dp" 
    android:layout_height="wrap_content"
    app:rcBackgroundPadding="10dp"
    app:rcIconSize="50dp" 
    app:rcIconPadding="5dp" />
```
![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/example_01.jpg)

![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/example_02.jpg)


CenteredRoundCornerProgressBar
---
Same as RoundCornerProgressBar

### Layout XML
```xml
<com.akexorcist.roundcornerprogressbar.CenteredRoundCornerProgressBar
        ... />
```

IconRoundCornerProgressBar
-------------------------------
Icon size is required for this progress bar. Use `wrap_content` for `layout_height` is recommended.

```xml
<com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
        android:layout_height="wrap_content"
        app:rcIconSize="40dp"
        ... />
```

### Layout XML ######
```xml
<com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar
        app:rcProgress="float"
        app:rcSecondaryProgress="float"
        app:rcMax="float"
        app:rcRadius="dimension"
        app:rcBackgroundPadding="dimension"
        app:rcReverse="boolean"
        app:rcProgressColor="color"
        app:rcSecondaryProgressColor="color"
        app:rcBackgroundColor="color"
        app:rcAnimationEnable="boolean"
        app:rcAnimationSpeedScale="float" 

        app:rcIconSrc="reference"
        app:rcIconSize="dimension"
        app:rcIconWidth="dimension"
        app:rcIconHeight="dimension"
        app:rcIconPadding="dimension"
        app:rcIconPaddingLeft="dimension"
        app:rcIconPaddingRight="dimension"
        app:rcIconPaddingTop="dimension"
        app:rcIconPaddingBottom="dimension"
        app:rcIconBackgroundColor="color" />
```

### Public Methods ######
```kotlin
// Progress
fun getMax(): Float
fun setMax(max: Float)
fun getProgress(): Float
fun setProgress(progress: Int)
fun setProgress(progress: Float)
fun getSecondaryProgress(): Float
fun setSecondaryProgress(secondaryProgress: Int)
fun setSecondaryProgress(secondaryProgress: Float)

// Dimension
fun getRadius(): Int
fun setRadius(radius: Int)
fun getPadding(): Int
fun setPadding(padding: Int)
fun getLayoutWidth(): Float

fun getIconSize(): Int
fun setIconSize(size: Int)
fun getIconPadding(): Int
fun setIconPadding(padding: Int)
fun getIconPaddingLeft(): Int
fun setIconPaddingLeft(padding: Int)
fun getIconPaddingTop(): Int
fun setIconPaddingTop(padding: Int)
fun getIconPaddingRight(): Int
fun setIconPaddingRight(padding: Int)
fun getIconPaddingBottom(): Int
fun setIconPaddingBottom(padding: Int)

// Animation
fun enableAnimation()
fun disableAnimation()
fun getAnimationSpeedScale(): Float
fun setAnimationSpeedScale(scale: Float)
fun isProgressAnimating(): Boolean
fun isSecondaryProgressAnimating(): Boolean

// Reversing Progress
fun isReverse(): Boolean
fun setReverse(isReverse: Boolean)

// Color
fun getProgressBackgroundColor(): Int
fun setProgressBackgroundColor(color: Int)
fun getProgressColor(): Int
fun setProgressColor(color: Int)
fun getProgressColors(): IntArray
fun setProgressColors(colors: IntArray)
fun getSecondaryProgressColor(): Int
fun setSecondaryProgressColor(color: Int)
fun getSecondaryProgressColors(): IntArray
fun setSecondaryProgressColors(colors: IntArray)

fun getColorIconBackground(): Int
fun setIconBackgroundColor(color: Int)

// Icon
fun getIconImageResource(): Int
fun setIconImageResource(resId: Int)
fun getIconImageBitmap(): Birmap
fun setIconImageBitmap(bitmap: Bitmap)
fun getIconImageDrawable(): Drawable
fun setIconImageDrawable(drawable: Drawable)

// Listener
fun setOnProgressChangedListener(listener: OnProgressChangedListener)

fun setOnIconClickListener(listener: OnIconClickListener)
```

TextRoundCornerProgressBar
---

### Layout XML
```xml
<com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
        app:rcProgress="float"
        app:rcSecondaryProgress="float"
        app:rcMax="float"
        app:rcRadius="dimension"
        app:rcBackgroundPadding="dimension"
        app:rcReverse="boolean"
        app:rcProgressColor="color"
        app:rcSecondaryProgressColor="color"
        app:rcBackgroundColor="color"
        app:rcAnimationEnable="boolean"
        app:rcAnimationSpeedScale="float"
        
        app:rcTextProgressColor="color"
        app:rcTextProgressSize="dimension"
        app:rcTextProgressMargin="dimension"
        app:rcTextProgress="String"
        app:rcTextInsideGravity="start|end"
        app:rcTextOutsideGravity="start|end"
        app:rcTextPositionPriority="inside|outside" />
```

### Public Methods
```kotlin
// Progress
fun getMax(): Float
fun setMax(max: Float)
fun getProgress(): Float
fun setProgress(progress: Int)
fun setProgress(progress: Float)
fun getSecondaryProgress(): Float
fun setSecondaryProgress(secondaryProgress: Int)
fun setSecondaryProgress(secondaryProgress: Float)

// Dimension
fun getRadius(): Int
fun setRadius(radius: Int)
fun getPadding(): Int
fun setPadding(padding: Int)
fun getLayoutWidth(): Float

fun getTextProgressSize(): Int
fun setTextProgressSize(size: Int)
fun getTextProgressMargin(): Int
fun setTextProgressMargin(margin: Int)

// Animation
fun enableAnimation()
fun disableAnimation()
fun getAnimationSpeedScale(): Float
fun setAnimationSpeedScale(scale: Float)
fun isProgressAnimating(): Boolean
fun isSecondaryProgressAnimating(): Boolean

// Reversing Progress
fun isReverse(): Boolean
fun setReverse(isReverse: Boolean)

// Color
fun getProgressBackgroundColor(): Int
fun setProgressBackgroundColor(color: Int)
fun getProgressColor(): Int
fun setProgressColor(color: Int)
fun getProgressColors(): IntArray
fun setProgressColors(colors: IntArray)
fun getSecondaryProgressColor(): Int
fun setSecondaryProgressColor(color: Int)
fun getSecondaryProgressColors(): IntArray
fun setSecondaryProgressColors(colors: IntArray)

fun getTextProgressColor(): Int
fun setTextProgressColor(color: Int)

// Text
fun getProgressText(): String
fun setProgressText(text: String)

// Position
fun getTextPositionPriority(): Int
fun setTextPositionPriority(priority: Int)
fun getTextInsideGravity(): Int
fun setTextInsideGravity(gravity: Int)
fun getTextOutsideGravity(): Int
fun setTextOutsideGravity(gravity: Int)

// Listener
fun setOnProgressChangedListener(listener: OnProgressChangedListener)
```

IndeterminateRoundCornerProgressBar
---

### Layout XML
```xml
<com.akexorcist.roundcornerprogressbar.indeterminate.IndeterminateRoundCornerProgressBar
        app:rcRadius="dimension"
        app:rcBackgroundPadding="dimension"
        app:rcReverse="boolean"
        app:rcProgressColor="color"
        app:rcSecondaryProgressColor="color"
        app:rcBackgroundColor="color"
        app:rcAnimationSpeedScale="float" />
```

### Public Methods
```kotlin
// Dimension
fun getRadius(): Int
fun setRadius(radius: Int)
fun getPadding(): Int
fun setPadding(padding: Int)
fun getLayoutWidth(): Float

// Animation
fun getAnimationSpeedScale(): Float
fun setAnimationSpeedScale(scale: Float)

// Reversing Progress
fun isReverse(): Boolean
fun setReverse(isReverse: Boolean)

// Color
fun getProgressBackgroundColor(): Int
fun setProgressBackgroundColor(color: Int)
fun getProgressColor(): Int
fun setProgressColor(color: Int)
fun getProgressColors(): IntArray
fun setProgressColors(colors: IntArray)
fun getSecondaryProgressColor(): Int
fun setSecondaryProgressColor(color: Int)
fun getSecondaryProgressColors(): IntArray
fun setSecondaryProgressColors(colors: IntArray)
```

IndeterminateCenteredRoundCornerProgressBar
---
Same as IndeterminateRoundCornerProgressBar

### Layout XML
```xml
<com.akexorcist.roundcornerprogressbar.IndeterminateCenteredRoundCornerProgressBar
        ... />
```

### Icon Size & Padding ###
![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_03.jpg)

Progress Bar Reversing
-------------------------------
![Icon Round Corner Progress Bar Usage](https://raw.githubusercontent.com/akexorcist/Android-RoundCornerProgressBar/master/image/usage_04.jpg)


Documentation
===========================
Thai Language : [Round Corner Progress Bar สำหรับคนบ้าขอบมน](http://www.akexorcist.com/2015/01/round-corner-progress-bar-library.html)


Special Thanks
===========================
@first087, @redsanso


What's Next
===========================
* IconTextRoundCornerProgressBar ([#69](https://github.com/akexorcist/Android-RoundCornerProgressBar/pull/69))
* Support color resource in color related attributes
* Add set max progress with integer


Known Issues
===
* Incorect progress showing in `CenteredRoundCornerProgressBar` with 1%-2% value
* Incorrect text's width in `TextRoundCornerProgressBar` when `outside` priority and value close to 100%  
* `setProgress(progress: Int)` does not update text position


Licence
===========================
Copyright 2020 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
