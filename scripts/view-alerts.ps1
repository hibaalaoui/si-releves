# ============================================
# Script PowerShell pour visualiser les alertes Prometheus
# ============================================

$prometheusUrl = "http://localhost:9090"

Write-Host "=== Visualisation des Alertes Prometheus ===" -ForegroundColor Green
Write-Host ""

# Fonction pour formater les alertes
function Format-Alert {
    param($alert)
    
    $state = $alert.state
    $name = $alert.labels.alertname
    $severity = $alert.labels.severity
    $service = $alert.labels.service
    
    $color = switch ($severity) {
        "critical" { "Red" }
        "warning" { "Yellow" }
        default { "White" }
    }
    
    Write-Host "  [$state] " -NoNewline
    Write-Host "$name" -ForegroundColor $color -NoNewline
    Write-Host " ($severity - $service)"
    
    if ($alert.annotations.summary) {
        Write-Host "    Summary: $($alert.annotations.summary)" -ForegroundColor Gray
    }
    if ($alert.annotations.description) {
        Write-Host "    Description: $($alert.annotations.description)" -ForegroundColor Gray
    }
    Write-Host ""
}

# 1. Obtenir toutes les alertes
Write-Host "1. Récupération des alertes..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$prometheusUrl/api/v1/alerts" -Method Get
    $alerts = $response.data.alerts
    
    Write-Host "   Total d'alertes: $($alerts.Count)" -ForegroundColor Green
    Write-Host ""
    
    # 2. Filtrer les alertes actives (Firing)
    $firingAlerts = $alerts | Where-Object { $_.state -eq "firing" }
    $pendingAlerts = $alerts | Where-Object { $_.state -eq "pending" }
    $inactiveAlerts = $alerts | Where-Object { $_.state -eq "inactive" }
    
    # 3. Afficher les statistiques
    Write-Host "=== Statistiques ===" -ForegroundColor Yellow
    Write-Host "  Alertes Firing (actives): " -NoNewline
    Write-Host "$($firingAlerts.Count)" -ForegroundColor $(if ($firingAlerts.Count -gt 0) { "Red" } else { "Green" })
    Write-Host "  Alertes Pending (en attente): " -NoNewline
    Write-Host "$($pendingAlerts.Count)" -ForegroundColor $(if ($pendingAlerts.Count -gt 0) { "Yellow" } else { "Green" })
    Write-Host "  Alertes Inactive (inactives): $($inactiveAlerts.Count)" -ForegroundColor Green
    Write-Host ""
    
    # 4. Afficher les alertes actives
    if ($firingAlerts.Count -gt 0) {
        Write-Host "=== Alertes Actives (Firing) ===" -ForegroundColor Red
        foreach ($alert in $firingAlerts) {
            Format-Alert -alert $alert
        }
    } else {
        Write-Host "Aucune alerte active" -ForegroundColor Green
        Write-Host ""
    }
    
    # 5. Afficher les alertes en attente
    if ($pendingAlerts.Count -gt 0) {
        Write-Host "=== Alertes en Attente (Pending) ===" -ForegroundColor Yellow
        foreach ($alert in $pendingAlerts) {
            Format-Alert -alert $alert
        }
    }
    
    # 6. Grouper par sévérité
    Write-Host "=== Répartition par Sévérité ===" -ForegroundColor Cyan
    $bySeverity = $firingAlerts | Group-Object -Property { $_.labels.severity }
    foreach ($group in $bySeverity) {
        $severity = $group.Name
        $count = $group.Count
        $color = switch ($severity) {
            "critical" { "Red" }
            "warning" { "Yellow" }
            default { "White" }
        }
        Write-Host "  $severity : $count alerte(s)" -ForegroundColor $color
    }
    Write-Host ""
    
    # 7. Grouper par service
    Write-Host "=== Répartition par Service ===" -ForegroundColor Cyan
    $byService = $firingAlerts | Group-Object -Property { $_.labels.service }
    foreach ($group in $byService) {
        $service = $group.Name
        $count = $group.Count
        Write-Host "  $service : $count alerte(s)"
    }
    Write-Host ""
    
    # 8. Liens utiles
    Write-Host "=== Liens Utiles ===" -ForegroundColor Cyan
    Write-Host "  Prometheus UI (Alertes): $prometheusUrl/alerts" -ForegroundColor White
    Write-Host "  Prometheus UI (Règles): $prometheusUrl/rules" -ForegroundColor White
    Write-Host "  Grafana: http://localhost:3000" -ForegroundColor White
    Write-Host ""
    
} catch {
    Write-Host "Erreur lors de la recuperation des alertes: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Verifiez que Prometheus est en cours d'execution:" -ForegroundColor Yellow
    Write-Host "  docker ps | grep prometheus" -ForegroundColor White
    exit 1
}

