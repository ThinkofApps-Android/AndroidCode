# Speed Math — Android App

A fast-paced mental arithmetic trainer built with Jetpack Compose + Kotlin.

## Requirements
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17+
- Android SDK 35 (compileSdk)
- minSdk 26 (Android 8.0+)

## How to Run

1. Unzip the archive
2. Open the `SpeedMathAndroid` folder in Android Studio
3. Let Gradle sync (it will download dependencies automatically)
4. Select an emulator or connected device (API 26+)
5. Press **▶ Run** (Shift+F10)

> No third-party libraries beyond Jetpack Compose + Material3.

## Project Structure

```
SpeedMathAndroid/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/values/{strings,themes}.xml
        └── java/com/speedmath/app/
            ├── MainActivity.kt         # Entry point + screen router
            ├── GameViewModel.kt        # State & logic (ViewModel + StateFlow)
            └── ui/
                ├── theme/Theme.kt      # SMColors, MaterialTheme
                ├── components/Components.kt
                └── screens/{Setup,Game,Results}Screen.kt
```

## Design
- Background: #0A0A0F · Surface: #111118 · Accent: #7C3AED
- Green: #10B981 · Red: #EF4444 · Amber: #F59E0B
- Monospace font for all numbers, system font for UI text

## Architecture
- Single Activity · AnimatedContent screen transitions
- GameViewModel owns all state via StateFlow
- Timer as a viewModelScope coroutine
