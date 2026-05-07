package br.com.pedrodev.spring_boot_essentials.util;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;

import java.math.BigDecimal;

public class AvaliacaoFisicaTestFactory {
    //Dto
    public static AvaliacaoFisicaDto createAvaliacaoFisicaDto() {
        AvaliacaoFisicaDto dto = new AvaliacaoFisicaDto();
        dto.setIdAluno(1);
        dto.setPeso(BigDecimal.valueOf(70.0));
        dto.setAltura(BigDecimal.valueOf(1.75));
        dto.setPorcentagemGorduraCorporal(BigDecimal.valueOf(15.0));
        return dto;
    }

    public static AvaliacaoFisicaDto createAvaliacaoFisicaDtoRequestId(Integer idAluno) {
        AvaliacaoFisicaDto dto = new AvaliacaoFisicaDto();
        dto.setIdAluno(idAluno);
        return dto;
    }

    //Entity
    public static AvaliacoesFisicasEntity createAvaliacaoFisicaEntity() {
        AvaliacoesFisicasEntity entity = new AvaliacoesFisicasEntity();
        entity.setId(1);
        entity.setPeso(BigDecimal.valueOf(70.0));
        entity.setAltura(BigDecimal.valueOf(1.75));
        entity.setPorcentagemGorduraCorporal(BigDecimal.valueOf(15.0));
        return entity;
    }

    //Aluno
    public static AlunosEntity alunoWithAvaliacaoFisica() {
        AlunosEntity aluno = new AlunosEntity();
        aluno.setId(1);
        aluno.setAvaliacaoFisica(createAvaliacaoFisicaEntity());
        return aluno;
    }

    public static AlunosEntity alunoWithAvaliacaoFisica(AvaliacoesFisicasEntity avaliacao) {
        AlunosEntity aluno = alunoWithoutAvaliacaoFisica();
        aluno.setAvaliacaoFisica(avaliacao);
        return aluno;
    }

    public static AlunosEntity alunoWithoutAvaliacaoFisica() {
        AlunosEntity aluno = new AlunosEntity();
        aluno.setId(1);
        aluno.setAvaliacaoFisica(null);
        return aluno;
    }
}
