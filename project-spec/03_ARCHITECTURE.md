# 03_ARCHITECTURE.md - Arquitetura do Sistema

## Visão Geral

O projeto segue **Clean Architecture** combinada com **MVVM** na camada de apresentação, garantindo separação de responsabilidades, testabilidade e manutenibilidade.

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Screens   │◄─│  ViewModels │◄─│  UI State (Flow)    │  │
│  │  (Compose)  │  │   (Hilt)    │  │                     │  │
│  └─────────────┘  └──────┬──────┘  └─────────────────────┘  │
└──────────────────────────┼──────────────────────────────────┘
                           │ injeta
┌──────────────────────────▼──────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │  Use Cases  │─►│   Models    │  │ Repository Interfaces│  │
│  │             │  │ (Entities)  │  │                     │  │
│  └──────┬──────┘  └─────────────┘  └──────────▲──────────┘  │
└─────────┼─────────────────────────────────────┼─────────────┘
          │ usa                                 │ implementa
┌─────────▼─────────────────────────────────────┼─────────────┐
│                       DATA LAYER                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ Repository  │─►│    DTOs     │  │   API Clients       │  │
│  │   Impls     │  │             │  │   (Retrofit)        │  │
│  └──────┬──────┘  └─────────────┘  └─────────────────────┘  │
│         │                                                    │
│  ┌──────▼──────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ Local Cache │  │TokenStorage │  │   Room Database     │  │
│  │ (futuro)    │  │ (Encrypted) │  │   (planejado)       │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## Estrutura de Diretórios

```
app/src/main/kotlin/com/plexwatch/
│
├── PlexWatchApp.kt              # Application class (@HiltAndroidApp)
│
├── di/                          # Dependency Injection
│   ├── NetworkModule.kt         # Retrofit, OkHttp, Moshi
│   └── RepositoryModule.kt      # Binds interfaces → implementations
│
├── domain/                      # DOMAIN LAYER (Kotlin puro)
│   ├── model/                   # Entidades de domínio
│   │   ├── PlexPin.kt
│   │   ├── PlexUser.kt
│   │   ├── PlexServer.kt
│   │   ├── PlexLibrary.kt
│   │   ├── MusicItem.kt         # Artist, Album, Track
│   │   └── AuthState.kt
│   │
│   ├── repository/              # Interfaces (contratos)
│   │   ├── AuthRepository.kt
│   │   ├── ServerRepository.kt
│   │   ├── LibraryRepository.kt
│   │   └── PlaybackRepository.kt
│   │
│   ├── usecase/                 # Casos de uso (1 classe = 1 operação)
│   │   ├── CreatePinUseCase.kt
│   │   ├── CheckPinUseCase.kt
│   │   ├── GetServersUseCase.kt
│   │   ├── GetLibrariesUseCase.kt
│   │   └── PlayTrackUseCase.kt
│   │
│   └── common/                  # Utilitários do domínio
│       └── Result.kt            # Result<T> wrapper
│
├── data/                        # DATA LAYER
│   ├── api/                     # Network
│   │   ├── PlexAuthApi.kt       # plex.tv auth endpoints
│   │   ├── PlexServerApi.kt     # Server discovery
│   │   ├── PlexMediaApi.kt      # Library/media endpoints
│   │   └── dto/
│   │       ├── AuthDto.kt
│   │       ├── ServerDto.kt
│   │       └── MediaDto.kt
│   │
│   ├── repository/              # Implementações
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── ServerRepositoryImpl.kt
│   │   ├── LibraryRepositoryImpl.kt
│   │   └── PlaybackRepositoryImpl.kt
│   │
│   ├── local/                   # Storage local
│   │   ├── TokenStorage.kt      # EncryptedSharedPreferences
│   │   └── TokenStorageInterface.kt
│   │
│   └── cache/                   # Cache (futuro)
│       ├── PlexDatabase.kt      # Room database
│       ├── dao/
│       │   ├── ArtistDao.kt
│       │   ├── AlbumDao.kt
│       │   └── TrackDao.kt
│       └── entity/
│           ├── ArtistEntity.kt
│           ├── AlbumEntity.kt
│           └── TrackEntity.kt
│
└── presentation/                # PRESENTATION LAYER
    ├── MainActivity.kt          # Entry point (@AndroidEntryPoint)
    │
    ├── navigation/
    │   ├── Screen.kt            # Route definitions
    │   └── PlexWatchNavHost.kt  # NavHost setup
    │
    ├── theme/
    │   ├── Color.kt
    │   ├── Typography.kt
    │   └── Theme.kt
    │
    ├── common/                  # Componentes compartilhados
    │   ├── LoadingIndicator.kt
    │   └── ErrorMessage.kt
    │
    └── ui/
        ├── home/
        │   ├── HomeScreen.kt
        │   └── HomeViewModel.kt
        │
        ├── login/
        │   ├── LoginScreen.kt
        │   ├── LoginViewModel.kt
        │   └── LoginUiState.kt
        │
        ├── servers/
        │   ├── ServersScreen.kt
        │   └── ServersViewModel.kt
        │
        ├── library/
        │   ├── LibrariesScreen.kt
        │   ├── ArtistsScreen.kt
        │   ├── AlbumsScreen.kt
        │   └── TracksScreen.kt
        │
        └── player/
            ├── NowPlayingScreen.kt
            ├── PlayerViewModel.kt
            └── PlaybackService.kt
```

