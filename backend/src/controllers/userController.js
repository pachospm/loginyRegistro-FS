const pool = require("../config/db");

async function getMe(req, res) {
  try {
    const result = await pool.query(
      "SELECT id, name, email FROM users WHERE id = $1",
      [req.user.id]
    );

    if (result.rowCount === 0) {
      return res.status(404).json({ message: "Usuario no encontrado." });
    }

    return res.json(result.rows[0]);
  } catch (error) {
    console.error("Error en getMe:", error);
    return res.status(500).json({ message: "Ocurrio un error inesperado." });
  }
}

module.exports = {
  getMe
};
