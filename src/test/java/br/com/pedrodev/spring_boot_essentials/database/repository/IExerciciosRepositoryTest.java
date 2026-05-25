package br.com.pedrodev.spring_boot_essentials.database.repository;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IExerciciosRepositoryTest {

    @Autowired
    private IExerciciosRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ExerciciosEntity buildExcercicio(String nome, String grupoMuscular) {
        return ExerciciosEntity.builder()
                .nome(nome)
                .grupoMuscular(grupoMuscular)
                .build();
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Nested
    class FindByGrupoMuscular {
        @Test
        @DisplayName("Should find exercicio by grupo muscular")
        void shouldFindExercicioByGrupoMuscular() {
            //Arrange
            repository.save(buildExcercicio("Supino", "Peito"));

            //Act
            var result = repository.findByGrupoMuscular("Peito");

            //Assert
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertEquals("Supino", result.getFirst().getNome());
            assertEquals("Peito", result.getFirst().getGrupoMuscular());
        }

        @Test
        @DisplayName("Should return empty list when no exercicio found")
        void shouldReturnEmptyListWhenNoExercicioFound() {
            //Arrange
            repository.save(buildExcercicio("Supino", "Peito"));

            //Act
            var result = repository.findByGrupoMuscular("Costas");

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when repository is empty")
        void shouldReturnEmptyListWhenRepositoryIsEmpty() {
            //Act
            var result = repository.findByGrupoMuscular("Peito");

            //Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindAllByOrderByGrupoMuscularAsc{
        @Test
        @DisplayName("Should find all exercicios ordered by grupo muscular ascending")
        void shouldFindAllByOrderByGrupoMuscularAsc() {
            //Arrange
            repository.save(buildExcercicio("Supino", "Peito"));
            repository.save(buildExcercicio("Remada", "Costas"));


            //Act
            var result = repository.findAllByOrderByGrupoMuscularAsc();

            //Assert
            assertFalse(result.isEmpty());
            assertEquals(2, result.size());
            assertEquals("Costas", result.getFirst().getGrupoMuscular());
            assertEquals("Peito", result.getLast().getGrupoMuscular());
        }

        @Test
        @DisplayName("Should return single exercicio when only one exists")
        void shouldReturnSingledExercicioWhenOnlyOneExists() {
            //Arrange
            repository.save(buildExcercicio("Supino", "Peito"));

            //Act
            var result = repository.findAllByOrderByGrupoMuscularAsc();

            //Assert
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertEquals("Supino", result.getFirst().getNome());
        }

        @Test
        @DisplayName("Should return empty list when repository is empty")
        void shouldReturnEmptyListWhenRepositoryIsEmpty() {
            //Act
            var result = repository.findAllByOrderByGrupoMuscularAsc();

            //Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByNome{

        @Test
        @DisplayName("Should return exercicio by nome")
        void shouldReturnExercicioByNome() {
            //Arrange
            repository.save(buildExcercicio("Supino", "Peito"));

            //Act
            var result = repository.findByNome("Supino");

            //Assert
            assertTrue(result.isPresent());
            assertEquals("Supino", result.get().getNome());
        }

        @Test
        @DisplayName("Should return empty when no exercicio found by nome")
        void shouldReturnEmptyWhenNoExercicioFoundByNome() {
            //Arrange
            repository.save(buildExcercicio("Supino", "Peito"));

            //Act
            var result = repository.findByNome("Remada");

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when repository is empty")
        void shouldReturnEmptyListWhenRepositoryIsEmpty() {
            //Act
            var result = repository.findByNome("Remada");

            //Assert
            assertTrue(result.isEmpty());
        }
    }
}