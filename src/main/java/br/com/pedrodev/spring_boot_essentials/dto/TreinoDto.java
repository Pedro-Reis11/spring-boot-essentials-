package br.com.pedrodev.spring_boot_essentials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreinoDto {

    @NotNull(message = "O id do aluno é obrigatório")
    private Integer idAluno;
    @NotBlank(message = "O nome do treino é obrigatório")
    private String nome;
    @NotEmpty(message = "A lista de exercícios não pode ser vazia")
    private Set<Integer> exerciciosIds = new HashSet<>();
}
