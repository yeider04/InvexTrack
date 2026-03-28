-- Estructura de Categorías
CREATE TABLE IF NOT EXISTS categorias (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id_categoria)
);

-- Estructura de Proveedores
CREATE TABLE IF NOT EXISTS proveedores (
  id_proveedor INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  contacto VARCHAR(100) DEFAULT NULL,
  direccion VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id_proveedor)
);

-- Estructura de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  correo VARCHAR(150) NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  rol VARCHAR(50) NOT NULL DEFAULT 'OPERARIO',
  PRIMARY KEY (id_usuario),
  UNIQUE KEY (correo)
);

-- Estructura de Productos
CREATE TABLE IF NOT EXISTS productos (
  id_producto INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  descripcion VARCHAR(255) DEFAULT NULL,
  cantidad INT NOT NULL DEFAULT '0',
  precio_unitario DOUBLE NOT NULL DEFAULT '0',
  id_categoria INT DEFAULT NULL,
  id_proveedor INT DEFAULT NULL,
  precio DOUBLE NOT NULL,
  sku VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_producto),
  FOREIGN KEY (id_categoria) REFERENCES categorias (id_categoria) ON DELETE SET NULL,
  FOREIGN KEY (id_proveedor) REFERENCES proveedores (id_proveedor) ON DELETE SET NULL
);

-- Estructura de Movimientos
CREATE TABLE IF NOT EXISTS movimientos_inventario (
  id_movimiento INT NOT NULL AUTO_INCREMENT,
  tipo VARCHAR(50) NOT NULL,
  fecha DATE NOT NULL,
  cantidad INT NOT NULL,
  id_producto INT NOT NULL,
  id_usuario INT NOT NULL,
  PRIMARY KEY (id_movimiento),
  FOREIGN KEY (id_producto) REFERENCES productos (id_producto) ON DELETE CASCADE,
  FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) ON DELETE CASCADE
);