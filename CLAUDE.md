# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PlexWatch - A Plex music client for Samsung Galaxy Watch (Wear OS). Allows users to browse their Plex music library and stream audio directly on their watch.

## Build Commands

```bash
# Build
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

**TESTES OBRIGATÓRIOS:** Todo código novo ou modificado deve incluir testes unitários ou de integração conforme a necessidade:
- **Domain Layer (use cases):** Testes unitários obrigatórios com mocks dos repositórios
- **Data Layer (repositories):** Testes de integração para validar comunicação com APIs
- **Presentation Layer (ViewModels):** Testes unitários com mocks dos use cases
- Execute `./gradlew test` para rodar todos os testes antes de fazer commit

## Testing Patterns

Tests use MockK for mocking and Turbine for Flow testing. Use `TestFixtures` object (`app/src/test/kotlin/com/plexwatch/util/TestFixtures.kt`) to create domain model instances:

```kotlin
val pin = TestFixtures.createPlexPin(code = "WXYZ")
val server = TestFixtures.createPlexServer(name = "My Server")
val track = TestFixtures.createTrack(title = "Song Name")
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
│   ├── ui/                  # Compose screens
│   ├── navigation/          # Navigation routes & NavHost
│   └── theme/               # Colors, Typography
│
└── di/                      # Hilt DI modules
```

**Dynamic Base URL:** `DynamicBaseUrlInterceptor` intercepts requests to `localhost` and rewrites them to the currently selected Plex server's URL. Media API requests use `localhost` as placeholder.

## Key Dependencies

- **UI**: Jetpack Compose for Wear OS + Horologist
- **DI**: Hilt
- **Network**: Retrofit + OkHttp + Moshi
- **Media**: Media3 ExoPlayer
- **Security**: EncryptedSharedPreferences
- **Testing**: MockK, Turbine, kotlinx-coroutines-test

## Plex API

- Authentication: PIN-based flow via plex.tv (`POST plex.tv/pins.json` → poll `GET plex.tv/pins/{id}.json`)
- Server discovery: `GET https://plex.tv/api/v2/resources`
- Library browsing: `GET {server}/library/sections`
- All requests require `X-Plex-Token` header
- Artist type in Plex API: `type=8`
- **Relay-only connections:** App uses exclusively Plex relay connections (`connection.relay == true`), ensuring HTTPS access through plex.direct domains

See `todo.md` for detailed task list and known bugs.

## License

Este projeto usa licença **GPL-3.0**. Veja `LICENSE.md` para o texto completo.

**IMPORTANTE:** NÃO adicione cabeçalhos de licença nos arquivos de código fonte. A licença é definida centralmente no arquivo `LICENSE.md` e cobre implicitamente todos os arquivos do repositório. Veja `project-spec/07_LICENSE.md` para detalhes.
