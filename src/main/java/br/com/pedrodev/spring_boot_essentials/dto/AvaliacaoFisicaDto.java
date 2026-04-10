package br.com.pedrodev.spring_boot_essentials.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvaliacaoFisicaDto {

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer idAluno;
    @NotNull
    private BigDecimal peso;
    @NotNull
    private BigDecimal altura;
    @NotNull
    private BigDecimal porcentagemGorduraCorporal;

}
