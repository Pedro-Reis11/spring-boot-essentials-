package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.mapper.ExerciciosMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciciosServiceTest {

    @Mock
    private IExerciciosRepository exerciciosRepository;

    @Mock
    private ExerciciosMapper exerciciosMapper;

    @InjectMocks
    private ExerciciosService exerciciosService;

    @Nested
    class FindAll {

        @Test
        @DisplayName("Shold return all exercises with success")
        void shouldReturnAllExercisesWithSuccess() {
            //Arrange
            var entities = List.of(new ExerciciosEntity());
            var dtos = List.of(new ExerciciosDto());

            when(exerciciosRepository.findAll()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(dtos);
            //Act
            var result = exerciciosService.findAll();

            //Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(exerciciosRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no exercises found")
        void shouldReturnEmptyListWhenNoExercisesFound() {
            //Arrange
            when(exerciciosRepository.findAll()).thenReturn(List.of());
            when(exerciciosMapper.toDtoList(List.of())).thenReturn(List.of());

            //Act
            var result = exerciciosService.findAll();

            //Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(exerciciosRepository).findAll();
            verify(exerciciosMapper).toDtoList(List.of());
        }

        @Test
        @DisplayName("Should call mapper with correct data")
        void shouldCallMapperWithCorrectData() {
            //Arrange
            var entities = List.of(new ExerciciosEntity());

            when(exerciciosRepository.findAll()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(List.of(new ExerciciosDto()));

            //Act
            exerciciosService.findAll();

            //Assert
            verify(exerciciosMapper).toDtoList(entities);
        }

        @Test
        @DisplayName("Should return mapped DTOs")
        void shouldReturnMappedDtos() {
            //Arrange
            var entities = List.of(new ExerciciosEntity());
            var expectedDtos = List.of(new ExerciciosDto());

            when(exerciciosRepository.findAll()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(expectedDtos);

            //Act
            var result = exerciciosService.findAll();

            //Assert
            assertEquals(expectedDtos, result);
        }

        @Test
        @DisplayName("Shold return multiple exercises with success")
        void shouldReturnMultipleExercisesWithSuccess() {
            //Arrange
            var entities = List.of(new ExerciciosEntity(), new ExerciciosEntity());
            var dtos = List.of(new ExerciciosDto(), new ExerciciosDto());

            when(exerciciosRepository.findAll()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(dtos);
            //Act
            var result = exerciciosService.findAll();

            //Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(dtos, result);
            verify(exerciciosRepository).findAll();
        }

        @Test
        @DisplayName("Should throw RuntimeException when repository fails")
        void shouldThrowRuntimeExceptionWhenRepositoryFails() {
            //Arrange
            when(exerciciosRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

            //Act & Assert
            assertThrows(RuntimeException.class, () -> exerciciosService.findAll());
        }

        @Test
        @DisplayName("Should throw RuntimeException when mapper fails")
        void shouldThrowRuntimeExceptionWhenMapperFails() {
            //Arrange
            var entities = List.of(new ExerciciosEntity());

            when(exerciciosRepository.findAll()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenThrow(new RuntimeException("Mapping error"));

            //Act & Assert
            assertThrows(RuntimeException.class, () -> exerciciosService.findAll());
        }
    }

    @Nested
    class FindByGrupoMuscular {

        @Test
        @DisplayName("Should return exercises by group with success")
        void shouldReturnExercisesByGruoup() {
            //Arrange
            var grupoMuscular = "Peito";
            var entities = List.of(new ExerciciosEntity());
            var dtos = List.of(new ExerciciosDto());

            when(exerciciosRepository.findByGrupoMuscular(grupoMuscular)).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(dtos);

            //Act
            var result = exerciciosService.findByGrupoMuscular(grupoMuscular);

            //Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(exerciciosRepository).findByGrupoMuscular(grupoMuscular);
        }

        @Test
        @DisplayName("Should call mapper with correct data")
        void shouldCallMapperWithCorrectData() {
            //Arrange
            var grupoMuscular = "Peito";
            var entities = List.of(new ExerciciosEntity());

            when(exerciciosRepository.findByGrupoMuscular(grupoMuscular)).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(List.of(new ExerciciosDto()));

            //Act
            var result = exerciciosService.findByGrupoMuscular(grupoMuscular);

            //Assert
            assertNotNull(result);
            verify(exerciciosMapper).toDtoList(entities);
        }

        @Test
        @DisplayName("Should return mapped DTOs")
        void shouldReturnMappedDtos() {
            //Arrange
            var entities = List.of(new ExerciciosEntity());
            var expectedDtos = List.of(new ExerciciosDto());

            when(exerciciosRepository.findByGrupoMuscular(any())).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(expectedDtos);

            //Act
            var result = exerciciosService.findByGrupoMuscular("Peito");

            //Assert
            assertNotNull(result);
            assertEquals(expectedDtos, result);
        }

        @Test
        @DisplayName("Should return empty list when no exercises found for group")
        void shouldReturnEmptyListWhenNoExercisesFoundForGrupo() {
            String grupo = "Inexistente";

            when(exerciciosRepository.findByGrupoMuscular(grupo)).thenReturn(List.of());
            when(exerciciosMapper.toDtoList(List.of())).thenReturn(List.of());

            List<ExerciciosDto> result = exerciciosService.findByGrupoMuscular(grupo);

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(exerciciosRepository).findByGrupoMuscular(grupo);
            verify(exerciciosMapper).toDtoList(List.of());
        }

        @Test
        @DisplayName("Should throw BadRequest when group is null")
        void shouldThrowBadRequestWhenGroupIsNull() {

            assertThrows(IllegalArgumentException.class, () -> exerciciosService.findByGrupoMuscular(null));
        }
    }

    @Nested
    class FindAllByOrderByGrupoMuscularAsc {

        @Test
        @DisplayName("Should return exercises ordered by group with success")
        void shouldReturnExercisesOrderedByGroup() {
            //Arrange
            var entities = List.of(new ExerciciosEntity(), new ExerciciosEntity());
            var dtos = List.of(new ExerciciosDto(), new ExerciciosDto());
            dtos.getFirst().setGrupoMuscular("Abdomen");
            dtos.getLast().setGrupoMuscular("Peito");


            when(exerciciosRepository.findAllByOrderByGrupoMuscularAsc()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities)).thenReturn(dtos);

            //Act
            var result = exerciciosService.findAllByOrderByGrupoMuscularAsc();

            //Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(dtos, result);
            verify(exerciciosRepository).findAllByOrderByGrupoMuscularAsc();
            verify(exerciciosMapper).toDtoList(entities);
            assertEquals("Abdomen", result.getFirst().getGrupoMuscular());
            assertEquals("Peito", result.getLast().getGrupoMuscular());
        }
    }

    @Nested
    class Save {

        @Test
        @DisplayName("Should save an exercise with success")
        void shouldSaveAnExerciseWithSuccess() {
            //Arrange
            var dto = new ExerciciosDto();
            dto.setNome("Upper");
            dto.setGrupoMuscular("Peito");

            var entity = new ExerciciosEntity();
            entity.setNome("Upper");
            entity.setGrupoMuscular("Peito");

            when(exerciciosMapper.toEntity(dto)).thenReturn(entity);
            when(exerciciosRepository.save(entity)).thenReturn(entity);
            when(exerciciosMapper.toDto(entity)).thenReturn(dto);

            //Act
            var result = exerciciosService.save(dto);

            //Assert
            assertNotNull(result);
            assertEquals(dto, result);
            assertEquals("Upper", result.getNome());
            assertEquals("Peito", result.getGrupoMuscular());

            var inOrder = inOrder(exerciciosMapper, exerciciosRepository);
            inOrder.verify(exerciciosMapper).toEntity(dto);
            inOrder.verify(exerciciosRepository).save(entity);
            inOrder.verify(exerciciosMapper).toDto(entity);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when try save an exercise with a blank attribute")
        void shouldThrownIllegalArgumentExceptionWhenTrySaveAnExcerciseWithABlankAttribute() {
            //Arrange
            var entity = new ExerciciosEntity();
            entity.setNome("");
            entity.setGrupoMuscular("");
            var dto = new ExerciciosDto();
            dto.setNome("");
            dto.setGrupoMuscular("");

            when(exerciciosMapper.toEntity(dto)).thenReturn(entity);
            when(exerciciosRepository.save(entity)).thenThrow(new IllegalArgumentException("Nome ou grupo não pode ser vazio"));
            //Act
            var exception = assertThrows(IllegalArgumentException.class, () -> exerciciosService.save(dto));

            //Assert
            assertEquals("Nome ou grupo não pode ser vazio", exception.getMessage());
            verify(exerciciosMapper).toEntity(dto);
            verify(exerciciosRepository).save(entity);
            verify(exerciciosMapper, never()).toDto(any());
        }
    }

    @Nested
    class Update {

        @Test
        @DisplayName("Should update an exercise with success")
        void shouldUpdateAExerciseWithSuccess() {
            //Arrange
            var entity = new ExerciciosEntity();
            entity.setNome("Upper");
            entity.setGrupoMuscular("Peito");
            var dto = new ExerciciosDto();
            dto.setNome("Lower");
            dto.setGrupoMuscular("Peito");

            when(exerciciosRepository.findByNome("Upper")).thenReturn(Optional.of(entity));
            when(exerciciosRepository.save(entity)).thenReturn(entity);
            when(exerciciosMapper.toDto(entity)).thenReturn(dto);

            //Act
            var result = exerciciosService.update(entity.getNome(), dto);

            //Assert
            assertNotNull(result);
            assertEquals(dto, result);
            assertEquals("Lower", result.getNome());
            assertEquals("Peito", result.getGrupoMuscular());
            var inOrder = inOrder(exerciciosRepository, exerciciosMapper);
            inOrder.verify(exerciciosRepository).findByNome(entity.getNome());
            inOrder.verify(exerciciosMapper).updateEntityFromDto(dto, entity);
            inOrder.verify(exerciciosRepository).save(entity);
            inOrder.verify(exerciciosMapper).toDto(entity);
        }

        @Test
        @DisplayName("Should update only grupoMuscular keeping the same name")
        void shouldUpdateOnlyGrupoMuscularKeepingTheSameName() {
            //Arrange
            var entity = new ExerciciosEntity();
            entity.setNome("Lower");
            entity.setGrupoMuscular("Perna");
            var dto = new ExerciciosDto();
            dto.setNome("Lower");
            dto.setGrupoMuscular("Posterior");

            when(exerciciosRepository.findByNome(entity.getNome())).thenReturn(Optional.of(entity));
            when(exerciciosRepository.save(entity)).thenReturn(entity);
            when(exerciciosMapper.toDto(entity)).thenReturn(dto);

            //Act
            var result = exerciciosService.update(entity.getNome(), dto);

            //Assert
            assertNotNull(result);
            assertEquals(dto, result);
            assertEquals("Lower", result.getNome());
            assertEquals("Posterior", result.getGrupoMuscular());
        }

        @Test
        @DisplayName("Should throw NotFoundException when exercise not found")
        void shouldThrowNotFoundExceptionWhenExerciseNotFound() {
            //Arrange
            var dto = new ExerciciosDto();
            when(exerciciosRepository.findByNome("Inexistente")).thenReturn(Optional.empty());
            //Act & Assert
            var ex = assertThrows(NotFoundException.class, () -> exerciciosService.update("Inexistente", dto));

            assertEquals("Exercício não encontrado", ex.getMessage());
            verify(exerciciosRepository).findByNome("Inexistente");
            verify(exerciciosMapper, never()).updateEntityFromDto(any(), any());
            verify(exerciciosRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw RuntimeException when repository fails to save")
        void shouldThrowRunTimeExceptionWhenRepositoryFailsToSave() {
            //Arrange
            var entity = new ExerciciosEntity();
            entity.setNome("Upper");
            var dto = new ExerciciosDto();
            dto.setNome("Lower");

            when(exerciciosRepository.findByNome("Upper")).thenReturn(Optional.of(entity));
            doThrow(new RuntimeException("Database error")).when(exerciciosRepository).save(entity);

            //Act & Assert
            assertThrows(RuntimeException.class, () -> exerciciosService.update("Upper", dto));

            verify(exerciciosMapper).updateEntityFromDto(dto, entity);
            verify(exerciciosMapper, never()).toDto(any());
        }
    }

    @Nested
    class Delete {

        @Test
        @DisplayName("Should delete an exercise with success")
        void shouldDeleteAnExerciseWithSuccess() {
            //Arrange
            var id = 1;

            //Act & Assert
            assertDoesNotThrow(() -> exerciciosService.delete(id));
            verify(exerciciosRepository).deleteById(id);
        }

        @Test
        @DisplayName("Should throw NotFoundException when id not found to delete")
        void shouldThrowNotFoundExceptionWhenIdNotFoundToDelete() {
            //Arrange
            var id = 999;
            doThrow(new NotFoundException("Exercise not found")).when(exerciciosRepository).deleteById(id);

            //Act & Assert
            var exception = assertThrows(NotFoundException.class, () -> exerciciosService.delete(id));

            assertEquals("Exercise not found", exception.getMessage());
            verify(exerciciosRepository).deleteById(id);
        }

        @Test
        @DisplayName("Should throw RuntimeException when repository fails to delete")
        void shouldThrowRuntimeExceptionWhenRepositoryFailsToDelete() {
            //Arrange
            var id = 1;
            doThrow(new RuntimeException("Db error")).when(exerciciosRepository).deleteById(id);

            //Act & Assert
            assertThrows(RuntimeException.class, () -> exerciciosService.delete(id));

            verify(exerciciosRepository).deleteById(id);
        }
    }
}