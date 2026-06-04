<<<<<<< HEAD
CREATE TABLE IF NOT EXISTS empresa
(
    id_empresa  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        VARCHAR(45) NOT NULL,
    cep         CHAR(15)    NOT NULL,
    endereco    VARCHAR(45) NOT NULL,
    complemento VARCHAR(45),
    numero      CHAR(4)     NOT NULL,
    bairro      VARCHAR(45) NOT NULL,
    cidade      VARCHAR(45) NOT NULL,
    estado      VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS espaco
(
=======
/* ── Tabelas ─────────────────────────────────────────────── */

CREATE TABLE IF NOT EXISTS empresa (
    id_empresa  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        VARCHAR(45)  NOT NULL,
    cep         CHAR(15),
    endereco    VARCHAR(45),
    complemento VARCHAR(45),
    numero      CHAR(4),
    bairro      VARCHAR(45),
    cidade      VARCHAR(45),
    estado      VARCHAR(45)
);

CREATE TABLE IF NOT EXISTS espaco (
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
    id_espaco    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    empresa_id   INT          NOT NULL,
    CONSTRAINT fk_espaco_empresa
<<<<<<< HEAD
        FOREIGN KEY (empresa_id)
            REFERENCES empresa (id_empresa)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tarefa
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        VARCHAR(255) NOT NULL,
    descricao   TEXT,
    etapa       VARCHAR(50)  NOT NULL,
    tipo        VARCHAR(50)  NOT NULL,
    empresa     VARCHAR(255),
    equipe      VARCHAR(255),
    responsavel VARCHAR(255),
    espaco      VARCHAR(255),
    criado_em   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cargo
(
=======
        FOREIGN KEY (empresa_id) REFERENCES empresa(id_empresa) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tarefa (
    id_tarefa    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo       VARCHAR(100) NOT NULL,
    descricao    VARCHAR(500),
    status       VARCHAR(30)  NOT NULL DEFAULT 'A_INICIAR',
    progresso    REAL         NOT NULL DEFAULT 0,
    data_inicio  DATE,
    data_entrega DATE,
    prioridade   VARCHAR(10)  NOT NULL DEFAULT 'BAIXA',
    espaco_id    INT          NOT NULL,
    CONSTRAINT fk_tarefa_espaco
        FOREIGN KEY (espaco_id) REFERENCES espaco(id_espaco) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cargo (
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
    id_cargo   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_cargo VARCHAR(45) NOT NULL
);

<<<<<<< HEAD
CREATE TABLE IF NOT EXISTS setor
(
=======
CREATE TABLE IF NOT EXISTS setor (
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
    id_setor   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_setor VARCHAR(45) NOT NULL
);

<<<<<<< HEAD
CREATE TABLE IF NOT EXISTS usuario
(
=======
CREATE TABLE IF NOT EXISTS usuario (
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
    id_usuario      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cargo_id        INT          NOT NULL,
    setor_id        INT          NOT NULL,
    nome            VARCHAR(45)  NOT NULL,
    email           VARCHAR(45)  NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    telefone        CHAR(13)     NOT NULL,
    data_nascimento DATE         NOT NULL,
<<<<<<< HEAD

    CONSTRAINT fk_usuario_cargo
        FOREIGN KEY (cargo_id)
            REFERENCES cargo (id_cargo)
            ON DELETE NO ACTION,

    CONSTRAINT fk_usuario_setor
        FOREIGN KEY (setor_id)
            REFERENCES setor (id_setor)
            ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS tarefa_usuario
(
    tarefa_id  INT NOT NULL,
    usuario_id INT NOT NULL,

    PRIMARY KEY (tarefa_id, usuario_id),

    CONSTRAINT fk_tarefa_usuario_tarefa
        FOREIGN KEY (tarefa_id)
            REFERENCES tarefa (id_tarefa)
            ON DELETE CASCADE,

    CONSTRAINT fk_tarefa_usuario_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuario (id_usuario)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS atualizacoes
(
    id_atualizacao INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tarefa_id      INT          NOT NULL,
    usuario_id     INT,
    descricao      VARCHAR(255) NOT NULL,
    data_time      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_atualizacao_tarefa
        FOREIGN KEY (tarefa_id)
            REFERENCES tarefa (id_tarefa)
            ON DELETE CASCADE,

    CONSTRAINT fk_atualizacao_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuario (id_usuario)
            ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_espaco_empresa_id
    ON espaco (empresa_id);

CREATE INDEX IF NOT EXISTS idx_tarefa_espaco_id
    ON tarefa (espaco_id);

CREATE INDEX IF NOT EXISTS idx_usuario_cargo_id
    ON usuario (cargo_id);

CREATE INDEX IF NOT EXISTS idx_usuario_setor_id
    ON usuario (setor_id);

CREATE INDEX IF NOT EXISTS idx_tarefa_usuario_usuario_id
    ON tarefa_usuario (usuario_id);

CREATE INDEX IF NOT EXISTS idx_atualizacoes_tarefa_id
    ON atualizacoes (tarefa_id);

CREATE INDEX IF NOT EXISTS idx_atualizacoes_usuario_id
    ON atualizacoes (usuario_id);
=======
    CONSTRAINT fk_usuario_cargo
        FOREIGN KEY (cargo_id)  REFERENCES cargo(id_cargo)  ON DELETE NO ACTION,
    CONSTRAINT fk_usuario_setor
        FOREIGN KEY (setor_id)  REFERENCES setor(id_setor)  ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS tarefa_usuario (
    tarefa_id  INT NOT NULL,
    usuario_id INT NOT NULL,
    PRIMARY KEY (tarefa_id, usuario_id),
    CONSTRAINT fk_tarefa_usuario_tarefa
        FOREIGN KEY (tarefa_id)  REFERENCES tarefa(id_tarefa)   ON DELETE CASCADE,
    CONSTRAINT fk_tarefa_usuario_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS atualizacoes (
    id_atualizacao INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tarefa_id      INT          NOT NULL,
    usuario_id     INT,
    tipo           VARCHAR(20)  NOT NULL DEFAULT 'ATIVIDADE',
    descricao      VARCHAR(255) NOT NULL,
    data_time      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atualizacao_tarefa
        FOREIGN KEY (tarefa_id)  REFERENCES tarefa(id_tarefa)   ON DELETE CASCADE,
    CONSTRAINT fk_atualizacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

/* ── Índices ─────────────────────────────────────────────── */

CREATE INDEX IF NOT EXISTS idx_espaco_empresa_id          ON espaco       (empresa_id);
CREATE INDEX IF NOT EXISTS idx_tarefa_espaco_id           ON tarefa       (espaco_id);
CREATE INDEX IF NOT EXISTS idx_usuario_cargo_id           ON usuario      (cargo_id);
CREATE INDEX IF NOT EXISTS idx_usuario_setor_id           ON usuario      (setor_id);
CREATE INDEX IF NOT EXISTS idx_tarefa_usuario_usuario_id  ON tarefa_usuario (usuario_id);
CREATE INDEX IF NOT EXISTS idx_atualizacoes_tarefa_id     ON atualizacoes (tarefa_id);
CREATE INDEX IF NOT EXISTS idx_atualizacoes_usuario_id    ON atualizacoes (usuario_id);

/* ── Trigger de log ─────────────────────────────────────────
   Executar manualmente no pgAdmin após o primeiro startup:

   CREATE OR REPLACE FUNCTION fn_log_update_tarefa()
   RETURNS TRIGGER AS $$
   BEGIN
       INSERT INTO atualizacoes (tarefa_id, tipo, descricao, data_time)
       VALUES (
           NEW.id_tarefa, 'ATIVIDADE',
           'Tarefa atualizada: ' || COALESCE(OLD.titulo,'') || ' -> ' || COALESCE(NEW.titulo,''),
           CURRENT_TIMESTAMP
       );
       RETURN NEW;
   END;
   $$ LANGUAGE plpgsql;

   DROP TRIGGER IF EXISTS trg_log_update_tarefa ON tarefa;
   CREATE TRIGGER trg_log_update_tarefa
       AFTER UPDATE ON tarefa FOR EACH ROW
       EXECUTE FUNCTION fn_log_update_tarefa();
── */
>>>>>>> 66aac797dcb5269ffa00671f93e736700f0dd81f
