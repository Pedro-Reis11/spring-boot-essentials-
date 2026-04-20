package br.com.pedrodev.spring_boot_essentials.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunoDto {

    private Integer id;
    @NotBlank(message = "O nome do aluno é obrigatório")
    private String nome;
    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;
    private AvaliacaoFisicaDto avaliacaoFisica;
    private List<TreinoDto> treinos = new ArrayList<>();
}
