![Android Kotlin](https://img.shields.io/badge/Android-Kotlin-6C3FD1.svg?style=flat&logo=android)
![Minimum SDK Version](https://img.shields.io/badge/API-17+-brightgreen)
[![Maven Central](https://img.shields.io/maven-central/v/com.akexorcist/roundcornerprogressbar-view?color=brightgreen&label=Maven%20Central)](https://search.maven.org/artifact/com.akexorcist/roundcornerprogressbar-view)
![Apache 2.0](https://img.shields.io/badge/License-Apache%202-brightgreen)
[![Workflow Status](https://github.com/akexorcist/RoundCornerProgressBar/actions/workflows/android.yml/badge.svg)](https://github.com/akexorcist/RoundCornerProgressBar/actions)

[![Google Dev Library](https://img.shields.io/badge/Google%20Dev%20Library-Round%20Corner%20Progress%20Bar-blue.svg?style=flat)](https://devlibrary.withgoogle.com/products/android/repos/akexorcist-RoundCornerProgressBar)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Round%20Corner%20Progress%20Bar-blue.svg?style=flat)](http://android-arsenal.com/details/1/1375)

# RoundCornerProgressBar

Round corner is cool. Let's make your progress bar with round corner

![Round Corner Progress Bar Sample](/image/header.jpg)

Colorful rounded corner progress bar for both **Android View** and **Jetpack Compose**.

# Documentation

This library ships in two modules. Pick the one that matches your UI toolkit:

| Module | Artifact | Documentation |
| --- | --- | --- |
| **Android View** | `com.akexorcist:roundcornerprogressbar-view` | [view/README.md](view/README.md) |
| **Jetpack Compose** | `com.akexorcist:roundcornerprogressbar-compose` | [compose/README.md](compose/README.md) |

Both modules share the same look, animation and feature set, so you can mix them in the same project.

# Download

## Gradle

```kotlin
// Android View
implementation("com.akexorcist:roundcornerprogressbar-view:2.2.2")

// Jetpack Compose
implementation("com.akexorcist:roundcornerprogressbar-compose:2.2.2")
```

## Demo

Go to [Google Play](https://play.google.com/store/apps/details?id=com.akexorcist.roundcornerprogressbar) to download the demo app

# Overview

### Round Corner Progress Bar

Simple round corner progress bar

![Round Corner Progress Bar](/image/overview_simple.jpg)

### Centered Round Corner Progress Bar

Round corner progress bar with progress expands from the center

![Centered Round Corner Progress Bar](/image/overview_centered.jpg)

### Icon Round Corner Progress Bar

Round corner progress bar with heading icon

![Icon Round Corner Progress Bar](/image/overview_icon.jpg)

### Text Round Corner Progress Bar

Round corner progress bar with text inside the progress

![Text Round Corner Progress Bar](/image/overview_text.jpg)

### Indeterminate Round Corner Progress Bar

Simple and centered round corner progress bar with indeterminate animation

![Indeterminate Round Corner Progress Bar and Indeterminate Centered Round Corner Progress Bar](/image/overview_indeterminate.gif)

# Feature

### Standard Features

- Primary progress and secondary progress supported
- Primary progress, secondary progress and progress background color are customizable
- Customize your own progress background padding
- Customize your own progress's corner radius
- Reversing progress bar supported
- Progress bar with gradient color? Yes!
- Progress change animation? Absolutely yes!

### Component Features

- Progress expanding from center with `CenteredRoundCornerProgressBar`
- Heading icon supported with `IconRoundCornerProgressBar`
- Text inside progress supported with `TextRoundCornerProgressBar`
- Indeterminate animation supported with `IndeterminateRoundCornerProgressBar` or `IndeterminateCenteredRoundCornerProgressBar`

# Change Log

See [CHANGELOG.md](CHANGELOG.md)

# Migration

See [MIGRATION.md](MIGRATION.md)

# Licence

Copyright 2026 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
