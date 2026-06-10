require("dotenv").config();

const app = require("./app");
const pool = require("./config/db");

const port = process.env.PORT || 3000;

async function startServer() {
  try {
    await pool.query("SELECT 1");
    console.log("Conexion a PostgreSQL correcta.");

    app.listen(port, () => {
      console.log(`API escuchando en http://localhost:${port}`);
    });
  } catch (error) {
    console.error("No se pudo conectar a PostgreSQL:", error.message);
    process.exit(1);
  }
}

startServer();
