# RoundCornerProgressBar — Jetpack Compose

The Jetpack Compose implementation of RoundCornerProgressBar, with full feature parity to the [Android View module](../view/README.md). Same look, same animation, same set of options — expressed as composable functions.

See the [project README](../README.md) for the library overview.

# Download

```kotlin
implementation("com.akexorcist:roundcornerprogressbar-compose:2.2.2")
```

# Usage

All composables live in the `com.akexorcist.roundcornerprogressbar.compose` package. Every parameter has a default that matches the View library, so the only required argument is `progress` (and `icon` / `text` for the icon and text variants).

Size is controlled through the `modifier`. When no size constraint is given the bar falls back to 240 x 30 dp.

## RoundCornerProgressBar

Simple round corner progress bar.

```kotlin
RoundCornerProgressBar(
    progress = 40f,
    modifier = Modifier
        .width(260.dp)
        .height(30.dp),
    max = 100f,
    radius = 10.dp,
    backgroundPadding = 2.dp,
    backgroundColor = Color(0x0A000000),
    progressColor = Color(0xFFEF5350),
    secondaryProgress = 60f,
    secondaryProgressColor = Color(0x40EF5350),
)

// Gradient, reversed, with progress change animation
RoundCornerProgressBar(
    progress = 70f,
    progressColors = listOf(Color(0xFF00BCD4), Color(0xFF3F51B5)),
    reverse = true,
    animationEnabled = true,
    animationSpeedScale = 1f,
    onProgressChanged = { progress -> /* ... */ },
)
```

### Parameters

| Parameter | Type | Default | Matches (View) |
| --- | --- | --- | --- |
| `progress` | `Float` | — | `rcProgress` |
| `modifier` | `Modifier` | `Modifier` | `layout_width` / `layout_height` |
| `max` | `Float` | `100f` | `rcMax` |
| `secondaryProgress` | `Float` | `0f` | `rcSecondaryProgress` |
| `radius` | `Dp` | `30.dp` | `rcRadius` |
| `backgroundPadding` | `Dp` | `0.dp` | `rcBackgroundPadding` |
| `reverse` | `Boolean` | `false` | `rcReverse` |
| `backgroundColor` | `Color` | `Color(0xFF5F5F5F)` | `rcBackgroundColor` |
| `progressColor` | `Color` | `Color(0xFF7F7F7F)` | `rcProgressColor` |
| `progressColors` | `List<Color>?` | `null` | `rcProgressColors` (gradient) |
| `secondaryProgressColor` | `Color` | `Color.Transparent` | `rcSecondaryProgressColor` |
| `secondaryProgressColors` | `List<Color>?` | `null` | `rcSecondaryProgressColors` (gradient) |
| `animationEnabled` | `Boolean` | `false` | `rcAnimationEnable` |
| `animationSpeedScale` | `Float` | `1f` | `rcAnimationSpeedScale` |
| `onProgressChanged` | `((Float) -> Unit)?` | `null` | `OnProgressChangedListener` |
| `onSecondaryProgressChanged` | `((Float) -> Unit)?` | `null` | `OnProgressChangedListener` |

## CenteredRoundCornerProgressBar

Round corner progress bar with the progress expanding from the center. Same parameters as `RoundCornerProgressBar`.

```kotlin
CenteredRoundCornerProgressBar(
    progress = 50f,
    modifier = Modifier
        .width(260.dp)
        .height(30.dp),
    radius = 10.dp,
    backgroundPadding = 4.dp,
)
```

## IconRoundCornerProgressBar

Round corner progress bar with a heading icon. The `icon` is a `Painter`, so use `painterResource(...)` for a drawable.

```kotlin
IconRoundCornerProgressBar(
    icon = painterResource(R.drawable.ic_android),
    progress = 90f,
    modifier = Modifier
        .width(260.dp)
        .height(44.dp),
    max = 150f,
    radius = 5.dp,
    backgroundPadding = 2.dp,
    iconSize = DpSize(40.dp, 40.dp),
    iconPadding = PaddingValues(5.dp),
    iconBackgroundColor = Color(0xFF00796B),
    onIconClick = { /* ... */ },
)
```

### Additional parameters

