package br.com.pedrodev.spring_boot_essentials.database.repository;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IExerciciosRepository extends JpaRepository<ExerciciosEntity, Integer> {

    List<ExerciciosEntity> findByGrupoMuscular(String grupoMuscular);

    List<ExerciciosEntity> findAllByOrderByGrupoMuscularAsc();

    Optional<ExerciciosEntity> findByNome(String nome);

//    @Query(value = """
//        SELECT e
//        FROM ExerciciosEntity e
//        WHERE UPPER(e.grupoMuscular) = UPPER(:grupoMuscular)
//        """){
//    List<ExerciciosEntity> findByGrupoMuscularJpql(@Param("grupoMuscular") String grupoMuscular);
//    }

//    @NativeQuery(value = """
//        SELECT e
//        FROM exercicios e
//        WHERE UPPER(e.grupo_muscular) = UPPER(:grupoMuscular)
//        """)
//    List<ExerciciosEntity> findByGrupoMuscularNative(@Param("grupoMuscular") String grupoMuscular);
}
