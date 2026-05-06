package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import br.com.pedrodev.spring_boot_essentials.mapper.ExerciciosMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
            var entities = List.of(new ExerciciosEntity()
                    , new ExerciciosEntity());
            var dtos = List.of(new ExerciciosDto()
                    , new ExerciciosDto());

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
            when(exerciciosRepository.findAll())
                    .thenThrow(new RuntimeException("Database connection failed"));

            //Act & Assert
            assertThrows(RuntimeException.class,
                    () -> exerciciosService.findAll());
        }

        @Test
        @DisplayName("Should throw RuntimeException when mapper fails")
        void shouldThrowRuntimeExceptionWhenMapperFails() {
            //Arrange
            var entities = List.of(new ExerciciosEntity());

            when(exerciciosRepository.findAll()).thenReturn(entities);
            when(exerciciosMapper.toDtoList(entities))
                    .thenThrow(new RuntimeException("Mapping error"));

            //Act & Assert
            assertThrows(RuntimeException.class,
                    () -> exerciciosService.findAll());
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

            assertThrows(IllegalArgumentException.class,
                    () -> exerciciosService.findByGrupoMuscular(null));
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
    class Save{

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
            entity.setGrupoMuscular("Peito");
            var dto = new ExerciciosDto();
            dto.setNome("");
            dto.setGrupoMuscular("Peito");

            when(exerciciosMapper.toEntity(dto)).thenReturn(entity);
            when(exerciciosRepository.save(entity))
                    .thenThrow(new IllegalArgumentException("Nome não pode ser vazio"));
            //Act
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> exerciciosService.save(dto));

            //Assert
            assertEquals("Nome não pode ser vazio", exception.getMessage());
            verify(exerciciosMapper).toEntity(dto);
            verify(exerciciosRepository).save(entity);
            verify(exerciciosMapper, never()).toDto(any());
        }
    }


}