---

## Camadas em Detalhe

### Domain Layer

**Responsabilidade:** Regras de negócio puras, sem dependências Android.

#### Models (Entidades)

```kotlin
// Entidades são data classes imutáveis
data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val artworkUrl: String?,
    val streamUrl: String
)
```

#### Use Cases

```kotlin
// Um use case = uma operação de negócio
class GetArtistsUseCase @Inject constructor(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(
        libraryId: String
    ): Result<List<Artist>> {
        return libraryRepository.getArtists(libraryId)
    }
}
```

#### Result Wrapper

```kotlin
// Tratamento de erros tipado
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data

    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }
}

// Extension para facilitar uso
suspend fun <T> safeApiCall(
    call: suspend () -> T
): Result<T> = try {
    Result.Success(call())
} catch (e: Exception) {
    Result.Error(e.message ?: "Unknown error", e)
}
```

---

### Data Layer

**Responsabilidade:** Implementação de acesso a dados (API, cache, storage).

#### Repository Pattern

```kotlin
// Interface no Domain
interface LibraryRepository {
    suspend fun getArtists(libraryId: String): Result<List<Artist>>
    suspend fun getAlbums(artistId: String): Result<List<Album>>
    suspend fun getTracks(albumId: String): Result<List<Track>>
}

// Implementação no Data
class LibraryRepositoryImpl @Inject constructor(
    private val api: PlexMediaApi,
    private val artistDao: ArtistDao,  // Cache local
    private val tokenStorage: TokenStorageInterface
) : LibraryRepository {

    override suspend fun getArtists(libraryId: String): Result<List<Artist>> {
        return safeApiCall {
            // Tenta cache primeiro
            val cached = artistDao.getByLibrary(libraryId)
            if (cached.isNotEmpty()) {
                return@safeApiCall cached.map { it.toDomain() }
            }

            // Busca da API
            val token = tokenStorage.getToken() ?: throw AuthException()
            val response = api.getArtists(libraryId, token)

            // Salva no cache
            artistDao.insertAll(response.map { it.toEntity() })

            response.map { it.toDomain() }
        }
    }
}
```

#### DTOs e Mapeamento

```kotlin
// DTO (Data Transfer Object) - representa JSON da API
@JsonClass(generateAdapter = true)
data class ArtistDto(
    @Json(name = "ratingKey") val id: String,
    @Json(name = "title") val name: String,
    @Json(name = "thumb") val thumbnailPath: String?
)

// Extension para converter DTO → Domain
fun ArtistDto.toDomain(baseUrl: String): Artist = Artist(
    id = id,
    name = name,
    thumbnailUrl = thumbnailPath?.let { "$baseUrl$it" }
)
```

#### Room Database (Planejado)

```kotlin
@Database(
    entities = [ArtistEntity::class, AlbumEntity::class, TrackEntity::class],
    version = 1
)
abstract class PlexDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao
    abstract fun albumDao(): AlbumDao
    abstract fun trackDao(): TrackDao
}

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val libraryId: String,
    val name: String,
    val thumbnailUrl: String?,
    val cachedAt: Long = System.currentTimeMillis()
)

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists WHERE libraryId = :libraryId")
    suspend fun getByLibrary(libraryId: String): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<ArtistEntity>)

    @Query("DELETE FROM artists WHERE cachedAt < :threshold")
    suspend fun deleteOlderThan(threshold: Long)
}
```

