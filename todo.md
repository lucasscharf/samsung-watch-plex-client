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

### Fase 5: Suporte a Conexão Relay (Exclusiva)
- [x] Adicionar campo `relay: Boolean = false` ao `ConnectionDto`
- [x] Remover campo `isLocal` do `PlexServer`
- [x] Adicionar campo `protocol: String = "https"` ao `PlexServer`
- [x] Modificar `baseUrl` para usar `"$protocol://$address:$port"`
- [x] Filtrar apenas conexões onde `connection.relay == true`
- [x] Mapear `connection.protocol` → `protocol`
- [x] Atualizar `TestFixtures.createPlexServer()` - remover `isLocal`, adicionar `protocol`
- [x] Atualizar testes existentes para refletir nova estrutura
- [x] Adicionar teste `refreshServers filters only relay connections`
- [x] Adicionar teste `refreshServers ignores non-relay connections`
- [x] Remover exibição de "Local"/"Remote" em `ServersScreen`
- [x] Simplificar `ServerChip` (remover lógica de isLocal)

### Fase 6: Licenciamento e Limpeza
- [x] Adicionar arquivo LICENSE com GPLv3
- [x] Atualizar README com informações de licença
- [x] Atualizar o project-spec com informações da licença
- [x] Remover referências ao Docker do README
- [x] Remover Dockerfile e arquivos relacionados ao Docker

### Fase 7: Correção de Bugs de Contagem
- [x] Investigar contagem de álbuns/tracks sempre zero (API do Plex não retorna childCount/leafCount)
- [x] Ocultar contagens na UI quando dados não disponíveis (em vez de mostrar "0")

### Fase 8: Melhoria de UX na Reprodução
- [x] Detectar se há música tocando ao abrir o app
- [x] Redirecionar automaticamente para NowPlayingScreen se houver reprodução ativa
- [x] Adicionar acesso rápido para pause/resume da música atual (botão "Now Playing" na HomeScreen)

### Fase 9: Cache e Persistência Local
- [x] Criar banco de dados local (Room) para cache de artistas/álbuns/músicas
- [x] Implementar sincronização inicial da biblioteca
- [x] Adicionar botão "Atualizar biblioteca" para refresh manual do cache
- [x] Usar dados do cache para navegação offline dos metadados
---

## Próximos Passos 🚀

### Fase 10
Criar um novo logo



---

## Notas Técnicas 📝

- API Plex usa `X-Plex-Token` em todas as requisições
- Autenticação via PIN: `POST plex.tv/pins.json` → polling `GET plex.tv/pins/{id}.json`
- Tipo de mídia para artistas na API Plex: `type=8`
- Servidor Plex roda na porta 32400 por padrão
- **Conexão Relay:** App usa exclusivamente conexões relay do Plex (sempre HTTPS via plex.tv)
