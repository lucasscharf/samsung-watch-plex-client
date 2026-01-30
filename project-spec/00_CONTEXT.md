# 00_CONTEXT.md - Contexto do Projeto

## Visão Geral

**PlexWatch** é um cliente de música Plex para Samsung Galaxy Watch (Wear OS). Permite que usuários naveguem pela biblioteca de música do Plex e façam streaming de áudio diretamente no relógio.

## Motivações

O projeto nasceu de três motivações principais:

1. **Uso Pessoal** - Resolver a necessidade real de ouvir músicas do servidor Plex pessoal no smartwatch durante atividades físicas ou momentos em que o celular não está disponível.

2. **Aprendizado** - Explorar tecnologias modernas de desenvolvimento Android:
   - Jetpack Compose para Wear OS
   - Clean Architecture com MVVM
   - Hilt para injeção de dependências
   - Coroutines e Flow para programação assíncrona
   - Media3 ExoPlayer para reprodução de áudio

3. **Open Source** - Contribuir com a comunidade disponibilizando uma solução que não existe oficialmente.

## Problema a Resolver

**Não existe um app oficial do Plex para Wear OS.** Usuários que possuem servidores Plex e smartwatches Samsung/Wear OS não têm como acessar suas bibliotecas de música diretamente do pulso.

## Público-Alvo

- **Primário:** O próprio desenvolvedor (dogfooding)
- **Secundário:** Entusiastas Plex que possuem Galaxy Watch ou outro relógio Wear OS e desejam ouvir música sem depender do celular

## Ambiente de Desenvolvimento e Teste

- **Servidor Plex local** disponível para testes
- **Docker** para builds (não requer Android SDK instalado localmente)
- **Gradle cache** otimizado para builds mais rápidos
- **ktlint** para padronização de código

## Stack Tecnológica

| Camada | Tecnologia |
|--------|-----------|
| UI | Jetpack Compose for Wear OS |
| Arquitetura | Clean Architecture + MVVM |
| DI | Hilt |
| Network | Retrofit + OkHttp + Moshi |
| Playback | Media3 ExoPlayer |
| Storage | EncryptedSharedPreferences |
| Testes | JUnit, MockK, Turbine |
| Build | Gradle Kotlin DSL, Docker |

## Estrutura do Projeto

```
app/src/main/kotlin/com/plexwatch/
├── data/                    # Camada de Dados
│   ├── api/                 # Interfaces Retrofit e DTOs
│   ├── repository/          # Implementações dos repositórios
│   └── local/               # TokenStorage (EncryptedSharedPreferences)
│
├── domain/                  # Camada de Domínio (Kotlin puro)
│   ├── model/               # Entidades (PlexServer, Track, Album, etc.)
│   ├── repository/          # Interfaces dos repositórios
│   └── usecase/             # Lógica de negócio
│
├── presentation/            # Camada de Apresentação
│   ├── ui/                  # Telas Compose
│   ├── navigation/          # Rotas e NavHost
│   └── theme/               # Cores, Tipografia
│
└── di/                      # Módulos Hilt
```

## Integração com Plex API

### Autenticação (PIN-based)
1. `POST plex.tv/pins.json` - Gera PIN
2. Usuário acessa `plex.tv/link` e insere o código
3. `GET plex.tv/pins/{id}.json` - Polling até autenticação
4. Token armazenado em EncryptedSharedPreferences

### Endpoints Principais
- Descoberta de servidores: `GET https://plex.tv/api/v2/resources`
- Bibliotecas: `GET {server}/library/sections`
- Todas as requisições requerem header `X-Plex-Token`

## Permissões Android

```xml
<!-- Rede -->
android.permission.INTERNET
android.permission.ACCESS_NETWORK_STATE
android.permission.ACCESS_WIFI_STATE

<!-- Playback em background -->
android.permission.FOREGROUND_SERVICE
android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
android.permission.WAKE_LOCK
```

## Referências

- [Plex API Documentation](https://github.com/Arcanemagus/plex-api/wiki)
- [Wear OS Compose Documentation](https://developer.android.com/training/wearables/compose)
- [Clean Architecture - Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
