-- ====================================
-- ROLES Y USUARIOS
-- ====================================

CREATE TABLE IF NOT EXISTS rol (
  id     SERIAL  PRIMARY KEY,
  nombre VARCHAR(100) UNIQUE NOT NULL,
  descripcion TEXT,
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS permiso (
  id          SERIAL  PRIMARY KEY,
  nombre      VARCHAR(100) UNIQUE NOT NULL,
  descripcion TEXT,
  modulo      VARCHAR(50) NOT NULL, -- PLANTILLAS, DOCUMENTAL, CONTABLE, SENTENCIAS, USUARIOS, etc.
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS rol_permiso (
  rol_id     INTEGER NOT NULL REFERENCES rol(id) ON DELETE CASCADE,
  permiso_id INTEGER NOT NULL REFERENCES permiso(id) ON DELETE CASCADE,
  created_at TIMESTAMPTZ DEFAULT now(),
  PRIMARY KEY (rol_id, permiso_id)
);

CREATE TABLE IF NOT EXISTS usuario (
  id       SERIAL  PRIMARY KEY,
  nombre   VARCHAR(100) NOT NULL,
  apellido VARCHAR(100),
  email    VARCHAR(200) UNIQUE NOT NULL,
  identificacion VARCHAR(100) UNIQUE,
  contraseña VARCHAR(255) NOT NULL,
  telefono VARCHAR(20),
  direccion TEXT,
  rol_id   INTEGER NOT NULL REFERENCES rol(id),
  activo   BOOLEAN DEFAULT TRUE,
  nuevo_usuario BOOLEAN  DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now(),
  last_login TIMESTAMPTZ
);

-- ====================================
-- CLIENTES
-- ====================================

CREATE TABLE IF NOT EXISTS cliente (
  id              SERIAL PRIMARY KEY,
  tipo_cliente    VARCHAR(20) CHECK (tipo_cliente IN ('NATURAL', 'JURIDICA')),
  nombre          VARCHAR(200) ,
  apellido        VARCHAR(200), -- Para personas naturales
  razon_social    VARCHAR(300), -- Para personas jurídicas
  identificacion  VARCHAR(100) UNIQUE NOT NULL,
  tipo_documento  VARCHAR(20) , -- CC, NIT, CE, PP, etc.
  email           VARCHAR(200),
  telefono        VARCHAR(20),
  direccion       TEXT,
  ciudad          VARCHAR(100),
  departamento    VARCHAR(100),
  pais            VARCHAR(100) DEFAULT 'Colombia',
  fecha_nacimiento DATE, -- Para personas naturales
  representante_legal VARCHAR(200), -- Para personas jurídicas
  activo          BOOLEAN  DEFAULT TRUE,
  observaciones   TEXT,
  created_by      INTEGER NOT NULL REFERENCES usuario(id),
  created_at      TIMESTAMPTZ DEFAULT now(),
  updated_at      TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX idx_cliente_identificacion ON cliente(identificacion);
CREATE INDEX idx_cliente_nombre ON cliente(nombre);
CREATE INDEX idx_cliente_activo ON cliente(activo);

-- ====================================
-- CATEGORÍAS GENERALES
-- ====================================

CREATE TABLE IF NOT EXISTS categoria (
  id          SERIAL PRIMARY KEY,
  nombre      VARCHAR(100) NOT NULL,
  descripcion TEXT,
  tipo        VARCHAR(20) NOT NULL, -- PLANTILLA, CONTABLE, PROCESO
  activo      BOOLEAN NOT NULL DEFAULT TRUE,
  created_at  TIMESTAMPTZ  DEFAULT now(),
  updated_at  TIMESTAMPTZ  DEFAULT now(),
  
  UNIQUE(nombre, tipo)
);

-- ====================================
-- PROCESOS Y EXPEDIENTES
-- ====================================

CREATE TABLE IF NOT EXISTS proceso (
  id              SERIAL  PRIMARY KEY,
  numero_proceso  VARCHAR(100) UNIQUE NOT NULL, -- Número único del proceso
  nombre          VARCHAR(255) NOT NULL,
  descripcion     TEXT,
  tipo_proceso    VARCHAR(100), -- Civil, Penal, Laboral, etc.
  estado          VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT now(),
  fecha_inicio    DATE,
  fecha_cierre DATE,
  cliente_id      INTEGER NOT NULL REFERENCES cliente(id),
  abogado_responsable_id INTEGER NOT NULL REFERENCES usuario(id),
  juzgado         VARCHAR(200),
  radicado        VARCHAR(100),
  demandante      VARCHAR(300),
  demandado       VARCHAR(300),
  cuantia         DECIMAL(15,2),
  observaciones   TEXT,
  activo          BOOLEAN DEFAULT TRUE,
  created_by      INTEGER NOT NULL REFERENCES usuario(id),
  created_at      TIMESTAMPTZ DEFAULT now(),
  updated_at      TIMESTAMPTZ DEFAULT now(),
  
  CONSTRAINT chk_proceso_estado CHECK (estado IN ('ACTIVO', 'SUSPENDIDO', 'CERRADO', 'ARCHIVADO'))
);

CREATE INDEX idx_proceso_cliente ON proceso(cliente_id);
CREATE INDEX idx_proceso_abogado ON proceso(abogado_responsable_id);
CREATE INDEX idx_proceso_estado ON proceso(estado);
CREATE INDEX idx_proceso_numero ON proceso(numero_proceso);

CREATE TABLE IF NOT EXISTS expediente (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255) NOT NULL,
  descripcion     TEXT,
  estado          VARCHAR(50) DEFAULT 'ACTIVO',
  fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT now(),
  fecha_cierre    TIMESTAMPTZ,
  proceso_id      INTEGER NOT NULL REFERENCES proceso(id) ON DELETE CASCADE,
  root_node_id    UUID UNIQUE, -- Carpeta raíz asociada
  orden           INTEGER DEFAULT 1, -- Para ordenar expedientes dentro del proceso
  created_by      INTEGER NOT NULL REFERENCES usuario(id),
  updated_at      TIMESTAMPTZ DEFAULT now(),
  is_deleted      BOOLEAN DEFAULT FALSE,
  
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
  color  VARCHAR(7) DEFAULT '#3498db',
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
  proceso_id      INTEGER REFERENCES proceso(id), -- Opcional
  expediente_id   INTEGER REFERENCES expediente(id), -- Opcional
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id),
  created_at      TIMESTAMPTZ DEFAULT now(),
  updated_at      TIMESTAMPTZ DEFAULT now(),
  is_deleted      BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_evento_proceso ON evento(proceso_id) WHERE is_deleted = false;
CREATE INDEX idx_evento_fecha ON evento(fecha_inicio) WHERE is_deleted = false;

-- ====================================
-- SISTEMA DE ARCHIVOS MEJORADO
-- ====================================

CREATE TABLE node (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  expediente_id  INTEGER REFERENCES expediente(id) ON DELETE CASCADE, -- Puede ser NULL para otros módulos
  contable_id    INTEGER, -- Para documentos contables (se definirá más adelante)
  parent_id      UUID REFERENCES node(id) ON DELETE CASCADE,
  type           VARCHAR(10) NOT NULL CHECK (type IN ('FOLDER','FILE')),
  name           VARCHAR(255) NOT NULL,
  description    TEXT,
  modulo         VARCHAR(20) NOT NULL DEFAULT 'DOCUMENTAL', -- DOCUMENTAL, CONTABLE, PLANTILLAS
  created_by     INTEGER NOT NULL REFERENCES usuario(id),
  created_at     TIMESTAMPTZ DEFAULT now(),
  updated_at     TIMESTAMPTZ DEFAULT now(),
  is_deleted     BOOLEAN DEFAULT FALSE,
  current_version_id UUID,
  
  -- Metadatos adicionales
  size_bytes     BIGINT DEFAULT 0,
  item_count     INTEGER DEFAULT 0,
  last_accessed  TIMESTAMPTZ,
  
  -- Constraint para nombres únicos por contexto
  CONSTRAINT uq_sibling_name UNIQUE (parent_id, name, expediente_id, contable_id, modulo, is_deleted)
);

CREATE INDEX idx_node_parent ON node(parent_id) WHERE is_deleted = false;
CREATE INDEX idx_node_expediente ON node(expediente_id) WHERE is_deleted = false;
CREATE INDEX idx_node_modulo ON node(modulo) WHERE is_deleted = false;
CREATE INDEX idx_node_type ON node(type) WHERE is_deleted = false;
CREATE INDEX idx_node_name ON node(name) WHERE is_deleted = false;

-- Árbol jerárquico (closure table)
CREATE TABLE node_closure (
  ancestor_id   UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  descendant_id UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  depth         INTEGER NOT NULL CHECK (depth >= 0),
  PRIMARY KEY (ancestor_id, descendant_id)
);

CREATE INDEX idx_closure_ancestor ON node_closure(ancestor_id, depth);
CREATE INDEX idx_closure_descendant ON node_closure(descendant_id, depth);

-- ====================================
-- ARCHIVOS Y VERSIONES MEJORADO
-- ====================================

CREATE TABLE file_blob (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  storage_key     VARCHAR(500) NOT NULL,
  bucket_name     VARCHAR(100) NOT NULL,
  size_bytes      BIGINT NOT NULL,
  checksum_sha256 VARCHAR(64) NOT NULL,
  mime_type       VARCHAR(100) NOT NULL,
  original_name   VARCHAR(255),
  created_at      TIMESTAMPTZ DEFAULT now(),
  
  -- Metadatos para optimización
  is_image        BOOLEAN DEFAULT FALSE,
  thumbnail_key   VARCHAR(500),
  
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
  uploaded_at   TIMESTAMPTZ  DEFAULT now(),
  note          TEXT,
  is_current    BOOLEAN DEFAULT TRUE,
  
  UNIQUE (node_id, version_num)
);

CREATE UNIQUE INDEX idx_one_current_version 
ON file_version(node_id) WHERE is_current = true;

CREATE INDEX idx_version_node ON file_version(node_id);
CREATE INDEX idx_version_uploaded_by ON file_version(uploaded_by);

ALTER TABLE node
  ADD CONSTRAINT fk_node_current_version
  FOREIGN KEY (current_version_id) REFERENCES file_version(id);

-- ====================================
-- GESTIÓN DE PLANTILLAS
-- ====================================

CREATE TABLE IF NOT EXISTS plantilla (
  id              SERIAL  PRIMARY KEY,
  nombre          VARCHAR(255) NOT NULL,
  categoria_id    INTEGER NOT NULL REFERENCES categoria(id),
  descripcion     TEXT,
  file_blob_id    UUID NOT NULL REFERENCES file_blob(id), -- Referencia al archivo
  fecha_creacion  TIMESTAMPTZ DEFAULT now(),
  responsable_id  INTEGER NOT NULL REFERENCES usuario(id),
  activo          BOOLEAN DEFAULT TRUE,
  version         VARCHAR(10) DEFAULT '1.0',
  updated_at      TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX idx_plantilla_categoria ON plantilla(categoria_id);
CREATE INDEX idx_plantilla_activo ON plantilla(activo);


-- ====================================
-- GESTIÓN CONTABLE
-- ====================================

CREATE TABLE IF NOT EXISTS documento_contable (
  id              SERIAL PRIMARY KEY,
  numero_documento VARCHAR(100),
  nombre          VARCHAR(255) NOT NULL,
  descripcion     TEXT,
  categoria_id    INTEGER NOT NULL REFERENCES categoria(id), -- Categoría contable
  cliente_id      INTEGER REFERENCES cliente(id), -- Opcional, puede ser documento general
  proceso_id      INTEGER REFERENCES proceso(id), -- Opcional, puede estar asociado a un proceso
  fecha_documento DATE ,
  root_node_id    UUID UNIQUE, -- Carpeta raíz para archivos de este documento
  created_by      INTEGER NOT NULL REFERENCES usuario(id),
  created_at      TIMESTAMPTZ DEFAULT now(),
  updated_at      TIMESTAMPTZ DEFAULT now(),
  is_deleted      BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_doc_contable_categoria ON documento_contable(categoria_id);
CREATE INDEX idx_doc_contable_cliente ON documento_contable(cliente_id);
CREATE INDEX idx_doc_contable_fecha ON documento_contable(fecha_documento);

-- Agregar referencia en node para documentos contables
ALTER TABLE node ADD CONSTRAINT fk_node_contable 
  FOREIGN KEY (contable_id) REFERENCES documento_contable(id) ON DELETE CASCADE;

-- ====================================
-- GESTIÓN DE SENTENCIAS
-- ====================================

CREATE TABLE IF NOT EXISTS sentencia (
  id              SERIAL PRIMARY KEY,
  numero_sentencia VARCHAR(100),
  nombre          VARCHAR(255) NOT NULL,
  descripcion     TEXT,
  tipo_sentencia  VARCHAR(50) NOT NULL, -- Absolutoria, Condenatoria, etc.
  fecha_sentencia DATE NOT NULL,
  fecha_notificacion DATE,
  juzgado         VARCHAR(200),
  magistrado      VARCHAR(200),
  proceso_id      INTEGER NOT NULL REFERENCES proceso(id),
  expediente_id   INTEGER REFERENCES expediente(id),
  cliente_id      INTEGER REFERENCES cliente(id),
  abogado_id      INTEGER NOT NULL REFERENCES usuario(id),
  estado          VARCHAR(30) DEFAULT 'PRIMERA_INSTANCIA',
  es_favorable    BOOLEAN,
  observaciones   TEXT,
  file_blob_id    UUID NOT NULL REFERENCES file_blob(id), -- Archivo PDF de la sentencia
  created_by      INTEGER NOT NULL REFERENCES usuario(id),
  created_at      TIMESTAMPTZ DEFAULT now(),
  updated_at      TIMESTAMPTZ DEFAULT now(),
  is_deleted      BOOLEAN DEFAULT FALSE,
  
  CONSTRAINT chk_sentencia_estado CHECK (estado IN (
    'PRIMERA_INSTANCIA', 'SEGUNDA_INSTANCIA', 'CASACION', 
    'EJECUTORIADA', 'APELADA', 'CUMPLIDA'
  ))
);

CREATE INDEX idx_sentencia_proceso ON sentencia(proceso_id);
CREATE INDEX idx_sentencia_cliente ON sentencia(cliente_id);
CREATE INDEX idx_sentencia_abogado ON sentencia(abogado_id);
CREATE INDEX idx_sentencia_fecha ON sentencia(fecha_sentencia);

-- ====================================
-- PERMISOS DE ARCHIVOS
-- ====================================

CREATE TABLE node_permission (
  id            SERIAL PRIMARY KEY,
  node_id       UUID NOT NULL REFERENCES node(id) ON DELETE CASCADE,
  usuario_id    INTEGER NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
  permission    VARCHAR(20) NOT NULL CHECK (permission IN ('read', 'write', 'admin')),
  granted_by    INTEGER NOT NULL REFERENCES usuario(id),
  granted_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at    TIMESTAMPTZ,
  
  UNIQUE (node_id, usuario_id)
);

-- ====================================
-- AUDITORÍA
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
  user_agent    TEXT,
  modulo        VARCHAR(20) -- DOCUMENTAL, CONTABLE, PLANTILLAS, etc.
);

CREATE INDEX idx_audit_table_record ON audit_log(table_name, record_id);
CREATE INDEX idx_audit_changed_by ON audit_log(changed_by);
CREATE INDEX idx_audit_changed_at ON audit_log(changed_at);
CREATE INDEX idx_audit_modulo ON audit_log(modulo);

-- ====================================
-- TRIGGERS
-- ====================================

-- Función para actualizar timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers para updated_at
CREATE TRIGGER trg_usuario_updated_at BEFORE UPDATE ON usuario
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    
CREATE TRIGGER trg_cliente_updated_at BEFORE UPDATE ON cliente
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    
CREATE TRIGGER trg_proceso_updated_at BEFORE UPDATE ON proceso
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    
CREATE TRIGGER trg_expediente_updated_at BEFORE UPDATE ON expediente
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    
CREATE TRIGGER trg_documento_contable_updated_at BEFORE UPDATE ON documento_contable
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    
CREATE TRIGGER trg_sentencia_updated_at BEFORE UPDATE ON sentencia
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger para mantener closure table
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
    -- Eliminar relaciones existentes excepto consigo mismo
    DELETE FROM node_closure 
    WHERE descendant_id = NEW.id AND depth > 0;
    
    -- Recrear relaciones si tiene nuevo padre
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
INSERT INTO rol (nombre, descripcion) VALUES 
  ('ADMIN', 'Administrador del sistema con todos los permisos'),
  ('ABOGADO', 'Abogado con acceso a gestión de casos y documentos'),
  ('SECRETARIO', 'Secretario con acceso limitado a documentos'),
  ('CONTADOR', 'Contador con acceso a gestión contable'),
  ('CLIENTE', 'Cliente con acceso de solo lectura a sus casos')
ON CONFLICT (nombre) DO NOTHING;

-- Permisos por módulo
INSERT INTO permiso (nombre, descripcion, modulo) VALUES 
  -- Gestión de usuarios
  ('MANAGE_USERS', 'Gestionar usuarios del sistema', 'USUARIOS'),
  
  -- Gestión documental
  ('MANAGE_PROCESOS', 'Gestionar procesos del sistema', 'DOCUMENTAL'),
  ('VIEW_PROCESO', 'Ver procesos', 'DOCUMENTAL'),
  ('MANAGE_EXPEDIENTES', 'Gestionar expedientes del sistema', 'DOCUMENTAL'),
  ('VIEW_EXPEDIENTE', 'Ver expedientes', 'DOCUMENTAL'),
  ('MANAGE_FILES', 'Gestionar archivos del sistema', 'DOCUMENTAL'),
  ('DOWNLOAD_FILE', 'Descargar archivos', 'DOCUMENTAL'),
  
  -- Gestión de plantillas
  ('MANAGE_PLANTILLAS', 'Gestionar plantillas del sistema', 'PLANTILLAS'),
  ('VIEW_PLANTILLA', 'Ver plantillas', 'PLANTILLAS'),
  
  -- Gestión contable
  ('MANAGE_CONTABLES', 'Gestionar documentos contables del sistema', 'CONTABLE'),
  ('VIEW_DOC_CONTABLE', 'Ver documentos contables', 'CONTABLE'),
  
  -- Gestión de sentencias
  ('MANAGE_SENTENCIAS', 'Gestionar sentencias del sistema', 'SENTENCIAS'),
  ('VIEW_SENTENCIA', 'Ver sentencias', 'SENTENCIAS'),
  
  -- Gestión de clientes
   ('MANAGE_CLIENTES', 'Gestionar clientes del sistema', 'CLIENTES'),
  ('VIEW_CLIENTE', 'Ver clientes', 'CLIENTES'),
  
  -- Auditoría
  ('VIEW_AUDIT', 'Ver logs de auditoría', 'AUDITORIA')
ON CONFLICT (nombre) DO NOTHING;

-- Categorías por defecto
INSERT INTO categoria (nombre, descripcion, tipo) VALUES 
  -- Categorías para plantillas
  ('Familia', 'Documentos de derecho de familia', 'PLANTILLA'),
  ('Civil', 'Documentos de derecho civil', 'PLANTILLA'),
  ('Penal', 'Documentos de derecho penal', 'PLANTILLA'),
  ('Laboral', 'Documentos de derecho laboral', 'PLANTILLA'),
  ('Comercial', 'Documentos de derecho comercial', 'PLANTILLA'),
  ('Administrativo', 'Documentos de derecho administrativo', 'PLANTILLA'),
  
  -- Categorías para documentos contables
  ('Declaración', 'Declaraciones tributarias', 'CONTABLE'),
  ('Balance', 'Balances contables', 'CONTABLE'),
  ('Factura', 'Facturas y documentos de venta', 'CONTABLE'),
  ('Comprobante', 'Comprobantes contables', 'CONTABLE'),
  ('Nómina', 'Documentos de nómina', 'CONTABLE'),
  ('Impuestos', 'Documentos tributarios', 'CONTABLE'),
  
  -- Categorías para procesos
  ('Civil', 'Procesos civiles', 'PROCESO'),
  ('Penal', 'Procesos penales', 'PROCESO'),
  ('Laboral', 'Procesos laborales', 'PROCESO'),
  ('Familia', 'Procesos de familia', 'PROCESO'),
  ('Comercial', 'Procesos comerciales', 'PROCESO'),
  ('Administrativo', 'Procesos administrativos', 'PROCESO')
ON CONFLICT (nombre, tipo) DO NOTHING;

-- Tipos de evento básicos
INSERT INTO tipo_evento (nombre, color) VALUES 
  ('Audiencia', '#e74c3c'),
  ('Reunión Cliente', '#3498db'),
  ('Vencimiento', '#f39c12'),
  ('Recordatorio', '#2ecc71'),
  ('Entrega Documentos', '#9b59b6')
ON CONFLICT (nombre) DO NOTHING;

-- Asignar permisos al rol ADMIN (todos los permisos)
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id 
FROM rol r, permiso p 
WHERE r.nombre = 'ADMIN'
ON CONFLICT DO NOTHING;

-- Asignar permisos básicos al rol ABOGADO
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id 
FROM rol r, permiso p 
WHERE r.nombre = 'ABOGADO' 
  AND p.nombre IN (
    'MANAGE_PROCESOS', 'MANAGE_EXPEDIENTES', 'MANAGE_PLANTILLAS', 'MANAGE_FILES',
    'MANAGE_SENTENCIAS', 'MANAGE_CLIENTES', 
  )
ON CONFLICT DO NOTHING;