/* ── Dados iniciais ── */

INSERT INTO empresa (nome)
SELECT 'TechCorp Solutions' WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE nome = 'TechCorp Solutions');

INSERT INTO empresa (nome)
SELECT 'InnovaTech Ltda' WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE nome = 'InnovaTech Ltda');

INSERT INTO empresa (nome)
SELECT 'StartupXYZ' WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE nome = 'StartupXYZ');

INSERT INTO espaco (nome, empresa_id)
SELECT 'Desenvolvimento Web', id_empresa FROM empresa WHERE nome = 'TechCorp Solutions'
AND NOT EXISTS (SELECT 1 FROM espaco WHERE nome = 'Desenvolvimento Web');

INSERT INTO espaco (nome, empresa_id)
SELECT 'Marketing Digital', id_empresa FROM empresa WHERE nome = 'TechCorp Solutions'
AND NOT EXISTS (SELECT 1 FROM espaco WHERE nome = 'Marketing Digital');

INSERT INTO espaco (nome, empresa_id)
SELECT 'Recursos Humanos', id_empresa FROM empresa WHERE nome = 'InnovaTech Ltda'
AND NOT EXISTS (SELECT 1 FROM espaco WHERE nome = 'Recursos Humanos');

INSERT INTO espaco (nome, empresa_id)
SELECT 'Vendas', id_empresa FROM empresa WHERE nome = 'InnovaTech Ltda'
AND NOT EXISTS (SELECT 1 FROM espaco WHERE nome = 'Vendas');

INSERT INTO espaco (nome, empresa_id)
SELECT 'Produto', id_empresa FROM empresa WHERE nome = 'StartupXYZ'
AND NOT EXISTS (SELECT 1 FROM espaco WHERE nome = 'Produto');

INSERT INTO cargo (nome_cargo)
SELECT 'Desenvolvedor' WHERE NOT EXISTS (SELECT 1 FROM cargo WHERE nome_cargo = 'Desenvolvedor');

INSERT INTO cargo (nome_cargo)
SELECT 'Designer' WHERE NOT EXISTS (SELECT 1 FROM cargo WHERE nome_cargo = 'Designer');

INSERT INTO cargo (nome_cargo)
SELECT 'Gerente de Projeto' WHERE NOT EXISTS (SELECT 1 FROM cargo WHERE nome_cargo = 'Gerente de Projeto');

INSERT INTO setor (nome_setor)
SELECT 'Tecnologia' WHERE NOT EXISTS (SELECT 1 FROM setor WHERE nome_setor = 'Tecnologia');

INSERT INTO setor (nome_setor)
SELECT 'Marketing' WHERE NOT EXISTS (SELECT 1 FROM setor WHERE nome_setor = 'Marketing');

INSERT INTO setor (nome_setor)
SELECT 'Recursos Humanos' WHERE NOT EXISTS (SELECT 1 FROM setor WHERE nome_setor = 'Recursos Humanos');

INSERT INTO usuario (cargo_id, setor_id, nome, email, senha, telefone, data_nascimento)
SELECT c.id_cargo, s.id_setor, 'Joao Silva', 'joao@techcorp.com', 'senha123', '11987654321', '1990-05-15'
FROM cargo c, setor s WHERE c.nome_cargo = 'Desenvolvedor' AND s.nome_setor = 'Tecnologia'
AND NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'joao@techcorp.com');

INSERT INTO usuario (cargo_id, setor_id, nome, email, senha, telefone, data_nascimento)
SELECT c.id_cargo, s.id_setor, 'Maria Santos', 'maria@techcorp.com', 'senha123', '11987654322', '1988-08-22'
FROM cargo c, setor s WHERE c.nome_cargo = 'Designer' AND s.nome_setor = 'Tecnologia'
AND NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'maria@techcorp.com');

