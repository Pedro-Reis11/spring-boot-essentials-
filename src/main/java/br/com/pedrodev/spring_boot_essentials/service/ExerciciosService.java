package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import br.com.pedrodev.spring_boot_essentials.mapper.ExerciciosMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciciosService {

    private final IExerciciosRepository exerciciosRepository;
    private final ExerciciosMapper exerciciosMapper;

    public List<ExerciciosDto> findAll() {
        return exerciciosMapper.toDtoList(exerciciosRepository.findAll());
    }

    public List<ExerciciosDto> findByGrupoMuscular(String grupoMuscular){
        return exerciciosMapper.toDtoList(exerciciosRepository.findByGrupoMuscular(grupoMuscular));
    }

    public List<ExerciciosDto> findAllByOrderByGrupoMuscularAsc() {
        return exerciciosMapper.toDtoList(exerciciosRepository.findAllByOrderByGrupoMuscularAsc());
    }

    public ExerciciosDto save(ExerciciosDto dto) {
        ExerciciosEntity entity = exerciciosMapper.toEntity(dto);
        ExerciciosEntity saved = exerciciosRepository.save(entity);
        return exerciciosMapper.toDto(saved);
    }

    @Transactional
    public void delete(Integer id) {
        exerciciosRepository.deleteById(id);
    }


}
