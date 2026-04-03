package br.com.pedrodev.spring_boot_essentials.mapper;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    @Mapping(source = "avaliacaoFisica", target = "avaliacaoFisica")
    AlunoDto toDto(AlunosEntity entity);

    List<AlunoDto> toDtoList(List<AlunosEntity> list);

    AlunosEntity toEntity(AlunoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AlunoDto dto, @MappingTarget AlunosEntity entity);
}
