-- ====================================
-- ROLES Y USUARIOS
-- ====================================

CREATE TABLE IF NOT EXISTS rol (
  id     SERIAL  PRIMARY KEY,
  nombre VARCHAR(100) UNIQUE NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS permiso (
  id          SERIAL  PRIMARY KEY,
  nombre      VARCHAR(100) UNIQUE NOT NULL,
  descripcion TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS rol_permiso (
  rol_id     INTEGER NOT NULL REFERENCES rol(id) ON DELETE CASCADE,
  permiso_id INTEGER NOT NULL REFERENCES permiso(id) ON DELETE CASCADE,
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
  nuevo_usuario BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  last_login TIMESTAMPTZ
);

-- ====================================
-- PROCESOS Y EXPEDIENTES
-- ====================================

CREATE TABLE IF NOT EXISTS proceso (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255) NOT NULL,
  descripcion     TEXT,
  fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT now(),
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id),
  activo          BOOLEAN NOT NULL DEFAULT TRUE,
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS expediente (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255) NOT NULL,
  estado          VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
  descripcion     TEXT,
  fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT now(),
  fecha_cierre    TIMESTAMPTZ,
  proceso_id      INTEGER NOT NULL REFERENCES proceso(id),
  root_node_id    UUID UNIQUE, -- Carpeta raíz asociada
  created_by      INTEGER NOT NULL REFERENCES usuario(id),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
  
  -- Índices para búsquedas frecuentes
  CONSTRAINT chk_expediente_estado CHECK (estado IN ('ACTIVO', 'ARCHIVADO', 'CERRADO'))
);

-- Índices para optimizar consultas
CREATE INDEX idx_expediente_proceso ON expediente(proceso_id) WHERE is_deleted = false;
CREATE INDEX idx_expediente_estado ON expediente(estado) WHERE is_deleted = false;
CREATE INDEX idx_expediente_created_by ON expediente(created_by);

-- ====================================
-- EVENTOS Y PLANTILLAS
-- ====================================

CREATE TABLE IF NOT EXISTS tipo_evento (
  id     SERIAL  PRIMARY KEY,
  nombre VARCHAR(50) UNIQUE NOT NULL,
  color  VARCHAR(7) DEFAULT '#3498db', -- Color hex para el frontend
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS evento (
  id              SERIAL  PRIMARY KEY,
  titulo          VARCHAR(255) NOT NULL,
  descripcion     TEXT,
  tipo_evento_id  INTEGER NOT NULL REFERENCES tipo_evento(id),
  fecha_inicio    TIMESTAMPTZ NOT NULL,
  fecha_fin       TIMESTAMPTZ,
  all_day         BOOLEAN NOT NULL DEFAULT FALSE,
  proceso_id      INTEGER NOT NULL REFERENCES proceso(id),
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id),
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  is_deleted      BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS plantilla (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255) NOT NULL,
  categoria       VARCHAR(100) NOT NULL,
  descripcion     TEXT,
  url             VARCHAR(500) NOT NULL, -- URLs pueden ser largas
  fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT now(),
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id),
  activo          BOOLEAN NOT NULL DEFAULT TRUE,
  tags            TEXT[], -- Array de tags para búsqueda
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ====================================
-- SISTEMA DE ARCHIVOS MEJORADO
-- ====================================

CREATE TABLE node (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  expediente_id  INTEGER NOT NULL REFERENCES expediente(id) ON DELETE CASCADE,
  parent_id      UUID REFERENCES node(id) ON DELETE CASCADE,
  type           VARCHAR(10) NOT NULL CHECK (type IN ('FOLDER','FILE')),
  name           VARCHAR(255) NOT NULL,
  description    TEXT,
  created_by     INTEGER NOT NULL REFERENCES usuario(id),
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  is_deleted     BOOLEAN NOT NULL DEFAULT FALSE,
  current_version_id UUID,
  
  -- Metadatos adicionales útiles para el frontend
  size_bytes     BIGINT DEFAULT 0, -- Para carpetas será la suma de archivos
  item_count     INTEGER DEFAULT 0, -- Para carpetas, número de elementos
  last_accessed  TIMESTAMPTZ,
  
  -- Constraint para nombres únicos por carpeta padre
  CONSTRAINT uq_sibling_name UNIQUE (parent_id, name, expediente_id, is_deleted)
);

