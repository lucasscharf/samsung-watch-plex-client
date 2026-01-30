# 06_FUNCTIONAL_REQUIREMENTS.md - Requisitos Funcionais

## Visão Geral

Este documento detalha os requisitos funcionais do PlexWatch, organizados por módulo e priorizados por importância.

**Legenda de Prioridade:**
- **P0** - Crítico (MVP)
- **P1** - Importante
- **P2** - Desejável
- **P3** - Futuro

**Legenda de Status:**
- [x] Implementado
- [ ] Pendente

---

## RF01 - Autenticação

### RF01.1 - Login via PIN (P0)
**Status:** [x] Implementado

O sistema deve permitir autenticação via fluxo PIN do Plex.

| ID | Requisito | Status |
|----|-----------|--------|
| RF01.1.1 | Gerar PIN único ao iniciar login | [x] |
| RF01.1.2 | Exibir código PIN de 4 caracteres na tela | [x] |
| RF01.1.3 | Exibir instruções para acessar plex.tv/link | [x] |
| RF01.1.4 | Mostrar countdown de expiração do PIN | [x] |
| RF01.1.5 | Fazer polling automático a cada 2.5s | [x] |
| RF01.1.6 | Detectar autenticação bem-sucedida | [x] |
| RF01.1.7 | Persistir token de forma segura | [x] |
| RF01.1.8 | Redirecionar para Home após sucesso | [x] |

**Fluxo:**
```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ LoginScreen │────►│ plex.tv/    │────►│ plex.tv/    │
│ Exibe PIN   │     │ pins.json   │     │ link        │
└─────────────┘     └─────────────┘     └──────┬──────┘
      │                                        │
      │ polling                                │ user insere
      ▼                                        ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ Verifica    │◄────│ authToken   │◄────│ Plex        │
│ PIN status  │     │ retornado   │     │ autentica   │
└─────────────┘     └─────────────┘     └─────────────┘
```

### RF01.2 - Persistência de Sessão (P0)
**Status:** [x] Implementado

| ID | Requisito | Status |
|----|-----------|--------|
| RF01.2.1 | Manter usuário logado entre sessões | [x] |
| RF01.2.2 | Verificar validade do token ao abrir app | [x] |
| RF01.2.3 | Redirecionar para login se token inválido | [x] |

### RF01.3 - Logout (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF01.3.1 | Opção de logout nas configurações | [ ] |
| RF01.3.2 | Limpar token armazenado | [ ] |
| RF01.3.3 | Limpar cache local | [ ] |
| RF01.3.4 | Redirecionar para login | [ ] |

---

## RF02 - Descoberta de Servidores

### RF02.1 - Listar Servidores (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF02.1.1 | Buscar servidores do usuário via API | [ ] |
| RF02.1.2 | Exibir lista de servidores disponíveis | [ ] |
| RF02.1.3 | Mostrar nome do servidor | [ ] |
| RF02.1.4 | Indicar status de conexão (online/offline) | [ ] |
| RF02.1.5 | Mostrar loading durante busca | [ ] |
| RF02.1.6 | Tratar erro de rede | [ ] |

**Dados exibidos por servidor:**
```
┌────────────────────────────┐
│ 🖥️ Servidor Casa          │
│    Online • Local          │
├────────────────────────────┤
│ 🖥️ Servidor Cloud         │
│    Online • Remoto         │
├────────────────────────────┤
│ 🖥️ Servidor Antigo        │
│    Offline                 │
└────────────────────────────┘
```

### RF02.2 - Selecionar Servidor (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF02.2.1 | Permitir toque para selecionar servidor | [ ] |
| RF02.2.2 | Verificar conectividade com servidor | [ ] |
| RF02.2.3 | Salvar servidor selecionado como padrão | [ ] |
| RF02.2.4 | Navegar para lista de bibliotecas | [ ] |

### RF02.3 - Reconexão Automática (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF02.3.1 | Conectar automaticamente ao último servidor | [ ] |
| RF02.3.2 | Fallback para lista se servidor indisponível | [ ] |
| RF02.3.3 | Notificar usuário sobre reconexão | [ ] |

---

## RF03 - Navegação de Biblioteca

### RF03.1 - Listar Bibliotecas (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF03.1.1 | Buscar bibliotecas de música do servidor | [ ] |
| RF03.1.2 | Filtrar apenas bibliotecas tipo "music" | [ ] |
| RF03.1.3 | Exibir nome da biblioteca | [ ] |
| RF03.1.4 | Mostrar contagem de itens (opcional) | [ ] |
| RF03.1.5 | Permitir seleção por toque | [ ] |

### RF03.2 - Listar Artistas (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF03.2.1 | Buscar artistas da biblioteca selecionada | [ ] |
| RF03.2.2 | Exibir lista ordenada alfabeticamente | [ ] |
| RF03.2.3 | Mostrar thumbnail do artista | [ ] |
| RF03.2.4 | Mostrar nome do artista | [ ] |
| RF03.2.5 | Suportar scroll com rotary input | [ ] |
| RF03.2.6 | Lazy loading para listas grandes | [ ] |

