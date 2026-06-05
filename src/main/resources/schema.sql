/* ── Tabelas ─────────────────────────────────────────────── */

CREATE TABLE IF NOT EXISTS empresa
(
    id_empresa  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        VARCHAR(45) NOT NULL,
    cep         CHAR(15),
    endereco    VARCHAR(45),
    complemento VARCHAR(45),
    numero      CHAR(4),
    bairro      VARCHAR(45),
    cidade      VARCHAR(45),
    estado      VARCHAR(45)
);

CREATE TABLE IF NOT EXISTS espaco
(
    id_espaco    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    empresa_id   INT          NOT NULL,
    CONSTRAINT fk_espaco_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa (id_empresa) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tarefa
(
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
        FOREIGN KEY (espaco_id) REFERENCES espaco (id_espaco) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cargo
(
    id_cargo   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_cargo VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS setor
(
    id_setor   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_setor VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario
(
    id_usuario      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cargo_id        INT          NOT NULL,
    setor_id        INT          NOT NULL,
    nome            VARCHAR(45)  NOT NULL,
    email           VARCHAR(45)  NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    telefone        CHAR(13)     NOT NULL,
    data_nascimento DATE         NOT NULL,
    CONSTRAINT fk_usuario_cargo
        FOREIGN KEY (cargo_id) REFERENCES cargo (id_cargo) ON DELETE NO ACTION,
    CONSTRAINT fk_usuario_setor
        FOREIGN KEY (setor_id) REFERENCES setor (id_setor) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS tarefa_usuario
(
    tarefa_id  INT NOT NULL,
    usuario_id INT NOT NULL,
    PRIMARY KEY (tarefa_id, usuario_id),
    CONSTRAINT fk_tarefa_usuario_tarefa
        FOREIGN KEY (tarefa_id) REFERENCES tarefa (id_tarefa) ON DELETE CASCADE,
    CONSTRAINT fk_tarefa_usuario_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS atualizacoes
(
    id_atualizacao INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tarefa_id      INT          NOT NULL,
    usuario_id     INT,
    tipo           VARCHAR(20)  NOT NULL DEFAULT 'ATIVIDADE',
    descricao      VARCHAR(255) NOT NULL,
    data_time      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atualizacao_tarefa
        FOREIGN KEY (tarefa_id) REFERENCES tarefa (id_tarefa) ON DELETE CASCADE,
    CONSTRAINT fk_atualizacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id_usuario) ON DELETE SET NULL
);

/* ── Índices ─────────────────────────────────────────────── */

CREATE INDEX IF NOT EXISTS idx_espaco_empresa_id ON espaco (empresa_id);
CREATE INDEX IF NOT EXISTS idx_tarefa_espaco_id ON tarefa (espaco_id);
CREATE INDEX IF NOT EXISTS idx_usuario_cargo_id ON usuario (cargo_id);
CREATE INDEX IF NOT EXISTS idx_usuario_setor_id ON usuario (setor_id);
CREATE INDEX IF NOT EXISTS idx_tarefa_usuario_usuario_id ON tarefa_usuario (usuario_id);
CREATE INDEX IF NOT EXISTS idx_atualizacoes_tarefa_id ON atualizacoes (tarefa_id);
CREATE INDEX IF NOT EXISTS idx_atualizacoes_usuario_id ON atualizacoes (usuario_id);