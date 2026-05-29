package br.com.pedrodev.spring_boot_essentials.integration;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.ITreinosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;
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

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class TreinosControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IAlunosRepository alunosRepository;

    @Autowired
    private IExerciciosRepository exerciciosRepository;

    @Autowired
    private ITreinosRepository repository;

    private AlunosEntity aluno;
    private ExerciciosEntity exercicios;

    private AlunosEntity buildAluno(String nome, String email) {
        return AlunosEntity.builder().nome(nome).email(email).build();
    }

    private ExerciciosEntity buildExercicio(String nome, String grupoMuscular) {
        return ExerciciosEntity.builder().nome(nome).grupoMuscular(grupoMuscular).build();
    }

    private TreinoDto buildTreinoDto(Integer idAluno, String nome, Set<Integer> exerciciosIds) {
        TreinoDto dto = new TreinoDto();
        dto.setIdAluno(idAluno);
        dto.setNome(nome);
        dto.setExerciciosIds(exerciciosIds);
        return dto;
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        alunosRepository.deleteAll();
        exerciciosRepository.deleteAll();

        aluno = alunosRepository.save(buildAluno("Pedro", "pedro@example.com"));
        exercicios = exerciciosRepository.save(buildExercicio("Supino", "Peito"));
    }

    //GET/treinos
    @Test
    @DisplayName("Should return a list with all treinos")
    void shouldReturnAListWithAllTreinos() {
        testRestTemplate.postForEntity("/treinos",
                buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId())),
                TreinoDto.class);

        var response = testRestTemplate.exchange(
                "/treinos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TreinoDto>>() {
                });

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getNome()).isEqualTo("Treino A");
    }

    @Test
    @DisplayName("Should return an empty list when no treinos exist")
    void shouldReturnAnEmptyListWhenNoTreinosExist() {
        var response = testRestTemplate.exchange(
                "/treinos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TreinoDto>>() {
                });

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    //GET/treinos/{id}/treinos
    @Test
    @DisplayName("Should return a treino for given aluno id")
    void shouldReturnTreinosForGivenAlunoId() {
        testRestTemplate.postForEntity("/treinos",
                buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId())),
                TreinoDto.class);

        var response = testRestTemplate.exchange(
                "/treinos/{idAluno}/treinos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TreinoDto>>() {
                },
                aluno.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getIdAluno()).isEqualTo(aluno.getId());
    }

    @Test
    @DisplayName("Should return 404 when aluno not found on list")
    void shouldReturn404WhenAlunoNotFoundOnList() {
        var response = testRestTemplate.exchange(
                "/treinos/99/treinos",
                HttpMethod.GET,
                null,
                ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Aluno não encontrado");
    }

    //POST/treinos

    @Test
    @DisplayName("Should create a treino and return created treino")
    void shouldCreateTreinoAndReturnCreatedDto() {
        var dto = buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId()));

        var response = testRestTemplate.postForEntity("/treinos", dto, TreinoDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo(dto.getNome());
        assertThat(response.getBody().getIdAluno()).isEqualTo(dto.getIdAluno());
        assertThat(response.getBody().getExerciciosIds()).isEqualTo(dto.getExerciciosIds());
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return 400 when treino already exists for aluno")
    void shouldReturn400WhenTreinoAlreadyExistsForAluno() {
        var dto = buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId()));

        testRestTemplate.postForEntity("/treinos", dto, TreinoDto.class);

        var response = testRestTemplate.postForEntity("/treinos", dto, ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo("Treino já existe para esse aluno");
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return 404 when aluno not found to create treino")
    void shouldReturn404WhenAlunoNotFoundToCreate() {
        var dto = buildTreinoDto(99, "Treino A", Set.of(exercicios.getId()));

        var response = testRestTemplate.postForEntity("/treinos", dto, ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Aluno não encontrado");
        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return 404 when exercicio not found to create treino")
    void shouldReturn404WhenExercicioNotFoundToCreate() {
        var dto = buildTreinoDto(aluno.getId(), "Treino A", Set.of(99));

        var response = testRestTemplate.postForEntity("/treinos", dto, ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Exercício com id "+dto.getExerciciosIds().iterator().next()+" não encontrado");
        assertThat(repository.count()).isZero();
    }

    //Put/treinos/{id}
    @Test
    @DisplayName("Should update a treino and return updated treino")
    void shouldUpdateATreinoAndReturnUpdatedDto() {
        var dto = buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId()));
        testRestTemplate.postForEntity("/treinos", dto, TreinoDto.class);

        var treino = repository.findByNomeAndAlunoId("Treino A", aluno.getId()).orElseThrow();

        var response = testRestTemplate.exchange("/treinos/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(buildTreinoDto(aluno.getId(), "Treino A - Updated", Set.of(exercicios.getId()))),
                TreinoDto.class,
                treino.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Treino A - Updated");
    }

    @Test
    @DisplayName("Should return 404 when treino not found to update")
    void shouldReturn404WhenTreinoNotFoundToUpdate() {
        var response = testRestTemplate.exchange("/treinos/99",
                HttpMethod.PUT,
                new HttpEntity<>(buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId()))),
                ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Treino não encontrado para esse aluno");
    }

    @Test
    @DisplayName("Should return 404 when one or more exercicios not found on update")
    void shouldReturn404WhenOneOrMoreExerciciosNotFoundOnUpdate() {
        testRestTemplate.postForEntity("/treinos",
                buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId())),
                TreinoDto.class);

        var treino = repository.findByNomeAndAlunoId("Treino A", aluno.getId()).orElseThrow();

        var response = testRestTemplate.exchange("/treinos/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(buildTreinoDto(aluno.getId(), "Treino A - Updated", Set.of(99))),
                ErrorResponse.class,
                treino.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Um ou mais exercícios não foram encontrados");
        assertThat(treino.getNome()).isEqualTo("Treino A");
    }

    //Delete/treinos/alunos/{idAluno}/treinos/{idTreino}

    @Test
    @DisplayName("Should delete a treino with success")
    void shouldDeleteATreinoWithSuccess() {
        testRestTemplate.postForEntity("/treinos",
                buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId())),
                TreinoDto.class);
        //pegar o id para deletar
        var treino = repository.findByNomeAndAlunoId("Treino A", aluno.getId()).orElseThrow();

        var response = testRestTemplate.exchange("/treinos/alunos/{idAluno}/treinos/{idTreino}",
                HttpMethod.DELETE,
                null,
                Void.class,
                treino.getAluno().getId(),
                treino.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(repository.findById(treino.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should return 404 when treino not found to delete")
    void shouldReturn404WhenTreinoNotFoundToDelete() {
        var response = testRestTemplate.exchange("/treinos/alunos/{idAluno}/treinos/{idTreino}",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class,
                aluno.getId(),
                99);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Treino não encontrado para esse aluno");
    }

    @Test
    @DisplayName("Should return 404 when aluno not found to delete")
    void shouldReturn404WhenAlunoNotFoundToDelete() {
        testRestTemplate.postForEntity("/treinos",
                buildTreinoDto(aluno.getId(), "Treino A", Set.of(exercicios.getId())),
                TreinoDto.class);
        var treino = repository.findByNomeAndAlunoId("Treino A", aluno.getId()).orElseThrow();

        var response = testRestTemplate.exchange("/treinos/alunos/{idAluno}/treinos/{idTreino}",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class,
                99,
                treino.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Treino não encontrado para esse aluno");
    }
}


