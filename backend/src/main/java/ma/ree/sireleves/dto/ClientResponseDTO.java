package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {

    private String idClient;
    private String nom;
    private String prenom;
    private LocalDateTime dateCreation;
    private Integer nombreAdresses;
}