-- Índices optimizados para consultas frecuentes
CREATE INDEX idx_node_parent ON node(parent_id) WHERE is_deleted = false;
CREATE INDEX idx_node_expediente ON node(expediente_id) WHERE is_deleted = false;
CREATE INDEX idx_node_type ON node(type) WHERE is_deleted = false;
CREATE INDEX idx_node_name ON node(name) WHERE is_deleted = false;
CREATE INDEX idx_node_created_by ON node(created_by);

-- Árbol jerárquico (closure table)
CREATE TABLE node_closure (
  ancestor_id   UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  descendant_id UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  depth         INTEGER NOT NULL CHECK (depth >= 0),
  PRIMARY KEY (ancestor_id, descendant_id)
);

-- Índices para optimizar consultas de jerarquía
CREATE INDEX idx_closure_ancestor ON node_closure(ancestor_id, depth);
CREATE INDEX idx_closure_descendant ON node_closure(descendant_id, depth);

-- ====================================
-- ARCHIVOS Y VERSIONES MEJORADO
-- ====================================

CREATE TABLE file_blob (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  storage_key     VARCHAR(500) NOT NULL, -- Clave en S3
  bucket_name     VARCHAR(100) NOT NULL, -- Nombre del bucket
  size_bytes      BIGINT NOT NULL,
  checksum_sha256 VARCHAR(64) NOT NULL,
  mime_type       VARCHAR(100) NOT NULL,
  original_name   VARCHAR(255), -- Nombre original del archivo
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  
  -- Metadatos para optimización
  is_image        BOOLEAN DEFAULT FALSE,
  thumbnail_key   VARCHAR(500), -- Para imágenes, clave del thumbnail
  
  UNIQUE (checksum_sha256, size_bytes)
);

CREATE INDEX idx_blob_storage_key ON file_blob(storage_key);
CREATE INDEX idx_blob_mime_type ON file_blob(mime_type);

CREATE TABLE file_version (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  node_id       UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  blob_id       UUID NOT NULL REFERENCES file_blob(id) ON DELETE RESTRICT,
  version_num   INTEGER NOT NULL,
  uploaded_by   INTEGER NOT NULL REFERENCES usuario(id),
  uploaded_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  note          TEXT,
  is_current    BOOLEAN NOT NULL DEFAULT TRUE,
  
  UNIQUE (node_id, version_num)
);

-- Solo puede haber una versión actual por archivo
CREATE UNIQUE INDEX idx_one_current_version 
ON file_version(node_id) WHERE is_current = true;

CREATE INDEX idx_version_node ON file_version(node_id);
CREATE INDEX idx_version_uploaded_by ON file_version(uploaded_by);

-- Enlace a versión actual (mejorado)
ALTER TABLE node
  ADD CONSTRAINT fk_node_current_version
  FOREIGN KEY (current_version_id) REFERENCES file_version(id);

-- ====================================
-- PERMISOS DE ARCHIVOS (NUEVO)
-- ====================================

CREATE TABLE node_permission (
  id            SERIAL PRIMARY KEY,
  node_id       UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  usuario_id    INTEGER NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
  permission    VARCHAR(20) NOT NULL CHECK (permission IN ('READ', 'write', 'admin')),
  granted_by    INTEGER NOT NULL REFERENCES usuario(id),
  granted_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at    TIMESTAMPTZ,
  
  UNIQUE (node_id, usuario_id)
);

-- ====================================
-- AUDITORÍA (NUEVO)
-- ====================================

CREATE TABLE audit_log (
  id            SERIAL PRIMARY KEY,
  table_name    VARCHAR(50) NOT NULL,
  record_id     VARCHAR(100) NOT NULL,
  action        VARCHAR(20) NOT NULL CHECK (action IN ('INSERT', 'UPDATE', 'DELETE')),
  old_values    JSONB,
  new_values    JSONB,
  changed_by    INTEGER NOT NULL REFERENCES usuario(id),
  changed_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  ip_address    INET,
  user_agent    TEXT
);

