package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.TreinosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.ITreinosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.mapper.TreinoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreinosService {

    private final IAlunosRepository alunosRepository;
    private final IExerciciosRepository exerciciosRepository;
    private final ITreinosRepository treinosRepository;
    private final TreinoMapper treinoMapper;


    public TreinoDto criarTreino(TreinoDto dto) throws NotFoundException, BadRequestException {
        try {
            Set<ExerciciosEntity> exerciciosIds = new HashSet<>();
            AlunosEntity aluno = alunosRepository.findById(dto.getIdAluno())
                    .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
            TreinosEntity treinos = treinosRepository.findByNomeAndAlunoId(dto.getNome(), dto.getIdAluno())
                    .orElse(null);
            if (treinos != null) {
                throw new BadRequestException("Treino já existe para esse aluno");
            }
            for (Integer idExercicio : dto.getExerciciosIds()) {
                ExerciciosEntity ex = exerciciosRepository.findById(idExercicio)
                        .orElseThrow(() -> new NotFoundException("Exercício com id " + idExercicio + " não encontrado"));
                exerciciosIds.add(ex);
            }
            treinos = TreinosEntity.builder()
                    .nome(dto.getNome())
                    .aluno(aluno)
                    .exercicios(exerciciosIds)
                    .build();
            treinosRepository.save(treinos);
            TreinoDto treinoDto = new TreinoDto();
            treinoDto.setIdAluno(treinos.getAluno().getId());
            treinoDto.setNome(treinos.getNome());

            // Mapear a lista/Set de ExerciciosEntity para Set<Integer> de IDs
            Set<Integer> ids = treinos.getExercicios().stream()
                    .map(ExerciciosEntity::getId)
                    .collect(Collectors.toSet());

            treinoDto.setExerciciosIds(ids);
            return (treinoDto);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<TreinoDto> findAll() {
        List<TreinosEntity> treinos = treinosRepository.findAll();
        List<TreinoDto> treinosDto = new ArrayList<>();

        for (TreinosEntity treino : treinos) {
            TreinoDto dto = new TreinoDto();
            dto.setIdAluno(treino.getAluno() != null ? treino.getAluno().getId() : null);
            dto.setNome(treino.getNome());

            // Mapear exercícios para Set<Integer>
            if (treino.getExercicios() != null && !treino.getExercicios().isEmpty()) {
                Set<Integer> ids = treino.getExercicios().stream()
                        .map(ExerciciosEntity::getId)
                        .collect(Collectors.toSet());
                dto.setExerciciosIds(ids);
            } else {
                dto.setExerciciosIds(new HashSet<>()); // garante que não venha null
            }

            treinosDto.add(dto);
        }

        return treinosDto;
    }
}
