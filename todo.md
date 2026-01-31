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

---

## Próximos Passos 🚀

### Fase 3: Descoberta de Servidores e Navegação de Bibliotecas
- [ ] Criar ServersScreen (lista de servidores disponíveis)
- [ ] Criar ServersViewModel
- [ ] Criar LibrariesScreen (lista de bibliotecas de música)
- [ ] Criar ArtistsScreen (lista de artistas)
- [ ] Criar AlbumsScreen (álbuns de um artista)
- [ ] Criar TracksScreen (faixas de um álbum)
- [ ] Implementar navegação com rotary input (coroa do relógio)
- [ ] Adicionar loading states e tratamento de erros

### Fase 4: Reprodução de Áudio
- [ ] Criar PlaybackRepositoryImpl com ExoPlayer
- [ ] Criar PlaybackService (foreground service)
- [ ] Criar NowPlayingScreen com controles (play/pause, próxima, anterior)
- [ ] Mostrar artwork do álbum
- [ ] Implementar barra de progresso
- [ ] Suportar reprodução em background
- [ ] Adicionar notificação de mídia

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
