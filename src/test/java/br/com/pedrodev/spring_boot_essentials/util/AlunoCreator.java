package br.com.pedrodev.spring_boot_essentials.util;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;

public class AlunoCreator {

    public static AlunoDto criarAlunoParaSerSalvo() {
        return AlunoDto.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .build();
    }

    public static AlunoDto criarAlunoValido() {
        return AlunoDto.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .build();
    }

    public static AlunoDto updateAlunoValido() {
        return AlunoDto.builder()
                .id(1)
                .nome("João Silva Gomes")
                .email("joao@email.com")
                .build();
    }
}
