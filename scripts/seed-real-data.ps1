# ============================================
# Script PowerShell pour peupler la base de donnees avec de vraies donnees
# Utilise SQL direct pour une insertion rapide et efficace
# ============================================

param(
    [string]$MYSQL_ROOT_PASSWORD = $env:MYSQL_ROOT_PASSWORD,
    [string]$MYSQL_DATABASE = $env:MYSQL_DATABASE
)

# Si les variables d'environnement ne sont pas definies, lire depuis .env
if (-not $MYSQL_ROOT_PASSWORD -or -not $MYSQL_DATABASE) {
    Write-Host "Lecture des variables depuis .env..." -ForegroundColor Yellow
    if (Test-Path ".env") {
        Get-Content ".env" | ForEach-Object {
            if ($_ -match "^([^#][^=]+)=(.*)$") {
                $key = $matches[1].Trim()
                $value = $matches[2].Trim()
                Set-Variable -Name $key -Value $value -Scope Script
            }
        }
    } else {
        Write-Host "Erreur: Fichier .env non trouve. Veuillez specifier MYSQL_ROOT_PASSWORD et MYSQL_DATABASE." -ForegroundColor Red
        exit 1
    }
}

# Valeurs par defaut si non definies
if (-not $MYSQL_DATABASE) { $MYSQL_DATABASE = "si_releves" }

Write-Host "=== Peuplement de la base de donnees avec de vraies donnees ===" -ForegroundColor Green
Write-Host "Base de donnees: $MYSQL_DATABASE" -ForegroundColor Cyan

# Verifier que Docker est en cours d'execution
$mysqlContainer = docker ps --filter "name=si-releves-mysql" --format "{{.Names}}"
if (-not $mysqlContainer) {
    Write-Host "Erreur: Le conteneur MySQL n'est pas en cours d'execution." -ForegroundColor Red
    Write-Host "Veuillez demarrer les services avec: docker-compose up -d" -ForegroundColor Yellow
    exit 1
}

Write-Host "Conteneur MySQL trouve: $mysqlContainer" -ForegroundColor Green

# Chemin du script SQL
$sqlScript = Join-Path $PSScriptRoot "seed-real-data.sql"

if (-not (Test-Path $sqlScript)) {
    Write-Host "Erreur: Script SQL non trouve: $sqlScript" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Execution du script SQL..." -ForegroundColor Yellow
Write-Host "Ce script va creer:" -ForegroundColor Cyan
Write-Host "  - 10 quartiers a Rabat" -ForegroundColor White
Write-Host "  - 50 clients avec de vrais noms marocains" -ForegroundColor White
Write-Host "  - 60+ adresses realistes dans differents quartiers" -ForegroundColor White

# Executer le script SQL
try {
    $sqlContent = Get-Content $sqlScript -Raw -Encoding UTF8
    $sqlContent | docker exec -i si-releves-mysql mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "Donnees inserees avec succes!" -ForegroundColor Green
        
        # Afficher les statistiques
        Write-Host ""
        Write-Host "=== Statistiques ===" -ForegroundColor Cyan
        docker exec si-releves-mysql mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" -e "SELECT 'Clients' AS Type, COUNT(*) AS Total FROM client UNION ALL SELECT 'Adresses', COUNT(*) FROM adresse UNION ALL SELECT 'Quartiers', COUNT(*) FROM quartier;"
        
        Write-Host ""
        Write-Host "=== Exemples de donnees ===" -ForegroundColor Cyan
        docker exec si-releves-mysql mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" -e "SELECT c.id_client, c.nom, c.prenom, COUNT(a.id_adresse) AS nb_adresses FROM client c LEFT JOIN adresse a ON c.id_client = a.id_client GROUP BY c.id_client, c.nom, c.prenom LIMIT 10;"
        
        Write-Host ""
        Write-Host "=== Donnees peuplees avec succes! ===" -ForegroundColor Green
        Write-Host "Vous pouvez maintenant:" -ForegroundColor Cyan
        Write-Host "  - Acceder au Frontend: http://localhost" -ForegroundColor White
        Write-Host "  - Verifier via l'API: http://localhost:8080/api/clients" -ForegroundColor White
        Write-Host "  - Verifier via l'API: http://localhost:8080/api/adresses" -ForegroundColor White
    } else {
        Write-Host ""
        Write-Host "Erreur lors de l'execution du script SQL" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host ""
    Write-Host "Erreur: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