In addition to the [`RoundCornerProgressBar` parameters](#parameters):

| Parameter | Type | Default | Matches (View) |
| --- | --- | --- | --- |
| `icon` | `Painter` | — | `rcIconSrc` / `setIconImage*` |
| `iconContentDescription` | `String?` | `null` | accessibility |
| `iconSize` | `DpSize` | `DpSize(20.dp, 20.dp)` | `rcIconSize` / `rcIconWidth` / `rcIconHeight` |
| `iconPadding` | `PaddingValues` | `PaddingValues(0.dp)` | `rcIconPadding` / `rcIconPadding*` |
| `iconBackgroundColor` | `Color` | `Color(0xFF5F5F5F)` | `rcIconBackgroundColor` |
| `onIconClick` | `(() -> Unit)?` | `null` | `OnIconClickListener` |

> The View version uses `wrap_content` for its height. In Compose, size the bar with the `modifier` — a common choice is `iconSize.height + backgroundPadding * 2`.

## TextRoundCornerProgressBar

Round corner progress bar with a text label that moves with the progress.

```kotlin
TextRoundCornerProgressBar(
    text = "75",
    progress = 75f,
    modifier = Modifier
        .width(260.dp)
        .height(30.dp),
    textColor = Color.White,
    textSize = 16.sp,
    textMargin = 10.dp,
    textInsideGravity = TextGravity.Start,
    textOutsideGravity = TextGravity.Start,
    textPositionPriority = TextPositionPriority.Inside,
)
```

### Additional parameters

In addition to the [`RoundCornerProgressBar` parameters](#parameters):

| Parameter | Type | Default | Matches (View) |
| --- | --- | --- | --- |
| `text` | `String` | — | `rcTextProgress` |
| `textColor` | `Color` | `Color.White` | `rcTextProgressColor` |
| `textSize` | `TextUnit` | `16.sp` | `rcTextProgressSize` |
| `textMargin` | `Dp` | `10.dp` | `rcTextProgressMargin` |
| `textInsideGravity` | `TextGravity` | `TextGravity.Start` | `rcTextInsideGravity` |
| `textOutsideGravity` | `TextGravity` | `TextGravity.Start` | `rcTextOutsideGravity` |
| `textPositionPriority` | `TextPositionPriority` | `TextPositionPriority.Inside` | `rcTextPositionPriority` |

`TextGravity` has `Start` and `End`. `TextPositionPriority` has `Inside` and `Outside`.

## IndeterminateRoundCornerProgressBar / IndeterminateCenteredRoundCornerProgressBar

Endlessly animating progress bars. They do not take a `progress` value — the sweep runs automatically. There is no `animationEnabled` parameter; `animationSpeedScale` controls the sweep duration.

```kotlin
IndeterminateRoundCornerProgressBar(
    modifier = Modifier
        .width(260.dp)
        .height(10.dp),
    backgroundColor = Color(0x0A000000),
    progressColor = Color(0xFFEF5350),
    animationSpeedScale = 3f,
)

IndeterminateCenteredRoundCornerProgressBar(
    modifier = Modifier
        .width(260.dp)
        .height(10.dp),
    backgroundColor = Color(0x0A000000),
    progressColor = Color(0xFFEF5350),
    animationSpeedScale = 0.75f,
)
```

### Parameters

| Parameter | Type | Default |
| --- | --- | --- |
| `modifier` | `Modifier` | `Modifier` |
| `secondaryProgress` | `Float` | `0f` |
| `radius` | `Dp` | `30.dp` |
| `backgroundPadding` | `Dp` | `0.dp` |
| `reverse` | `Boolean` | `false` |
| `backgroundColor` | `Color` | `Color(0xFF5F5F5F)` |
| `progressColor` | `Color` | `Color(0xFF7F7F7F)` |
| `progressColors` | `List<Color>?` | `null` |
| `secondaryProgressColor` | `Color` | `Color.Transparent` |
| `secondaryProgressColors` | `List<Color>?` | `null` |
| `animationSpeedScale` | `Float` | `1f` |
| `onProgressChanged` | `((Float) -> Unit)?` | `null` |

# Gradient Progress Color

Pass two or more colors through `progressColors` (or `secondaryProgressColors`) to render a horizontal gradient. It overrides the single `progressColor` when set.

```kotlin
RoundCornerProgressBar(
    progress = 50f,
    backgroundColor = Color(0x0A000000),
    backgroundPadding = 4.dp,
    radius = 30.dp,
    progressColors = listOf(Color(0xFF009688), Color(0xFF80CBC4)),
)
```

# Progress Change Animation

Animation is disabled by default. Enable it with `animationEnabled = true`; the bar then animates whenever `progress` changes. `animationSpeedScale` accepts a value between 0.2 and 5.0 (default 1.0) — higher slows the animation down, lower speeds it up. This reproduces the `AccelerateDecelerateInterpolator` curve of the View library.

```kotlin
RoundCornerProgressBar(
    progress = progress,
    animationEnabled = true,
    animationSpeedScale = 1f,
)
```

The indeterminate bars are always animating and ignore `animationEnabled`.
