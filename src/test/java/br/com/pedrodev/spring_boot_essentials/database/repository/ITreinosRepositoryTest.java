package br.com.pedrodev.spring_boot_essentials.database.repository;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.TreinosEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ITreinosRepositoryTest {

    @Autowired
    private ITreinosRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private AlunosEntity aluno;

    private AlunosEntity buildAluno(String nome, String email) {
        return AlunosEntity.builder().nome(nome).email(email).build();
    }

    private TreinosEntity buildTreino(String nome, AlunosEntity aluno) {
        return TreinosEntity.builder().nome(nome).aluno(aluno).build();
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        aluno = entityManager.persistAndFlush(buildAluno("Pedro", "pedro@gmail.com"));
    }

    @Nested
    class FindByNomeAndAlunoId {
        @Test
        @DisplayName("Should return treino when nome and alunoId match")
        void shouldReturnTreinoWhenNomeAndAlunoIdMatch() {
            //Arrange
            repository.save(buildTreino("Treino A", aluno));

            //Act
            var result = repository.findByNomeAndAlunoId("Treino A", aluno.getId());

            //Assert
            assertTrue(result.isPresent());
            assertEquals("Treino A", result.get().getNome());
            assertEquals(aluno.getId(), result.get().getAluno().getId());
        }

        @Test
        @DisplayName("Should return empty optional when nome doesn't match")
        void shouldReturnEmptyOptionalWhenNomeDoesntMatch() {
            //Arrange
            repository.save(buildTreino("Treino A", aluno));

            //Act
            var result = repository.findByNomeAndAlunoId("Treino B", aluno.getId());

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty optional when id doesn't match")
        void shouldReturnEmptyOptionalWhenIDDoesntMatch() {
            //Arrange
            repository.save(buildTreino("Treino A", aluno));

            //Act
            var result = repository.findByNomeAndAlunoId("Treino A", 100);

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty optional when repository is empty")
        void shouldReturnEmptyWhenRepoitoryisEmpty() {
            //Act
            var result = repository.findByNomeAndAlunoId("Treino A", aluno.getId());
            //Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByIdAndAlunoId{

        @Test
        @DisplayName("Should return treino when id and alunoId match")
        void shouldReturnTreinoWhenIdAndAlunoIdMatch() {
            //Arrange
            var treino = repository.save(buildTreino("Treino A", aluno));

            //Act
            var result = repository.findByIdAndAlunoId(treino.getId(), aluno.getId());

            //Assert
            assertTrue(result.isPresent());
            assertEquals(treino.getId(), result.get().getId());
        }

        @Test
        @DisplayName("Should return empty optional when id doesn't match")
        void shouldReturnEmptyOptionalWhenIdDoesntMatch() {
            //Arrange
            repository.save(buildTreino("Treino A", aluno));

            //Act
            var result = repository.findByIdAndAlunoId(100, aluno.getId());

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty optional when aluno id doesn't match")
        void shouldReturnEmptyOptionalWhenAlunoIdDoesntMatch() {
            //Arrange
            var treino = repository.save(buildTreino("Treino A", aluno));

            //Act
            var result = repository.findByIdAndAlunoId(treino.getId(), 100);

            //Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindAllByAlunoId{

        @Test
        @DisplayName("Should return all treinos for a given alunoId")
        void shouldReturnAllTreinosForGivenAlunoId() {
            //Arrange
            repository.save(buildTreino("Treino A", aluno));
            repository.save(buildTreino("Treino B", aluno));

            //Act
            var result = repository.findAllByAlunoId(aluno.getId());

            //Assert
            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(t -> t.getAluno().getId().equals(aluno.getId())));
        }

        @Test
        @DisplayName("Should return empty list when no treinos found")
        void shouldReturnEmptyListWhenNoTreinosFound() {
            //Act
            var result = repository.findAllByAlunoId(aluno.getId());

            //Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return only treinos belonging to given aluno")
        void shouldReturnOnlyTreinosBelongingToGivenAluno() {
            //Arrange
            var outroAluno = entityManager.persistAndFlush(buildAluno("Maria", "maria@gmail.com"));
            repository.save(buildTreino("Treino A", aluno));
            repository.save(buildTreino("Treino B", outroAluno));

            //Act
            var result = repository.findAllByAlunoId(aluno.getId());

            //Assert
            assertEquals(1, result.size());
            assertTrue(result.stream().allMatch(t -> t.getAluno().getId().equals(aluno.getId())));
            assertEquals("Treino A", result.getFirst().getNome());
        }
    }

    @Nested
    class SaveAndDelete{

        @Test
        @DisplayName("Should persist treino and generate id")
        void shouldPersistTreinoAndGenerateId() {
            //Arrange
            var treino = buildTreino("Treino A", aluno);

            //Act
            var save = repository.save(treino);

            //Assert
            assertNotNull(save.getId());
            assertEquals("Treino A", save.getNome());
            assertEquals(aluno.getId(), save.getAluno().getId());
        }

        @Test
        @DisplayName("Should delete a treino by id")
        void shouldDeleteATreinoById() {
            //Arrange
            var save = repository.save(buildTreino("Treino A", aluno));

            //Act
            repository.deleteById(save.getId());

            //Assert
            assertTrue(repository.findById(save.getId()).isEmpty());
        }

        @Test
        @DisplayName("Should update a treino when saved with existing id")
        void shouldUpdateTreinoWhenSavedWithExistingId() {
            //Arrange
            var saved = repository.save(buildTreino("Treino A", aluno));
            saved.setNome("Treino A - Updated");

            //Act
            var updated = repository.save(saved);

            //Assert
            assertTrue(repository.findById(saved.getId()).isPresent());
            assertEquals(saved.getId(), updated.getId());
            assertEquals("Treino A - Updated", updated.getNome());
        }
    }
}