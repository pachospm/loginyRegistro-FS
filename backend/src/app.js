// Importar cors para permitir peticiones desde otros clientes
const cors = require("cors")
// Importar Express para crear la API REST
const express = require("express")
// Importar las rutas de autenticación
//const authRoutes = require("./routes/authRoute")

//Crear la aplicación Express
const app = express();

// Habilitar CORS
// Esto permite que Andorid, Postma u otro clientes consuma la API
app.use(cors());

app.use(express.json());

// Endpoint raiz para verificar que la API esta activa
app.get("/", (req, res) => {
    //Responde el JSON 
    res.json({message: "API de autenticación funcionando"});
});

// Motar las rutas de usuario bajo /users.
// Ejemplo: Get /users/me

//app.use("/users", userRoutes)

app.use((req, res) => {
    res.status(404).json({message: "Ruta no encontrada"});
});

// Exportar app para que server.js pueda levantar el servidor
module.exports = app;