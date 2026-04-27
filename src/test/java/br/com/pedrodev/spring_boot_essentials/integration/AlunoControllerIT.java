package br.com.pedrodev.spring_boot_essentials.integration;

import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.database.repository.IAlunosRepository;
import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
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
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Import(AlunoMapperImpl.class)
public class AlunoControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private IAlunosRepository alunosRepository;

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
}
