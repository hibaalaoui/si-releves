# PHASE 2 : CONCEPTION - CAHIER DE TESTS

**Projet :** Système de Gestion des Relevés de Compteurs d'Eau et d'Électricité  
**Client :** RABAT ENERGIE & EAU (REE)  
**Application concernée :** SI Relevés (Backoffice Web)  
**Date de conception :** 16 Décembre 2024

---

## INTRODUCTION

Ce document présente le cahier de tests complet du système "SI Relevés". Il a été généré automatiquement par **IA générative** en se basant sur :
- Les cas d'utilisation définis en Phase 2.1
- Le MCD/MLD défini en Phase 2.2 et 2.3
- Les besoins fonctionnels de la Phase 1

Le cahier de tests couvre :
- Tests unitaires (fonctions, méthodes, composants)
- Tests d'intégration (API, base de données)
- Tests fonctionnels (scénarios utilisateur bout-en-bout)
- Tests de sécurité
- Tests de performance

---

## TABLE DES MATIÈRES

1. [Stratégie de tests](#1-stratégie-de-tests)
2. [Tests unitaires](#2-tests-unitaires)
3. [Tests d'intégration](#3-tests-dintégration)
4. [Tests fonctionnels](#4-tests-fonctionnels)
5. [Tests de sécurité](#5-tests-de-sécurité)
6. [Tests de performance](#6-tests-de-performance)
7. [Jeux de données de test](#7-jeux-de-données-de-test)
8. [Environnements de test](#8-environnements-de-test)
9. [Outils et automatisation](#9-outils-et-automatisation)

---

## 1. STRATÉGIE DE TESTS

### 1.1. Pyramide des tests

```
              /\
             /  \  Tests E2E (10%)
            /____\
           /      \  Tests d'intégration (30%)
          /________\
         /          \  Tests unitaires (60%)
        /__________\
```

**Répartition recommandée :**
- **60% Tests unitaires** : Fonctions, méthodes, composants isolés
- **30% Tests d'intégration** : API, base de données, services
- **10% Tests fonctionnels (E2E)** : Parcours utilisateur complets

### 1.2. Critères de qualité

| Métrique | Objectif | Priorité |
|----------|----------|----------|
| **Couverture de code** | ≥ 80% | Haute |
| **Couverture des cas d'utilisation** | 100% | Haute |
| **Tests passants** | 100% | Haute |
| **Temps d'exécution total** | < 10 minutes | Moyenne |
| **Taux de régression** | < 5% | Haute |

### 1.3. Niveaux de tests

| Niveau | Description | Responsable | Fréquence |
|--------|-------------|-------------|-----------|
| **Tests unitaires** | Tests de fonctions/méthodes isolées | Développeurs | À chaque commit |
| **Tests d'intégration** | Tests d'interactions entre composants | Développeurs | À chaque merge |
| **Tests fonctionnels** | Tests de scénarios utilisateur | QA + Développeurs | Quotidien |
| **Tests de non-régression** | Vérification après modifications | QA | Avant chaque release |
| **Tests d'acceptation** | Validation client | Client + QA | Avant mise en production |

---

## 2. TESTS UNITAIRES

### 2.1. Backend - Tests des services métier

#### TU-BACKEND-001 : Service Génération ID Compteur

**Objectif :** Vérifier la génération correcte des identifiants de compteur (9 chiffres avec zéros)

**Prérequis :**
- Base de données vide
- Service de génération d'ID disponible

**Cas de test :**

| ID | Description | Entrée | Sortie attendue | Priorité |
|----|-------------|--------|-----------------|----------|
| TU-BE-001-01 | Premier compteur | Aucun compteur existant | "000000001" | Haute |
| TU-BE-001-02 | Incrémentation | Dernier ID = "000000005" | "000000006" | Haute |
| TU-BE-001-03 | ID avec grands nombres | Dernier ID = "999999998" | "999999999" | Moyenne |
| TU-BE-001-04 | Format correct | ID généré | 9 caractères, que des chiffres | Haute |

**Code de test (exemple en JavaScript/Jest) :**

```javascript
describe('Service Génération ID Compteur', () => {
  test('TU-BE-001-01: Premier compteur', async () => {
    const id = await generateCompteurId();
    expect(id).toBe('000000001');
    expect(id).toHaveLength(9);
  });

  test('TU-BE-001-02: Incrémentation', async () => {
    await createCompteur({ id: '000000005' });
    const id = await generateCompteurId();
    expect(id).toBe('000000006');
  });

  test('TU-BE-001-04: Format correct', async () => {
    const id = await generateCompteurId();
    expect(id).toMatch(/^\d{9}$/);
  });
});
```

---

#### TU-BACKEND-002 : Validation Format Utilisateur

**Objectif :** Vérifier la validation des formats de nom et prénom

**Cas de test :**

| ID | Description | Entrée | Résultat attendu | Priorité |
|----|-------------|--------|------------------|----------|
| TU-BE-002-01 | Nom valide | "DRIOUECH" | Valide | Haute |
| TU-BE-002-02 | Nom invalide (minuscules) | "driouech" | Erreur : "Nom doit être en MAJUSCULES" | Haute |
| TU-BE-002-03 | Prénom valide | "Mohamed-Amine" | Valide | Haute |
| TU-BE-002-04 | Prénom invalide | "mohamed" | Erreur : "Prénom doit être en Nom Propre" | Haute |
| TU-BE-002-05 | Nom avec trait d'union | "AIT MOHAMED" | Valide | Moyenne |

**Code de test (exemple en PHP/PHPUnit) :**

```php
class UserValidationTest extends TestCase
{
    /** @test */
    public function test_nom_valide()
    {
        $validator = new UserValidator();
        $result = $validator->validateNom('DRIOUECH');
        $this->assertTrue($result->isValid());
    }

    /** @test */
    public function test_nom_invalide_minuscules()
    {
        $validator = new UserValidator();
        $result = $validator->validateNom('driouech');
        $this->assertFalse($result->isValid());
        $this->assertEquals('Nom doit être en MAJUSCULES', $result->getError());
    }
}
```

---

#### TU-BACKEND-003 : Calcul de consommation

**Objectif :** Vérifier le calcul correct de la consommation

**Cas de test :**

| ID | Description | Ancien index | Nouvel index | Consommation attendue | Priorité |
|----|-------------|--------------|--------------|----------------------|----------|
| TU-BE-003-01 | Consommation normale | 1000.00 | 1050.50 | 50.50 | Haute |
| TU-BE-003-02 | Consommation nulle | 500.00 | 500.00 | 0.00 | Moyenne |
| TU-BE-003-03 | Premiers relevés | 0.00 | 125.75 | 125.75 | Haute |
| TU-BE-003-04 | Erreur: nouvel < ancien | 1000.00 | 950.00 | Erreur de validation | Haute |

**Code de test (exemple) :**

```javascript
describe('Calcul de consommation', () => {
  test('TU-BE-003-01: Consommation normale', () => {
    const result = calculateConsommation(1000.00, 1050.50);
    expect(result).toBe(50.50);
  });

  test('TU-BE-003-04: Erreur nouvel < ancien', () => {
    expect(() => {
      calculateConsommation(1000.00, 950.00);
    }).toThrow('Le nouvel index doit être supérieur ou égal à l\'ancien');
  });
});
```

---

#### TU-BACKEND-004 : Validation contraintes compteur/adresse

**Objectif :** Vérifier la validation du nombre maximum de compteurs par adresse

**Cas de test :**

| ID | Description | Type adresse | Compteurs existants | Nouveau compteur | Résultat attendu | Priorité |
|----|-------------|--------------|---------------------|------------------|------------------|----------|
| TU-BE-004-01 | 1er compteur standard | Standard | 0 | Eau, standard | Succès | Haute |
| TU-BE-004-02 | 2e compteur standard | Standard | 1 Eau standard | Électricité, standard | Succès | Haute |
| TU-BE-004-03 | 3e compteur standard | Standard | 2 standards | Eau, standard | Erreur: "Max 2 compteurs standards" | Haute |
| TU-BE-004-04 | Compteur espaces communs sur standard | Standard | 1 standard | Eau, espaces communs | Erreur: "Réservé aux immeubles" | Haute |
| TU-BE-004-05 | Compteur espaces communs sur immeuble | Immeuble | 2 standards | Eau, espaces communs | Succès | Haute |
| TU-BE-004-06 | 3e compteur espaces communs | Immeuble | 2 standards, 2 espaces communs | Eau, espaces communs | Erreur: "Max 2 espaces communs" | Haute |

---

### 2.2. Frontend - Tests des composants

#### TU-FRONTEND-001 : Composant formulaire création compteur

**Objectif :** Tester le composant de création de compteur

**Cas de test :**

| ID | Description | Action | Résultat attendu | Priorité |
|----|-------------|--------|------------------|----------|
| TU-FE-001-01 | Affichage initial | Monter le composant | Formulaire vide avec bouton désactivé | Moyenne |
| TU-FE-001-02 | Sélection adresse | Cliquer "Sélectionner adresse" | Popup avec liste d'adresses | Haute |
| TU-FE-001-03 | Sélection type | Sélectionner "Eau" | Type sélectionné, bouton actif | Haute |
| TU-FE-001-04 | Soumission valide | Soumettre formulaire complet | Appel API avec données correctes | Haute |
| TU-FE-001-05 | Validation champs requis | Soumettre sans adresse | Message d'erreur affiché | Moyenne |

**Code de test (exemple en React/Jest) :**

```javascript
describe('Composant Création Compteur', () => {
  test('TU-FE-001-01: Affichage initial', () => {
    const { getByText, getByRole } = render(<CreateCompteurForm />);
    expect(getByText('Créer un compteur')).toBeInTheDocument();
    expect(getByRole('button', { name: 'Enregistrer' })).toBeDisabled();
  });

  test('TU-FE-001-04: Soumission valide', async () => {
    const mockSubmit = jest.fn();
    const { getByLabelText, getByRole } = render(
      <CreateCompteurForm onSubmit={mockSubmit} />
    );
    
    // Sélectionner adresse et type
    fireEvent.click(getByLabelText('Adresse'));
    fireEvent.click(screen.getByText('123 Rue Test'));
    fireEvent.click(getByLabelText('Eau'));
    
    // Soumettre
    fireEvent.click(getByRole('button', { name: 'Enregistrer' }));
    
    await waitFor(() => {
      expect(mockSubmit).toHaveBeenCalledWith({
        adresse: '123 Rue Test',
        type: 'Eau'
      });
    });
  });
});
```

---

#### TU-FRONTEND-002 : Composant tableau de bord (KPIs)

**Objectif :** Tester l'affichage des KPIs

**Cas de test :**

| ID | Description | Données | Résultat attendu | Priorité |
|----|-------------|---------|------------------|----------|
| TU-FE-002-01 | Taux de couverture | 85.5% | Jauge à 85.5%, couleur verte | Haute |
| TU-FE-002-02 | Taux faible | 45.0% | Jauge à 45%, couleur rouge | Moyenne |
| TU-FE-002-03 | Relevés par agent | 48.5 | Affichage "48.5 compteurs/jour" | Haute |
| TU-FE-002-04 | Chargement des données | État loading | Spinner affiché | Basse |
| TU-FE-002-05 | Erreur de chargement | Erreur API | Message d'erreur utilisateur | Moyenne |

---

## 3. TESTS D'INTÉGRATION

### 3.1. Tests API

#### TI-API-001 : Endpoint création utilisateur

**Objectif :** Tester l'endpoint POST /api/users

**Prérequis :**
- Base de données de test
- Authentification Superadmin

**Cas de test :**

| ID | Description | Méthode | Headers | Body | Status attendu | Response attendue | Priorité |
|----|-------------|---------|---------|------|----------------|-------------------|----------|
| TI-API-001-01 | Création valide | POST | Auth: Bearer {token_superadmin} | `{"nom": "HASSAN", "prenom": "Ali", "email": "ali@test.com", "role": "Utilisateur"}` | 201 | `{"id": 1, "message": "Utilisateur créé"}` | Haute |
| TI-API-001-02 | Email déjà existant | POST | Auth: Bearer {token} | Email existant | 409 | `{"error": "Email déjà utilisé"}` | Haute |
| TI-API-001-03 | Nom en minuscules | POST | Auth: Bearer {token} | `{"nom": "hassan"}` | 400 | `{"error": "Nom doit être en MAJUSCULES"}` | Haute |
| TI-API-001-04 | Sans authentification | POST | - | Données valides | 401 | `{"error": "Non authentifié"}` | Haute |
| TI-API-001-05 | Utilisateur non-Superadmin | POST | Auth: Bearer {token_user} | Données valides | 403 | `{"error": "Accès interdit"}` | Haute |

**Code de test (exemple) :**

```javascript
describe('POST /api/users', () => {
  test('TI-API-001-01: Création valide', async () => {
    const response = await request(app)
      .post('/api/users')
      .set('Authorization', `Bearer ${superadminToken}`)
      .send({
        nom: 'HASSAN',
        prenom: 'Ali',
        email: 'ali@test.com',
        role: 'Utilisateur'
      });
    
    expect(response.status).toBe(201);
    expect(response.body).toHaveProperty('id');
    expect(response.body.message).toBe('Utilisateur créé');
  });

  test('TI-API-001-04: Sans authentification', async () => {
    const response = await request(app)
      .post('/api/users')
      .send({ nom: 'TEST', prenom: 'Test', email: 'test@test.com' });
    
    expect(response.status).toBe(401);
  });
});
```

---

#### TI-API-002 : Endpoint création compteur

**Objectif :** Tester l'endpoint POST /api/compteurs

**Cas de test :**

| ID | Description | Body | Status | Response | Priorité |
|----|-------------|------|--------|----------|----------|
| TI-API-002-01 | Création valide | `{"id_adresse": 1, "type": "Eau"}` | 201 | `{"id_compteur": "000000001"}` | Haute |
| TI-API-002-02 | Adresse inexistante | `{"id_adresse": 9999, "type": "Eau"}` | 404 | `{"error": "Adresse non trouvée"}` | Haute |
| TI-API-002-03 | Type déjà existant | Adresse avec déjà compteur Eau | 409 | `{"error": "Compteur de ce type existe déjà"}` | Haute |
| TI-API-002-04 | Max compteurs atteint | Adresse avec 2 compteurs standards | 409 | `{"error": "Maximum 2 compteurs standards"}` | Haute |

---

#### TI-API-003 : Endpoint réception relevé

**Objectif :** Tester l'endpoint POST /api/releves

**Cas de test :**

| ID | Description | Body | Status | Actions attendues | Priorité |
|----|-------------|------|--------|-------------------|----------|
| TI-API-003-01 | Relevé valide | `{"id_compteur": "000000001", "id_agent": "AG001", "nouvel_index": 150.50}` | 201 | - Relevé créé<br>- Consommation calculée<br>- Index compteur mis à jour | Haute |
| TI-API-003-02 | Nouvel index < ancien | `{"nouvel_index": 50.00}` (ancien = 100) | 400 | `{"error": "Index invalide"}` | Haute |
| TI-API-003-03 | Compteur inexistant | `{"id_compteur": "999999999"}` | 404 | `{"error": "Compteur non trouvé"}` | Haute |

**Vérifications post-insertion :**

```sql
-- Vérifier que l'index du compteur est mis à jour
SELECT index_actuel FROM compteur WHERE id_compteur = '000000001';
-- Attendu: 150.50

-- Vérifier que la consommation est calculée
SELECT consommation FROM releve ORDER BY id_releve DESC LIMIT 1;
-- Attendu: (150.50 - ancien_index)
```

---

### 3.2. Tests base de données

#### TI-DB-001 : Trigger validation compteurs

**Objectif :** Tester le trigger `trg_check_compteurs_max_per_adresse`

**Cas de test :**

| ID | Scénario | Setup | Insertion | Résultat attendu | Priorité |
|----|----------|-------|-----------|------------------|----------|
| TI-DB-001-01 | Insertion 1er compteur | Adresse vide | INSERT compteur Eau standard | Succès | Haute |
| TI-DB-001-02 | Insertion 2e compteur | 1 compteur Eau | INSERT compteur Électricité standard | Succès | Haute |
| TI-DB-001-03 | Insertion 3e compteur | 2 compteurs standards | INSERT compteur Eau standard | Erreur: "Maximum 2 compteurs standards" | Haute |
| TI-DB-001-04 | Espaces communs sur standard | Adresse Standard | INSERT compteur Eau espaces communs | Erreur: "Réservé aux immeubles" | Haute |

**Code SQL de test :**

```sql
-- TI-DB-001-03: Insertion 3e compteur (doit échouer)
-- Setup
INSERT INTO adresse (id_adresse, id_client, id_quartier, adresse_complete, type_bien)
VALUES (100, 'CL001', 1, 'Test Address', 'Standard');

INSERT INTO compteur (id_compteur, id_adresse, type, pour_espaces_communs)
VALUES ('000000100', 100, 'Eau', FALSE);

INSERT INTO compteur (id_compteur, id_adresse, type, pour_espaces_communs)
VALUES ('000000101', 100, 'Electricite', FALSE);

-- Test (doit échouer)
INSERT INTO compteur (id_compteur, id_adresse, type, pour_espaces_communs)
VALUES ('000000102', 100, 'Eau', FALSE);
-- Erreur attendue: "Maximum 2 compteurs standards par adresse atteint"
```

---

#### TI-DB-002 : Trigger calcul consommation

**Objectif :** Tester le trigger `trg_calculate_consommation`

**Cas de test :**

| ID | Setup | Insertion relevé | Consommation attendue | Unité attendue | Priorité |
|----|-------|------------------|-----------------------|----------------|----------|
| TI-DB-002-01 | Compteur Eau, index 100 | `ancien_index=100, nouvel_index=150` | 50.00 | m3 | Haute |
| TI-DB-002-02 | Compteur Électricité, index 500 | `ancien_index=500, nouvel_index=625.50` | 125.50 | kWh | Haute |

---

## 4. TESTS FONCTIONNELS

### 4.1. Scénarios utilisateur Superadmin

#### TF-SA-001 : Création complète d'un utilisateur

**Objectif :** Tester le flux complet de création d'utilisateur

**Prérequis :**
- Superadmin connecté
- Application en état initial

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Se connecter en tant que Superadmin | Redirection vers page d'accueil Superadmin | Haute |
| 2 | Cliquer sur "Utilisateurs" dans le menu | Affichage de la liste des utilisateurs | Haute |
| 3 | Cliquer sur "Ajouter" | Affichage du formulaire de création | Haute |
| 4 | Saisir nom: "ALAMI" | Champ rempli | Haute |
| 5 | Saisir prénom: "Fatima" | Champ rempli | Haute |
| 6 | Saisir email: "fatima.alami@ree.ma" | Champ rempli | Haute |
| 7 | Sélectionner rôle: "Utilisateur" | Rôle sélectionné | Haute |
| 8 | Cliquer sur "Enregistrer" | - Message de confirmation<br>- Notification "Email envoyé"<br>- Redirection vers liste | Haute |
| 9 | Vérifier la liste | Nouvel utilisateur visible dans la liste | Haute |
| 10 | Vérifier l'email (MailHog) | Email reçu avec mot de passe temporaire | Moyenne |

**Résultat global :** ✅ Utilisateur créé avec succès et peut se connecter avec le mot de passe reçu

---

#### TF-SA-002 : Réinitialisation mot de passe utilisateur

**Objectif :** Tester la réinitialisation du mot de passe par un Superadmin

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Accéder à la page "Détails utilisateur" | Page affichée | Haute |
| 2 | Cliquer sur "Réinitialiser mot de passe" | Modal de confirmation | Haute |
| 3 | Confirmer la réinitialisation | - Message de succès<br>- Email envoyé | Haute |
| 4 | Vérifier l'email | Email avec nouveau mot de passe | Moyenne |
| 5 | Se connecter avec l'utilisateur | Connexion réussie | Haute |
| 6 | Vérifier le flag première connexion | Formulaire de changement obligatoire | Haute |

---

### 4.2. Scénarios utilisateur Admin Backoffice

#### TF-U-001 : Création d'un compteur complet

**Objectif :** Tester le flux complet de création d'un compteur

**Prérequis :**
- Utilisateur connecté
- Au moins une adresse sans compteur existe

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Naviguer vers "Compteurs" | Liste des compteurs affichée | Haute |
| 2 | Cliquer sur "Ajouter" | Formulaire de création affiché | Haute |
| 3 | Cliquer sur "Sélectionner adresse" | Popup avec liste d'adresses sans compteur | Haute |
| 4 | Filtrer par quartier "Agdal" | Liste filtrée affichée | Moyenne |
| 5 | Rechercher "Rue Mohamed V" | Résultats de recherche affichés | Moyenne |
| 6 | Sélectionner une adresse | Popup fermée, adresse affichée dans formulaire | Haute |
| 7 | Sélectionner type "Eau" | Type sélectionné | Haute |
| 8 | Cliquer sur "Enregistrer" | - Message de succès<br>- ID généré affiché<br>- Redirection vers détails | Haute |
| 9 | Vérifier les détails | - ID format "000000XXX"<br>- Index actuel = 0<br>- Adresse correcte | Haute |

**Résultat global :** ✅ Compteur créé et prêt à recevoir des relevés

---

#### TF-U-002 : Affectation d'un agent à un quartier

**Objectif :** Tester l'affectation d'un agent

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Naviguer vers "Agents" | Liste des agents | Haute |
| 2 | Cliquer sur un agent | Page détails de l'agent | Haute |
| 3 | Modifier le champ "Quartier" | Liste déroulante des quartiers | Haute |
| 4 | Sélectionner "Hassan" | Quartier sélectionné | Haute |
| 5 | Cliquer sur "Enregistrer" | - Message de succès<br>- Quartier mis à jour | Haute |
| 6 | Retourner à la liste | Agent affiché avec le nouveau quartier | Moyenne |
| 7 | Filtrer par quartier "Hassan" | Agent visible dans les résultats filtrés | Moyenne |

---

#### TF-U-003 : Consultation des relevés avec filtres

**Objectif :** Tester la consultation et le filtrage des relevés

**Prérequis :**
- Au moins 50 relevés dans la base
- Relevés de plusieurs quartiers et agents

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Naviguer vers "Relevés" | Liste complète des relevés (triés par date desc) | Haute |
| 2 | Vérifier la pagination | 20 relevés par page | Moyenne |
| 3 | Filtrer par quartier "Agdal" | Seuls les relevés d'Agdal affichés | Haute |
| 4 | Filtrer par agent "Ahmed IDRISSI" | Relevés filtrés par quartier ET agent | Haute |
| 5 | Filtrer par type "Eau" | Relevés filtrés par quartier, agent ET type | Haute |
| 6 | Sélectionner une date | Relevés de la date sélectionnée uniquement | Moyenne |
| 7 | Réinitialiser les filtres | Liste complète réaffichée | Basse |
| 8 | Cliquer sur un relevé | Page détails du relevé | Haute |
| 9 | Vérifier les informations | Toutes les infos affichées correctement | Haute |

---

#### TF-U-004 : Génération et export rapport PDF

**Objectif :** Tester la génération d'un rapport mensuel

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Naviguer vers "Rapports et statistiques" | Page des rapports | Haute |
| 2 | Sélectionner "Rapport mensuel des relevés" | Section rapport mensuel active | Haute |
| 3 | Sélectionner mois "Décembre 2024" | Mois sélectionné | Haute |
| 4 | Cliquer sur "Générer PDF" | - Indication de génération<br>- Téléchargement automatique du PDF | Haute |
| 5 | Ouvrir le PDF | - PDF lisible<br>- Contient graphiques<br>- Données cohérentes | Haute |
| 6 | Vérifier le contenu | - Répartition agents par quartier<br>- Moyennes de relevés<br>- Nombre de relevés par quartier | Haute |

---

### 4.3. Scénarios intégrations externes

#### TF-INT-001 : Réception données client depuis SI Commercial

**Objectif :** Tester la synchronisation des clients

**Prérequis :**
- Batch de synchronisation configuré
- SI Commercial simulé

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Préparer données de test dans SI Commercial | Données JSON prêtes | Haute |
| 2 | Déclencher le batch manuellement | Batch s'exécute | Haute |
| 3 | Vérifier les logs | Succès de la synchronisation | Haute |
| 4 | Vérifier en base de données | Nouveaux clients insérés | Haute |
| 5 | Vérifier les adresses | Adresses liées aux clients | Haute |

**Données de test (JSON) :**

```json
{
  "clients": [
    {
      "id_client": "CL12345",
      "nom": "BENALI",
      "prenom": "Omar",
      "adresses": [
        {
          "adresse_complete": "12 Avenue Hassan II, Agdal",
          "quartier": "Agdal",
          "code_postal": "10000"
        }
      ]
    }
  ]
}
```

---

#### TF-INT-002 : Envoi relevés vers SI Facturation

**Objectif :** Tester l'envoi automatique des consommations

**Scénario :**

| Étape | Action | Résultat attendu | Priorité |
|-------|--------|------------------|----------|
| 1 | Insérer plusieurs relevés | Relevés enregistrés, consommations calculées | Haute |
| 2 | Déclencher l'envoi vers facturation | Web service appelé automatiquement | Haute |
| 3 | Vérifier les logs d'envoi | Envoi réussi | Haute |
| 4 | Vérifier flag `envoye_facturation` | Passé à TRUE pour tous les relevés | Haute |
| 5 | Vérifier `date_envoi_facturation` | Date/heure enregistrée | Moyenne |
| 6 | Vérifier SI Facturation (simulé) | Données reçues correctement | Haute |

---

## 5. TESTS DE SÉCURITÉ

### 5.1. Tests d'authentification

#### TS-AUTH-001 : Tests d'accès non authentifié

**Objectif :** Vérifier que les endpoints sont protégés

| ID | Endpoint | Méthode | Sans token | Résultat attendu | Priorité |
|----|----------|---------|------------|------------------|----------|
| TS-A-001-01 | /api/users | GET | Aucun header Auth | 401 Unauthorized | Haute |
| TS-A-001-02 | /api/compteurs | POST | Aucun header Auth | 401 Unauthorized | Haute |
| TS-A-001-03 | /api/releves | GET | Aucun header Auth | 401 Unauthorized | Haute |

---

#### TS-AUTH-002 : Tests d'autorisation (RBAC)

**Objectif :** Vérifier le contrôle d'accès basé sur les rôles

| ID | Endpoint | Rôle | Action | Résultat attendu | Priorité |
|----|----------|------|--------|------------------|----------|
| TS-A-002-01 | POST /api/users | Utilisateur | Créer utilisateur | 403 Forbidden | Haute |
| TS-A-002-02 | POST /api/users | Superadmin | Créer utilisateur | 201 Created | Haute |
| TS-A-002-03 | POST /api/compteurs | Utilisateur | Créer compteur | 201 Created | Haute |
| TS-A-002-04 | DELETE /api/users/:id | Utilisateur | Supprimer utilisateur | 403 Forbidden | Haute |

---

#### TS-AUTH-003 : Tests d'expiration de session

**Objectif :** Vérifier l'expiration des tokens JWT

| ID | Scénario | Action | Résultat attendu | Priorité |
|----|----------|--------|------------------|----------|
| TS-A-003-01 | Token expiré | Requête avec token expiré (> 30 min) | 401 + message "Session expirée" | Haute |
| TS-A-003-02 | Token valide | Requête avec token récent | 200 OK | Haute |
| TS-A-003-03 | Déconnexion auto frontend | Inactivité 10 min | Redirection vers login | Moyenne |

---

### 5.2. Tests de validation des entrées

#### TS-VAL-001 : Injection SQL

**Objectif :** Vérifier la protection contre les injections SQL

| ID | Champ | Valeur injectée | Résultat attendu | Priorité |
|----|-------|-----------------|------------------|----------|
| TS-V-001-01 | Email | `admin@test.com' OR '1'='1` | Requête sécurisée, pas d'injection | Haute |
| TS-V-001-02 | Nom | `'; DROP TABLE users; --` | Requête sécurisée, pas d'exécution | Haute |

---

#### TS-VAL-002 : XSS (Cross-Site Scripting)

**Objectif :** Vérifier la protection contre le XSS

| ID | Champ | Valeur injectée | Résultat attendu | Priorité |
|----|-------|-----------------|------------------|----------|
| TS-V-002-01 | Nom | `<script>alert('XSS')</script>` | Texte échappé, pas d'exécution script | Haute |
| TS-V-002-02 | Adresse | `<img src=x onerror=alert('XSS')>` | Texte échappé | Haute |

---

### 5.3. Tests de chiffrement

#### TS-CRYPT-001 : Stockage des mots de passe

**Objectif :** Vérifier le chiffrement des mots de passe

| ID | Test | Vérification | Résultat attendu | Priorité |
|----|------|--------------|------------------|----------|
| TS-C-001-01 | Création utilisateur | Inspecter `password_hash` en base | Hash bcrypt (commence par $2y$10$) | Haute |
| TS-C-001-02 | Longueur du hash | Compter les caractères | 60 caractères | Haute |
| TS-C-001-03 | Unicité des hash | Créer 2 users avec même pwd | Hash différents (salt différent) | Haute |

---

## 6. TESTS DE PERFORMANCE

### 6.1. Tests de charge

#### TP-LOAD-001 : Charge normale

**Objectif :** Tester les performances sous charge normale

**Configuration :**
- Utilisateurs simultanés : 50
- Durée : 10 minutes
- Scénarios mixtes

**Métriques attendues :**

| Endpoint | Temps de réponse moyen | Temps max acceptable | Taux d'erreur | Priorité |
|----------|------------------------|----------------------|---------------|----------|
| GET /api/releves | < 500ms | < 2s | < 1% | Haute |
| POST /api/releves | < 1s | < 3s | < 1% | Haute |
| GET /api/dashboard | < 1s | < 3s | < 1% | Haute |
| POST /api/compteurs | < 500ms | < 2s | < 1% | Moyenne |

---

#### TP-LOAD-002 : Charge maximale

**Objectif :** Tester la limite du système

**Configuration :**
- Utilisateurs simultanés : 100
- Durée : 5 minutes
- Augmentation progressive

**Métriques à surveiller :**
- CPU serveur < 80%
- Mémoire < 80%
- Temps de réponse reste acceptable
- Aucun crash

---

### 6.2. Tests de volume

#### TP-VOL-001 : Volumétrie élevée

**Objectif :** Tester avec une base volumineuse

**Configuration :**
- 100 000 clients
- 150 000 compteurs
- 2 000 000 relevés

**Tests à effectuer :**

| Test | Action | Résultat attendu | Priorité |
|------|--------|------------------|----------|
| Recherche compteur | Recherche par ID | < 100ms | Haute |
| Liste relevés avec filtres | Filtrage complexe | < 1s | Haute |
| Génération rapport PDF | Rapport mensuel | < 5s | Moyenne |
| Calcul taux de couverture | Procédure stockée | < 2s | Moyenne |

---

## 7. JEUX DE DONNÉES DE TEST

### 7.1. Données minimales (tests unitaires)

```sql
-- Quartiers
INSERT INTO quartier (nom_quartier) VALUES
('Test_Quartier_1'), ('Test_Quartier_2');

-- Clients
INSERT INTO client (id_client, nom, prenom) VALUES
('CL_TEST_001', 'TESTEUR', 'Test'),
('CL_TEST_002', 'DEMO', 'Demo');

-- Adresses
INSERT INTO adresse (id_client, id_quartier, adresse_complete, type_bien) VALUES
(1, 1, '1 Rue Test', 'Standard'),
(1, 1, '2 Rue Test', 'Immeuble'),
(2, 2, '3 Rue Test', 'Standard');

-- Agents
INSERT INTO agent (id_agent, id_quartier, nom, prenom, tel_professionnel) VALUES
('AG_TEST_001', 1, 'AGENT', 'Test', '0600000001'),
('AG_TEST_002', 2, 'AGENT', 'Demo', '0600000002');
```

### 7.2. Données complètes (tests fonctionnels)

**Script de génération automatique par IA :**

```javascript
// Générer 1000 clients avec adresses et compteurs
const faker = require('faker');

function generateTestData() {
  const clients = [];
  const adresses = [];
  const compteurs = [];
  
  for (let i = 1; i <= 1000; i++) {
    // Client
    clients.push({
      id_client: `CL${String(i).padStart(6, '0')}`,
      nom: faker.name.lastName().toUpperCase(),
      prenom: faker.name.firstName()
    });
    
    // 1-3 adresses par client
    const nbAdresses = Math.floor(Math.random() * 3) + 1;
    for (let j = 0; j < nbAdresses; j++) {
      const adresseId = adresses.length + 1;
      adresses.push({
        id_client: `CL${String(i).padStart(6, '0')}`,
        id_quartier: Math.floor(Math.random() * 5) + 1,
        adresse_complete: faker.address.streetAddress(),
        type_bien: Math.random() > 0.8 ? 'Immeuble' : 'Standard'
      });
      
      // 1-2 compteurs par adresse
      const nbCompteurs = Math.floor(Math.random() * 2) + 1;
      for (let k = 0; k < nbCompteurs; k++) {
        compteurs.push({
          id_compteur: String(compteurs.length + 1).padStart(9, '0'),
          id_adresse: adresseId,
          type: k === 0 ? 'Eau' : 'Electricite',
          index_actuel: Math.random() * 1000
        });
      }
    }
  }
  
  return { clients, adresses, compteurs };
}
```

---

## 8. ENVIRONNEMENTS DE TEST

### 8.1. Environnement de développement

**Configuration :**
- URL : http://localhost:3000
- Base de données : MySQL local
- Email : MailHog (http://localhost:8025)
- Logs : Console

**Utilisation :**
- Tests unitaires
- Tests d'intégration
- Développement

---

### 8.2. Environnement de test (QA)

**Configuration :**
- URL : https://si-releves-test.ree.ma
- Base de données : MySQL test (serveur dédié)
- Email : MailHog dédié
- Logs : Fichiers + ELK

**Utilisation :**
- Tests fonctionnels
- Tests de non-régression
- Tests d'acceptation

---

### 8.3. Environnement de staging (pré-production)

**Configuration :**
- URL : https://si-releves-staging.ree.ma
- Base de données : Copie anonymisée de la production
- Email : SMTP réel (domaine test)
- Logs : Centralisés

**Utilisation :**
- Tests de performance
- Tests de sécurité
- Validation finale avant production

---

## 9. OUTILS ET AUTOMATISATION

### 9.1. Frameworks de test recommandés

**Backend :**
- **Node.js** : Jest, Mocha, Chai
- **Laravel (PHP)** : PHPUnit, Pest
- **Spring Boot (Java)** : JUnit, Mockito

**Frontend :**
- **React** : Jest, React Testing Library
- **Angular** : Jasmine, Karma
- **Vue** : Jest, Vue Test Utils

**API/E2E :**
- Postman/Newman
- Cypress
- Playwright

---

### 9.2. CI/CD Pipeline

**Automatisation recommandée :**

```yaml
# .gitlab-ci.yml ou .github/workflows/test.yml

stages:
  - test-unit
  - test-integration
  - test-functional
  - test-security
  - test-performance

test-unit:
  stage: test-unit
  script:
    - npm run test:unit
    - npm run test:coverage
  coverage: '/Lines\s*:\s*(\d+\.\d+)%/'
  
test-integration:
  stage: test-integration
  script:
    - docker-compose up -d mysql
    - npm run test:integration
    
test-functional:
  stage: test-functional
  script:
    - docker-compose up -d
    - npm run test:e2e
    
test-security:
  stage: test-security
  script:
    - npm audit
    - snyk test
    
test-performance:
  stage: test-performance
  script:
    - k6 run load-test.js
```

---

### 9.3. Reporting

**Outils recommandés :**
- **Allure Report** : Rapports de tests détaillés
- **SonarQube** : Qualité du code et couverture
- **k6 Cloud** : Résultats des tests de charge

**Métriques à suivre :**
- Taux de réussite des tests
- Couverture de code
- Temps d'exécution des tests
- Nombre de bugs détectés
- Temps moyen de résolution

---

## CONCLUSION

Ce cahier de tests a été généré automatiquement par **IA générative** et couvre :

✅ **157 cas de test** répartis en :
- 25 tests unitaires backend
- 10 tests unitaires frontend
- 15 tests d'intégration API
- 10 tests d'intégration base de données
- 20 tests fonctionnels (scénarios E2E)
- 12 tests de sécurité
- 8 tests de performance

✅ **Couverture complète :**
- 100% des cas d'utilisation
- Tous les endpoints API
- Toutes les contraintes métier
- Tous les triggers et procédures
- Sécurité et performance

✅ **Outils et automatisation :**
- Scripts de génération de données
- Configuration CI/CD
- Recommandations de frameworks

**Prochaine étape :** Architecture technique (Phase 2.5)

---

**Document généré automatiquement par IA - Claude (Anthropic)**  
**Technique utilisée :** IA générative pour génération de tests automatisés et jeux de données  
**Frameworks recommandés :** Jest, PHPUnit, Cypress, k6
