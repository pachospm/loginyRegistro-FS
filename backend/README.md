# Backend Auth API

API REST para la app Android de registro e inicio de sesion.

## 1. Crear la base de datos

En PostgreSQL ejecuta:

```sql
CREATE DATABASE auth_app_db;
```

Tambien esta disponible en:

```text
database/create_database.sql
```

Luego conectate a `auth_app_db` y ejecuta el script `database/schema.sql`:

```sql
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
```

El script completo esta en `database/schema.sql`.

## 2. Configurar variables de entorno

Copia `.env.example` como `.env` y ajusta tus datos:

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

## 3. Instalar dependencias

```bash
npm install
```

## 4. Ejecutar en desarrollo

```bash
npm run dev
```

La API debe quedar disponible en:

```text
http://localhost:3000/
```

La app Android en emulador se conecta a esa misma API usando:

```text
http://10.0.2.2:3000/
```

## Endpoints

```text
POST /auth/register
POST /auth/login
GET  /users/me
```
