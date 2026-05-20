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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}