package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAvaliacoesFisicasRepository;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.mapper.AvaliacaoFisicaMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoFisicaService {

    private final IAvaliacoesFisicasRepository avaliacoesFisicasRepository;
    private final IAlunosRepository alunosRepository;
    private final AvaliacaoFisicaMapper avaliacaoFisicaMapper;

    public AvaliacaoFisicaDto criarAvaliacaoFisica(AvaliacaoFisicaDto dto) throws BadRequestException {
         AlunosEntity aluno = alunosRepository.findById(dto.getIdAluno())
                 .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        if (aluno.getAvaliacaoFisica() != null) {
            throw new BadRequestException("Avaliação física já existe para este aluno");
        }
        aluno.setAvaliacaoFisica(avaliacaoFisicaMapper.toEntity(dto));
        AlunosEntity alunoSalvo = alunosRepository.save(aluno);
        return avaliacaoFisicaMapper.toDto(alunoSalvo.getAvaliacaoFisica());
    }


}
