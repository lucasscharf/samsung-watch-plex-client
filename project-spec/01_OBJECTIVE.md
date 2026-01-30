# 01_OBJECTIVE.md - Objetivos do Projeto

## Objetivo Principal

Criar um aplicativo funcional para Wear OS que permita **navegar e reproduzir músicas de um servidor Plex** diretamente no smartwatch, sem necessidade do celular por perto.

## Requisitos Funcionais

### RF01 - Autenticação
- [x] Autenticar usuário via PIN flow do Plex
- [x] Exibir código PIN para usuário inserir em plex.tv/link
- [x] Polling automático até autenticação
- [x] Persistir token de forma segura
- [ ] Permitir logout/troca de conta

### RF02 - Descoberta de Servidores
- [ ] Listar todos os servidores Plex do usuário
- [ ] Mostrar status de conexão (online/offline)
- [ ] Permitir selecionar servidor padrão
- [ ] Reconectar automaticamente ao último servidor

### RF03 - Navegação de Biblioteca
- [ ] Listar bibliotecas de música disponíveis
- [ ] Navegar por Artistas → Álbuns → Faixas
- [ ] Exibir capas de álbuns (thumbnails)
- [ ] Buscar na biblioteca (opcional)

### RF04 - Reprodução de Áudio
- [ ] Reproduzir faixas em streaming
- [ ] Controles: play/pause, próxima, anterior
- [ ] Barra de progresso com seek
- [ ] Reprodução em background
- [ ] Notificação de mídia

### RF05 - UX de Smartwatch
- [ ] Navegação por rotary input (coroa/bezel)
- [ ] Suporte a gestos Wear OS (swipe dismiss)
- [ ] Tiles para controle rápido
- [ ] Complications para status

## Requisitos Não-Funcionais

### RNF01 - Performance
- Tempo de carregamento inicial < 3s
- Transições fluidas (60fps)
- Otimização de bateria

### RNF02 - Segurança
- Tokens em EncryptedSharedPreferences
- Comunicação HTTPS obrigatória
- Sem credenciais hardcoded

### RNF03 - Qualidade de Código
- Clean Architecture com separação de camadas
- Cobertura de testes > 70%
- ktlint para formatação
- Documentação de código crítico

### RNF04 - Usabilidade
- Interface adaptada para telas pequenas (~1.4")
- Fontes legíveis
- Áreas de toque adequadas (>48dp)
- Feedback visual para ações

## Fases de Desenvolvimento

### Fase 1 - Setup e Infraestrutura ✅
- [x] Estrutura Clean Architecture
- [x] Configuração de dependências
- [x] Módulos Hilt
- [x] Build com Docker
- [x] ktlint configurado

### Fase 2 - Autenticação ✅
- [x] LoginScreen com PIN flow
- [x] LoginViewModel com polling
- [x] TokenStorage seguro
- [x] HomeScreen com estado de auth
- [x] Testes unitários

### Fase 3 - Servidores e Bibliotecas 🔄 (Próxima)
- [ ] ServersScreen
- [ ] ServersViewModel
- [ ] LibrariesScreen
- [ ] ArtistsScreen / AlbumsScreen / TracksScreen
- [ ] Rotary input

### Fase 4 - Reprodução de Áudio
- [ ] PlaybackRepositoryImpl (ExoPlayer)
- [ ] PlaybackService (foreground service)
- [ ] NowPlayingScreen
- [ ] Controles e progresso
- [ ] Background playback

### Fase 5 - Polish
- [ ] Tiles e Complications
- [ ] Otimização de bateria
- [ ] Cache offline de metadados
- [ ] CI/CD com GitHub Actions

## Critérios de Sucesso

1. **MVP Funcional:** Conseguir autenticar, selecionar servidor, navegar biblioteca e reproduzir uma música
2. **Usabilidade Real:** Usar o app no dia-a-dia para ouvir música durante exercícios
3. **Código Publicável:** Qualidade suficiente para ser open source

## Não-Escopo (Out of Scope)

- Download de músicas para offline (pode ser adicionado depois)
- Suporte a vídeo
- Sincronização com outros apps Plex
- Playlists inteligentes
- Transcodificação no servidor (assume streaming direto)

## Métricas de Acompanhamento

| Métrica | Valor Atual | Meta |
|---------|-------------|------|
| Cobertura de testes | ~74% | >70% |
| Arquivos Kotlin | 50 | - |
| LOC (produção) | ~1.670 | - |
| LOC (testes) | ~1.233 | - |
| Fases completas | 2/5 | 5/5 |

## Notas Técnicas

### Plex API
- Tipo "Artist" na API: `type=8`
- Porta padrão: `32400`
- Todas requisições precisam de `X-Plex-Token` no header

### Limitações Wear OS
- RAM limitada (~512MB-1GB)
- Bateria é prioridade
- Tela pequena (~1.2"-1.4")
- Conectividade pode ser instável (WiFi watch vs LTE)

### Decisões de Arquitetura
- **Clean Architecture:** Separação clara facilita testes e manutenção
- **Hilt:** DI nativo do Google, bem integrado com Compose
- **Media3 ExoPlayer:** Padrão moderno para playback no Android
- **Moshi:** Mais eficiente que Gson para Kotlin
