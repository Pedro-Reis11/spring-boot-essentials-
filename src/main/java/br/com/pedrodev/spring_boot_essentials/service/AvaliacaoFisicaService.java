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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoFisicaService {

    private final IAvaliacoesFisicasRepository avaliacoesFisicasRepository;
    private final IAlunosRepository alunosRepository;
    private final AvaliacaoFisicaMapper avaliacaoFisicaMapper;

    //Post
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

    //GetAll
    public List<AvaliacaoFisicaDto> findAll() {
        return avaliacaoFisicaMapper.toDtoList(avaliacoesFisicasRepository.findAll());
    }

    //Put
    public AvaliacaoFisicaDto updateAvaliacaoFisica(Integer idAluno, AvaliacaoFisicaDto dto) throws BadRequestException {
        AlunosEntity aluno = alunosRepository.findById(idAluno)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        AvaliacoesFisicasEntity avaliacao = aluno.getAvaliacaoFisica();
        if (avaliacao == null) {
            throw new NotFoundException("Avaliação física não encontrada");
        }

        avaliacaoFisicaMapper.updateEntityFromDto(dto, avaliacao);
        avaliacoesFisicasRepository.save(avaliacao);
        return avaliacaoFisicaMapper.toDto(avaliacao);
    }

    //Delete
    @Transactional
    public void deleteAvaliacaoFisica(Integer idAluno) throws NotFoundException {
        AlunosEntity aluno = alunosRepository.findById(idAluno)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        AvaliacoesFisicasEntity avaliacao = aluno.getAvaliacaoFisica();
        if (avaliacao == null) {
            throw new NotFoundException("Avaliação física não encontrada");
        }
        // Desassocia a avaliação física do aluno antes de deletar
        aluno.setAvaliacaoFisica(null);
    }
}
