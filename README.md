# PlexWatch - Samsung Watch Plex Client

A Plex music client for Samsung Galaxy Watch (Wear OS). Stream your Plex music library directly on your watch.

## Prerequisites

- Java 17+
- Android SDK with API 34

## Build

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

#### 1. Enable Developer Mode on the Watch

1. On watch: **Settings → About watch → Software → Software version**
2. Tap "Software version" **5 times** until you see "Developer mode enabled"

#### 2. Enable ADB Debugging

1. Go to **Settings → Developer options**
2. Enable **ADB debugging**
3. Enable **Debug over Wi-Fi** (or "Wireless debugging")
4. Wait for the IP address and port to appear (e.g., `192.168.1.100:5555`)

#### 3. Pair the Watch (Required for Wear OS 3+)

On Wear OS 3 and later, you need to pair before connecting:

1. On the watch: **Developer options → Pair new device**
2. Note the pairing code and the IP:port shown (e.g., `192.168.1.100:12345`)
3. On your computer, run:

```bash
# Pair with the watch (use the IP:port shown on "Pair new device" screen)
adb pair 192.168.1.100:12345
# Enter the pairing code when prompted
```

#### 4. Connect and Install

```bash
# Connect to watch (use the IP:port from "Debug over Wi-Fi", NOT the pairing port)
adb connect 192.168.1.100:5555

# Verify connection
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch the app
adb shell am start -n com.plexwatch.debug/com.plexwatch.presentation.MainActivity
```

> **Note:** The pairing port (step 3) and the debug port (step 4) are different. Use the correct port for each step.

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

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.md](LICENSE.md) file for details.

This means you are free to:
- Use, copy, and distribute the software
- Modify the source code
- Distribute modified versions

Under the condition that:
- Any distributed copies or modifications must also be licensed under GPL-3.0
- Source code must be made available when distributing the software
- Changes must be documented
