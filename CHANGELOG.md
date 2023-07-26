Release Notes
====

2.2.1
--
* Gradle 8.0
* Android Gradle Plugin 8.1.0
* Using Java 17 for Compile & Kotlin options

2.2.0
--
* Convert all Java classes to Kotlin
* Gradle 7.5
* Android Gradle Plugin 7.4.2
* Kotlin 1.8.20
* Compile & Target SDK version 33
* Minimum SDK version 17
* Update dependencies
  * AndroidX Annotation 1.6.0

2.1.2
-- 
* Fix unspecified module error

2.1.1
-- 
* Fix bug in ([#57](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/57)) ([#77](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/77))

2.1.0
----
* `CenteredRoundCornerProgressBar` added ([#42](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/42))
* `IndeterminateRoundCornerProgressBar` and `IndeterminateCenteredRoundCornerProgressBar` added
* `IconRoundCornerProgressBar` now support for `Bitmap` and `Drawable` for icon
* Animation for progress update (disable by default) added. This feature applied to all progress bars
* Gradient progress color support (both primary and secondary progress) added. This feature applied to all progress bars (([#39](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/39)))
* Text gravity when inside/outside and text position priority attribute in `TextRoundCornerProgressBar` added
* Integer value support for progress setter (convert to float inside) added
* Update to Gradle Plugin 3.6.3 and Gradle 5.6.4
* Migrate from Android Support to AndroidX
* Still in Java! (will be Kotlin in next version)
* Fix bug in ([#43](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/43)) ([#20](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/20)) ([#74](https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/74))
* Moved from MavenCentral to JCenter. Please see "Installation" section for new artifact ID
* All new sample code. You should try it!
* Add useful annotations for Kotlin

2.0.X
----
* New code structure for further development
