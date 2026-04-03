package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvaliacaoFisicaMapper {

    @Mapping(source = "id", target = "idAluno")
    AvaliacaoFisicaDto toDto(AvaliacoesFisicasEntity avaliacoesFisicasEntity);

    List<AvaliacaoFisicaDto> toDtoList(List<AvaliacoesFisicasEntity> avaliacoesFisicasEntityList);

    AvaliacoesFisicasEntity toEntity(AvaliacaoFisicaDto avaliacaoFisicaDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AvaliacaoFisicaDto dto, @MappingTarget AvaliacoesFisicasEntity entity);
}
