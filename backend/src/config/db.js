const { Pool } = require("pg");

// Pool de conexiones a PostgreSQL. Los datos reales vienen del archivo .env.
const pool = new Pool({
  host: process.env.DB_HOST,
  port: Number(process.env.DB_PORT || 5432),
  database: process.env.DB_NAME,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD
});

module.exports = pool;