---

### Presentation Layer

**Responsabilidade:** UI e lógica de apresentação.

#### ViewModel Pattern

```kotlin
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val libraryId: String = savedStateHandle["libraryId"]!!

    private val _uiState = MutableStateFlow<ArtistsUiState>(ArtistsUiState.Loading)
    val uiState: StateFlow<ArtistsUiState> = _uiState.asStateFlow()

    init {
        loadArtists()
    }

    private fun loadArtists() {
        viewModelScope.launch {
            _uiState.value = ArtistsUiState.Loading

            when (val result = getArtistsUseCase(libraryId)) {
                is Result.Success -> {
                    _uiState.value = ArtistsUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = ArtistsUiState.Error(result.message)
                }
            }
        }
    }

    fun retry() = loadArtists()
}

// UI State como sealed interface
sealed interface ArtistsUiState {
    object Loading : ArtistsUiState
    data class Success(val artists: List<Artist>) : ArtistsUiState
    data class Error(val message: String) : ArtistsUiState
}
```

#### Compose Screen

```kotlin
@Composable
fun ArtistsScreen(
    onArtistClick: (String) -> Unit,
    viewModel: ArtistsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ArtistsUiState.Loading -> LoadingIndicator()
        is ArtistsUiState.Error -> ErrorMessage(
            message = state.message,
            onRetry = viewModel::retry
        )
        is ArtistsUiState.Success -> ArtistsList(
            artists = state.artists,
            onArtistClick = onArtistClick
        )
    }
}
```

---

## Dependency Injection

### Módulos Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(PlexHeaderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @Named("plex")
    fun providePlexRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://plex.tv/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLibraryRepository(
        impl: LibraryRepositoryImpl
    ): LibraryRepository
}
```

### Grafo de Dependências

```
┌─────────────────────────────────────────────────────────────┐
│                     SingletonComponent                       │
│                                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌─────────────┐  │
│  │  Moshi   │  │ OkHttp   │  │ Retrofit │  │ PlexDatabase│  │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └──────┬──────┘  │
│       │             │             │               │          │
│       └─────────────┼─────────────┘               │          │
│                     ▼                             │          │
│  ┌─────────────────────────────────┐              │          │
│  │         API Clients             │              │          │
│  │  PlexAuthApi | PlexMediaApi     │              │          │
│  └───────────────┬─────────────────┘              │          │
│                  │                                │          │
│                  ▼                                ▼          │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                    Repositories                          ││
│  │  AuthRepositoryImpl | LibraryRepositoryImpl | ...        ││
│  └───────────────────────────┬─────────────────────────────┘│
└──────────────────────────────┼──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│                     ViewModelComponent                       │
│                                                              │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                      Use Cases                           ││
│  │  GetArtistsUseCase | PlayTrackUseCase | ...              ││
│  └───────────────────────────┬─────────────────────────────┘│
│                              │                               │
│  ┌───────────────────────────▼─────────────────────────────┐│
│  │                     ViewModels                           ││
│  │  HomeViewModel | LoginViewModel | PlayerViewModel        ││
│  └─────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
```

---

## Fluxo de Dados

### Unidirectional Data Flow (UDF)

```
┌─────────┐     ┌───────────┐     ┌──────────┐     ┌────────┐
│  User   │────►│  Screen   │────►│ ViewModel│────►│UseCase │
│ Action  │     │ (Compose) │     │          │     │        │
└─────────┘     └───────────┘     └──────────┘     └───┬────┘
                     ▲                                  │
                     │                                  ▼
                     │            ┌──────────┐     ┌────────┐
                     │            │ UI State │◄────│  Repo  │
                     │            │  (Flow)  │     │        │
                     │            └────┬─────┘     └────────┘
                     │                 │
                     └─────────────────┘
```

### Exemplo: Carregar Artistas

```
1. User abre LibraryScreen
2. Screen observa uiState do ViewModel
3. ViewModel.init() chama GetArtistsUseCase
4. UseCase chama LibraryRepository.getArtists()
5. Repository:
   a. Verifica cache (Room)
   b. Se vazio, busca da API
   c. Salva no cache
   d. Retorna Result<List<Artist>>
