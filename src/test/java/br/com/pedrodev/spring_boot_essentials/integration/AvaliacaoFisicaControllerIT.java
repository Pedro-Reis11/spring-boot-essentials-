package br.com.pedrodev.spring_boot_essentials.integration;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAvaliacoesFisicasRepository;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AvaliacaoFisicaControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IAvaliacoesFisicasRepository repository;

    @Autowired
    private IAlunosRepository alunosRepository;

    private AlunosEntity savedAluno;

    @BeforeEach
    void setUp() {
        AlunosEntity aluno = new AlunosEntity();
        aluno.setNome("Pedro");
        aluno.setEmail("pedro@gmail.com");
        savedAluno = alunosRepository.save(aluno);
    }

    private AvaliacaoFisicaDto buildAvaliacaoFisicaDto() {
        AvaliacaoFisicaDto dto = new AvaliacaoFisicaDto();
        dto.setIdAluno(savedAluno.getId());
        dto.setAltura(BigDecimal.valueOf(1.75));
        dto.setPeso(BigDecimal.valueOf(70.0));
        dto.setPorcentagemGorduraCorporal(BigDecimal.valueOf(15.0));
        return dto;
    }

    //GETAll/avaliacoes
    @Test
    @DisplayName("Should return all avaliações físicas")
    void shouldReturnAllAvaliacoes() {
        testRestTemplate.postForEntity("/avaliacoes", buildAvaliacaoFisicaDto(), AvaliacaoFisicaDto.class);

        var response = testRestTemplate.exchange("/avaliacoes", HttpMethod.GET, null, new ParameterizedTypeReference<List<AvaliacaoFisicaDto>>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getPeso()).isEqualByComparingTo(BigDecimal.valueOf(70.0));
    }

    @Test
    @DisplayName("Should return empty list when no avaliações are found")
    void shouldReturnEmptyListWhenNoAvaliacoesAreFound() {
        var response = testRestTemplate.exchange("/avaliacoes", HttpMethod.GET, null, new ParameterizedTypeReference<List<AvaliacaoFisicaDto>>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    //Post/avaliacoes

    @Test
    @DisplayName("Should create an avaliação and return created DTO")
    void shouldCreateAnAvaliacaoAndReturnCreatedDto() {
        var response = testRestTemplate.exchange("/avaliacoes", HttpMethod.POST, new HttpEntity<>(buildAvaliacaoFisicaDto()), AvaliacaoFisicaDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPeso()).isEqualByComparingTo(BigDecimal.valueOf(70.0));
        assertThat(response.getBody().getAltura()).isEqualByComparingTo(BigDecimal.valueOf(1.75));
    }

    @Test
    @DisplayName("Should return 404 when aluno not found to create an avaliação")
    void shouldReturn404WhenAlunoNotFoundToCreateAnAvaliacao() {
        var dto = buildAvaliacaoFisicaDto();
        dto.setIdAluno(999); // ID de aluno inexistente

        var response = testRestTemplate.exchange("/avaliacoes", HttpMethod.POST, new HttpEntity<>(dto), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Aluno não encontrado");
        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should return 400 when avaliação already exists for aluno")
    void shouldReturn400WhenAvaliacaoAlreadyExistsForAluno() {
        testRestTemplate.postForEntity("/avaliacoes", buildAvaliacaoFisicaDto(), AvaliacaoFisicaDto.class);

        var response = testRestTemplate.exchange("/avaliacoes", HttpMethod.POST, new HttpEntity<>(buildAvaliacaoFisicaDto()), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Avaliação física já existe para este aluno");
        assertThat(repository.count()).isEqualTo(1);
    }

    //Put/avaliacoes/{idAluno}
    @Test
    @DisplayName("Should update an existing avaliação and return updated DTO")
    void shouldUpdateAnExistingAvaliacaoAndReturnUpdatedDto() {
        testRestTemplate.postForEntity("/avaliacoes", buildAvaliacaoFisicaDto(), AvaliacaoFisicaDto.class);

        var updatedDto = buildAvaliacaoFisicaDto();
        updatedDto.setPeso(BigDecimal.valueOf(75.0));

        var response = testRestTemplate.exchange("/avaliacoes/{idAluno}", HttpMethod.PUT, new HttpEntity<>(updatedDto), AvaliacaoFisicaDto.class, savedAluno.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPeso()).isEqualByComparingTo(BigDecimal.valueOf(75.0));
    }

    @Test
    @DisplayName("Should return 404 when aluno not found")
    void shouldReturn404WhenAlunoNotFound() {
        var updatedDto = buildAvaliacaoFisicaDto();
        updatedDto.setPeso(BigDecimal.valueOf(75.0));

        var response = testRestTemplate.exchange("/avaliacoes/{idAluno}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedDto),
                ErrorResponse.class,
                999);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Aluno não encontrado");
    }

    @Test
    @DisplayName("Should return 404 when avaliação not found to update")
    void shouldReturn404WhenAvaliacaoNotFoundToUpdate() {
        var response = testRestTemplate.exchange("/avaliacoes/{idAluno}",
                HttpMethod.PUT,
                new HttpEntity<>(buildAvaliacaoFisicaDto()),
                ErrorResponse.class,
                savedAluno.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Avaliação física não encontrada");
    }

    //Delete/avaliacoes/{idAluno}
    @Test
    @DisplayName("Should delete an existing avaliação")
    void shouldDeleteAnExistingAvaliacao() {
        testRestTemplate.postForEntity("/avaliacoes", buildAvaliacaoFisicaDto(), AvaliacaoFisicaDto.class);

        var response = testRestTemplate.exchange("/avaliacoes/{idAluno}", HttpMethod.DELETE, null, Void.class, savedAluno.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should return 404 when aluno not found to delete")
    void shouldReturn404WhenAlunoNotFoundToDelete() {
        var response = testRestTemplate.exchange("/avaliacoes/{idAluno}",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class,
                999);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Aluno não encontrado");
    }

    @Test
    @DisplayName("Should return 404 when avaliação not found to delete")
    void shouldReturn404WhenAvaliacaoNotFoundToDelete() {
        var response = testRestTemplate.exchange("/avaliacoes/{idAluno}",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class,
                savedAluno.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Avaliação física não encontrada");
    }
}