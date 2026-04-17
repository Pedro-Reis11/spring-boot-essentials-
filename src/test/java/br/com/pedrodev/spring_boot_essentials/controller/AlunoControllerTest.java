package br.com.pedrodev.spring_boot_essentials.controller;

import br.com.pedrodev.spring_boot_essentials.dto.AlunoDto;
import br.com.pedrodev.spring_boot_essentials.service.AlunoService;
import br.com.pedrodev.spring_boot_essentials.util.AlunoCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    }

    @Test
    @DisplayName("Deve retornar uma lista de alunos dentro de uma página")
    void findAll(){
        String nome = AlunoCreator.criarAlunoValido().getNome();
        Page<AlunoDto> alunoPage = alunoController.findAll(PageRequest.of(0,1));

        Assertions.assertThat(alunoPage).isNotNull();
        Assertions.assertThat(alunoPage.toList()).isNotEmpty();
        Assertions.assertThat(alunoPage.toList().get(0).getNome()).isEqualTo(nome);
    }

}