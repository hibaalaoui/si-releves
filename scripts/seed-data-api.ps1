# ============================================
# Script PowerShell pour remplir les tables via l'API REST
# ============================================

$baseUrl = "http://localhost:8080/api"

Write-Host "=== Remplissage des tables via l'API REST ===" -ForegroundColor Green

# Fonction pour faire des requêtes POST
function Invoke-PostRequest {
    param(
        [string]$Url,
        [object]$Body
    )
    try {
        $jsonBody = $Body | ConvertTo-Json
        $response = Invoke-RestMethod -Uri $Url -Method Post -Body $jsonBody -ContentType "application/json"
        Write-Host "✓ Succès: $Url" -ForegroundColor Green
        return $response
    } catch {
        Write-Host "✗ Erreur: $Url - $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# 1. Créer des Quartiers
Write-Host "`n1. Création des quartiers..." -ForegroundColor Yellow
$quartiers = @(
    @{ nomQuartier = "Agdal"; ville = "Rabat" },
    @{ nomQuartier = "Hay Riad"; ville = "Rabat" },
    @{ nomQuartier = "Souissi"; ville = "Rabat" },
    @{ nomQuartier = "Hassan"; ville = "Rabat" },
    @{ nomQuartier = "Akkari"; ville = "Rabat" }
)

$quartierIds = @()
foreach ($q in $quartiers) {
    $result = Invoke-PostRequest -Url "$baseUrl/quartiers" -Body $q
    if ($result) {
        $quartierIds += $result.idQuartier
    }
}

# 2. Créer des Agents
Write-Host "`n2. Création des agents..." -ForegroundColor Yellow
$agents = @(
    @{ idAgent = "AGT001"; idQuartier = $quartierIds[0]; nom = "Bouazza"; prenom = "Karim"; telPersonnel = "0661234567"; telProfessionnel = "0537123456" },
    @{ idAgent = "AGT002"; idQuartier = $quartierIds[0]; nom = "Tazi"; prenom = "Sanae"; telPersonnel = "0662345678"; telProfessionnel = "0537234567" },
    @{ idAgent = "AGT003"; idQuartier = $quartierIds[1]; nom = "Lahlou"; prenom = "Youssef"; telPersonnel = "0663456789"; telProfessionnel = "0537345678" },
    @{ idAgent = "AGT004"; idQuartier = $quartierIds[2]; nom = "Berrada"; prenom = "Nadia"; telPersonnel = "0664567890"; telProfessionnel = "0537456789" },
    @{ idAgent = "AGT005"; idQuartier = $quartierIds[3]; nom = "Chraibi"; prenom = "Omar"; telPersonnel = "0665678901"; telProfessionnel = "0537567890" }
)

foreach ($a in $agents) {
    Invoke-PostRequest -Url "$baseUrl/agents" -Body $a
}

# 3. Créer des Clients (si l'endpoint existe)
Write-Host "`n3. Création des clients..." -ForegroundColor Yellow
$clients = @(
    @{ idClient = "CLI001"; nom = "Alaoui"; prenom = "Ahmed"; email = "ahmed.alaoui@example.com"; tel = "0612345678" },
    @{ idClient = "CLI002"; nom = "Benali"; prenom = "Fatima"; email = "fatima.benali@example.com"; tel = "0623456789" },
    @{ idClient = "CLI003"; nom = "Idrissi"; prenom = "Mohamed"; email = "mohamed.idrissi@example.com"; tel = "0634567890" }
)

foreach ($c in $clients) {
    Invoke-PostRequest -Url "$baseUrl/clients" -Body $c
}

# 4. Créer des Adresses
Write-Host "`n4. Création des adresses..." -ForegroundColor Yellow
$adresses = @(
    @{ idClient = "CLI001"; idQuartier = $quartierIds[0]; adresseComplete = "123 Avenue Mohammed V, Agdal"; typeBien = "Standard"; codePostal = "10000" },
    @{ idClient = "CLI002"; idQuartier = $quartierIds[1]; adresseComplete = "45 Rue Al Fath, Hay Riad"; typeBien = "Standard"; codePostal = "10100" },
    @{ idClient = "CLI003"; idQuartier = $quartierIds[0]; adresseComplete = "78 Boulevard Zerktouni, Agdal"; typeBien = "Immeuble"; codePostal = "10000" }
)

$adresseIds = @()
foreach ($a in $adresses) {
    $result = Invoke-PostRequest -Url "$baseUrl/adresses" -Body $a
    if ($result) {
        $adresseIds += $result.idAdresse
    }
}

# 5. Créer des Compteurs
Write-Host "`n5. Création des compteurs..." -ForegroundColor Yellow
$compteurs = @(
    @{ idAdresse = $adresseIds[0]; numeroSerie = "COMP001"; typeCompteur = "Electronique"; dateInstallation = "2024-01-15" },
    @{ idAdresse = $adresseIds[0]; numeroSerie = "COMP002"; typeCompteur = "Electronique"; dateInstallation = "2024-01-15" },
    @{ idAdresse = $adresseIds[1]; numeroSerie = "COMP003"; typeCompteur = "Mecanique"; dateInstallation = "2023-12-10" },
    @{ idAdresse = $adresseIds[2]; numeroSerie = "COMP004"; typeCompteur = "Electronique"; dateInstallation = "2024-02-20" }
)

$compteurIds = @()
foreach ($c in $compteurs) {
    $result = Invoke-PostRequest -Url "$baseUrl/compteurs" -Body $c
    if ($result) {
        $compteurIds += $result.idCompteur
    }
}

# 6. Créer des Utilisateurs
Write-Host "`n6. Création des utilisateurs..." -ForegroundColor Yellow
$utilisateurs = @(
    @{ nomUtilisateur = "admin"; email = "admin@sireleves.ma"; motDePasse = "password123"; role = "ADMIN" },
    @{ nomUtilisateur = "agent1"; email = "agent1@sireleves.ma"; motDePasse = "password123"; role = "AGENT" },
    @{ nomUtilisateur = "superviseur1"; email = "superviseur1@sireleves.ma"; motDePasse = "password123"; role = "SUPERVISEUR" }
)

foreach ($u in $utilisateurs) {
    Invoke-PostRequest -Url "$baseUrl/utilisateurs" -Body $u
}

Write-Host "`n=== Remplissage terminé ===" -ForegroundColor Green
Write-Host "`nVous pouvez maintenant:" -ForegroundColor Cyan
Write-Host "  - Accéder à Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "  - Accéder au Frontend: http://localhost" -ForegroundColor White
Write-Host "  - Vérifier les données via l'API: http://localhost:8080/api/quartiers" -ForegroundColor White
