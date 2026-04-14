package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.TreinosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TreinoMapper {
    // Converte entidade para DTO
    @Mapping(source = "aluno.id", target = "idAluno")
    @Mapping(target = "exerciciosIds", expression = "java(mapExerciciosToIds(treino.getExercicios()))")
    TreinoDto toDto(TreinosEntity treino);

    // Converte DTO para entidade
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "treinoDto.nome", target = "nome")
    @Mapping(source = "aluno", target = "aluno")
    @Mapping(source = "exerciciosIds", target = "exercicios")
    TreinosEntity toEntity(TreinoDto treinoDto, AlunosEntity aluno, Set<ExerciciosEntity> exerciciosIds);

    // Método auxiliar para mapear Set<ExerciciosEntity> para Set<Integer>
    default Set<Integer> mapExerciciosToIds(Set<ExerciciosEntity> exercicios) {
        return exercicios != null
                ? exercicios.stream().map(ExerciciosEntity::getId).collect(Collectors.toSet())
                : new HashSet<>();
    }
}
