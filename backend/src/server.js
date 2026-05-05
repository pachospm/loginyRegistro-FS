// Crag las variables del archivo .env en process.env
require("dotenv").config();

// Importar la aplicacion Exprees configurada en app.js
const app = require("./app");

// Importar el pool de conexión a PostgreSQL
const pool = require("./config/db.js");

//Defini el puerto del servidor
const port = process.env.PORT || 3000;

// Función asincrona para iniciar el backend
async function startServer(){
    try{
        await pool.query("SELECT 1");
        console.log("Conexion a PostgreSQL correcta");
        app.listen(port, () => {
            console.log(`API escuchando en http://localhost:$(port)`);
        });
    }catch (e){
        console.error("No se pudo conectar a PostgreSQL: ", e.message);
        process.exit(1)
    }
}

// Ejecutar al inicio del servidor
startServer();