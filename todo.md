# PlexWatch - TODO

## Concluído ✅

### Fase 1: Setup do Projeto
- [x] Inicializar projeto Wear OS com Gradle Kotlin DSL
- [x] Configurar dependências (Compose, Hilt, Retrofit, Coroutines, ExoPlayer)
- [x] Criar estrutura de pastas Clean Architecture (data/domain/presentation)
- [x] Configurar ktlint para formatação de código
- [x] Criar Dockerfile para build sem Android SDK local
- [x] Criar CLAUDE.md com comandos de build
- [x] Criar README.md com instruções

### Arquitetura Base
- [x] Modelos de domínio (PlexServer, PlexLibrary, Artist, Album, Track, PlexUser, PlexPin)
- [x] Interfaces de repositórios (AuthRepository, ServerRepository, LibraryRepository, PlaybackRepository)
- [x] Use cases (CreatePinUseCase, CheckPinUseCase, GetServersUseCase, GetLibrariesUseCase, PlayTrackUseCase)
- [x] APIs Retrofit (PlexAuthApi, PlexServerApi, PlexMediaApi)
- [x] DTOs para respostas da API
- [x] Implementações de repositórios (AuthRepositoryImpl, ServerRepositoryImpl, LibraryRepositoryImpl)
- [x] TokenStorage com EncryptedSharedPreferences
- [x] Módulos Hilt (NetworkModule, RepositoryModule)

### UI Base
- [x] Theme Plex (cores, tipografia)
- [x] Navegação com SwipeDismissableNavHost
- [x] HomeScreen com estado de autenticação
- [x] HomeViewModel

### Fase 2: Autenticação Plex
- [x] Criar LoginScreen com exibição do PIN
- [x] Criar LoginViewModel com polling do PIN
- [x] Implementar fluxo completo: gerar PIN → mostrar código → polling → autenticado
- [x] Adicionar tela de "Acesse plex.tv/link e digite o código"
- [x] Tratar erros de autenticação

### Fase 3: Descoberta de Servidores e Navegação de Bibliotecas
- [x] Criar ServersScreen (lista de servidores disponíveis)
- [x] Criar ServersViewModel
- [x] Criar LibrariesScreen (lista de bibliotecas de música)
- [x] Criar ArtistsScreen (lista de artistas)
- [x] Criar AlbumsScreen (álbuns de um artista)
- [x] Criar TracksScreen (faixas de um álbum)
- [x] Implementar navegação com rotary input (coroa do relógio)
- [x] Adicionar loading states e tratamento de erros

### Fase 4: Reprodução de Áudio
- [x] Criar PlaybackRepositoryImpl com ExoPlayer
- [x] Criar PlaybackService (foreground service)
- [x] Criar NowPlayingScreen com controles (play/pause, próxima, anterior)
- [x] Mostrar artwork do álbum
- [x] Implementar barra de progresso
- [x] Suportar reprodução em background
- [x] Adicionar notificação de mídia

---

## Próximos Passos 🚀

### Fase 5: Polimento e Features do Watch
- [ ] Navegação com rotary bezel/crown
- [ ] Complication para acesso rápido
- [ ] Tile para controle de reprodução
- [ ] Otimização de bateria
- [ ] Cache de metadados offline
- [ ] Download de faixas para offline (opcional)

### Testes
- [ ] Testes unitários para use cases
- [ ] Testes unitários para ViewModels
- [ ] Testes de integração para repositórios
- [ ] Testes de UI com Compose Testing

---

## Bugs Conhecidos 🐛

(Nenhum no momento)

---

## Notas Técnicas 📝

- API Plex usa `X-Plex-Token` em todas as requisições
- Autenticação via PIN: `POST plex.tv/pins.json` → polling `GET plex.tv/pins/{id}.json`
- Tipo de mídia para artistas na API Plex: `type=8`
- Servidor Plex roda na porta 32400 por padrão
