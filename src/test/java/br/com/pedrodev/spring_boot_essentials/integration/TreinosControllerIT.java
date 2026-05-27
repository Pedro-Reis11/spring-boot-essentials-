package br.com.pedrodev.spring_boot_essentials.integration;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.ExerciciosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IExerciciosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.ITreinosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.TreinoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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
                new ParameterizedTypeReference<List<TreinoDto>>() {},
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
                TreinoDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat("Aluno não encontrado").isNotNull();
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

        var response = testRestTemplate.postForEntity("/treinos", dto, TreinoDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(repository.count()).isEqualTo(1);
    }
}


