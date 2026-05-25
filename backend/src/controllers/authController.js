// Importar bcrypt cifrar y comparar constraseñas
const bcrypt = require("bcrypt");
// Importar jsonwebtoken para crear tokens jwt
const jwt = require("jsonwebtoken");
// Importar el pool de PostgreSQL
const pool = require("../config/db");

// Expresión regular basica para validar correos.
const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

// Validar los datos recibidos en el modulo registro
function validateRegisterInput(name, email, password){
    if(!name || !name.trim()) return "Elnombre es obligatorio";
     
    if (!email || !email.trim()) return "El corre es obligatorio";

    if(!emailRegex.test(email)) return "Ingrese un correo valido";
    
    if(!password) return "La contraseña es obligatoria"

    if(password.length < 6) return "La contraseña debe tener al menos 6 caracteres";

    // Si no hay errores, retorne null
    return null;
}

function validateLoginInput(email, password){
    if(!email || !email.trim()) return "El correo es obligatorio";

    if(!password) return "La contraseña es obligatoria";

    // Si no hay errores retornamos null
    return null;
}

function createToken(user){
    return jwt.sign(
        {
            sub: user.id,
            name: user.name,
            email: user.email
        },

        process.env.JWT_SECRET,
        // Tiempo de expiración del token
        { expiresIn: process.env.JWT_EXPIRES_IN || "5m"}
    );
}

// Controlador para POST /auth/register.

async function register(req, res){
    try{
        const { name, email, password} = req.body;
        const validationError = validateRegisterInput(name, email, password);

        if(validationError){
            return res.status(400).json({message: validationError});
        }

        // Normalizar el correo quitando espacios y pasado a minusculas
        const normalizedEmail = email.toLowerCase();

        // Consulta si ya existe un usuario con ese correo
        const existingUser = await pool.query(
            "SELECT id FROM users WHERE email = $1",
            [normalizedEmail]
        );

        if(existingUser.rowCount > 0){
            return res.status(403).json({message: "El correo ya esta registrado"});
        }

        // Cifrar contraseña
        const passwordHash = await bcrypt.hash(password, 10);

        // Insertar el usuario en PostgreSQL
        await pool.query(
            "INSERT INTO users (name, email, password_hash) VALUES ($1,$2,$3)",
            [name.trim(), normalizedEmail, passwordHash]
        );
        return res.status(200).json({message: "Usuario registrado correctamente"});
    }catch (error){
        console.error("Error en register:", error);
        return res.status(500).json({message: "Ocurrio un error inesperado."});
    }
}

// Controlador para POST /auth/login

module.exports = {register}