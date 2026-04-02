package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.TreinosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TreinoMapper {

    TreinoDto toDto(TreinosEntity treino);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "treinoDto.nome", target = "nome")
    @Mapping(source = "aluno", target = "aluno")
    @Mapping(source = "exerciciosIds", target = "exercicios")
    TreinosEntity toEntity(TreinoDto treinoDto, AlunosEntity aluno, Set<ExerciciosEntity> exerciciosIds);
}
