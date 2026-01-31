# 02_SCOPE.md - Escopo do Projeto

## Escopo Incluído (In Scope)

### Funcionalidades Core

| Feature | Descrição | Prioridade |
|---------|-----------|------------|
| Autenticação PIN | Login via plex.tv/link sem digitar senha | Alta |
| Descoberta de servidores | Listar e selecionar servidores do usuário | Alta |
| Navegação de biblioteca | Artistas → Álbuns → Faixas | Alta |
| Streaming de áudio | Reproduzir músicas diretamente do servidor | Alta |
| Controles de playback | Play/pause, próxima, anterior, seek | Alta |
| Background playback | Continuar tocando com tela apagada | Alta |
| Rotary input | Navegação pela coroa/bezel do relógio | Média |
| Notificação de mídia | Controles na notificação do sistema | Média |

### Plataforma

- **Target:** Wear OS 3.0+ (API 30+)
- **Dispositivos:** Samsung Galaxy Watch 4/5/6, Pixel Watch, outros Wear OS
- **Compilação:** API 34

### Tipos de Mídia

- **Incluído:** Bibliotecas de música (Artists, Albums, Tracks)
- **Formatos suportados:** MP3, FLAC, AAC, OGG (via ExoPlayer)

### Modos de Conexão

- **WiFi direto:** Relógio conectado à mesma rede do servidor
- **Via celular:** Relógio usando conexão do celular pareado
- **LTE (se disponível):** Relógio com conectividade própria

---

## Escopo Excluído (Out of Scope)

### Funcionalidades Não Planejadas

| Feature | Motivo da Exclusão |
|---------|-------------------|
| Download offline | Complexidade alta, pode ser v2.0 |
| Reprodução de vídeo | Tela muito pequena, não faz sentido |
| Playlists inteligentes | Requer lógica complexa do servidor |
| Letras de músicas | Escopo adicional desnecessário |
| Equalizer | Raramente usado em smartwatch |
| Transcodificação | Assume servidor configurado corretamente |
| Sincronização watch position | Complexidade vs benefício baixo |
| Suporte a podcasts | Foco em música apenas |
| Suporte a audiobooks | Foco em música apenas |

### Plataformas Não Suportadas

- Apple Watch (watchOS)
- Fitbit OS
- Garmin
- Wear OS 2.x (legado)

### Integrações Não Planejadas

- Plex Pass features exclusivas
- Tidal/Amazon Music via Plex
- Sincronização com Plexamp
- Scrobbling para Last.fm

---

## Limites Técnicos

### Restrições de Hardware

```
┌─────────────────────────────────────┐
│ Wear OS Typical Specs               │
├─────────────────────────────────────┤
│ RAM: 1-2 GB (compartilhada)         │
│ Storage: 16-32 GB                   │
│ Tela: 1.2" - 1.4" (~450x450px)      │
│ Bateria: 300-600 mAh                │
│ CPU: Dual/Quad-core ARM             │
└─────────────────────────────────────┘
```

### Implicações

- **Memória:** Limitar cache de imagens, lazy loading
- **Bateria:** Minimizar polling, usar WorkManager quando possível
- **Tela:** UI simplificada, elementos grandes (>48dp touch targets)
- **Rede:** Timeout adequados, retry com backoff

---

## Fases e Entregas

### Fase 1 - Infraestrutura ✅
**Entrega:** Projeto configurado e compilando

- Estrutura Clean Architecture
- Dependências configuradas
- Testes configurados

### Fase 2 - Autenticação ✅
**Entrega:** Usuário consegue fazer login

- Tela de login com PIN
- Polling de autenticação
- Persistência segura de token
- Navegação básica

### Fase 3 - Navegação 🔄
**Entrega:** Usuário consegue navegar pela biblioteca

- Lista de servidores
- Lista de bibliotecas
- Navegação Artistas → Álbuns → Faixas
- Exibição de capas (thumbnails)
- Suporte a rotary input

### Fase 4 - Playback
**Entrega:** Usuário consegue ouvir música

- Player com ExoPlayer
- Foreground service
- Tela Now Playing
- Controles completos
- Background playback

### Fase 5 - Polish
**Entrega:** App pronto para uso diário

- Tiles para controle rápido
- Complications
- Otimização de bateria
- Tratamento de erros robusto
- Testes E2E

---

## Critérios de Aceite por Fase

### Fase 3 - Navegação
- [ ] Ao abrir o app logado, exibe lista de servidores
- [ ] Ao selecionar servidor, exibe bibliotecas de música
- [ ] Ao selecionar biblioteca, exibe lista de artistas
- [ ] Ao selecionar artista, exibe álbuns
- [ ] Ao selecionar álbum, exibe faixas
- [ ] Rotary input funciona em todas as listas
- [ ] Loading states visíveis durante carregamento
- [ ] Erros de rede são tratados com mensagem amigável

### Fase 4 - Playback
- [ ] Ao selecionar faixa, inicia reprodução
- [ ] Controles play/pause funcionam
- [ ] Próxima/anterior funcionam
- [ ] Seek por barra de progresso funciona
- [ ] Música continua com tela apagada
- [ ] Música continua ao sair do app
- [ ] Notificação de mídia aparece e funciona

### Fase 5 - Polish
- [ ] Tile de playback instalável
- [ ] Complication mostra faixa atual
- [ ] App não drena bateria excessivamente
- [ ] Sem crashes em uso normal por 1 semana

---

## Dependências Externas

### APIs

| Serviço | Dependência | Risco |
|---------|-------------|-------|
| plex.tv | Autenticação PIN | Baixo - API estável |
| Servidor Plex | Streaming | Baixo - Controle próprio |

### Bibliotecas Críticas

| Biblioteca | Versão | Risco de Breaking Change |
|------------|--------|--------------------------|
| Compose Wear | 1.3+ | Médio - API em evolução |
| Media3 | 1.2+ | Baixo - Estável |
| Hilt | 2.50+ | Baixo - Estável |
| Retrofit | 2.9+ | Baixo - Muito estável |

---

## Decisões de Escopo Pendentes

| Decisão | Opções | Status |
|---------|--------|--------|
| Cache de metadados | Room vs DataStore | Pendente |
| Fila de reprodução | Simples vs completa | Pendente |
| Shuffle/Repeat | Implementar ou não | Pendente |
| Busca | Local vs server-side | Pendente |

---

## Versionamento Planejado

```
v0.1.0 - MVP (Fases 1-4 completas)
         Autenticação + Navegação + Playback básico

v0.2.0 - Polish (Fase 5)
         Tiles, Complications, otimizações

v1.0.0 - Release
         Estável para uso diário, pronto para open source

v1.x.0 - Futuro (talvez)
         Download offline, busca, melhorias UX
```
