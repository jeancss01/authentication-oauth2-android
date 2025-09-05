# Authentication OAuth2 Android

Exemplo de autenticação OAuth2 + PKCE em Android com Kotlin, usando componentes Jetpack.

## Tecnologias
- Kotlin e Java
- Android Jetpack (inclui Navigation com Safe Args)
- Injeção de dependência (Hilt)

## Arquitetura
MVVM:
- Apresentação: Fragments/Activities, ViewModel, navegação com Safe Args.
- Domínio: Modelos e casos de uso.
- Dados: Repository, DataSource remoto (Retrofit/OkHttp), mapeadores DTO ↔ domínio.
- DI: Módulos Hilt para Retrofit, OkHttpClient, Repository, casos de uso.
- OAuth2: AuthRepository, armazenamento seguro de tokens, Interceptor para Authorization.

## Requisitos
- Android Studio Narwhal \| 2025.1.1 Patch 1
- JDK 17 (configure o Gradle para usar JDK 17)
- Dispositivo ou emulador Android
- Acesso a um provedor OAuth2

## Configuração do ambiente

1. Clone o repositório:
    
        git clone https://github.com/jeancss01/authentication-oauth2-android.git
        cd authentication-oauth2-android

2. Abra no Android Studio e configure o Gradle JDK:
   - macOS: Preferences → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK = JDK 17

3. Defina as credenciais/URLs do OAuth2 em `local.properties` (ou `.env`, ambos ignorados pelo `.gitignore`):
   - Exemplo em `local.properties`:
     
         OAUTH2_AUTH_URL=https://seu-provedor/authorize
         OAUTH2_TOKEN_URL=https://seu-provedor/token
         OAUTH2_CLIENT_ID=seu_client_id
         OAUTH2_REDIRECT_URI=seu.app://callback

   - Nunca versione segredos. Os arquivos `local.properties` e `.env` já estão no `.gitignore`.

## Build e execução

- Linha de comando:

      ./gradlew clean assembleDebug

- Android Studio:
  - Sincronize o Gradle.
  - Selecione uma configuração `Run` e inicie no dispositivo/emulador.

## Estrutura (resumo)

- `app/` — Código-fonte do aplicativo Android
- `gradle/` — Arquivos do wrapper do Gradle
- `build.gradle[.kts]`, `settings.gradle[.kts]` — Configuração do build
- `gradle.properties` — Opções do Gradle (memória, jvmargs, etc.)
- `.gitignore` — Ignora builds, IDE e segredos

## Licença

Defina aqui a licença do projeto (ex.: MIT, Apache-2.0).
