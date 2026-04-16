package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.ITreinosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.mapper.AlunoMapper;
import br.com.pedrodev.spring_boot_essentials.mapper.AvaliacaoFisicaMapper;
import br.com.pedrodev.spring_boot_essentials.mapper.TreinoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final IAlunosRepository alunosRepository;
    private final AlunoMapper alunoMapper;
    private final AvaliacaoFisicaMapper avaliacaoFisicaMapper;
    private final ITreinosRepository treinosRepository;
    private final TreinoMapper treinoMapper;

    //Post
    @Transactional
    public AlunoDto criarAluno(AlunoDto alunoDto) throws BadRequestException {
        try {
            AlunosEntity aluno = alunosRepository.findByEmail(alunoDto.getEmail())
                    .orElse(null);
            if (aluno != null) {
                throw new BadRequestException("Já existe um aluno cadastrado com esse email");
            }
            AlunosEntity alunoEntity = alunoMapper.toEntity(alunoDto);
            return alunoMapper.toDto(alunosRepository.save(alunoEntity));
        } catch (Exception e) {
            throw new BadRequestException("Erro ao criar aluno: " + e.getMessage());
        }
    }

    //GetAll
    public Page<AlunoDto> findAll(Pageable pageable) {
        return alunosRepository.findAll(pageable).map(alunoMapper::toDto);
    }

    //Get
    public AvaliacaoFisicaDto getAlunoAvaliacao(Integer idAluno) throws NotFoundException {
        AlunosEntity aluno = alunosRepository.findById(idAluno)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        AvaliacoesFisicasEntity avaliacao = aluno.getAvaliacaoFisica();
        if (avaliacao == null) {
            throw new NotFoundException("Avaliação física não encontrada para este aluno");
        }
        return avaliacaoFisicaMapper.toDto(avaliacao);
    }

    //Put
    public AlunoDto updateAluno(Integer id, AlunoDto dto) throws NotFoundException {
        AlunosEntity aluno = alunosRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
        alunoMapper.updateEntityFromDto(dto, aluno);
        alunosRepository.save(aluno);
        return alunoMapper.toDto(aluno);
    }

    //Delete
    @Transactional
    public void deletarAluno(Integer id) {
        alunosRepository.deleteById(id);
    }

}