### RF03.3 - Listar Álbuns (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF03.3.1 | Buscar álbuns do artista selecionado | [ ] |
| RF03.3.2 | Exibir capa do álbum | [ ] |
| RF03.3.3 | Mostrar título do álbum | [ ] |
| RF03.3.4 | Mostrar ano de lançamento | [ ] |
| RF03.3.5 | Ordenar por ano (mais recente primeiro) | [ ] |

### RF03.4 - Listar Faixas (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF03.4.1 | Buscar faixas do álbum selecionado | [ ] |
| RF03.4.2 | Exibir número da faixa | [ ] |
| RF03.4.3 | Mostrar título da faixa | [ ] |
| RF03.4.4 | Mostrar duração (mm:ss) | [ ] |
| RF03.4.5 | Ordenar por número da faixa | [ ] |
| RF03.4.6 | Indicar faixa atualmente tocando | [ ] |

**Layout da lista de faixas:**
```
┌────────────────────────────┐
│ 1. Bohemian Rhapsody  5:55 │
│ 2. Another One Bites  3:35 │
│ 3. ▶ Killer Queen     2:57 │  ← tocando
│ 4. Fat Bottomed Girls 4:16 │
│ 5. Bicycle Race       3:01 │
└────────────────────────────┘
```

---

## RF04 - Reprodução de Áudio

### RF04.1 - Iniciar Reprodução (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF04.1.1 | Iniciar reprodução ao tocar em faixa | [ ] |
| RF04.1.2 | Construir URL de streaming com token | [ ] |
| RF04.1.3 | Exibir tela Now Playing | [ ] |
| RF04.1.4 | Carregar metadados da faixa | [ ] |
| RF04.1.5 | Carregar capa do álbum | [ ] |

### RF04.2 - Controles Básicos (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF04.2.1 | Botão Play/Pause | [ ] |
| RF04.2.2 | Botão Próxima faixa | [ ] |
| RF04.2.3 | Botão Faixa anterior | [ ] |
| RF04.2.4 | Barra de progresso | [ ] |
| RF04.2.5 | Seek por toque na barra | [ ] |
| RF04.2.6 | Exibir tempo atual / duração total | [ ] |

**Layout Now Playing:**
```
┌──────────────────────────┐
│      ┌──────────┐        │
│      │  Album   │        │
│      │  Cover   │        │
│      └──────────┘        │
│                          │
│    Bohemian Rhapsody     │
│         Queen            │
│                          │
│  ━━━━━━━━━━━●━━━━━━━━━  │
│   2:34          5:55     │
│                          │
│    ⏮️    ▶️    ⏭️       │
└──────────────────────────┘
```

### RF04.3 - Background Playback (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF04.3.1 | Continuar tocando com tela apagada | [ ] |
| RF04.3.2 | Continuar tocando ao sair do app | [ ] |
| RF04.3.3 | Foreground service para manter ativo | [ ] |
| RF04.3.4 | Notificação com controles | [ ] |
| RF04.3.5 | MediaSession para controles externos | [ ] |

### RF04.4 - Fila de Reprodução (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF04.4.1 | Tocar álbum inteiro a partir de faixa | [ ] |
| RF04.4.2 | Próxima faixa automática ao fim | [ ] |
| RF04.4.3 | Parar ao fim do álbum | [ ] |

---

## RF05 - Busca

### RF05.1 - Busca Simples (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF05.1.1 | Campo de busca acessível na navegação | [ ] |
| RF05.1.2 | Buscar por nome de artista | [ ] |
| RF05.1.3 | Buscar por nome de álbum | [ ] |
| RF05.1.4 | Buscar por nome de faixa | [ ] |
| RF05.1.5 | Exibir resultados agrupados por tipo | [ ] |
| RF05.1.6 | Permitir seleção de resultado | [ ] |

**Layout de resultados:**
```
┌────────────────────────────┐
│ 🔍 "queen"                 │
├────────────────────────────┤
│ Artistas                   │
│   Queen                    │
│   Queens of the Stone Age  │
├────────────────────────────┤
│ Álbuns                     │
│   A Night at the Opera     │
│   Queen II                 │
├────────────────────────────┤
│ Faixas                     │
│   Killer Queen             │
│   Queen of Hearts          │
└────────────────────────────┘
```

### RF05.2 - Input de Busca (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF05.2.1 | Teclado do sistema ou voice input | [ ] |
| RF05.2.2 | Busca ao confirmar (não em tempo real) | [ ] |
| RF05.2.3 | Histórico de buscas recentes (opcional) | [ ] |

---

## RF06 - Playlists

