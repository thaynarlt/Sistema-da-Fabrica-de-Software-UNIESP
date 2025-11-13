# Script para Configurar Java 17 no Windows
# Execute como Administrador

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Configurar Java 17 JDK" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se está executando como Administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "ERRO: Execute este script como Administrador!" -ForegroundColor Red
    Write-Host "Botão direito > Executar como administrador" -ForegroundColor Yellow
    pause
    exit 1
}

# Verificar instalações Java existentes
Write-Host "Verificando instalações Java existentes..." -ForegroundColor Yellow

$javaPaths = @(
    "C:\Program Files\Eclipse Adoptium",
    "C:\Program Files\Java",
    "C:\Program Files\Amazon Corretto",
    "C:\Program Files (x86)\Java"
)

$jdk17Path = $null

foreach ($path in $javaPaths) {
    if (Test-Path $path) {
        Write-Host "Verificando: $path" -ForegroundColor Gray
        
        # Procurar por JDK 17
        $dirs = Get-ChildItem -Path $path -Directory -ErrorAction SilentlyContinue | Where-Object { 
            $_.Name -match 'jdk-17|jdk17|17' -and (Test-Path (Join-Path $_.FullName "bin\javac.exe"))
        }
        
        if ($dirs) {
            $jdk17Path = $dirs[0].FullName
            Write-Host "✓ JDK 17 encontrado: $jdk17Path" -ForegroundColor Green
            break
        }
    }
}

# Se não encontrou, pedir caminho manual
if (-not $jdk17Path) {
    Write-Host ""
    Write-Host "JDK 17 não encontrado automaticamente." -ForegroundColor Yellow
    Write-Host "Por favor, informe o caminho completo da instalação do JDK 17:" -ForegroundColor Yellow
    Write-Host "Exemplo: C:\Program Files\Eclipse Adoptium\jdk-17.0.9+9-hotspot" -ForegroundColor Gray
    Write-Host ""
    
    $jdk17Path = Read-Host "Caminho do JDK 17"
    
    # Validar caminho
    if (-not (Test-Path $jdk17Path)) {
        Write-Host "ERRO: Caminho não encontrado!" -ForegroundColor Red
        pause
        exit 1
    }
    
    if (-not (Test-Path (Join-Path $jdk17Path "bin\javac.exe"))) {
        Write-Host "ERRO: Caminho inválido! Não encontrou javac.exe" -ForegroundColor Red
        Write-Host "Certifique-se de que é um JDK (não JRE)" -ForegroundColor Yellow
        pause
        exit 1
    }
}

# Confirmar configuração
Write-Host ""
Write-Host "Configurando:" -ForegroundColor Cyan
Write-Host "  JAVA_HOME = $jdk17Path" -ForegroundColor White
Write-Host ""

$confirm = Read-Host "Continuar? (S/N)"
if ($confirm -ne "S" -and $confirm -ne "s") {
    Write-Host "Cancelado." -ForegroundColor Yellow
    exit 0
}

# Configurar JAVA_HOME
try {
    [System.Environment]::SetEnvironmentVariable('JAVA_HOME', $jdk17Path, 'Machine')
    Write-Host "✓ JAVA_HOME configurado" -ForegroundColor Green
} catch {
    Write-Host "ERRO ao configurar JAVA_HOME: $_" -ForegroundColor Red
    pause
    exit 1
}

# Adicionar ao PATH
try {
    $currentPath = [System.Environment]::GetEnvironmentVariable('Path', 'Machine')
    $javaBinPath = "$jdk17Path\bin"
    
    # Remover entradas Java antigas do PATH (opcional - comentado por segurança)
    # $pathEntries = $currentPath -split ';' | Where-Object { $_ -notmatch 'java' -and $_ -notmatch 'jdk' }
    # $currentPath = $pathEntries -join ';'
    
    # Adicionar Java 17 no início do PATH
    if ($currentPath -notlike "*$javaBinPath*") {
        $newPath = "$javaBinPath;$currentPath"
        [System.Environment]::SetEnvironmentVariable('Path', $newPath, 'Machine')
        Write-Host "✓ PATH atualizado (Java 17 adicionado no início)" -ForegroundColor Green
    } else {
        Write-Host "✓ Java já estava no PATH" -ForegroundColor Green
    }
} catch {
    Write-Host "ERRO ao atualizar PATH: $_" -ForegroundColor Red
    pause
    exit 1
}

# Exibir configuração
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Configuração Concluída!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "IMPORTANTE:" -ForegroundColor Yellow
Write-Host "  1. Feche TODOS os terminais abertos" -ForegroundColor White
Write-Host "  2. Abra um novo terminal" -ForegroundColor White
Write-Host "  3. Execute: java -version" -ForegroundColor White
Write-Host "  4. Execute: javac -version" -ForegroundColor White
Write-Host ""
Write-Host "Se ainda mostrar Java 8, reinicie o computador." -ForegroundColor Yellow
Write-Host ""

pause


