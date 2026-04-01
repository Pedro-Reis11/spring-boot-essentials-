package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvaliacaoFisicaMapper {

    AvaliacaoFisicaDto toDto(AvaliacoesFisicasEntity avaliacoesFisicasEntity);

    List<AvaliacaoFisicaDto> toDtoList(List<AvaliacoesFisicasEntity> avaliacoesFisicasEntityList);

    AvaliacoesFisicasEntity toEntity(AvaliacaoFisicaDto avaliacaoFisicaDto);
}
