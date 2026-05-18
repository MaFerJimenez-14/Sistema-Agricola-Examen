-- Script de creación de base de datos

CREATE TABLE IF NOT EXISTS parcelas (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    area DECIMAL(10,2) NOT NULL,
    tipo_suelo VARCHAR(80) NOT NULL,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS cultivos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    variedad VARCHAR(100) NOT NULL,
    fecha_siembra VARCHAR(20) NOT NULL,
    tipo_cultivo VARCHAR(30) NOT NULL,
    duracion_ciclo_dias INT,
    anios_produccion INT
);

CREATE TABLE IF NOT EXISTS responsables (
    identificacion VARCHAR(30) PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    correo VARCHAR(120) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    tipo_responsable VARCHAR(40) NOT NULL,
    nombre_finca_asociacion VARCHAR(150),
    especialidad_tecnica VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS labores_agricolas (
    codigo VARCHAR(20) PRIMARY KEY,
    codigo_parcela VARCHAR(20) NOT NULL,
    codigo_cultivo VARCHAR(20) NOT NULL,
    identificacion_responsable VARCHAR(30) NOT NULL,
    fecha_realizacion VARCHAR(20) NOT NULL,
    tipo_labor VARCHAR(50) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    costo_estimado DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (codigo_parcela) REFERENCES parcelas(codigo),
    FOREIGN KEY (codigo_cultivo) REFERENCES cultivos(codigo),
    FOREIGN KEY (identificacion_responsable) REFERENCES responsables(identificacion)
);
