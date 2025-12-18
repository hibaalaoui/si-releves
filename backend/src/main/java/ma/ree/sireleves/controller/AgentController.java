package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.AgentRequestDTO;
import ma.ree.sireleves.dto.AgentResponseDTO;
import ma.ree.sireleves.dto.AgentUpdateDTO;
import ma.ree.sireleves.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    /**
     * Créer un nouvel agent
     * POST /api/agents
     */
    @PostMapping
    public ResponseEntity<AgentResponseDTO> creerAgent(
            @Valid @RequestBody AgentRequestDTO requestDTO) {
        AgentResponseDTO response = agentService.creerAgent(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les agents
     * GET /api/agents
     */
    @GetMapping
    public ResponseEntity<List<AgentResponseDTO>> getAllAgents(
            @RequestParam(required = false) Integer idQuartier,
            @RequestParam(required = false) Boolean actifOnly) {

        List<AgentResponseDTO> agents;

        if (idQuartier != null) {
            agents = agentService.getAgentsByQuartier(idQuartier);
        } else if (actifOnly != null && actifOnly) {
            agents = agentService.getActiveAgents();
        } else {
            agents = agentService.getAllAgents();
        }

        return ResponseEntity.ok(agents);
    }

    /**
     * Récupérer un agent par son ID
     * GET /api/agents/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgentResponseDTO> getAgentById(@PathVariable String id) {
        AgentResponseDTO agent = agentService.getAgentById(id);
        return ResponseEntity.ok(agent);
    }

    /**
     * Mettre à jour un agent
     * PUT /api/agents/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgentResponseDTO> updateAgent(
            @PathVariable String id,
            @Valid @RequestBody AgentUpdateDTO updateDTO) {
        AgentResponseDTO response = agentService.updateAgent(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Activer/Désactiver un agent
     * PATCH /api/agents/{id}/toggle-status
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<AgentResponseDTO> toggleAgentStatus(@PathVariable String id) {
        AgentResponseDTO response = agentService.toggleAgentStatus(id);
        return ResponseEntity.ok(response);
    }
}