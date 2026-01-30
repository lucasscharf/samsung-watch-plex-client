# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PlexWatch - A Plex music client for Samsung Galaxy Watch (Wear OS). Allows users to browse their Plex music library and stream audio directly on their watch.

## Build Commands

```bash
# Build with Docker (no Android SDK required)
docker build -t android-build .
docker run --rm -v "$(pwd)":/app android-build ./gradlew build --no-daemon

# Build with local Android SDK
./gradlew build

# Run unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.plexwatch.domain.usecase.*"

# Lint check and auto-format
./gradlew ktlintCheck
./gradlew ktlintFormat

# Install on device/emulator
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.plexwatch.debug/com.plexwatch.presentation.MainActivity

# View logs
adb logcat | grep -i plexwatch
```

## Development Workflow

**IMPORTANTE:** Sempre execute `./gradlew ktlintFormat` antes de fazer build ou commit para garantir que o código esteja formatado corretamente.

## Architecture

Clean Architecture with MVVM pattern:

```
app/src/main/kotlin/com/plexwatch/
├── data/                    # Data Layer
│   ├── api/                 # Retrofit interfaces & DTOs
│   ├── repository/          # Repository implementations
│   └── local/               # TokenStorage (EncryptedSharedPreferences)
│
├── domain/                  # Domain Layer (pure Kotlin)
│   ├── model/               # Domain entities (PlexServer, Track, Album, etc.)
│   ├── repository/          # Repository interfaces
│   └── usecase/             # Business logic (one class per operation)
│
├── presentation/            # Presentation Layer
│   ├── ui/                  # Compose screens
│   ├── navigation/          # Navigation routes & NavHost
│   └── theme/               # Colors, Typography
│
└── di/                      # Hilt DI modules
```

## Key Dependencies

- **UI**: Jetpack Compose for Wear OS
- **DI**: Hilt
- **Network**: Retrofit + OkHttp + Moshi
- **Media**: Media3 ExoPlayer
- **Security**: EncryptedSharedPreferences

## Plex API

- Authentication: PIN-based flow via plex.tv (`POST plex.tv/pins.json` → poll `GET plex.tv/pins/{id}.json`)
- Server discovery: `GET https://plex.tv/api/v2/resources`
- Library browsing: `GET {server}/library/sections`
- All requests require `X-Plex-Token` header
- Artist type in Plex API: `type=8`
- Default Plex server port: `32400`

## Current Status

**Implemented:** Project setup, Clean Architecture base, domain models, repository interfaces/implementations, Plex API clients, Hilt DI, HomeScreen with auth state.

**Next:** LoginScreen (PIN flow), ServersScreen, LibrariesScreen, audio playback with ExoPlayer.

See `todo.md` for detailed task list.
