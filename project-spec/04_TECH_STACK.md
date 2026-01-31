# 04_TECH_STACK.md - Stack Tecnológica

## Resumo

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| Linguagem | Kotlin | 2.0.0 |
| Build | Gradle (Kotlin DSL) | 8.5.0 |
| UI | Jetpack Compose for Wear OS | 1.3.1 |
| DI | Hilt | 2.51.1 |
| Network | Retrofit + OkHttp + Moshi | 2.11.0 / 4.12.0 / 1.15.1 |
| Media | Media3 ExoPlayer | 1.3.1 |
| Storage | EncryptedSharedPreferences | 1.1.0-alpha06 |
| Testes | JUnit + MockK + Turbine | 4.13.2 / 1.13.10 / 1.1.0 |

---

## Plataforma Android

### Configuração SDK

```kotlin
android {
    compileSdk = 34
    minSdk = 30        // Wear OS 3.0+
    targetSdk = 34
}
```

### Compatibilidade

| Dispositivo | Suporte |
|-------------|---------|
| Galaxy Watch 4/5/6 | Sim |
| Pixel Watch 1/2 | Sim |
| Outros Wear OS 3.0+ | Sim |
| Wear OS 2.x | Não |

### Build Variants

| Variant | Application ID | Minify | Uso |
|---------|----------------|--------|-----|
| debug | com.plexwatch.debug | Não | Desenvolvimento |
| release | com.plexwatch | Sim (R8) | Produção |

---

## Linguagem e Compilação

### Kotlin

```toml
kotlin = "2.0.0"
ksp = "2.0.0-1.0.21"
```

**Features utilizadas:**
- Coroutines + Flow
- Sealed classes/interfaces
- Data classes
- Extension functions
- KSP para code generation (Hilt, Moshi)

### Java Compatibility

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlinOptions {
    jvmTarget = "17"
}
```

---

## UI Framework

### Jetpack Compose for Wear OS

```toml
composeBom = "2024.06.00"
wearCompose = "1.3.1"
```

**Bibliotecas:**

| Biblioteca | Propósito |
|------------|-----------|
| `wear.compose.material` | Componentes Material para Wear |
| `wear.compose.foundation` | Layouts base (ScalingLazyColumn, etc.) |
| `wear.compose.navigation` | SwipeDismissableNavHost |
| `horologist-*` | Utilitários Google para Wear OS |

### Horologist

```toml
horologist = "0.6.10"
```

Biblioteca do Google com utilitários para Wear OS:

| Módulo | Uso |
|--------|-----|
| `horologist-compose-layout` | Layouts otimizados para tela redonda |
| `horologist-compose-material` | Componentes Material estendidos |
| `horologist-media-ui` | UI para players de mídia |
| `horologist-audio-ui` | Controles de volume |

### Compose Compiler

```kotlin
plugins {
    alias(libs.plugins.kotlin.compose)  // Kotlin 2.0 compose compiler
}
```

---

## Dependency Injection

### Hilt

```toml
hilt = "2.51.1"
hiltNavigation = "1.2.0"
```

**Configuração:**

```kotlin
plugins {
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}
```

**Uso:**
- `@HiltAndroidApp` na Application
- `@AndroidEntryPoint` na Activity
- `@HiltViewModel` nos ViewModels
- `@Module` + `@InstallIn` para providers
- `hiltViewModel()` no Compose

---

## Networking

### Retrofit

```toml
retrofit = "2.11.0"
```

Cliente HTTP type-safe para chamadas REST.

```kotlin
interface PlexAuthApi {
    @POST("pins.json")
    suspend fun createPin(@Body request: CreatePinRequest): PinResponse

    @GET("pins/{id}.json")
    suspend fun checkPin(@Path("id") pinId: Int): PinResponse
}
```

### OkHttp

```toml
okhttp = "4.12.0"
```

HTTP client com interceptors para logging e headers.

```kotlin
OkHttpClient.Builder()
    .addInterceptor(PlexHeaderInterceptor())
    .addInterceptor(HttpLoggingInterceptor())
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()
```

### Moshi

```toml
moshi = "1.15.1"
```

JSON parser para Kotlin com code generation.

```kotlin
@JsonClass(generateAdapter = true)
data class PinResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String,
    @Json(name = "authToken") val authToken: String?
)
```

**Vantagens sobre Gson:**
- Kotlin-first
- Null safety
- Code generation (sem reflection em runtime)
- Menor tamanho

---

## Media Playback

### Media3 ExoPlayer

```toml
media3 = "1.3.1"
```

**Módulos:**

| Módulo | Propósito |
|--------|-----------|
| `media3-exoplayer` | Player core |
| `media3-session` | MediaSession para controles externos |
| `media3-ui` | PlayerView e controles UI |

**Uso planejado:**

```kotlin
val player = ExoPlayer.Builder(context).build()

