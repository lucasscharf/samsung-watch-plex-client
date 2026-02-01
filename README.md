# PlexWatch - Samsung Watch Plex Client

A Plex music client for Samsung Galaxy Watch (Wear OS). Stream your Plex music library directly on your watch.

## Prerequisites

- Java 17+
- One of:
  - Docker (recommended)
  - Android SDK with API 34

## Build

### Using Docker (Recommended)

```bash
# Build the Docker image (first time only)
docker build -t android-build .

# Build the project
docker run --rm -v "$(pwd)":/app android-build ./gradlew build --no-daemon

# Build debug APK only
docker run --rm -v "$(pwd)":/app android-build ./gradlew assembleDebug --no-daemon
```

### Using Local Android SDK

```bash
# Set ANDROID_HOME
export ANDROID_HOME=/path/to/Android/Sdk

# Build
./gradlew build

# Build debug APK
./gradlew assembleDebug
```

## Testing on Device

### Option 1: Wear OS Emulator

1. Install [Android Studio](https://developer.android.com/studio)
2. Open AVD Manager: **Tools → Device Manager → Create Device**
3. Select **Wear OS → Wear OS Small Round → API 34**
4. Start the emulator
5. Install and run:

```bash
export ANDROID_HOME=/home/aleatorio/Android/Sdk && export ANDROID_SDK_ROOT=/home/aleatorio/Android/Sdk 

/home/aleatorio/Android/Sdk/emulator/emulator -avd WearOS 

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch the app
adb shell am start -n com.plexwatch.debug/com.plexwatch.presentation.MainActivity
```

### Option 2: Physical Samsung Galaxy Watch

1. On watch: **Settings → About watch → Software version** → tap 5 times to enable Developer Mode
2. **Settings → Developer options → ADB debugging → Enable**
3. **Developer options → Debug over WiFi** → note the IP and port
4. Connect and install:

```bash
# Connect to watch (replace with your watch IP)
adb connect 192.168.1.100:5555

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Debugging

```bash
# List connected devices
adb devices

# View app logs
adb logcat | grep -i plexwatch
```

## Run Unit Tests

```bash
./gradlew test
```

## Project Structure

- `app/src/main/kotlin/com/plexwatch/`
  - `data/` - API clients, repositories, local storage
  - `domain/` - Business logic, models, use cases
  - `presentation/` - UI screens, ViewModels, navigation
  - `di/` - Dependency injection modules
