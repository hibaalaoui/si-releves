# Guide : Comment Visualiser les Alertes

Ce guide explique comment visualiser les alertes configurées dans votre système de monitoring SI Relevés.

## 📊 Méthodes de Visualisation

### 1. Via l'Interface Prometheus (Recommandé)

**URL :** http://localhost:9090/alerts

#### Étapes :
1. Ouvrez votre navigateur et accédez à http://localhost:9090/alerts
2. Vous verrez toutes les alertes configurées avec leur état :
   - **Inactive** : L'alerte n'est pas déclenchée
   - **Pending** : L'alerte est en cours d'évaluation (pendant la période `for`)
   - **Firing** : L'alerte est active et déclenchée

#### Informations affichées :
- **Nom de l'alerte** : Ex. "BackendDown", "MySQLHighConnections"
- **État** : Inactive, Pending, ou Firing
- **Labels** : Severity (critical/warning), Service (backend/mysql/frontend)
- **Annotations** : Summary et Description
- **Dernière évaluation** : Timestamp de la dernière vérification

#### Exemple d'URLs utiles :
- **Toutes les alertes** : http://localhost:9090/alerts
- **Règles d'alertes** : http://localhost:9090/rules
- **Statut des targets** : http://localhost:9090/targets
- **Graphiques** : http://localhost:9090/graph

### 2. Via l'API Prometheus

Vous pouvez interroger l'API Prometheus pour obtenir les alertes :

```bash
# Obtenir toutes les alertes actives
curl http://localhost:9090/api/v1/alerts

# Obtenir les règles d'alertes
curl http://localhost:9090/api/v1/rules

# Obtenir les alertes avec un filtre
curl "http://localhost:9090/api/v1/alerts?active=true"
```

#### Via PowerShell :
```powershell
# Obtenir toutes les alertes
Invoke-RestMethod -Uri "http://localhost:9090/api/v1/alerts" | ConvertTo-Json

# Obtenir uniquement les alertes actives (Firing)
$alerts = Invoke-RestMethod -Uri "http://localhost:9090/api/v1/alerts"
$alerts.data.alerts | Where-Object { $_.state -eq "firing" }
```

### 3. Via Grafana

**URL :** http://localhost:3000

#### Créer un Dashboard d'Alertes :

1. **Connectez-vous à Grafana** :
   - URL : http://localhost:3000
   - Username : `admin`
   - Password : `admin` (à changer après la première connexion)

2. **Créer un nouveau Dashboard** :
   - Cliquez sur "+" → "Create" → "Dashboard"
   - Ajoutez un nouveau panel

3. **Configurer le Panel pour les Alertes** :
   - **Data source** : Prometheus
   - **Query** : Utilisez les métriques Prometheus pour visualiser les alertes
   - **Visualization** : Table, Stat, ou Alert List

#### Exemple de Requête PromQL pour Alertes :

```promql
# Compter les alertes actives par sévérité
count by (severity) (ALERTS{alertstate="firing"})

# Liste des alertes actives
ALERTS{alertstate="firing"}

# Alertes par service
count by (service) (ALERTS{alertstate="firing"})
```

### 4. Via Docker (Ligne de Commande)

```bash
# Voir les alertes via le conteneur Prometheus
docker exec si-releves-prometheus wget -qO- http://localhost:9090/api/v1/alerts

# Voir les règles d'alertes
docker exec si-releves-prometheus wget -qO- http://localhost:9090/api/v1/rules
```

## 🔔 Types d'Alertes Configurées

### Alertes Critiques (Severity: critical)
- **BackendDown** : Service backend arrêté
- **FrontendContainerDown** : Conteneur frontend arrêté
- **MySQLDown** : Service MySQL arrêté
- **PrometheusDown** : Service Prometheus arrêté

### Alertes d'Avertissement (Severity: warning)
- **BackendHighResponseTime** : Temps de réponse > 1 seconde
- **BackendHighMemoryUsage** : Mémoire JVM > 85%
- **BackendHighCPUUsage** : CPU > 80%
- **BackendDatabaseConnectionsHigh** : Connexions DB > 15
- **FrontendHighCPUUsage** : CPU frontend > 80%
- **FrontendHighMemoryUsage** : Mémoire frontend > 85%
- **MySQLHighConnections** : Connexions MySQL > 80% du max
- **MySQLSlowQueries** : Plus de 10 requêtes lentes/min
- **ContainerHighCPUUsage** : CPU conteneur > 80%
- **ContainerHighMemoryUsage** : Mémoire conteneur > 85%
- **ContainerRestarting** : Conteneur redémarre fréquemment

## 📈 Exemples de Requêtes PromQL Utiles

### Voir toutes les alertes actives :
```promql
ALERTS{alertstate="firing"}
```

### Compter les alertes par sévérité :
```promql
count by (severity) (ALERTS{alertstate="firing"})
```

### Compter les alertes par service :
```promql
count by (service) (ALERTS{alertstate="firing"})
```

### Voir les alertes critiques uniquement :
```promql
ALERTS{alertstate="firing", severity="critical"}
```

### Historique d'une alerte spécifique :
```promql
ALERTS{alertname="BackendDown"}
```

## 🛠️ Dépannage

### Les alertes ne s'affichent pas :
1. Vérifiez que Prometheus est en cours d'exécution :
   ```bash
   docker ps | grep prometheus
   ```

2. Vérifiez que les règles sont chargées :
   - Accédez à http://localhost:9090/rules
   - Vous devriez voir le groupe "application_alerts"

3. Vérifiez les logs de Prometheus :
   ```bash
   docker logs si-releves-prometheus
   ```

### Les alertes ne se déclenchent pas :
1. Vérifiez que les métriques sont collectées :
   - Accédez à http://localhost:9090/targets
   - Tous les targets doivent être "UP"

2. Testez les requêtes PromQL dans Prometheus :
   - Accédez à http://localhost:9090/graph
   - Testez les expressions des alertes

3. Vérifiez la configuration des alertes :
   ```bash
   docker exec si-releves-prometheus cat /etc/prometheus/alert_rules.yml
   ```

## 📝 Notes Importantes

- Les alertes sont évaluées toutes les 15 secondes (selon `evaluation_interval`)
- Les alertes avec `for: 5m` doivent être actives pendant 5 minutes avant de se déclencher
- Les alertes critiques (`for: 1m`) se déclenchent après 1 minute
- Les alertes sont stockées dans Prometheus mais ne sont pas envoyées automatiquement (Alertmanager non configuré)

## 🔗 Liens Utiles

- **Prometheus UI** : http://localhost:9090
- **Grafana** : http://localhost:3000
- **Documentation Prometheus** : https://prometheus.io/docs/alerting/latest/overview/
- **Documentation Grafana** : https://grafana.com/docs/grafana/latest/alerting/


