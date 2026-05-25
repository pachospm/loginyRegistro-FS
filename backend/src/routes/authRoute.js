// Importar Express
const express = require("express");

// Importar los controladores de autenticación
// register maneja el registro
// login maneja el inicio de sesión
const {login, register} = require("../controllers/authController");

// Crea un router para agrupar rutas relacionadas con auth
const router = express.Router();

//Definir la ruta de registro
router.post("/register", register);

//Definir la ruta de login
//router.post("/login", login);

// Exportar el router para usarlo en app.js
module.exports = router;