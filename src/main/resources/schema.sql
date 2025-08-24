DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
                         id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(255) NOT NULL,
                         apellido VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         documento_identidad VARCHAR(50),
                         telefono VARCHAR(50),
                         fecha_nacimiento DATE,
                         direccion VARCHAR(255),
                         id_rol VARCHAR(50),
                         salario_base DECIMAL(15, 2) NOT NULL
);