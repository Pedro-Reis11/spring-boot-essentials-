package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciciosMapper {

    ExerciciosDto toDto(ExerciciosEntity exerciciosEntity);

    List<ExerciciosDto> toDtoList(List<ExerciciosEntity> exerciciosEntityList);

    ExerciciosEntity toEntity(ExerciciosDto exerciciosDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "grupoMuscular", source = "grupoMuscular")
    ExerciciosEntity updateEntityFromDto(ExerciciosDto dto, @MappingTarget ExerciciosEntity entity);
}