player.setMediaItem(MediaItem.fromUri(streamUrl))
player.prepare()
player.play()
```

**Formatos suportados:**
- MP3
- AAC
- FLAC
- OGG Vorbis
- WAV

---

## Storage Local

### EncryptedSharedPreferences

```toml
securityCrypto = "1.1.0-alpha06"
```

Armazenamento seguro para tokens de autenticação.

```kotlin
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val prefs = EncryptedSharedPreferences.create(
    context,
    "plex_secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

**Segurança:**
- Chaves: AES256-SIV
- Valores: AES256-GCM
- Master key no Android Keystore

### Room (Planejado)

Para cache de metadados (artistas, álbuns, faixas).

```kotlin
// Será adicionado quando necessário
dependencies {
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
}
```

### Image Loading (A definir)

Opções consideradas para carregar capas de álbum:

| Biblioteca | Tamanho | Compose Support | Recomendação |
|------------|---------|-----------------|--------------|
| Coil | ~1.5 MB | Nativo | Preferido |
| Glide | ~3 MB | Via extension | Alternativa |

---

## Testes

### Unit Testing

```toml
junit = "4.13.2"
mockk = "1.13.10"
coroutinesTest = "1.8.0"
turbine = "1.1.0"
```

| Biblioteca | Uso |
|------------|-----|
| JUnit 4 | Test runner e assertions |
| MockK | Mocking para Kotlin |
| Coroutines Test | `runTest`, `TestDispatcher` |
| Turbine | Testing de Flows |

**Exemplo:**

```kotlin
@Test
fun `test flow emission`() = runTest {
    viewModel.uiState.test {
        assertEquals(UiState.Loading, awaitItem())
        assertEquals(UiState.Success(data), awaitItem())
    }
}
```

### Instrumented Testing

```toml
junitExt = "1.1.5"
espresso = "3.5.1"
```

Para testes no device/emulador (UI tests).

---

## Code Quality

### ktlint

```toml
ktlint = "12.1.1"
```

Linter e formatter para Kotlin.

```kotlin
ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.SARIF)
    }
}
```

**Comandos:**
```bash
./gradlew ktlintCheck    # Verificar
./gradlew ktlintFormat   # Auto-corrigir
```

### ProGuard/R8

Habilitado em release para:
- Minificação de código
- Shrinking de recursos
- Ofuscação

```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

---

## Build System

### Gradle

```
gradle-wrapper.properties → Gradle 8.x
build.gradle.kts → Kotlin DSL
```

### Version Catalog

Todas as dependências centralizadas em `gradle/libs.versions.toml`:

```toml
[versions]
kotlin = "2.0.0"
hilt = "2.51.1"

[libraries]
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }

[plugins]
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

---

## Dependências Completas

### Production

```kotlin
// AndroidX Core
implementation(libs.androidx.core.ktx)              // 1.13.1
implementation(libs.androidx.activity.compose)       // 1.9.0
implementation(libs.androidx.lifecycle.runtime.ktx)  // 2.8.2
implementation(libs.androidx.lifecycle.viewmodel.compose)

// Compose BOM
implementation(platform(libs.androidx.compose.bom))  // 2024.06.00
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.ui.tooling.preview)

// Wear OS
implementation(libs.wear.compose.material)           // 1.3.1
implementation(libs.wear.compose.foundation)
implementation(libs.wear.compose.navigation)
implementation(libs.horologist.compose.layout)       // 0.6.10
implementation(libs.horologist.compose.material)
implementation(libs.horologist.media.ui)
implementation(libs.horologist.audio.ui)
implementation(libs.play.services.wearable)          // 18.2.0

// Hilt
implementation(libs.hilt.android)                    // 2.51.1
implementation(libs.hilt.navigation.compose)         // 1.2.0
ksp(libs.hilt.compiler)

// Network
implementation(libs.retrofit)                        // 2.11.0
implementation(libs.retrofit.moshi)
implementation(libs.okhttp)                          // 4.12.0
implementation(libs.okhttp.logging)
implementation(libs.moshi)                           // 1.15.1
ksp(libs.moshi.kotlin.codegen)

// Media
implementation(libs.media3.exoplayer)                // 1.3.1
implementation(libs.media3.session)
implementation(libs.media3.ui)

// Security
implementation(libs.security.crypto)                 // 1.1.0-alpha06
```

### Testing

```kotlin
testImplementation(libs.junit)                       // 4.13.2
testImplementation(libs.mockk)                       // 1.13.10
testImplementation(libs.mockk.agent)
testImplementation(libs.kotlinx.coroutines.test)     // 1.8.0
testImplementation(libs.turbine)                     // 1.1.0
androidTestImplementation(libs.androidx.junit)       // 1.1.5
androidTestImplementation(libs.androidx.espresso)    // 3.5.1
```

---

## Plugins Gradle

```kotlin
plugins {
    alias(libs.plugins.android.application)  // 8.5.0
    alias(libs.plugins.kotlin.android)       // 2.0.0
    alias(libs.plugins.kotlin.compose)       // 2.0.0
    alias(libs.plugins.hilt)                 // 2.51.1
    alias(libs.plugins.ksp)                  // 2.0.0-1.0.21
    alias(libs.plugins.ktlint)               // 12.1.1
}
```

---

## Notas de Atualização

### Dependências Críticas

| Dependência | Política | Risco |
|-------------|----------|-------|
| Kotlin | Atualizar com cuidado | Médio |
| Compose BOM | Seguir releases | Baixo |
| Hilt | Manter estável | Baixo |
| Media3 | Atualizar para fixes | Baixo |

### Verificar Atualizações

```bash
./gradlew dependencyUpdates
```

Ou usar [Renovate](https://renovatebot.com/) / [Dependabot](https://dependabot.com/) para PRs automáticos.
