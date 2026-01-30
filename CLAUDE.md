# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PlexWatch - A Plex music client for Samsung Galaxy Watch (Wear OS). Allows users to browse their Plex music library and stream audio directly on their watch.

## Build Commands

```bash
# Build the project
./gradlew build

# Run unit tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.plexwatch.domain.usecase.*"

# Lint check (ktlint)
./gradlew ktlintCheck

# Auto-format code
./gradlew ktlintFormat

# Install debug APK to connected device/emulator
./gradlew installDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean
```

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
│   ├── ui/                  # Compose screens (HomeScreen, etc.)
│   ├── viewmodel/           # ViewModels
│   ├── navigation/          # Navigation routes & NavHost
│   └── theme/               # Colors, Typography
│
└── di/                      # Hilt DI modules
```

## Key Dependencies

- **UI**: Jetpack Compose for Wear OS + Horologist
- **DI**: Hilt
- **Network**: Retrofit + OkHttp + Moshi
- **Media**: Media3 ExoPlayer
- **Security**: EncryptedSharedPreferences

## Plex API

- Authentication: PIN-based flow via plex.tv
- Server discovery: `GET https://plex.tv/api/v2/resources`
- Library browsing: `GET {server}/library/sections`
- All requests require `X-Plex-Token` header

## SOLID Guidelines

- **S**: One use case per business operation
- **O**: Interfaces for all repositories
- **L**: Mock implementations for testing
- **I**: Separate API interfaces (PlexAuthApi, PlexServerApi, PlexMediaApi)
- **D**: Domain defines interfaces, data implements them
