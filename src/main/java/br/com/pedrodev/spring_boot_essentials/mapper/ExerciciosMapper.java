package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciciosMapper {

    ExerciciosDto toDto(ExerciciosEntity exerciciosEntity);

    List<ExerciciosDto> toDtoList(List<ExerciciosEntity> exerciciosEntityList);

    ExerciciosEntity toEntity(ExerciciosDto exerciciosDto);
}
