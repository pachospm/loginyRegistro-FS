// Importar jsonwebtoken para validar tokens JWT
const jwt = require("jsonwebtoken")

// Middleware que protege rutas privadas
function authMiddleware(req, res, next){
    // lee el header Authorization de la petición
    const authorization = req.headers.authorization;

    // Valida que el header exista y tenga un formato Bearer
    if(!authorization || !authorization.startWith("Bearer")){
        return res.status(401).json({message: "Token no proporcionado"})
    }

    // Extraer solo el token
    const token = authorization.split(" ")[1];

    try{
        // Verificar que el token sea valid usando el secreto JWT
        const payload = jwt.verify(token, process.env.JWT_SECRET);
        req.user = {
            id: payload.sub,
            email: payload.email
        };
        //Permite continuar hacia el controlador final
        return next();
    }catch (error){
        return res.status(401).json({message: "Token invalido o expirado"});
    }
}

module.exports = authMiddleware;