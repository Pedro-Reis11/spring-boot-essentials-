package br.com.pedrodev.spring_boot_essentials.integration;

import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.ExerciciosDto;
import br.com.pedrodev.spring_boot_essentials.exception.ErrorResponse;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ExerciciosControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IExerciciosRepository repository;

    private ExerciciosDto buildExercicioDto() {
        ExerciciosDto dto = new ExerciciosDto();
        dto.setNome("Supino");
        dto.setGrupoMuscular("Peito");
        return dto;
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    //Getall/exercicios
    @Test
    @DisplayName("Should return list of exercicios")
    void shouldReturnListOfExercicios() {
        testRestTemplate.postForEntity("/exercicios", buildExercicioDto(), ExerciciosEntity.class);

        var response = testRestTemplate.exchange("/exercicios", HttpMethod.GET, null, new ParameterizedTypeReference<List<ExerciciosDto>>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getNome()).isEqualTo("Supino");
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Should return empty list when no exercicio found")
    void shouldReturnEmptyListWhenNoExercicioFound() {
        var response = testRestTemplate.exchange("/exercicios", HttpMethod.GET, null, new ParameterizedTypeReference<List<ExerciciosDto>>() {
        });

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    //GetByGrupo/exercicios/grupos/{grupoMuscular}
    @Test
    @DisplayName("Should return exercicios by grupo muscular")
    void shouldReturnExerciciosByGrupoMuscular() {
        var dto = testRestTemplate.postForEntity("/exercicios",
                buildExercicioDto(), ExerciciosDto.class);

        Assertions.assertNotNull(dto.getBody());
        var response = testRestTemplate.exchange("/exercicios/grupos/{grupoMuscular}",
                HttpMethod.GET,
                null,

                new ParameterizedTypeReference<List<ExerciciosDto>>() {
                }, dto.getBody().getGrupoMuscular());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getGrupoMuscular()).isEqualTo("Peito");
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Should return 400 when grupo muscular is blank or empty")
    void shouldReturn400WhenGrupoMuscularIsBlankOrEmpty() {
        var dto = testRestTemplate.postForEntity("/exercicios",
                buildExercicioDto(), ExerciciosDto.class);

        Assertions.assertNotNull(dto.getBody());
        var response = testRestTemplate.exchange("/exercicios/grupos/ ",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getMessage())
                .isEqualTo("Grupo muscular não pode ser vazio");
    }

    //GetAllByOrderByGrupoMuscularAsc/exercicios/grupos
    @Test
    @DisplayName("Should return list of exercicios ordered by grupo muscular asc")
    void shouldReturnListOfExerciciosOrderedByGrupoMuscularAsc() {
        var dto1 = buildExercicioDto();
        testRestTemplate.postForEntity("/exercicios", dto1, ExerciciosDto.class);

        var dto2 = buildExercicioDto();
        dto2.setNome("Agachamento");
        dto2.setGrupoMuscular("Perna");
        testRestTemplate.postForEntity("/exercicios", dto2, ExerciciosDto.class);

        var response = testRestTemplate.exchange("/exercicios/grupos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExerciciosDto>>() {
                },ExerciciosDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getFirst()
                .getGrupoMuscular()).isEqualTo("Peito");
        assertThat(response.getBody().getLast()
                .getGrupoMuscular()).isEqualTo("Perna");
        assertThat(response.getBody()).hasSize(2);
    }

    //Post/exercicios
    @Test
    @DisplayName("Should create exercicio and return created dto")
    void shoudCreateExercicioAndReturnCreatedDto() {
        var response = testRestTemplate.postForEntity("/exercicios",
                buildExercicioDto(), ExerciciosDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getNome()).isEqualTo("Supino");
        assertThat(response.getBody().getGrupoMuscular()).isEqualTo("Peito");
    }

    //Put/exercicios/{nome}
    @Test
    @DisplayName("Should update an exercicio and return updated dto")
    void shouldUpdateAnExercicioAndReturnUpdatedDto() {
        var dto = testRestTemplate.postForEntity("/exercicios",
                buildExercicioDto(), ExerciciosDto.class);

        var created = buildExercicioDto();
        created.setGrupoMuscular("Costas");

        var response = testRestTemplate.exchange("/exercicios/{nome}",
                HttpMethod.PUT,
                new HttpEntity<>(created),
                ExerciciosDto.class, dto.getBody().getNome());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Supino");
        assertThat(response.getBody().getGrupoMuscular()).isEqualTo("Costas");
    }

    @Test
    @DisplayName("Should return 404 when exercicio not found by nome param")
    void shouldReturn404WhenExercicioNotFoundByNomeParam() {
        var dto = buildExercicioDto();

        var response = testRestTemplate.exchange("/exercicios/{nome}",
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                ErrorResponse.class, "UnexistingName");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage())
                .isEqualTo("Exercício não encontrado");
    }

    //Delete/exercicios/{id}
    @Test
    @DisplayName("Should delete an exercicio by id")
    void shouldDeleteAnExercicio() {
        var dto = testRestTemplate.postForEntity("/exercicios",
                buildExercicioDto(), ExerciciosDto.class);

        var exercicio = repository.findByNome(dto.getBody().getNome());

        var response = testRestTemplate.exchange("/exercicios/{id}",
                HttpMethod.DELETE,
                null,
                Void.class, exercicio.get().getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(repository.findById(exercicio.get().getId())).isEmpty();
    }

    @Test
    @DisplayName("Should return 404 when exercicio not found to delete")
    void shouldReturn404WhenExercicioNotFoundToDelete() {
        var response = testRestTemplate.exchange("/exercicios/{id}",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class,99);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

