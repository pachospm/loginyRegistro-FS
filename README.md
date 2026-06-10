# Login y Registro Full Stack

Aplicacion full stack para registro, inicio de sesion y consulta de perfil de usuario. El proyecto integra una app Android nativa construida con Jetpack Compose y una API REST en Node.js/Express conectada a PostgreSQL, usando JWT para autenticacion.

## Caracteristicas

- Registro de usuarios con validacion de datos.
- Inicio de sesion con correo y contrasena.
- Persistencia local del token con DataStore.
- Interceptor HTTP para enviar el token en peticiones autenticadas.
- Pantalla de carga para restaurar sesion.
- Pantalla principal con datos del usuario autenticado.
- Cierre de sesion.
- Backend con contrasenas cifradas mediante bcrypt y autenticacion JWT.

## Stack Tecnico

**Android**

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel
- Coroutines
- Retrofit
- OkHttp
- DataStore Preferences

**Backend**

- Node.js
- Express
- PostgreSQL
- pg
- bcrypt
- jsonwebtoken
- dotenv
- cors

## Estructura del Proyecto

```text
.
+-- app/                 # Aplicacion Android
|   +-- src/main/java/com/applogin/loginyregistro/
|       +-- authapp/     # Capas de dominio, datos y presentacion
|       +-- core/        # Navegacion, red y utilidades
|       +-- ui/theme/    # Tema visual Compose
+-- backend/             # API REST Node.js + Express
|   +-- database/        # Scripts SQL
|   +-- src/
|       +-- config/
|       +-- controllers/
|       +-- middleware/
|       +-- routes/
+-- gradle/              # Configuracion Gradle
```

## Configuracion del Backend

Entra a la carpeta del backend:

```bash
cd backend
```

Instala dependencias:

```bash
npm install
```

Crea la base de datos en PostgreSQL:

```sql
CREATE DATABASE auth_app_db;
```

Ejecuta el esquema:

```bash
psql -U postgres -d auth_app_db -f database/schema.sql
```

Copia el archivo de variables de entorno:

```bash
copy .env.example .env
```

Configura `.env` con tus credenciales locales:

```env
PORT=3000
DB_HOST=localhost
DB_PORT=5432
DB_NAME=auth_app_db
DB_USER=postgres
DB_PASSWORD=tu_password
JWT_SECRET=cambia_este_secreto_en_desarrollo
JWT_EXPIRES_IN=2h
```

Levanta la API:

```bash
npm run dev
```

La API queda disponible en:

```text
http://localhost:3000/
```

## Endpoints Principales

```text
POST /auth/register   Registra un usuario
POST /auth/login      Inicia sesion y devuelve JWT
GET  /users/me        Devuelve el perfil autenticado
```

## Configuracion de Android

Abre el proyecto en Android Studio y ejecuta el modulo `app`.

La app esta configurada para consumir la API local desde un emulador Android usando:

```text
http://10.0.2.2:3000/
```

Esta URL esta definida en `app/build.gradle.kts` como `BuildConfig.BASE_URL`.

Para compilar desde terminal:

```bash
.\gradlew.bat :app:assembleDebug
```

## Verificacion Rapida

Backend:

```bash
cd backend
node -e "require('./src/app'); console.log('backend ok')"
```

Android:

```bash
.\gradlew.bat :app:assembleDebug
```

## Estado del Proyecto

El proyecto incluye el flujo funcional completo de autenticacion:

1. Registro de usuario.
2. Inicio de sesion.
3. Guardado de token.
4. Restauracion de sesion.
5. Consulta de perfil.
6. Cierre de sesion.

## Autor

Proyecto academico desarrollado como practica full stack Android + API REST.
Mg Francisco Javier Samacá Piñeros.
