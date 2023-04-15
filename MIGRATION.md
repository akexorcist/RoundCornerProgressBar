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
