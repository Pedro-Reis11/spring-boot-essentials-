package br.com.pedrodev.spring_boot_essentials.util;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;

public class AlunoCreator {

    public static AlunoDto criarAlunoParaSerSalvo() {
        return AlunoDto.builder()
                .nome(AlunoEntityCreator.criarAlunoParaSerSalvo().getNome())
                .email(AlunoEntityCreator.criarAlunoParaSerSalvo().getEmail())
                .build();
    }

    public static AlunoDto criarAlunoValido() {
        return AlunoDto.builder()
                .id(AlunoEntityCreator.criarAlunoValido().getId())
                .nome(AlunoEntityCreator.criarAlunoValido().getNome())
                .email(AlunoEntityCreator.criarAlunoValido().getEmail())
                .build();
    }

    public static AlunoDto updateAlunoValido() {
        return AlunoDto.builder()
                .id(AlunoEntityCreator.updateAlunoValido().getId())
                .nome(AlunoEntityCreator.updateAlunoValido().getNome())
                .email(AlunoEntityCreator.updateAlunoValido().getEmail())
                .build();
    }
}
