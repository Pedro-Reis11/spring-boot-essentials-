package br.com.pedrodev.spring_boot_essentials.util;


import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;

public class AlunoEntityCreator {

    public static AlunosEntity criarAlunoParaSerSalvo() {
        return AlunosEntity.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .build();
    }

    public static AlunosEntity criarAlunoValido() {
        return AlunosEntity.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .build();
    }

    public static AlunosEntity updateAlunoValido() {
        return AlunosEntity.builder()
                .id(1)
                .nome("João Silva Gomes")
                .email("joao@email.com")
                .build();
    }
}
