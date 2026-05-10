package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAvaliacoesFisicasRepository;
import br.com.pedrodev.spring_boot_essentials.exception.NotFoundException;
import br.com.pedrodev.spring_boot_essentials.mapper.AvaliacaoFisicaMapper;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.pedrodev.spring_boot_essentials.util.AvaliacaoFisicaTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliacaoFisicaServiceTest {

    @Mock
    private IAvaliacoesFisicasRepository repository;

    @Mock
    private IAlunosRepository alunosRepository;

    @Mock
    private AvaliacaoFisicaMapper mapper;

    @InjectMocks
    private AvaliacaoFisicaService service;

    @Captor
    private ArgumentCaptor<AlunosEntity> alunoCaptor;


    @Nested
    class CreateAvaliacaoFisica {
        @Test
        @DisplayName("Should create a avaliacao with success")
        void shouldCreateAAvaliacaoWithSuccess() throws BadRequestException {
            //Arrange
            var dto = createAvaliacaoFisicaDto();
            var aluno = alunoWithoutAvaliacaoFisica();
            var entity = createAvaliacaoFisicaEntity();
            var alunoSalvo = alunoWithAvaliacaoFisica(entity);
            var dtoSalvo = createAvaliacaoFisicaDto();

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.of(aluno));
            when(mapper.toEntity(dto)).thenReturn(entity);
            when(alunosRepository.save(aluno)).thenReturn(alunoSalvo);
            when(mapper.toDto(entity)).thenReturn(dtoSalvo);

            //Act
            var result = service.criarAvaliacaoFisica(dto);

            //Assert
            assertThat(result).isNotNull()
                    .isEqualTo(dtoSalvo);

            assertThat(aluno.getAvaliacaoFisica()).isEqualTo(entity);

            var inOrder = inOrder(mapper, alunosRepository);
            inOrder.verify(alunosRepository).findById(dto.getIdAluno());
            inOrder.verify(mapper).toEntity(dto);
            inOrder.verify(alunosRepository).save(alunoCaptor.capture());
            inOrder.verify(mapper).toDto(entity);

            var alunoCapturado = alunoCaptor.getValue();

            assertThat(alunoCapturado.getAvaliacaoFisica())
                    .isEqualTo(entity);
        }

        @Test
        @DisplayName("Should throw NotFoundException when aluno does not exit")
        void shouldThrowNotFoundExceptionWhenAlunoDoesNotExit() {
            //Arrange
            var dto = createAvaliacaoFisicaDtoRequestId(999);

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.empty());

            //Act & Assert
            assertThrows(NotFoundException.class,
                    () -> service.criarAvaliacaoFisica(dto));

            verify(alunosRepository, times(1)).findById(dto.getIdAluno());
            verify(alunosRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw BadRequestException when avaliacao already exists for the aluno")
        void shouldThrowBadRequestExceptionWhenAvaliacaoAlreadyExistsForTheAluno() {
            //Arrange
            var dto = createAvaliacaoFisicaDto();
            var aluno = alunoWithAvaliacaoFisica();

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.of(aluno));

            //Act & Assert
            assertThrows(BadRequestException.class,
                    () -> service.criarAvaliacaoFisica(dto));

            verify(alunosRepository, times(1)).findById(dto.getIdAluno());
            verify(alunosRepository, never()).save(any());
        }
    }

    @Nested
    class FindAll {

        @Test
        @DisplayName("Should return all avaliations with success")
        void shouldReturnAllAvaliationsWithSuccess() {
            //Arrange
            var entities = List.of(createAvaliacaoFisicaEntity(), createAvaliacaoFisicaEntity());
            var dtos = List.of(createAvaliacaoFisicaDto(), createAvaliacaoFisicaDto());

            when(repository.findAll()).thenReturn(entities);
            when(mapper.toDtoList(entities)).thenReturn(dtos);

            //Act
            var all = service.findAll();

            //Assert
            assertNotNull(all);
            assertThat(all).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no avaliations found")
        void shouldReturnEmptyListWhenNoAvaliationsFound() {
            //Arrange
            when(repository.findAll()).thenReturn(Collections.emptyList());
            when(mapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

            //Act
            var all = service.findAll();

            //Assert
            assertThat(all).isEmpty();
            verify(repository, times(1)).findAll();
            verify(mapper).toDtoList(Collections.emptyList());
        }
    }

    @Nested
    class Update {

        @Test
        @DisplayName("Should update a avaliacao with success")
        void shouldUpdateAAvaliacaoWithSuccess() throws BadRequestException {
            //Arrange
            var entity = createAvaliacaoFisicaEntity();
            var aluno = alunoWithAvaliacaoFisica(entity);
            var dto = createAvaliacaoFisicaDto();
            var dtoAtualizado = createAvaliacaoFisicaDto();

            when(alunosRepository.findById(dto.getIdAluno())).thenReturn(Optional.of(aluno));
            when(repository.save(entity)).thenReturn(entity);
            when(mapper.toDto(entity)).thenReturn(dtoAtualizado);

            //Act
            var updated = service.updateAvaliacaoFisica(dto.getIdAluno(), dto);

            //Assert
            assertThat(updated).isNotNull()
                    .isEqualTo(dtoAtualizado);
            verify(alunosRepository, times(1)).findById(dto.getIdAluno());
            verify(mapper).updateEntityFromDto(dto, entity);
            verify(repository).save(entity);
        }
    }

}