CREATE INDEX idx_audit_table_record ON audit_log(table_name, record_id);
CREATE INDEX idx_audit_changed_by ON audit_log(changed_by);
CREATE INDEX idx_audit_changed_at ON audit_log(changed_at);



-- ====================================
-- TRIGGERS MEJORADOS
-- ====================================

-- 1) Consistencia de expediente en nodos
CREATE OR REPLACE FUNCTION enforce_node_case_consistency()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.parent_id IS NOT NULL THEN
    PERFORM 1 FROM node p 
    WHERE p.id = NEW.parent_id 
      AND p.expediente_id = NEW.expediente_id
      AND p.is_deleted = false;
    IF NOT FOUND THEN
      RAISE EXCEPTION 'El nodo padre no existe o pertenece a otro expediente';
    END IF;
  END IF;
  
  -- Actualizar timestamp
  NEW.updated_at := now();
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_node_case_consistency
  BEFORE INSERT OR UPDATE ON node
  FOR EACH ROW
  EXECUTE FUNCTION enforce_node_case_consistency();

-- 2) Mantener closure table actualizada
CREATE OR REPLACE FUNCTION maintain_node_closure()
RETURNS TRIGGER AS $$
BEGIN
  IF TG_OP = 'INSERT' THEN
    -- Insertar relación consigo mismo
    INSERT INTO node_closure (ancestor_id, descendant_id, depth)
    VALUES (NEW.id, NEW.id, 0);
    
    -- Insertar relaciones con ancestros
    IF NEW.parent_id IS NOT NULL THEN
      INSERT INTO node_closure (ancestor_id, descendant_id, depth)
      SELECT ancestor_id, NEW.id, depth + 1
      FROM node_closure
      WHERE descendant_id = NEW.parent_id;
    END IF;
    
  ELSIF TG_OP = 'UPDATE' AND OLD.parent_id IS DISTINCT FROM NEW.parent_id THEN
    -- Mover nodo requiere reconstruir closure table
    -- Para simplicidad, eliminar y recrear
    DELETE FROM node_closure WHERE descendant_id = NEW.id AND depth > 0;
    
    IF NEW.parent_id IS NOT NULL THEN
      INSERT INTO node_closure (ancestor_id, descendant_id, depth)
      SELECT ancestor_id, NEW.id, depth + 1
      FROM node_closure
      WHERE descendant_id = NEW.parent_id;
    END IF;
  END IF;
  
  RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_maintain_node_closure
  AFTER INSERT OR UPDATE ON node
  FOR EACH ROW
  EXECUTE FUNCTION maintain_node_closure();


-- ====================================
-- DATOS INICIALES
-- ====================================

-- Roles básicos
INSERT INTO rol (nombre) VALUES 
  ('ADMIN'), 
  ('ABOGADO'), 
  ('SECRETARIO'), 
  ('CLIENTE')
ON CONFLICT (nombre) DO NOTHING;

-- Permisos básicos
INSERT INTO permiso (nombre, descripcion) VALUES 
  ('CREATE_EXPEDIENTE', 'Crear expedientes'),
  ('READ_EXPEDIENTE', 'Ver expedientes'),
  ('UPDATE_EXPEDIENTE', 'Modificar expedientes'),
  ('DELETE_EXPEDIENTE', 'Eliminar expedientes'),
  ('UPLOAD_FILE', 'Subir archivos'),
  ('DOWNLOAD_FILE', 'Descargar archivos'),
  ('MANAGE_USERS', 'Gestionar usuarios'),
  ('VIEW_AUDIT', 'Ver logs de auditoría')
ON CONFLICT (nombre) DO NOTHING;

-- Tipos de evento básicos
INSERT INTO tipo_evento (nombre, color) VALUES 
  ('Audiencia', '#e74c3c'),
  ('Reunión Cliente', '#3498db'),
  ('Vencimiento', '#f39c12'),
  ('Recordatorio', '#2ecc71')
ON CONFLICT (nombre) DO NOTHING;