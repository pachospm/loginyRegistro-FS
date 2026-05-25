const express = require("express");

// Importar el controlador que devuelve el perfil autentiación
const {getMe} = require("../controllers/userController");

// Importar el middleware que valida el token JWT
const authMiddleware = require("../middlewares/authMiddleware");

// Crear un router para rutas relacionadas con usuario
const router = express.Router();

// Definir la ruta del perfil de autenticación
router.get("/me", authMiddleware, getMe);

module.exports = router