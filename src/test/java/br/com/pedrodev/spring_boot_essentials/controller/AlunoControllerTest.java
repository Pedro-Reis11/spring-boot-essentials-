package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.service.AlunoService;
import br.com.pedrodev.spring_boot_essentials.util.AlunoCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AlunoControllerTest {

    @InjectMocks
    private AlunoController alunoController;
    @Mock
    private AlunoService alunoService;

    @BeforeEach
    void setUp() {
        AlunoDto aluno = AlunoCreator.criarAlunoValido();
        PageImpl<AlunoDto> alunoPage = new PageImpl<AlunoDto>(List.of(aluno));
        BDDMockito.when(alunoService.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(alunoPage);
        BDDMockito.when(alunoService.criarAluno(ArgumentMatchers.any(AlunoDto.class)))
                .thenReturn(AlunoCreator.criarAlunoValido());
        BDDMockito.when(alunoService.updateAluno(ArgumentMatchers.anyInt()
                        ,ArgumentMatchers.any(AlunoDto.class)))
                .thenReturn(AlunoCreator.updateAlunoValido());
        BDDMockito.doNothing().when(alunoService).deletarAluno(ArgumentMatchers.anyInt());
    }

    @Test
    @DisplayName("Deve retornar uma lista de alunos dentro de uma página")
    void findAll() {
        String nome = AlunoCreator.criarAlunoValido().getNome();
        Page<AlunoDto> alunoPage = alunoController.findAll(PageRequest.of(0, 1));

        Assertions.assertThat(alunoPage).isNotNull();
        Assertions.assertThat(alunoPage.toList()).isNotEmpty();
        Assertions.assertThat(alunoPage.toList().get(0).getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve retornar uma avaliacao fisica do aluno")
    void getAlunoAvaliacao() throws Exception {
        Integer idAluno = 1;

        AvaliacaoFisicaDto avaliacaoMock = AvaliacaoFisicaDto.builder()
                .peso(BigDecimal.valueOf(70.0))
                .altura(BigDecimal.valueOf(1.75))
                .build();

        Mockito.when(alunoService.getAlunoAvaliacao(idAluno))
                .thenReturn(avaliacaoMock);

        AvaliacaoFisicaDto response = alunoController.getAlunoAvaliacao(idAluno);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getPeso()).isEqualByComparingTo(new BigDecimal(70.0));
        Assertions.assertThat(response.getAltura()).isEqualByComparingTo(new BigDecimal(1.75));
    }

    @Test
    @DisplayName("Deve criar um aluno e retornar o aluno criado")
    void criarAluno() throws Exception {
        AlunoDto aluno = alunoController.criarAluno(AlunoCreator.criarAlunoValido());

        Assertions.assertThat(aluno).isNotNull()
                .isEqualTo(AlunoCreator.criarAlunoValido());;
    }

    @Test
    @DisplayName("Deve atualizar um aluno e retornar o aluno atualizado")
    void updateAluno() throws Exception {
        AlunoDto aluno = alunoController.updateAluno(AlunoCreator.updateAlunoValido().getId(), AlunoCreator.updateAlunoValido());

        Assertions.assertThat(aluno).isNotNull()
                .isEqualTo(AlunoCreator.updateAlunoValido());;
    }

    @Test
    @DisplayName("Deve deletar um aluno e não retornar nada")
    void deleteAluno() {
        Assertions.assertThatCode(() -> alunoController.deleteAluno(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> response = alunoController.deleteAluno(1);

        Assertions.assertThat(response).isNotNull();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
