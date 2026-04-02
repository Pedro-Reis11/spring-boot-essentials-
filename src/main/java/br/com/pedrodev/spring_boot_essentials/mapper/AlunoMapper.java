package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    @Mapping(source = "avaliacaoFisica", target = "avaliacaoFisica")
    AlunoDto toDto(AlunosEntity entity);

    List<AlunoDto> toDtoList(List<AlunosEntity> list);

    AlunosEntity toEntity(AlunoDto dto);
}
