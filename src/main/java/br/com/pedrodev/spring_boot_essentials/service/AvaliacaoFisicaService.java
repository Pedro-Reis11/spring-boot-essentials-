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

@Service
@RequiredArgsConstructor
public class AvaliacaoFisicaService {

    private final IAvaliacoesFisicasRepository avaliacoesFisicasRepository;
    private final IAlunosRepository alunosRepository;
    private final AvaliacaoFisicaMapper avaliacaoFisicaMapper;

    public void criarAvaliacaoFisica(AvaliacaoFisicaDto dto) throws BadRequestException {
         AlunosEntity aluno = alunosRepository.findById(dto.getIdAluno())
                 .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        AvaliacoesFisicasEntity avaliacao = aluno.getAvaliacaoFisica();
        if (avaliacao != null) {
            throw new BadRequestException("Avaliação física já existe para este aluno");
        }
        aluno.setAvaliacaoFisica(avaliacaoFisicaMapper.toEntity(dto));
        alunosRepository.save(aluno);
    }

}
