-- ====================================
-- ROLES Y USUARIOS (igual que archivo original)
-- ====================================

CREATE TABLE IF NOT EXISTS rol (
  id     SERIAL  PRIMARY KEY,
  nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS permiso (
  id          SERIAL  PRIMARY KEY,
  nombre      VARCHAR(100) UNIQUE NOT NULL,
  descripcion TEXT
);

CREATE TABLE IF NOT EXISTS rol_permiso (
  rol_id     INTEGER NOT NULL REFERENCES rol(id)  ON DELETE CASCADE,
  permiso_id INTEGER NOT NULL REFERENCES permiso(id)  ON DELETE CASCADE,
  PRIMARY KEY (rol_id, permiso_id)
);

CREATE TABLE IF NOT EXISTS usuario (
  id       SERIAL  PRIMARY KEY,
  nombre   VARCHAR(100) NOT NULL,
  apellido VARCHAR(100),
  email    VARCHAR(200) UNIQUE NOT NULL,
  identificacion VARCHAR(100),
  contraseña VARCHAR(255) NOT NULL,
  rol_id   INTEGER NOT NULL REFERENCES rol(id),
  activo   BOOLEAN NOT NULL DEFAULT TRUE,
  nuevo_usuario   BOOLEAN NOT NULL DEFAULT TRUE
);

-- ====================================
-- PROCESOS Y EXPEDIENTES
-- ====================================

CREATE TABLE IF NOT EXISTS proceso (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255)   NOT NULL,
  descripcion     TEXT,
  fecha_creacion  TIMESTAMP      NOT NULL,
  responsable_id  INTEGER        NOT NULL REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS expediente (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255)   NOT NULL,
  estado          VARCHAR(255)   NOT NULL,
  descripcion     TEXT,
  fecha_creacion  TIMESTAMP      NOT NULL,
  fecha_cierre    TIMESTAMP,
  proceso_id      INTEGER NOT NULL REFERENCES proceso(id),
  root_node_id    UUID UNIQUE    -- Carpeta raíz asociada
);

-- ====================================
-- EVENTOS Y PLANTILLAS (igual que original)
-- ====================================

CREATE TABLE IF NOT EXISTS tipo_evento (
  id     SERIAL  PRIMARY KEY,
  nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS evento (
  id              SERIAL  PRIMARY KEY,
  titulo          VARCHAR(255)   NOT NULL,
  tipo_evento_id  INTEGER NOT NULL REFERENCES tipo_evento(id),
  fecha_inicio    TIMESTAMP      NOT NULL,
  fecha_fin       TIMESTAMP,
  all_day         boolean,
  proceso_id      INTEGER NOT NULL REFERENCES proceso(id),
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS plantilla (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255)   NOT NULL,
  categoria       VARCHAR(255)   NOT NULL,
  descripcion     TEXT,
  url             VARCHAR(255)   NOT NULL,
  fecha_creacion  TIMESTAMP      NOT NULL,
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id)
);

-- ====================================
-- NUEVO: SISTEMA DE ARCHIVOS (tipo Drive)
-- ====================================

-- Nodos: carpetas y archivos
CREATE TABLE node (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  expediente_id  INTEGER NOT NULL REFERENCES expediente(id) ON DELETE CASCADE,
  parent_id      UUID REFERENCES node(id) ON DELETE CASCADE,
  type           TEXT NOT NULL CHECK (type IN ('FOLDER','FILE')),
  name           TEXT NOT NULL,
  created_by     INTEGER NOT NULL REFERENCES usuario(id),
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  is_deleted     BOOLEAN NOT NULL DEFAULT FALSE,
  current_version_id UUID,
  CONSTRAINT uq_sibling_name UNIQUE (parent_id, name, is_deleted)
);

-- Árbol jerárquico (closure table)
CREATE TABLE node_closure (
  ancestor_id   UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  descendant_id UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  depth         INT  NOT NULL CHECK (depth >= 0),
  PRIMARY KEY (ancestor_id, descendant_id)
);

-- Archivos binarios en S3
CREATE TABLE file_blob (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  storage_key   TEXT NOT NULL,                 -- Ej: s3://bucket/expediente/node/uuid
  size_bytes    BIGINT NOT NULL,
  checksum_sha256 TEXT NOT NULL,
  mime_type     TEXT NOT NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (checksum_sha256, size_bytes)
);

-- Versiones de archivo
CREATE TABLE file_version (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  node_id       UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  blob_id       UUID NOT NULL REFERENCES file_blob(id) ON DELETE RESTRICT,
  version_num   INT  NOT NULL,
  uploaded_by   INTEGER NOT NULL REFERENCES usuario(id),
  uploaded_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  note          TEXT,
  UNIQUE (node_id, version_num)
);

-- Enlace a versión actual
ALTER TABLE node
  ADD CONSTRAINT fk_node_current_version
  FOREIGN KEY (current_version_id) REFERENCES file_version(id);

-- ====================================
-- TRIGGERS
-- ====================================

-- 1) Consistencia de expediente en nodos
CREATE OR REPLACE FUNCTION enforce_node_case_consistency()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.parent_id IS NOT NULL THEN
    PERFORM 1 FROM node p WHERE p.id = NEW.parent_id AND p.expediente_id = NEW.expediente_id;
    IF NOT FOUND THEN
      RAISE EXCEPTION 'El nodo padre pertenece a otro expediente';
    END IF;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_node_case_consistency
BEFORE INSERT OR UPDATE ON node
FOR EACH ROW
EXECUTE FUNCTION enforce_node_case_consistency();

-- 2) Auto-incrementar número de versión y actualizar current_version
CREATE OR REPLACE FUNCTION set_current_version()
RETURNS TRIGGER AS $$
DECLARE
  v_num INT;
BEGIN
  SELECT COALESCE(MAX(version_num),0)+1 INTO v_num FROM file_version WHERE node_id = NEW.node_id;
  NEW.version_num := v_num;

  UPDATE node SET current_version_id = NEW.id, updated_at = now()
  WHERE id = NEW.node_id;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_current_version
BEFORE INSERT ON file_version
FOR EACH ROW
EXECUTE FUNCTION set_current_version();
