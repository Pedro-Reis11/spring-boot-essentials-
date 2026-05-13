package br.com.pedrodev.spring_boot_essentials.util;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.TreinosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;

import java.util.HashSet;
import java.util.Set;

public class TreinoTestFactory {

    public static AlunosEntity createAluno(int id) {
        AlunosEntity aluno = new AlunosEntity();
        aluno.setId(id);
        return aluno;
    }

    public static ExerciciosEntity createExercicio(int id) {
        ExerciciosEntity exercicio = new ExerciciosEntity();
        exercicio.setId(id);
        return exercicio;
    }

    public static TreinosEntity createTreino(int id) {
        TreinosEntity treino = new TreinosEntity();
        treino.setId(id);
        treino.setNome("Treino A");
        treino.setAluno(createAluno(1));
        return treino;
    }

    public static TreinosEntity createTreinoWithExercicios(int id, Set<ExerciciosEntity> exercicios) {
        TreinosEntity treino = createTreino(id);
        treino.setExercicios(exercicios);
        return treino;
    }

    public static TreinoDto createTreinoDto(int idAluno, Set<Integer> exerciciosIds) {
        TreinoDto dto = new TreinoDto();
        dto.setIdAluno(idAluno);
        dto.setNome("Treino A");
        dto.setExerciciosIds(new HashSet<>(exerciciosIds));
        return dto;
    }
}