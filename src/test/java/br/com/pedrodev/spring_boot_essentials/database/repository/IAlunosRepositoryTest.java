package br.com.pedrodev.spring_boot_essentials.database.repository;


import br.com.pedrodev.spring_boot_essentials.database.model.AlunosEntity;
import br.com.pedrodev.spring_boot_essentials.util.AlunoEntityCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@DisplayName("Testes para o repositório de alunos")
class IAlunosRepositoryTest {

    @Autowired
    private IAlunosRepository alunosRepository;

    @Test
    @DisplayName("Deve salvar um aluno no banco de dados")
    void criarAluno() {
        AlunosEntity aluno = AlunoEntityCreator.criarAlunoParaSerSalvo();
        AlunosEntity alunoSalvo = this.alunosRepository.save(aluno);

        Assertions.assertThat(alunoSalvo).isNotNull();
        Assertions.assertThat(alunoSalvo.getId()).isNotNull();
        Assertions.assertThat(alunoSalvo.getNome()).isEqualTo(aluno.getNome());
        Assertions.assertThat(alunoSalvo.getEmail()).isEqualTo(aluno.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar e salvar um aluno no banco de dados")
    void updateAluno() {
        AlunosEntity aluno = AlunoEntityCreator.criarAlunoParaSerSalvo();
        AlunosEntity alunoSalvo = this.alunosRepository.save(aluno);

        alunoSalvo.setNome("Carlos Silva");
        alunoSalvo.setEmail("carlossilva@gmail.com");
        AlunosEntity alunoAtualizado = this.alunosRepository.save(alunoSalvo);

        Assertions.assertThat(alunoAtualizado).isNotNull();
        Assertions.assertThat(alunoAtualizado.getId()).isNotNull();
        Assertions.assertThat(alunoAtualizado.getNome()).isEqualTo(alunoSalvo.getNome());
        Assertions.assertThat(alunoAtualizado.getEmail()).isEqualTo(alunoSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve deletar um aluno no banco de dados")
    void deleteAluno() {
        AlunosEntity aluno = AlunoEntityCreator.criarAlunoParaSerSalvo();
        AlunosEntity alunoSalvo = this.alunosRepository.save(aluno);

        this.alunosRepository.delete(alunoSalvo);

        Optional<AlunosEntity> alunosEntityOptional = this.alunosRepository.findById(alunoSalvo.getId());

        Assertions.assertThat(alunosEntityOptional).isEmpty();
    }

    @Test
    @DisplayName("Deve encontrar um aluno por nome no banco de dados")
    void findByName() {
        AlunosEntity aluno = AlunoEntityCreator.criarAlunoParaSerSalvo();
        AlunosEntity alunoSalvo = this.alunosRepository.save(aluno);

        String email = alunoSalvo.getEmail();

        Optional<AlunosEntity> byEmail = this.alunosRepository.findByEmail(email);

        Assertions.assertThat(byEmail).isNotEmpty();
        Assertions.assertThat(byEmail.get().getEmail()).isEqualTo(email);
    }
}