package br.com.pedrodev.spring_boot_essentials.service;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.mapper.AlunoMapper;
import br.com.pedrodev.spring_boot_essentials.mapper.AvaliacaoFisicaMapper;
import br.com.pedrodev.spring_boot_essentials.util.AlunoCreator;
import br.com.pedrodev.spring_boot_essentials.util.AlunoEntityCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class AlunoServiceTest {

    @InjectMocks
    private AlunoService alunoService;
    @Mock
    private IAlunosRepository alunosRepositoryMock;
    @Mock
    private AlunoMapper alunoMapperMock;
    @Mock
    private AvaliacaoFisicaMapper avaliacaoFisicaMapper;

    @BeforeEach
    void setUp() {
        AlunosEntity aluno = AlunoEntityCreator.criarAlunoValido();
        PageImpl<AlunosEntity> alunoPage = new PageImpl<AlunosEntity>(List.of(aluno));
        BDDMockito.when(alunoMapperMock.toDto(any()))
                .thenReturn(AlunoCreator.criarAlunoValido());
        BDDMockito.when(alunoMapperMock.toEntity(any()))
                .thenReturn(AlunoEntityCreator.criarAlunoValido());
        BDDMockito.when(alunosRepositoryMock.findAll(any(PageRequest.class)))
                .thenReturn(alunoPage);
        BDDMockito.when(alunosRepositoryMock.findById(any()))
                .thenReturn(Optional.of(AlunoEntityCreator.criarAlunoValido()));
        BDDMockito.when(alunosRepositoryMock.save(any(AlunosEntity.class)))
                .thenReturn(AlunoEntityCreator.criarAlunoValido());
        BDDMockito.doNothing().when(alunosRepositoryMock).delete(any(AlunosEntity.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista de alunos dentro de uma página")
    void findAll() {
        String nome = AlunoCreator.criarAlunoValido().getNome();
        Page<AlunoDto> alunoPage = alunoService.findAll(PageRequest.of(0, 1));

        Assertions.assertThat(alunoPage).isNotNull();
        Assertions.assertThat(alunoPage.toList()).isNotEmpty();
        Assertions.assertThat(alunoPage.toList().get(0).getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve retornar uma avaliacao fisica do aluno")
    void getAlunoAvaliacao() {
        Integer idAluno = 1;

        AlunosEntity aluno = AlunoEntityCreator.criarAlunoValido();

        AvaliacoesFisicasEntity avaliacao = new AvaliacoesFisicasEntity();
        avaliacao.setPeso(BigDecimal.valueOf(70));
        avaliacao.setAltura(BigDecimal.valueOf(1.75));

        aluno.setAvaliacaoFisica(avaliacao);

        Mockito.when(alunosRepositoryMock.findById(idAluno))
                .thenReturn(Optional.of(aluno));

        Mockito.when(avaliacaoFisicaMapper.toDto(avaliacao))
                .thenReturn(AvaliacaoFisicaDto.builder()
                        .peso(BigDecimal.valueOf(70))
                        .altura(BigDecimal.valueOf(1.75))
                        .build());

        AvaliacaoFisicaDto response = alunoService.getAlunoAvaliacao(idAluno);

        Assertions.assertThat(response.getPeso()).isEqualByComparingTo("70");
    }

    @Test
    @DisplayName("Deve criar um aluno e retornar o aluno criado")
    void criarAluno() throws Exception {
        AlunoDto aluno = alunoService.criarAluno(AlunoCreator.criarAlunoValido());

        Assertions.assertThat(aluno).isNotNull()
                .isEqualTo(AlunoCreator.criarAlunoValido());
        ;
    }

    @Test
    @DisplayName("Deve atualizar um aluno e retornar o aluno atualizado")
    void updateAluno() throws Exception {
        AlunoDto aluno = alunoService.updateAluno(AlunoCreator.updateAlunoValido().getId(), AlunoCreator.updateAlunoValido());
        Mockito.doAnswer(invocation -> {
            AlunoDto dto = invocation.getArgument(0);
            AlunosEntity entity = invocation.getArgument(1);

            entity.setNome(dto.getNome());
            entity.setEmail(dto.getEmail());
            return null;
        }).when(alunoMapperMock).updateEntityFromDto(any(), any());
        Assertions.assertThat(aluno).isNotNull()
                .isEqualTo(AlunoCreator.updateAlunoValido());
        ;
    }

    @Test
    @DisplayName("Deve deletar um aluno e não retornar nada")
    void deleteAluno() {
        Assertions.assertThatCode(() -> alunoService.deletarAluno(1))
                .doesNotThrowAnyException();
    }
}
