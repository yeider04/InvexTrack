-- Datos iniciales de Categorías
INSERT IGNORE INTO categorias (id_categoria, nombre, descripcion) VALUES 
(1,'Electrónica','Productos electrónicos y accesorios'),
(2,'Papelería','Artículos de oficina y papelería'),
(3,'Herramientas','Herramientas manuales y eléctricas');

-- Datos iniciales de Proveedores
INSERT IGNORE INTO proveedores (id_proveedor, nombre, contacto, direccion) VALUES 
(1,'TechDistrib S.A.','3001234567','Calle 10 # 5-20, Bogotá'),
(2,'OfficeMax Ltda.','3109876543','Carrera 15 # 30-10, Medellín');

-- Usuarios por defecto
INSERT IGNORE INTO usuarios (id_usuario, nombre, correo, contrasena, rol) VALUES 
(1,'Admin Principal','admin@invextrack.com','admin1234','ADMINISTRADOR'),
(2,'Operario Uno','operario@invextrack.com','oper1234','OPERARIO');