# sepivos

Se trata de un servidor de autenticación con las operaciones login, logout, check y acceso autenticado a parte privada.

No accede a bases de datos. Las contraseñas en SHA-256 están escritas programáticamente.

Implementa CORS mediante un filtro. El filtro no funciona en Tomcat 10, debe usarse una versión anterior.

Se usa con un cliente kolsany (https://github.com/rafaelaznar/kolsany)
