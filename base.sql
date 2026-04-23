-- Computación Web, UC3M. 2025/26

-- BASE DE DATOS TIENDA MERCHANDISING --

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS valor_campo;
DROP TABLE IF EXISTS carrito_item;
DROP TABLE IF EXISTS opcion_campo;
DROP TABLE IF EXISTS campo_personalizacion;
DROP TABLE IF EXISTS foto;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuario;

SET FOREIGN_KEY_CHECKS = 1;


-- 1. Tabla: usuario
CREATE TABLE usuario(
    id              INT             NOT NULL AUTO_INCREMENT,
    email           VARCHAR(255)    NOT NULL,
    password_hash   VARCHAR(255)    NOT NULL,
    nombre          VARCHAR(100)    NOT NULL,
    rol             ENUM('CLIENTE', 'ADMIN') NOT NULL DEFAULT 'CLIENTE',
    PRIMARY KEY (id),
    UNIQUE INDEX idx_usuario_email (email)
);

-- 2. Tabla: producto
CREATE TABLE producto(
    id              INT             NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(255)    NOT NULL,
    descripcion     TEXT,
    precio_base     DECIMAL(10,2)   NOT NULL,
    activo          BOOLEAN         NOT NULL DEFAULT FALSE,
    tipo            ENUM('FIJO', 'PERSONALIZABLE') NOT NULL,
    PRIMARY KEY(id)
);

-- 3. Tabla: foto
CREATE TABLE foto(
    id              INT             NOT NULL AUTO_INCREMENT, 
    image_type      VARCHAR(100)    NOT NULL,
    image_data      LONGBLOB        NOT NULL,
    producto_id     INT             NOT NULL,
    PRIMARY KEY(id),
    UNIQUE INDEX idx_foto_producto(producto_id),
    CONSTRAINT fk_foto_producto
        FOREIGN KEY (producto_id) REFERENCES producto(id)
        ON DELETE CASCADE
);

-- 4. Tabla: campo_personalizacion
CREATE TABLE campo_personalizacion(
    id               INT           NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(255)  NOT NULL,
    tipo_campo       ENUM('SELECCION', 'TEXTO') NOT NULL,
    obligatorio      BOOLEAN       NOT NULL DEFAULT TRUE,
    regex_validacion VARCHAR(500),  -- NULL si tipo_campo = 'SELECCION'
    producto_id      INT           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_campo_producto
        FOREIGN KEY (producto_id) REFERENCES producto(id)
        ON DELETE CASCADE
);

-- 5. Tabla: opcion_campo
CREATE TABLE opcion_campo (
    id                INT           NOT NULL AUTO_INCREMENT,
    etiqueta          VARCHAR(255)  NOT NULL,
    incremento_precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    campo_id          INT           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_opcion_campo
        FOREIGN KEY (campo_id) REFERENCES campo_personalizacion(id)
        ON DELETE CASCADE
);

-- 6. Tabla: carrito_item
CREATE TABLE carrito_item (
    id               INT           NOT NULL AUTO_INCREMENT,
    cantidad         INT           NOT NULL DEFAULT 1,
    precio_calculado DECIMAL(10,2) NOT NULL,
    usuario_id       INT           NOT NULL,
    producto_id      INT           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_item_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_item_producto
        FOREIGN KEY (producto_id) REFERENCES producto(id)
        ON DELETE RESTRICT
);

-- 7. Tabla: valor_campo
CREATE TABLE valor_campo (
    id              INT           NOT NULL AUTO_INCREMENT,
    valor_texto     VARCHAR(500)  NOT NULL,
    carrito_item_id INT           NOT NULL,
    campo_id        INT           NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_valor_item
        FOREIGN KEY (carrito_item_id) REFERENCES carrito_item(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_valor_campo
        FOREIGN KEY (campo_id) REFERENCES campo_personalizacion(id)
        ON DELETE RESTRICT
);



-- Admin y un cliente de prueba
-- IMPORTANTE: en producción real, los passwords van hasheados por Spring Security.
-- Durante desarrollo puedes usar BCrypt: '$2a$10$...' generado por la aplicación.
INSERT INTO usuario (email, password_hash, nombre, rol) VALUES
('admin@lumina.com',  '{noop}admin123',  'Administrador', 'ADMIN'),
('cliente@test.com',  '{noop}cliente123', 'Ana García',   'CLIENTE');

-- Dos productos fijos (velas de catálogo cerrado)
INSERT INTO producto (nombre, descripcion, precio_base, activo, tipo) VALUES
('Vela Relax',
 'Aroma de lavanda y manzanilla. Perfecta para el baño o antes de dormir.',
 12.90, TRUE, 'FIJO'),
('Vela Invierno',
 'Canela, naranja y clavo. El aroma del hogar en los días fríos.',
 14.50, TRUE, 'FIJO');

-- Un producto configurable (la vela personalizada — núcleo del proyecto)
INSERT INTO producto (nombre, descripcion, precio_base, activo, tipo) VALUES
('Crea tu vela',
 'Diseña tu propia vela eligiendo aroma, color, intensidad y packaging.',
 9.90, TRUE, 'PERSONALIZABLE');

-- Campos de personalización para el producto configurable (id=3)
INSERT INTO campo_personalizacion (nombre, tipo_campo, obligatorio, regex_validacion, producto_id) VALUES
('Aroma',      'SELECCION', TRUE,  NULL,             3),
('Intensidad', 'SELECCION', TRUE,  NULL,             3),
('Color',      'SELECCION', TRUE,  NULL,             3),
('Mensaje',    'TEXTO',     FALSE, '^.{0,40}$',      3);
-- El regex '^.{0,40}$' valida que el mensaje tenga máximo 40 caracteres.

-- Opciones para el campo "Aroma" (id=1)
INSERT INTO opcion_campo (etiqueta, incremento_precio, campo_id) VALUES
('Lavanda',         0.00, 1),
('Vainilla',        1.50, 1),
('Sándalo',         2.00, 1),
('Cítrico',         1.00, 1);

-- Opciones para el campo "Intensidad" (id=2)
INSERT INTO opcion_campo (etiqueta, incremento_precio, campo_id) VALUES
('Suave',   0.00, 2),
('Media',   0.50, 2),
('Intensa', 1.00, 2);

-- Opciones para el campo "Color" (id=3)
INSERT INTO opcion_campo (etiqueta, incremento_precio, campo_id) VALUES
('Blanco',    0.00, 3),
('Crema',     0.00, 3),
('Terracota', 0.50, 3),
('Negro',     0.50, 3);

