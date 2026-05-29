@echo off
echo ========================================
echo       PipeUp - Sistema de Tarefas
echo ========================================
echo.

echo Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado!
    echo Instale Java 17 ou superior
    pause
    exit /b 1
)

echo Java encontrado!
echo.

echo Verificando PostgreSQL...
pg_isready >nul 2>&1
if %errorlevel% neq 0 (
    echo AVISO: PostgreSQL pode nao estar rodando
    echo Certifique-se de que o PostgreSQL esta ativo
    echo.
)

echo Compilando projeto...
call mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao
    pause
    exit /b 1
)

echo.
echo Iniciando aplicacao...
echo Acesse: http://localhost:8080
echo.
echo Pressione Ctrl+C para parar
echo.

call mvnw.cmd spring-boot:run