6. UseCase retorna Result para ViewModel
7. ViewModel atualiza _uiState
8. Screen recompõe com novos dados
```

---

## Tratamento de Erros

### Estratégia

```kotlin
// 1. Repository retorna Result<T>
override suspend fun getArtists(id: String): Result<List<Artist>> {
    return safeApiCall {
        api.getArtists(id, getToken())
            .map { it.toDomain() }
    }
}

// 2. ViewModel trata Result
when (val result = getArtistsUseCase(libraryId)) {
    is Result.Success -> _uiState.value = UiState.Success(result.data)
    is Result.Error -> _uiState.value = UiState.Error(result.message)
}

// 3. Screen exibe erro
when (state) {
    is UiState.Error -> ErrorMessage(
        message = state.message,
        onRetry = { viewModel.retry() }
    )
}
```

### Tipos de Erro

| Erro | Tratamento |
|------|------------|
| Sem internet | Mostrar mensagem + botão retry |
| Token expirado | Redirecionar para login |
| Servidor offline | Mostrar servidores alternativos |
| Timeout | Retry automático (1x) + mensagem |
| 404 Not Found | Mensagem específica |
| Erro desconhecido | Mensagem genérica + log |

---

## Playback Architecture

### Media3 + Foreground Service

```
┌─────────────────────────────────────────────────────────────┐
│                        App Process                           │
│                                                              │
│  ┌─────────────────┐       ┌────────────────────────────┐   │
│  │ NowPlayingScreen│◄─────►│     PlayerViewModel        │   │
│  │   (Compose)     │       │  - playbackState: Flow     │   │
│  └─────────────────┘       │  - currentTrack: Flow      │   │
│                            └─────────────┬──────────────┘   │
│                                          │                   │
│                                          ▼                   │
│                            ┌────────────────────────────┐   │
│                            │    PlaybackRepository      │   │
│                            │  - play(track)             │   │
│                            │  - pause()                 │   │
│                            │  - seekTo(position)        │   │
│                            └─────────────┬──────────────┘   │
│                                          │                   │
└──────────────────────────────────────────┼───────────────────┘
                                           │ binds
┌──────────────────────────────────────────▼───────────────────┐
│                     PlaybackService                           │
│                  (Foreground Service)                         │
│                                                               │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐  │
│  │   ExoPlayer     │  │ MediaSession    │  │ Notification │  │
│  │                 │  │                 │  │   Manager    │  │
│  └─────────────────┘  └─────────────────┘  └──────────────┘  │
└───────────────────────────────────────────────────────────────┘
```

---

## Testes

### Estratégia por Camada

| Camada | Tipo de Teste | Ferramentas |
|--------|---------------|-------------|
| Domain (UseCases) | Unit | JUnit, MockK |
| Data (Repositories) | Integration | JUnit, MockK, MockWebServer |
| Presentation (ViewModels) | Unit | JUnit, MockK, Turbine |
| UI (Screens) | Instrumented | Compose Testing |

### Exemplo de Teste

```kotlin
@Test
fun `getArtists returns success when api succeeds`() = runTest {
    // Given
    val artists = listOf(TestFixtures.artist())
    coEvery { api.getArtists(any(), any()) } returns artists.map { it.toDto() }

    // When
    val result = repository.getArtists("library-1")

    // Then
    assertThat(result).isInstanceOf(Result.Success::class.java)
    assertThat((result as Result.Success).data).isEqualTo(artists)
}
```

---

## Decisões de Arquitetura

| Decisão | Escolha | Justificativa |
|---------|---------|---------------|
| Arquitetura | Clean + MVVM | Separação clara, testável, escalável |
| DI | Hilt | Padrão Google, integrado com Compose |
| Network | Retrofit + Moshi | Maduro, type-safe, bom para Kotlin |
| Cache | Room | Type-safe SQL, padrão Android |
| Async | Coroutines + Flow | Nativo Kotlin, boa integração Compose |
| Errors | Result wrapper | Explícito, forçar tratamento |
| State | StateFlow | Lifecycle-aware, Compose friendly |
| Playback | Media3 ExoPlayer | Moderno, suporte oficial Google |
