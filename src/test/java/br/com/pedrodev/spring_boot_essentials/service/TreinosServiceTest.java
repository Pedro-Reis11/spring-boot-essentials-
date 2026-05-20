package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.ITreinosRepository;
import br.com.pedrodev.spring_boot_essentials.exception.BadRequestException;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.mapper.TreinoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.pedrodev.spring_boot_essentials.util.TreinoTestFactory.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreinosServiceTest {
    @Mock
    private IAlunosRepository alunosRepository;

    @Mock
    private IExerciciosRepository exerciciosRepository;

    @Mock
    private ITreinosRepository repository;

    @Mock
    private TreinoMapper mapper;

    @InjectMocks
    private TreinosService service;


    @Nested
    class CreateTreino{

        @Test
        @DisplayName("Should create treino with success")
        void shouldCreateTreinoWithSuccess() {
            //Arrange
            var dto = createTreinoDto(1, Set.of(10));
            var aluno = createAluno(1);
            var exercicio = createExercicio(10);
            var treino = createTreino(1);
            var expectDto = createTreinoDto(1, Set.of(10));

            when(alunosRepository.findById(1)).thenReturn(Optional.of(aluno));
            when(repository.findByNomeAndAlunoId(treino.getNome(), aluno.getId())).thenReturn(Optional.empty());
            when(exerciciosRepository.findById(exercicio.getId())).thenReturn(Optional.of(exercicio));
            when(mapper.toEntity(eq(dto), eq(aluno), any())).thenReturn(treino);
            when(repository.save(treino)).thenReturn(treino);
            when(mapper.toDto(treino)).thenReturn(expectDto);


            //Act
            var result = service.criarTreino(dto);

            //Assert
            assertThat(result).isNotNull();
            assertThat(result.getIdAluno()).isEqualTo(expectDto.getIdAluno());
            assertThat(result.getNome()).isEqualTo(expectDto.getNome());
            verify(repository).save(treino);
        }

        @Test
        @DisplayName("Should throw NotFoundException when aluno not found")
        void shouldThrowNotFoundExceptionWhenAlunoNotFound() {
            //Arrange
            var dto = createTreinoDto(1, Set.of(10));

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.criarTreino(dto));
            assertThat("Aluno não encontrado").isNotNull();
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw BadRequestException when treino already exists")
        void shouldThrowBadRequestExceptionWhenTreinoAlreadyExists() {
            //Arrange
            var dto = createTreinoDto(1, Set.of(10));
            var aluno = createAluno(1);
            var treino = createTreino(1);

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.of(aluno));
            when(repository.findByNomeAndAlunoId(treino.getNome(), aluno.getId())).thenReturn(Optional.of(treino));

            //Act & Assert
            assertThrows(BadRequestException.class,
                    () -> service.criarTreino(dto));
            assertThat("Treino já existe para esse aluno").isNotNull();
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NotFoundException when exercise not found")
        void shouldThrowNotFoundExceptionWhenExerciseNotFound() {
            //Arrange
            var dto = createTreinoDto(1, Set.of(10));
            var aluno = createAluno(1);

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.of(aluno));
            when(repository.findByNomeAndAlunoId(dto.getNome(), aluno.getId())).thenReturn(Optional.empty());
            when(exerciciosRepository.findById(10)).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.criarTreino(dto));
            assertThat("Exercício com id 10 não encontrado").isNotNull();
            verify(repository, never()).save(any());
        }
    }

    @Nested
    class FindAll{

        @Test
        @DisplayName("Should return list of treinoDtos")
        void shouldReturnListOfTreinoDtos() {
            //Arrange
            var treino = createTreino(1);
            var dto = createTreinoDto(1, Set.of());

            when(repository.findAll()).thenReturn(List.of(treino));
            when(mapper.toDto(treino)).thenReturn(dto);

            //Act
            var all = service.findAll();

            //Assert
            assertThat(all).isNotNull();
            assertEquals(1, all.size());
            assertEquals(dto.getIdAluno(), all.getFirst().getIdAluno());
            assertEquals(dto.getNome(), all.getFirst().getNome());
        }

        @Test
        @DisplayName("Should return empty list when treinos dont exist")
        void shouldReturnEmptyListWhenNoTreinosExist() {
            //Arrange
            when(repository.findAll()).thenReturn(List.of());

            //Act
            var result = service.findAll();

            //Assert
            assertThat(result).isNotNull();
            assertTrue(result.isEmpty());
            verify(repository).findAll();
        }
    }

    @Nested
    class ListarTreinosPorAluno{

        @Test
        @DisplayName("Should return list of treinos for given aluno")
        void shouldReturnListOfTreinosForGivenAluno() {
            //Arrange
            var aluno = createAluno(1);
            var treino = createTreino(1);
            var dto = createTreinoDto(1, Set.of());


            when(alunosRepository.existsById(aluno.getId())).thenReturn(true);
            when(repository.findAllByAlunoId(aluno.getId())).thenReturn(List.of(treino));
            when(mapper.toDto(treino)).thenReturn(dto);

            //Act
            var result = service.listarTreinosPorAluno(aluno.getId());

            //Assert
            assertThat(result).isNotNull();
            assertEquals(1, result.size());
            assertEquals(dto.getIdAluno(), result.getFirst().getIdAluno());
            verify(repository).findAllByAlunoId(aluno.getId());
        }

        @Test
        @DisplayName("Should return empty list when no treinos exist for given aluno")
        void shouldReturnEmptyListWhenNoTreinosExistForAluno() {
            //Arrange
            var aluno = createAluno(1);

            when(alunosRepository.existsById(aluno.getId())).thenReturn(true);
            when(repository.findAllByAlunoId(aluno.getId())).thenReturn(List.of());

            //Act
            var result = service.listarTreinosPorAluno(aluno.getId());

            //Assert
            assertThat(result).isNotNull();
            assertTrue(result.isEmpty());
            verify(repository).findAllByAlunoId(aluno.getId());
        }

        @Test
        @DisplayName("Should throw NotFoundException when aluno not found")
        void shouldThrowNotFoundExceptionWhenAlunoNotFound() {
            //Arrange
            when(alunosRepository.existsById(1)).thenReturn(false);
            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.listarTreinosPorAluno(1));
            assertThat("Aluno não encontrado").isNotNull();
            verify(repository, never()).findAllByAlunoId(anyInt());
        }
    }

    @Nested
    class Update{

        @Test
        @DisplayName("Should update treino with success")
        void shouldUpdateTreinoWithSuccess() {
            //Arrange
            var aluno = createAluno(1);
            var treino = createTreino(1);
            var exercicio = createExercicio(10);
            var dto = createTreinoDto(1, Set.of(10));
            var expectDto = createTreinoDto(1, Set.of(10));
            when(repository.findByIdAndAlunoId(treino.getId(), aluno.getId())).thenReturn(Optional.of(treino));
            when(exerciciosRepository.findAllById(dto.getExerciciosIds())).thenReturn(List.of(exercicio));
            when(mapper.toDto(treino)).thenReturn(expectDto);

            //Act
            var result = service.updateTreino(treino.getId(), dto);

            //Assert
            assertThat(result).isNotNull();
            assertEquals(expectDto.getIdAluno(), result.getIdAluno());
            assertEquals(expectDto.getNome(), result.getNome());
            assertEquals(expectDto.getExerciciosIds(), result.getExerciciosIds());
            verify(repository).save(treino);
        }

        @Test
        @DisplayName("Should throw NotFoundException when treino not found for given aluno")
        void shouldThrowNotFoundExceptionWhenTreinoNotFoundForALuno() {
            //Arrange
            var aluno = createAluno(1);
            var treino = createTreino(1);
            var dto = createTreinoDto(1, Set.of(10));

            when(repository.findByIdAndAlunoId(treino.getId(), aluno.getId())).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.updateTreino(1, dto));
            assertThat("Treino não encontrado para esse aluno").isNotNull();
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NotFoundException when exercicio not found for given aluno")
        void shouldThrowNotFoundExceptionWhenExercicioNotFoundForAluno() {
            //Arrange
            var aluno = createAluno(1);
            var treino = createTreino(1);
            var dto = createTreinoDto(1, Set.of(10));

            when(repository.findByIdAndAlunoId(treino.getId(), aluno.getId())).thenReturn(Optional.of(treino));
            when(exerciciosRepository.findAllById(dto.getExerciciosIds())).thenReturn(List.of());

            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.updateTreino(1, dto));
            assertThat("Um ou mais exercícios não foram encontrados").isNotNull();
            verify(repository, never()).save(any());
        }
    }

    @Nested
    class Delete{

        @Test
        @DisplayName("Should delete treino with success")
        void shouldDeleteTreinoWithSuccess() {
            //Arrange
            var aluno = createAluno(1);
            var treino = createTreino(1);

            when(repository.findByIdAndAlunoId(treino.getId(), aluno.getId())).thenReturn(Optional.of(treino));

            //Act
            service.deleteTreino(1, 1);

            //Assert
            verify(repository).delete(treino);
        }

        @Test
        @DisplayName("Should throw NotFoundException when treino not found")
        void shouldThrowNotFoundExceptionWhenTreinoNotFound() {
            //Arrange
            var aluno = createAluno(1);

            when(repository.findByIdAndAlunoId(1, aluno.getId())).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.deleteTreino(1, 1));
            assertThat("Treino não encontrado para esse aluno").isNotNull();
            verify(repository, never()).delete(any());
        }
    }

}