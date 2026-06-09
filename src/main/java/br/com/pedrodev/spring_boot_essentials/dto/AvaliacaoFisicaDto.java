package br.com.pedrodev.spring_boot_essentials.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Integer idAluno;

    @NotNull
    private BigDecimal peso;
    @NotNull
    private BigDecimal altura;
    @NotNull
    private BigDecimal porcentagemGorduraCorporal;

    @JsonProperty("idAluno")
    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }
}
