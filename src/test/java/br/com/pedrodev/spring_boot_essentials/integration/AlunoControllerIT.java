package br.com.pedrodev.spring_boot_essentials.integration;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.model.AvaliacoesFisicasEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAvaliacoesFisicasRepository;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.dto.AvaliacaoFisicaDto;
import br.com.pedrodev.spring_boot_essentials.mapper.AlunoMapperImpl;
import br.com.pedrodev.spring_boot_essentials.util.AlunoCreator;
import br.com.pedrodev.spring_boot_essentials.util.AlunoEntityCreator;
import br.com.pedrodev.spring_boot_essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Import(AlunoMapperImpl.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AlunoControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private IAlunosRepository alunosRepository;
    @Autowired
    private IAvaliacoesFisicasRepository avaliacoesFisicasRepository;

    @Test
    @DisplayName("Deve retornar uma lista de alunos dentro de uma página")
    void findAll() {
        AlunosEntity saved = alunosRepository.save(AlunoEntityCreator.criarAlunoValido());
        String nome = saved.getNome();

        PageableResponse<AlunoDto> alunoPage = testRestTemplate.exchange("/alunos", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<AlunoDto>>() {
                }).getBody();

        Assertions.assertThat(alunoPage).isNotNull();
        Assertions.assertThat(alunoPage.toList()).isNotEmpty();
        Assertions.assertThat(alunoPage.toList().get(0).getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve retornar uma avaliacao fisica do aluno")
    void getAlunoAvaliacao() throws Exception {
       AlunosEntity aluno = alunosRepository.save(AlunoEntityCreator.criarAlunoValido());
       Integer idAluno = aluno.getId();
        AvaliacoesFisicasEntity save = avaliacoesFisicasRepository.save(AlunoEntityCreator.criarAvaliacaoFisicaValida(aluno));
        aluno.setAvaliacaoFisica(save);
        alunosRepository.save(aluno);
        ResponseEntity<AvaliacaoFisicaDto> response =
                testRestTemplate.getForEntity("/alunos/{idAluno}/avaliacao"
                        , AvaliacaoFisicaDto.class
                        , idAluno);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar um aluno e retornar o aluno criado")
    void criarAluno() throws Exception {
        ResponseEntity<AlunosEntity> alunosEntityResponseEntity = testRestTemplate.postForEntity("/alunos"
                , AlunoCreator.criarAlunoValido()
                , AlunosEntity.class);

        Assertions.assertThat(alunosEntityResponseEntity).isNotNull();
        Assertions.assertThat(alunosEntityResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(alunosEntityResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("Deve atualizar um aluno e retornar o aluno atualizado")
    void updateAluno() throws Exception {
        AlunosEntity aluno = alunosRepository.save(AlunoEntityCreator.criarAlunoValido());
        aluno.setNome("Novo nome");
        aluno.setEmail("novoemail@gmail.com");

        ResponseEntity<AlunosEntity> alunosEntityResponseEntity = testRestTemplate.exchange("/alunos/{id}"
                , HttpMethod.PUT
                , new HttpEntity<>(aluno)
                , AlunosEntity.class
                , aluno.getId());

        Assertions.assertThat(alunosEntityResponseEntity).isNotNull();
        Assertions.assertThat(alunosEntityResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Deve deletar um aluno e não retornar nada")
    void deleteAluno() {
        AlunosEntity aluno = alunosRepository.save(AlunoEntityCreator.criarAlunoValido());

        ResponseEntity<Void> alunosEntityResponseEntity =
                testRestTemplate.exchange("/alunos/{id}"
                , HttpMethod.DELETE
                , null
                , Void.class
                , aluno.getId());

        Assertions.assertThat(alunosEntityResponseEntity).isNotNull();
        Assertions.assertThat(alunosEntityResponseEntity.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }
}