INSERT INTO usuario (cargo_id, setor_id, nome, email, senha, telefone, data_nascimento)
SELECT c.id_cargo, s.id_setor, 'Pedro Costa', 'pedro@innovatech.com', 'senha123', '11987654323', '1985-12-10'
FROM cargo c, setor s WHERE c.nome_cargo = 'Gerente de Projeto' AND s.nome_setor = 'Marketing'
AND NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'pedro@innovatech.com');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Desenvolver sistema de login', 'Implementar autenticacao de usuarios', 'EM_ANDAMENTO', 65.0, '2024-01-15', '2024-02-15', id_espaco, 'ALTA'
FROM espaco WHERE nome = 'Desenvolvimento Web'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Desenvolver sistema de login');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Criar landing page', 'Desenvolver pagina inicial responsiva', 'A_INICIAR', 0.0, '2024-02-01', '2024-02-28', id_espaco, 'MEDIA'
FROM espaco WHERE nome = 'Desenvolvimento Web'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Criar landing page');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Campanha redes sociais', 'Planejar campanha no Instagram e Facebook', 'EM_ANDAMENTO', 30.0, '2024-01-20', '2024-03-01', id_espaco, 'ALTA'
FROM espaco WHERE nome = 'Marketing Digital'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Campanha redes sociais');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Processo seletivo', 'Recrutar desenvolvedores senior', 'EM_ATRASO', 15.0, '2024-01-01', '2024-01-31', id_espaco, 'ALTA'
FROM espaco WHERE nome = 'Recursos Humanos'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Processo seletivo');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Analise de vendas Q1', 'Relatorio de vendas do primeiro trimestre', 'CONCLUIDO', 100.0, '2024-01-01', '2024-01-30', id_espaco, 'MEDIA'
FROM espaco WHERE nome = 'Vendas'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Analise de vendas Q1');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Redesign do produto', 'Atualizar interface do usuario', 'A_INICIAR', 0.0, '2024-02-15', '2024-04-15', id_espaco, 'BAIXA'
FROM espaco WHERE nome = 'Produto'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Redesign do produto');

INSERT INTO tarefa (titulo, descricao, status, progresso, data_inicio, data_entrega, espaco_id, prioridade)
SELECT 'Sistema de notificacoes', 'Implementar push notifications', 'EM_ANDAMENTO', 45.0, '2024-01-25', '2024-03-10', id_espaco, 'MEDIA'
FROM espaco WHERE nome = 'Desenvolvimento Web'
AND NOT EXISTS (SELECT 1 FROM tarefa WHERE titulo = 'Sistema de notificacoes');

INSERT INTO atualizacoes (tarefa_id, usuario_id, tipo, descricao, data_time)
SELECT t.id_tarefa, u.id_usuario, 'ATIVIDADE', 'Iniciado desenvolvimento da tela de login', CURRENT_TIMESTAMP
FROM tarefa t, usuario u WHERE t.titulo = 'Desenvolver sistema de login' AND u.email = 'joao@techcorp.com'
AND NOT EXISTS (SELECT 1 FROM atualizacoes WHERE descricao = 'Iniciado desenvolvimento da tela de login');

INSERT INTO atualizacoes (tarefa_id, usuario_id, tipo, descricao, data_time)
SELECT t.id_tarefa, u.id_usuario, 'NOTA', 'Utilizando Spring Security para autenticacao', CURRENT_TIMESTAMP
FROM tarefa t, usuario u WHERE t.titulo = 'Desenvolver sistema de login' AND u.email = 'joao@techcorp.com'
AND NOT EXISTS (SELECT 1 FROM atualizacoes WHERE descricao = 'Utilizando Spring Security para autenticacao');

INSERT INTO atualizacoes (tarefa_id, usuario_id, tipo, descricao, data_time)
SELECT t.id_tarefa, u.id_usuario, 'ATIVIDADE', 'Definidas personas para a campanha', CURRENT_TIMESTAMP
FROM tarefa t, usuario u WHERE t.titulo = 'Campanha redes sociais' AND u.email = 'pedro@innovatech.com'
AND NOT EXISTS (SELECT 1 FROM atualizacoes WHERE descricao = 'Definidas personas para a campanha');

INSERT INTO atualizacoes (tarefa_id, usuario_id, tipo, descricao, data_time)
SELECT t.id_tarefa, u.id_usuario, 'ATIVIDADE', 'Relatorio finalizado e enviado para diretoria', CURRENT_TIMESTAMP
FROM tarefa t, usuario u WHERE t.titulo = 'Analise de vendas Q1' AND u.email = 'joao@techcorp.com'
AND NOT EXISTS (SELECT 1 FROM atualizacoes WHERE descricao = 'Relatorio finalizado e enviado para diretoria');
