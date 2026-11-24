CREATE TABLE cliente (
    idcliente  INTEGER PRIMARY KEY AUTOINCREMENT,
    nombres    TEXT NOT NULL,
    apellidos  TEXT NOT NULL,
    dni        TEXT NOT NULL UNIQUE,
    telefono   TEXT,
    direccion  TEXT,
    estado     TEXT NOT NULL DEFAULT 'Activo'      -- Activo / Inactivo
);
CREATE TABLE veterinario (
    idveterinario INTEGER PRIMARY KEY AUTOINCREMENT,
    nombres       TEXT NOT NULL,
    apellidos     TEXT NOT NULL,
    dni           TEXT NOT NULL UNIQUE,
    telefono      TEXT,
    especialidad  TEXT,
    estado        TEXT NOT NULL DEFAULT 'Activo'   -- Activo / Inactivo
);
CREATE TABLE mascota (
    idmascota  INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre     TEXT NOT NULL,
    especie    TEXT NOT NULL,
    raza       TEXT,
    clienteId  INTEGER NOT NULL,
    estado     TEXT NOT NULL DEFAULT 'Activo',     -- Activo / Inactivo
    FOREIGN KEY (clienteId) REFERENCES cliente(idcliente)
);
CREATE TABLE cita (
    idcita         INTEGER PRIMARY KEY AUTOINCREMENT,
    mascotaId      INTEGER NOT NULL,
    veterinarioId  INTEGER NOT NULL,
    fecha          TEXT NOT NULL,                  -- formato 2026-10-30
    hora           TEXT NOT NULL,                  -- formato 14:30
    motivo         TEXT,
    estado         TEXT NOT NULL DEFAULT 'Pendiente',  -- Pendiente / Atendida / Cancelada
    FOREIGN KEY (mascotaId)     REFERENCES mascota(idmascota),
    FOREIGN KEY (veterinarioId) REFERENCES veterinario(idveterinario)
);
CREATE TABLE consulta (
    idconsulta     INTEGER PRIMARY KEY AUTOINCREMENT,
    citaId         INTEGER NOT NULL UNIQUE,
    diagnostico    TEXT NOT NULL,
    observaciones  TEXT,
    FOREIGN KEY (citaId) REFERENCES cita(idcita)
);
CREATE TABLE tratamiento (
    idtratamiento  INTEGER PRIMARY KEY AUTOINCREMENT,
    consultaId     INTEGER NOT NULL,
    descripcion    TEXT NOT NULL,
    indicaciones   TEXT,
    duracionDias   INTEGER,
    FOREIGN KEY (consultaId) REFERENCES consulta(idconsulta)
);
CREATE TABLE usuario (
    idusuario  INTEGER PRIMARY KEY AUTOINCREMENT,
    user       TEXT NOT NULL UNIQUE,
    password   TEXT NOT NULL,
    tipoUser   TEXT NOT NULL,                      -- Administrador / Recepcionista
    nombres    TEXT NOT NULL,
    apellidos  TEXT NOT NULL,
    dni        TEXT NOT NULL UNIQUE
);
