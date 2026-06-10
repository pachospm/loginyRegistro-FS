const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const pool = require("../config/db");

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function validateRegisterInput(name, email, password) {
  if (!name || !name.trim()) return "El nombre es obligatorio.";
  if (!email || !email.trim()) return "El correo es obligatorio.";
  if (!emailRegex.test(email)) return "Ingrese un correo valido.";
  if (!password) return "La contrasena es obligatoria.";
  if (password.length < 6) return "La contrasena debe tener al menos 6 caracteres.";
  return null;
}

function validateLoginInput(email, password) {
  if (!email || !email.trim()) return "El correo es obligatorio.";
  if (!emailRegex.test(email)) return "Ingrese un correo valido.";
  if (!password) return "La contrasena es obligatoria.";
  return null;
}

function createToken(user) {
  return jwt.sign(
    {
      sub: user.id,
      email: user.email
    },
    process.env.JWT_SECRET,
    { expiresIn: process.env.JWT_EXPIRES_IN || "2h" }
  );
}

async function register(req, res) {
  try {
    const { name, email, password } = req.body;
    const validationError = validateRegisterInput(name, email, password);

    if (validationError) {
      return res.status(400).json({ message: validationError });
    }

    const normalizedEmail = email.trim().toLowerCase();
    const existingUser = await pool.query(
      "SELECT id FROM users WHERE email = $1",
      [normalizedEmail]
    );

    if (existingUser.rowCount > 0) {
      return res.status(400).json({ message: "El correo ya esta registrado." });
    }

    const passwordHash = await bcrypt.hash(password, 10);

    await pool.query(
      "INSERT INTO users (name, email, password_hash) VALUES ($1, $2, $3)",
      [name.trim(), normalizedEmail, passwordHash]
    );

    return res.status(201).json({ message: "Usuario registrado correctamente" });
  } catch (error) {
    console.error("Error en register:", error);
    return res.status(500).json({ message: "Ocurrio un error inesperado." });
  }
}

async function login(req, res) {
  try {
    const { email, password } = req.body;
    const validationError = validateLoginInput(email, password);

    if (validationError) {
      return res.status(400).json({ message: validationError });
    }

    const normalizedEmail = email.trim().toLowerCase();
    const result = await pool.query(
      "SELECT id, name, email, password_hash FROM users WHERE email = $1",
      [normalizedEmail]
    );

    if (result.rowCount === 0) {
      return res.status(401).json({ message: "Credenciales incorrectas." });
    }

    const user = result.rows[0];
    const isPasswordValid = await bcrypt.compare(password, user.password_hash);

    if (!isPasswordValid) {
      return res.status(401).json({ message: "Credenciales incorrectas." });
    }

    const token = createToken(user);

    return res.json({
      message: "Login exitoso",
      token,
      user: {
        id: user.id,
        name: user.name,
        email: user.email
      }
    });
  } catch (error) {
    console.error("Error en login:", error);
    return res.status(500).json({ message: "Ocurrio un error inesperado." });
  }
}

module.exports = {
  register,
  login
};
