package ma.ree.sireleves.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleveRequestDTO {

    @NotBlank(message = "L'ID du compteur est obligatoire")
    private String idCompteur;

    @NotBlank(message = "L'ID de l'agent est obligatoire")
    private String idAgent;

    @NotNull(message = "Le nouvel index est obligatoire")
    @DecimalMin(value = "0.0", message = "L'index doit être positif")
    private BigDecimal nouvelIndex;

    private LocalDateTime dateReleve;
}