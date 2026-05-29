# PipeUp - Sistema de Gerenciamento de Tarefas

Sistema completo de gerenciamento de tarefas desenvolvido com Spring Boot, PostgreSQL e interface moderna.

## 🚀 Funcionalidades Implementadas

### ✅ Dashboard Funcional
- **Kanban Board** com 4 colunas (Em Atraso, Em Andamento, A Iniciar, Concluído)
- **Drag & Drop** para mover tarefas entre colunas
- **Pesquisa em tempo real** de tarefas
- **Botões funcionais** para criar novas tarefas
- **Atalhos de teclado** (Ctrl+N para nova tarefa, Ctrl+/ para pesquisar)

### ✅ Gerenciamento de Tarefas
- **CRUD completo** de tarefas
- **Formulário de criação** com validação
- **Tela de detalhes** com edição inline
- **Auto-save** das alterações
- **Histórico de atividades** com diferentes tipos (Nota, Atividade, Arquivo)
- **Calendário interativo** para datas importantes
- **Controle de progresso** com slider

### ✅ Funcionalidades Avançadas
- **Validação em tempo real** dos formulários
- **Notificações** de sucesso/erro
- **Interface responsiva** e moderna
- **Dados de exemplo** pré-carregados
- **Navegação intuitiva** entre telas

## 🛠️ Tecnologias Utilizadas

- **Backend**: Spring Boot 3.2.0, Spring Data JPA, Spring Web
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript ES6+
- **Banco de Dados**: PostgreSQL
- **Build**: Maven
- **Java**: 17

## 📋 Pré-requisitos

1. **Java 17** ou superior
2. **PostgreSQL** instalado e rodando
3. **Maven** (ou usar o wrapper incluído)

## 🔧 Configuração do Banco de Dados

1. Criar banco de dados PostgreSQL:
```sql
CREATE DATABASE pipeup;
```

2. Configurar usuário (opcional):
```sql
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE pipeup TO postgres;
```

3. As tabelas serão criadas automaticamente pelo Spring Boot

## 🚀 Como Executar

### Opção 1: Via IDE
1. Abrir o projeto na sua IDE favorita
2. Executar a classe `PipeUpApplication.java`

### Opção 2: Via Maven
```bash
# No diretório do projeto
./mvnw spring-boot:run
```

### Opção 3: Via JAR
```bash
# Compilar
./mvnw clean package

# Executar
java -jar target/PipeUp-0.0.1-SNAPSHOT.jar
```

## 🌐 Acessando o Sistema

Após iniciar a aplicação, acesse:
- **URL**: http://localhost:8080
- **Redirecionamento automático** para o dashboard

## 📊 Dados de Exemplo

O sistema já vem com dados pré-carregados:
- **3 empresas** (TechCorp Solutions, InnovaTech Ltda, StartupXYZ)
- **6 espaços** de trabalho
- **7 tarefas** em diferentes status
- **5 usuários** de exemplo
- **Histórico de atividades** para demonstração

## 🎯 Funcionalidades Principais

### Dashboard
- Visualização em Kanban das tarefas
- Filtros por status
- Pesquisa instantânea
- Criação rápida de tarefas

### Detalhes da Tarefa
- Edição completa dos dados
- Calendário para datas importantes
- Histórico de atividades
- Controle de progresso
- Abas para diferentes tipos de conteúdo

### Atalhos de Teclado
- `Ctrl + N`: Nova tarefa
- `Ctrl + /`: Focar na pesquisa
- `Ctrl + S`: Salvar (na tela de detalhes)
- `ESC`: Voltar/Limpar pesquisa

## 🔄 Melhorias Implementadas

### Interface
- Design moderno e responsivo
- Animações suaves
- Tooltips informativos
- Feedback visual para ações

### Funcionalidade
- Auto-save das alterações
- Validação em tempo real
- Drag & drop funcional
- Pesquisa instantânea
- Navegação intuitiva

### Dados
- Estrutura completa do banco
- Relacionamentos funcionais
- Dados de exemplo realistas
- Validações de negócio

## 🐛 Solução de Problemas

### Erro de Conexão com Banco
- Verificar se PostgreSQL está rodando
- Confirmar credenciais no `application.properties`
- Verificar se o banco `pipeup` existe

### Porta em Uso
- Alterar porta no `application.properties`:
```properties
server.port=8081
```

### Problemas de Compilação
- Verificar versão do Java (deve ser 17+)
- Limpar e recompilar:
```bash
./mvnw clean compile
```

## 📝 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/example/pipeup/
│   │   ├── controller/     # Controladores REST e Web
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositórios de dados
│   │   ├── service/        # Lógica de negócio
│   │   └── PipeUpApplication.java
│   └── resources/
│       ├── static/
│       │   ├── css/        # Estilos
│       │   ├── js/         # JavaScript
│       │   └── img/        # Imagens
│       ├── templates/      # Templates Thymeleaf
│       ├── application.properties
│       ├── schema.sql      # Estrutura do banco
│       └── data.sql        # Dados iniciais
```

## 🎉 Status do Projeto

✅ **Projeto Totalmente Funcional**
- Todas as funcionalidades principais implementadas
- Interface moderna e responsiva
- Dados de exemplo carregados
- Navegação completa entre telas
- Validações e feedback adequados

O sistema agora é um **programa funcional completo**, não mais um "site estático"!