### RF06.1 - Visualizar Playlists (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF06.1.1 | Listar playlists do usuário | [ ] |
| RF06.1.2 | Mostrar nome da playlist | [ ] |
| RF06.1.3 | Mostrar quantidade de faixas | [ ] |
| RF06.1.4 | Mostrar thumbnail (se disponível) | [ ] |

### RF06.2 - Reproduzir Playlist (P1)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF06.2.1 | Exibir faixas da playlist | [ ] |
| RF06.2.2 | Tocar playlist a partir de faixa | [ ] |
| RF06.2.3 | Reproduzir playlist inteira | [ ] |

---

## RF07 - Navegação e UX

### RF07.1 - Rotary Input (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF07.1.1 | Scroll de listas com coroa/bezel | [ ] |
| RF07.1.2 | Ajuste de volume com rotary (Now Playing) | [ ] |
| RF07.1.3 | Feedback háptico ao girar | [ ] |

### RF07.2 - Gestos Wear OS (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF07.2.1 | Swipe right para voltar | [ ] |
| RF07.2.2 | SwipeDismissableNavHost | [x] |
| RF07.2.3 | Manter contexto ao voltar | [ ] |

### RF07.3 - Loading States (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF07.3.1 | Indicador de loading em todas as telas | [ ] |
| RF07.3.2 | Skeleton loading para listas (opcional) | [ ] |
| RF07.3.3 | Timeout com mensagem amigável | [ ] |

### RF07.4 - Tratamento de Erros (P0)
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF07.4.1 | Mensagem de erro para falha de rede | [ ] |
| RF07.4.2 | Botão "Tentar novamente" | [ ] |
| RF07.4.3 | Fallback para offline (se cache existe) | [ ] |

---

## RF08 - Tiles e Complications (P2)

### RF08.1 - Tile de Playback
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF08.1.1 | Tile com controles de playback | [ ] |
| RF08.1.2 | Exibir faixa atual | [ ] |
| RF08.1.3 | Botões play/pause, próx, anterior | [ ] |

### RF08.2 - Complication
**Status:** [ ] Pendente

| ID | Requisito | Status |
|----|-----------|--------|
| RF08.2.1 | Complication com faixa atual | [ ] |
| RF08.2.2 | Toque abre app no Now Playing | [ ] |

---

## Matriz de Rastreabilidade

### Por Fase

| Fase | Requisitos |
|------|------------|
| Fase 1 (Setup) | - |
| Fase 2 (Auth) | RF01.1, RF01.2 |
| Fase 3 (Navegação) | RF02, RF03, RF07.1, RF07.2, RF07.3, RF07.4 |
| Fase 4 (Playback) | RF04, RF06.2 |
| Fase 5 (Polish) | RF01.3, RF05, RF06.1, RF08 |

### Por Prioridade

| Prioridade | Requisitos |
|------------|------------|
| P0 (MVP) | RF01.1, RF01.2, RF02.1, RF02.2, RF03.*, RF04.1-RF04.3, RF07.* |
| P1 | RF01.3, RF02.3, RF04.4, RF05.*, RF06.* |
| P2 | RF08.* |

---

## Casos de Uso Principais

### UC01 - Ouvir música do servidor

**Ator:** Usuário
**Pré-condição:** Usuário logado, servidor online
**Fluxo principal:**
1. Usuário abre o app
2. Sistema exibe lista de servidores
3. Usuário seleciona servidor
4. Sistema exibe bibliotecas de música
5. Usuário seleciona biblioteca
6. Sistema exibe lista de artistas
7. Usuário seleciona artista
8. Sistema exibe álbuns do artista
9. Usuário seleciona álbum
10. Sistema exibe faixas do álbum
11. Usuário seleciona faixa
12. Sistema inicia reprodução

**Fluxo alternativo A:** Servidor salvo
- 2a. Sistema conecta ao último servidor automaticamente
- Continua no passo 4

**Fluxo alternativo B:** Busca
- 6a. Usuário toca em buscar
- 6b. Usuário digita termo de busca
- 6c. Sistema exibe resultados
- 6d. Usuário seleciona resultado
- Continua no passo 12 ou 8/10 conforme tipo

### UC02 - Controlar reprodução

**Ator:** Usuário
**Pré-condição:** Música tocando
**Fluxo principal:**
1. Usuário visualiza tela Now Playing
2. Usuário toca em pause
3. Sistema pausa reprodução
4. Usuário toca em play
5. Sistema retoma reprodução

**Variações:**
- Próxima faixa: toque no botão next
- Faixa anterior: toque no botão previous
- Seek: toque na barra de progresso
- Volume: gira coroa/bezel

### UC03 - Reproduzir playlist

**Ator:** Usuário
**Pré-condição:** Usuário logado, playlists existem
**Fluxo principal:**
1. Usuário navega para Playlists
2. Sistema exibe playlists do usuário
3. Usuário seleciona playlist
4. Sistema exibe faixas da playlist
5. Usuário seleciona faixa
6. Sistema inicia reprodução da playlist
