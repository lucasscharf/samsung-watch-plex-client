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

## Run

### Install on connected watch/emulator

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Run tests

```bash
./gradlew test
```

## Project Structure

- `app/src/main/kotlin/com/plexwatch/`
  - `data/` - API clients, repositories, local storage
  - `domain/` - Business logic, models, use cases
  - `presentation/` - UI screens, ViewModels, navigation
  - `di/` - Dependency injection modules
