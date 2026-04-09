package br.com.pedrodev.spring_boot_essentials.database.repository;

import br.com.pedrodev.spring_boot_essentials.database.model.TreinosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITreinosRepository extends JpaRepository<TreinosEntity, Integer> {

    Optional<TreinosEntity> findByNomeAndAlunoId(String nome, Integer alunoId);

    Optional<TreinosEntity> findByIdAndAlunoId(Integer id, Integer alunoId);

    List<TreinosEntity> findAllByAlunoId(Integer alunoId);
}
