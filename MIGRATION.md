# Migration

## Migrate from 2.0 to 2.1+

### BaseRoundCornerProgressBar.OnProgressChangedListener

Change the view ID parameter in `onProgressChanged` to View class

```kotlin
// Old
fun onProgressChanged(
    viewId: Int,
    progress: Float,
    isPrimaryProgress: Boolean,
    isSecondaryProgress: Boolean
)

// New
fun onProgressChanged(
    view: View,
    progress: Float,
    isPrimaryProgress: Boolean,
    isSecondaryProgress: Boolean
)
```

### Custom your own progress bar by extends BaseRoundCornerProgressBar

Use AnimatedRoundCornerProgressBar instead of BaseRoundCornerProgressBar for progress change animation support.

```Kotlin
class CustomRoundCornerProgressBar: AnimatedRoundCornerProgressBar() {
    /* ... */
}
```

And you do not have to create the `GradientDrawable` by yourself anymore. `drawProgress` will send it as parameter.

```kotlin
// Old
fun drawProgress(
    layoutProgress: LinearLayout,
    max: Float,
    progress: Float,
    totalWidth: Float,
    radius: Int,
    padding: Int,
    progressColor: Int,
    isReverse: Boolean
)

// New
fun drawProgress(
    layoutProgress: LinearLayout,
    progressDrawable: GradientDrawable,
    max: Float,
    progress: Float,
    totalWidth: Float,
    radius: Int,
    padding: Int,
    isReverse: Boolean
)
```